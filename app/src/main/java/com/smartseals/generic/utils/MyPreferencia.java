package com.smartseals.generic.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.smartseals.generic.Parametro.Parametro;

import java.util.Calendar;
import java.util.Date;

public class MyPreferencia {
    private static MyPreferencia ourInstance = null;
    private SharedPreferences mSharedPreferences;
    public static final String BYTES_SENT = "BytesSent";
    public static final String BYTES_ARRIVAL = "BytesArrival";
    public static final String DIA_CICLO = "DiaCiclo";
    public static final String ULTIMO_RECETEO_CICLO = "ultimoRecetoCiclo";


    private final String VERSION_APP = "versionApp";
    private final String VERSION_MENSAJE = "versionMensaje";
    private final String TOKEN = "token";



    private Context mContext;

    private MyPreferencia(Context mContext) {
        this.mContext = mContext;
        mSharedPreferences = mContext.getSharedPreferences(Parametro.PREFERENCIA, Context.MODE_PRIVATE);
    }

    public static MyPreferencia getInstance(Context mContext) {
        if (ourInstance == null) {
            ourInstance = new MyPreferencia(mContext);
        }
        return ourInstance;
    }

    public  String getToken() {
        return mSharedPreferences.getString(TOKEN, "NOK");
    }

    public void setToken(String token) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(TOKEN, token);
        editor.commit();
    }

    private boolean tiempoUltimoReceteo() {
        long ultimoReceto = mSharedPreferences.getLong(ULTIMO_RECETEO_CICLO, -1);

        if (ultimoReceto > 0) {
            Calendar calendar = Calendar.getInstance();
            int diaActual = calendar.get(Calendar.DAY_OF_MONTH);

            calendar.setTimeInMillis(ultimoReceto);

            int diaReceteo = calendar.get(Calendar.DAY_OF_MONTH);

            double dias = (diaActual - diaReceteo);
            return dias != 0;
        } else {
            return true;
        }

    }

    public int getBytesSent() {
        return mSharedPreferences.getInt(BYTES_SENT, 0);
    }

    public void setBytesSent(int bytesSent) {

        Calendar c = Calendar.getInstance();
        int mesDias = c.getInstance().getActualMaximum(c.DAY_OF_MONTH);
        int diaHoy = c.get(Calendar.DAY_OF_MONTH);
        int ciclo = getDiaCliclo();

        SharedPreferences.Editor editor = mSharedPreferences.edit();

        if (((diaHoy == ciclo) || (mesDias < ciclo && diaHoy == mesDias)) && tiempoUltimoReceteo()) {
            editor.putInt(BYTES_SENT, bytesSent);
            editor.putInt(BYTES_ARRIVAL, 0);
            editor.putLong(ULTIMO_RECETEO_CICLO, new Date().getTime());
            editor.commit();
        } else {
            editor.putInt(BYTES_SENT, bytesSent + getBytesSent());
            editor.commit();
        }

    }

    public int getDiaCliclo() {
        return mSharedPreferences.getInt(DIA_CICLO, 1);
    }




    public void setVersionActualizada(boolean isActualizada) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(VERSION_APP, isActualizada);
        editor.commit();
    }

    public void setVersionMensaje(String versionMensaje) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(VERSION_MENSAJE, versionMensaje);
        editor.commit();
    }
}
