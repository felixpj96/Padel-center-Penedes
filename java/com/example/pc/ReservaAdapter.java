package com.example.pc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ReservaAdapter extends ArrayAdapter<Reserva> {

    public ReservaAdapter(Context context, ArrayList<Reserva> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent) {

        Reserva reserva = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row,parent,false);
        }

        TextView txtPista = convertView.findViewById(R.id.txtPista);
        TextView txtHora = convertView.findViewById(R.id.txtHora);
        ImageView imgPista = convertView.findViewById(R.id.imgPista);

        txtPista.setText(reserva.getPista());
        txtHora.setText(reserva.getFecha());
        if (txtPista.getText().equals("Pista Central")) {
            imgPista.setImageResource(R.drawable.pistacentral);
        }
        else{
            imgPista.setImageResource(R.drawable.pistaestrella);
        }
        return convertView;
    }
}
