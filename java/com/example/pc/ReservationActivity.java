package com.example.pc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReservationActivity extends AppCompatActivity {


    Spinner spPista, spFecha;
    Button btDisponibilidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        Intent intent = getIntent();

        final String user = intent.getStringExtra("username");

        btDisponibilidad = findViewById(R.id.bt_disponibilidad);
        spPista = findViewById(R.id.spPista);
        spFecha = findViewById(R.id.spFecha);
        final List<String> Pistas = new ArrayList<>();
        Pistas.add("Selecciona una Pista");
        Pistas.add("Pista Central");
        Pistas.add("Pista Estrella DAM");

        final List<String> Fecha = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date dia = Calendar.getInstance().getTime();
        Fecha.add("Selecciona el dia de tu reserva");
        Fecha.add(sdf.format( dia.getTime()));
        Fecha.add(sdf.format( dia.getTime() + 86400000));
        Fecha.add(sdf.format( dia.getTime() + 172800000));

        final ArrayAdapter<String> adapterFecha = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                Fecha );

        adapterFecha.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        spFecha.setAdapter(adapterFecha);
        spFecha.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedItem = "";
                if(!spFecha.getItemAtPosition(position).equals("Selecciona el dia de tu reserva")){
                    if(Fecha.size() == 4){
                        Fecha.remove(0);
                        adapterFecha.notifyDataSetChanged();
                        spFecha.setSelection(position - 1);
                        selectedItem = spFecha.getItemAtPosition(position - 1).toString();
                    }
                    else{
                        selectedItem = spFecha.getItemAtPosition(position).toString();
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final ArrayAdapter<String> adapterPista = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                Pistas );

        adapterPista.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        spPista.setAdapter(adapterPista);

        spPista.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedItem = "";
                if(!spPista.getItemAtPosition(position).equals("Selecciona una Pista")){
                    if(Pistas.size() == 3){
                        Pistas.remove(0);
                        adapterPista.notifyDataSetChanged();
                        spPista.setSelection(position - 1);
                        selectedItem = spPista.getItemAtPosition(position - 1).toString();
                    }
                    else{
                         selectedItem = spPista.getItemAtPosition(position).toString();
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btDisponibilidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReservationActivity.this,AvailabilityActivity.class);
                intent.putExtra("Fecha",spFecha.getSelectedItem().toString());
                intent.putExtra("Pista", spPista.getSelectedItem().toString());
                intent.putExtra("username",user);
                startActivity(intent);
            }
        });
    }
}
