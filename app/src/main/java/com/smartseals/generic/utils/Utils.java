package com.smartseals.generic.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.smartseals.generic.Dao.LogDao;
import com.smartseals.generic.GenericApp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    private static final boolean swLog = true;


    public static void log(String tag, Throwable exception) {
        if (swLog) {
            Log.e(tag, exception.toString(), exception);
        }

        try {
            LogDao logDao = LogDao.getInstance(GenericApp.getContext());
            logDao.insertarLog(tag, exception);
        } catch (Exception e) {
            Log.e(tag, exception.toString(), exception);
        }
    }

    public static void log(String tag, String mensaje) {
        if (swLog) {
            Log.e(tag, mensaje);
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String dateToString(Date fecha) {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        String cadenaFecha = df.format(fecha);

        return cadenaFecha;
    }

    public static boolean isOnline(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
