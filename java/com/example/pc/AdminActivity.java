package com.example.pc;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {
    ListView lvReserva;
    Button btnAdd;
    EditText edTitulo, edTexto;
    private static final String getReservas = "http://192.168.1.136:80/padelcenter/getReservas.php";
    private static final String setNoticia = "http://192.168.1.136:80/padelcenter/setNoticia.php";
    private static final String deleteReserva = "http://192.168.1.136:80/padelcenter/eliminarReserva.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);

        recuperarReservas();

        btnAdd = findViewById(R.id.bt_introducir);
        edTexto = findViewById(R.id.etTexto);
        edTitulo = findViewById(R.id.etTitulo);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String texto = edTexto.getText().toString().trim();
                 String titulo = edTitulo.getText().toString();
                if(!texto.isEmpty() && !titulo.isEmpty()){

                    insertarNoticia(titulo,texto);
                }
            }
        });


    }
//Función para eliminar una reserva cuando damos click sobre un elemento del listview
    private void eliminarReserva(final String fecha, final String pista){
        StringRequest request=new StringRequest(Request.Method.POST, deleteReserva, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),"Pista Eliminada",Toast.LENGTH_LONG).show();
                recuperarReservas();
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
                params.put("eliminarReserva","true");
                params.put("fecha",fecha.trim());
                params.put("pista", pista.trim());
                return  params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
    //funcion para recuperar todas aquellas pistas reservadas y mostrarlas en el listview
    private void recuperarReservas() {
        final ArrayList<Reserva> arrayReservas = new ArrayList<>();
        StringRequest request=new StringRequest(Request.Method.POST, getReservas, new Response.Listener<String>() {
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
                        ReservaAdapter reservaAdapter = new ReservaAdapter(AdminActivity.this,arrayReservas);
                        lvReserva = findViewById(R.id.lvReservas);
                        lvReserva.setAdapter(reservaAdapter);

                        lvReserva.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                final String selectedPista = ((TextView)view.findViewById(R.id.txtPista)).getText().toString();
                                final String selectedFecha = ((TextView)view.findViewById(R.id.txtHora)).getText().toString();

                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                                builder.setCancelable(true);
                                builder.setTitle("Eliminar Reserva");
                                builder.setMessage("¿Seguro que quieres eliminar esta reserva?");
                                builder.setPositiveButton("Confirma", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        eliminarReserva(selectedFecha,selectedPista);
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


                    }catch (JSONException e) {
                        e.printStackTrace();
                }

            }
            else{
                    Toast.makeText(getApplicationContext(),"Error al mostrar las pistas reservadas", Toast.LENGTH_LONG).show();
                }
        }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("getReservas","true");
                return  params;
            }
        };
        Volley.newRequestQueue(this).add(request);

    }
//función para insertar una nueva noticia
    private void insertarNoticia(final String titulo, final String texto){
        StringRequest request=new StringRequest(Request.Method.POST, setNoticia, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("success")){
                    Toast.makeText(getApplicationContext(),"Noticia insertada",Toast.LENGTH_LONG).show();
                    edTexto.setText("");
                    edTitulo.setText("");
                }else{
                    Toast.makeText(getApplicationContext(),"Error al añadir la noticia",Toast.LENGTH_LONG).show();
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
                params.put("new","true");
                params.put("titulo",titulo.trim());
                params.put("texto", texto.trim());
                return  params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
}
