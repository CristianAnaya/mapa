package com.smartseals.generic.Modelo;

import android.content.Context;

import com.smartseals.generic.utils.Utils;
import com.google.gson.annotations.Expose;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class PlanificacionSync {


    @Expose
    private ArrayList<Descargo> descargo;

    public ArrayList<Descargo> getDescargo() {
        return descargo;
    }

    public void setDescargo(ArrayList<Descargo> descargo) {
        this.descargo = descargo;
    }

    public JSONObject planificacionBrigadaToDataBase(long usuarioId, long brigadaId, Context mContext) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();

            jsonObject.put("id_usuario", usuarioId);
            jsonObject.put("id_brigada", brigadaId);

            int cantidadDescargo = 0;

            try {
                if (descargo != null) {

                } else {
                    jsonObject.put("odt", new JSONArray());
                }
            } catch (Exception e) {
                Utils.log(TAG, e);
            }


        } catch (Exception e) {
            Utils.log(TAG, e);
        }

        return jsonObject;
    }

}
