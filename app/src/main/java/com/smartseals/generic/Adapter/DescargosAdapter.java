package com.smartseals.generic.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.smartseals.generic.Modelo.Descargo;
import com.smartseals.generic.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class DescargosAdapter extends ArrayAdapter<Descargo> {

    private Context context;
    private List<Descargo> descargoList;
    Calendar calendar =  Calendar.getInstance();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    String fecha = format.format(calendar.getTime());

    public DescargosAdapter(Context context, List<Descargo> descargoList) {
        super(context, R.layout.layout_trabajos);
        this.context = context;
        this.descargoList = descargoList;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_trabajos,null,true);
        TextView txtOdt = view.findViewById(R.id.txtODT);
        TextView txtFechaInicioPlanificado = view.findViewById(R.id.txtFechaInicioPlanificada);
        TextView txtFechaFinPlanificado = view.findViewById(R.id.txtFechaFinPlanificada);
        LinearLayout linearLayout = view.findViewById(R.id.linearDescargo);


            linearLayout.setVisibility(View.VISIBLE);
            txtOdt.setText(descargoList.get(position).getOdt());
            txtFechaInicioPlanificado.setText(descargoList.get(position).getFechaInicio());
            txtFechaFinPlanificado.setText(descargoList.get(position).getFechaFin());

        return view;

    }

    @Override
    public int getCount() {
        return descargoList.size();
    }

    @Nullable
    @Override
    public Descargo getItem(int position) {
        return descargoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getItemCount() {
        return descargoList == null ? 0 : descargoList.size();
    }

    public boolean empty() {
        return getItemCount() == 0;
    }


}
