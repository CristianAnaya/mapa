package com.smartseals.generic;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.firebase.database.FirebaseDatabase;

public class GenericApp extends MultiDexApplication {

    private static final String TAG = GenericApp.class.getSimpleName();

    private static GenericApp instance;
    private static Activity mCurrentActivity = null;
    private static FirebaseDatabase mDatabase;


    public GenericApp() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }




    public static Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public static void setCurrentActivity(Activity mCurrentActivity) {
        GenericApp.mCurrentActivity = mCurrentActivity;
    }

    /**
     * Obtiene el contexto de la aplicacion
     *
     * @return el contexto de la aplicacion
     */

    public static Context getContext() {
        return instance.getApplicationContext();
    }


    /**
     * Obtiene el identificador del dispositivo
     *
     * @return ID del dispositivo actual
     */
    public static String getDeviceId() {
        return Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }



}
