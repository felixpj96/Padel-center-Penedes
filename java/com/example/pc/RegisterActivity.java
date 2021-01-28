package com.example.pc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText name,useremail,userpassword;
    Button BtnRegister, BtnIniciar;

    private static final String registerUrl = "http://192.168.1.136:80/padelcenter/registeruser.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.et_nombre);
        useremail = findViewById(R.id.et_correo);
        userpassword = findViewById(R.id.et_contrasena);
        BtnIniciar = findViewById(R.id.bt_iniciarSesion);
        BtnRegister = findViewById(R.id.bt_CrearCuenta);

        BtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registernewuser();
            }
        });
    }
//función para realizar el registro de un nuevo usuario
    private void registernewuser(){
        StringRequest request=new StringRequest(Request.Method.POST, registerUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("success")){
                    Toast.makeText(getApplicationContext(),"Cuenta Creada",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
               }else{
                    Toast.makeText(getApplicationContext(),"Error al crear la cuenta",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("registeruser","true");
                params.put("email",useremail.getText().toString().trim());
                params.put("password",userpassword.getText().toString().trim());
                params.put("nombre",name.getText().toString().trim());
                return  params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
}
