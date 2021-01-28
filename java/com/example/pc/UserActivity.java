package com.example.pc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
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

public class UserActivity extends AppCompatActivity {

    ListView lvActualidad;
    ImageButton IBreservaPista;
    private static final String getNoticias = "http://192.168.1.136:80/padelcenter/getNoticias.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        IBreservaPista = findViewById(R.id.IBreservaPista);
        Intent intent = getIntent();

        final String user = intent.getStringExtra("username");
        recuperarNoticias();
        IBreservaPista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserActivity.this,ReservationActivity.class);
                intent.putExtra("username",user);
                startActivity(intent);
            }
        });


    }
//función que recupera las noticias y las muestra en el listview de actualidad
    private void recuperarNoticias(){
        final ArrayList<Actualidad> arrayNoticias = new ArrayList<>();
        StringRequest request=new StringRequest(Request.Method.POST, getNoticias, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("error")){
                    try {
                        JSONArray array = new JSONArray(response);

                        for (int i = 0; i < array.length();i++){
                            JSONObject noticia = array.getJSONObject(i);

                            arrayNoticias.add(new Actualidad(noticia.getString("titulo"),
                                    noticia.getString("texto")));
                        }
                        ActualidadAdapter actualidadAdapter = new ActualidadAdapter(UserActivity.this,arrayNoticias);
                        lvActualidad = findViewById(R.id.lvActualidad);
                        lvActualidad.setAdapter(actualidadAdapter);

                    }catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    Toast.makeText(getApplicationContext(),"Error al mostrar las noticias", Toast.LENGTH_LONG).show();
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
                params.put("getNoticias","true");
                return  params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
}
