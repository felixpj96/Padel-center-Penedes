package com.example.pc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ActualidadAdapter extends ArrayAdapter<Actualidad> {

    public ActualidadAdapter(Context context, ArrayList<Actualidad> objects) {
        super(context, 0, objects);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Actualidad actualidad = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_news,parent,false);
        }

        TextView txtTitulo = convertView.findViewById(R.id.txtTitulo);
        TextView txtTexto = convertView.findViewById(R.id.txtTexto);

        txtTitulo.setText(actualidad.getTitulo());
        txtTexto.setText(actualidad.getTexto());
        return convertView;
    }
}
