package com.example.dam.zkeep;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dam.zkeep.pojo.Keep;

import java.util.ArrayList;

public class Adaptador extends ArrayAdapter<Keep> {

    private Context ctx;
    private int res;
    private LayoutInflater lInflator;
    private ArrayList<Keep> values;

    static class ViewHolder {
        TextView tv1, tv2;
    }

    public Adaptador(Context context, ArrayList<Keep> objects) {
        super(context, R.layout.elemento_lista, objects);
        this.ctx = context;
        this.res = R.layout.elemento_lista;
        this.lInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.values = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = new ViewHolder();

        if (convertView == null) {
            convertView = lInflator.inflate(res, null);
            TextView tv = (TextView) convertView.findViewById(R.id.tv_l);
            vh.tv1 = tv;
            tv = (TextView) convertView.findViewById(R.id.tv_m);
            vh.tv2 = tv;
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.tv1.setText(values.get(position).getContenido());
        vh.tv2.setText(Integer.toString(values.get(position).getId()));


        return convertView;
    }

}
