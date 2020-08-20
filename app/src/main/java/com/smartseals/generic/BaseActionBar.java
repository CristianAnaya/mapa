package com.smartseals.generic;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.smartseals.generic.utils.MyPreferencia;

public class BaseActionBar extends AppCompatActivity {

    protected MyPreferencia myPreferencia;
    protected Context mContext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        GenericApp.setCurrentActivity(this);

        myPreferencia = MyPreferencia.getInstance(mContext);
    }

}
