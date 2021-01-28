package com.example.pc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView signUp;
    EditText username,userpassword;
    Button btnLogin;

    //private static final String loginUrl = "http://10.0.2.2/padelcenter/login.php";
    //private static final String getName = "http://10.0.2.2/padelcenter/getName.php";
    private static final String getName = "http://192.168.1.136:80/padelcenter/getName.php";
    private static final String loginUrl = "http://192.168.1.136:80/padelcenter/login.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username=findViewById(R.id.et_correo);
        userpassword=findViewById(R.id.et_contrasena);
        signUp=findViewById(R.id.bt_crear);

        btnLogin=findViewById(R.id.bt_iniciar);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Llamamos a la funcion que realiza el login
                userLogin();
            }
        });
    }

    //Realiza el login del usuario
    public void userLogin(){
        StringRequest request = new StringRequest(StringRequest.Method.POST, loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("error")){
                    if(response.equals("1")){
                        Intent intent = new Intent(getApplicationContext(),AdminActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(getApplicationContext(),UserActivity.class);
                        intent.putExtra("username",username.getText().toString());
                        startActivity(intent);
                    }


                }else{
                    Toast.makeText(getApplicationContext(),"Invalid username or Password",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("loginuser","true");
                params.put("email",username.getText().toString().trim());
                params.put("password",userpassword.getText().toString().trim());
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

}
