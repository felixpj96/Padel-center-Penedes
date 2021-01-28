package com.example.pc;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AvailabilityActivity extends AppCompatActivity {
    private static final String crearReserva = "http://192.168.1.136:80/padelcenter/setReserva.php";
    private static final String pistasReservadas = "http://192.168.1.136:80/padelcenter/pistasReservadas.php";
    ListView lvReserva;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.availability_activity);

        Intent intent = getIntent();
        final String fecha = intent.getStringExtra("Fecha");
        final String pista = intent.getStringExtra("Pista");
        final String user = intent.getStringExtra("username");

        recuperarReservas(fecha,pista,user);

    }
//creación de las horas disponibles para mostrarlas en el listview
    private List<String> crearHorasDisponibles(String fecha, String pista,ArrayList<Reserva> reservas) {
        List<String> horas = new ArrayList<>();
        horas.add(fecha + "  9:00 - 10:30");
        horas.add(fecha + "  10:30 - 12:00");
        horas.add(fecha + "  12:00 - 13:30");
        horas.add(fecha + "  13:30 - 15:00");
        horas.add(fecha + "  15:00 - 16:30");
        horas.add(fecha + "  16:30 - 18:00");
        horas.add(fecha + "  18:00 - 19:30");
        horas.add(fecha + "  19:30 - 21:00");

        for (int i = 0; i<horas.size();i++){
            for(int e = 0; e<reservas.size();e++){
                if(horas.get(i).equals(reservas.get(e).getFecha())){
                    horas.remove(i);
                    i=0;
                }
            }
        }

        return horas;
    }
//función para realizar una reserva
    private void crearReserva(final String fecha, final String propietario, final String pista){
        StringRequest request=new StringRequest(Request.Method.POST, crearReserva, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("success")){
                    enviarCorreo(propietario,fecha,pista);
                    Toast.makeText(getApplicationContext(),"Pista Reservada",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AvailabilityActivity.this,UserActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Error al reservar la pista",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("reserva","true");
                params.put("fecha",fecha.trim());
                params.put("propietario", propietario.trim());
                params.put("pista", pista.trim());
                return  params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
//recupera todas las reservas realizadas
    private void recuperarReservas(final String fecha, final String pista, final String user){
        final ArrayList<Reserva> arrayReservas = new ArrayList<>();
        StringRequest request=new StringRequest(Request.Method.POST, pistasReservadas, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("error")){
                    try {
                        JSONArray array = new JSONArray(response);

                        for (int i = 0; i < array.length();i++){
                            JSONObject reserva = array.getJSONObject(i);

                            arrayReservas.add(new Reserva(reserva.getString("pista"),
                                    reserva.getString("fecha")));
                        }
                        List<String> horasDisponibles = crearHorasDisponibles(fecha,pista,arrayReservas);

                        ArrayList<Reserva> arrayList = new ArrayList<>();
                        for (int i = 0;i<horasDisponibles.size();i++){
                            arrayList.add(new Reserva(pista,horasDisponibles.get(i)));
                        }

                        ReservaAdapter reservaAdapter = new ReservaAdapter(AvailabilityActivity.this,arrayList);

                        lvReserva = findViewById(R.id.lvReserva);
                        lvReserva.setAdapter(reservaAdapter);

                        lvReserva.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                final String selectedPista = ((TextView) view.findViewById(R.id.txtPista)).getText().toString();
                                final String selectedFecha = ((TextView) view.findViewById(R.id.txtHora)).getText().toString();
                                AlertDialog.Builder builder = new AlertDialog.Builder(AvailabilityActivity.this);
                                builder.setCancelable(true);
                                builder.setTitle("Confirmación reserva");
                                builder.setMessage("¿Quieres reservar la " + selectedPista + " \nel " + selectedFecha + "?");
                                builder.setPositiveButton("Confirma", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        crearReserva(selectedFecha, user,selectedPista);
                                    }
                                });
                                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Error al reservar la pista",Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("pistasReservadas","true");
                params.put("fecha",fecha.substring(0,9).trim());
                params.put("pista", pista.trim());
                return  params;
            }
        };
        Volley.newRequestQueue(this).add(request);

    }

//función para enviar correo electrónico una vez de haya efectuado la reserva
    @SuppressLint("IntentReset")
    private void enviarCorreo(String user, String fecha, String pista){
        String toEmail = user;
        List toEmailList = Arrays.asList(toEmail.split("\\s*,\\s*"));

        String emailBody = "Reserva efectuada en la "+ pista + " el dia " + fecha + ". Gracias por confiar en PCP.";

        new SendMailTask(AvailabilityActivity.this).execute("felixpj1996@gmail.com",
                "31Tipsa18*", toEmailList, "Confirmación Reserva PCP", emailBody);
    }
}
