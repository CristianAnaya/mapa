package com.smartseals.generic.Basedato;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.smartseals.generic.Modelo.Descargo;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    private static final String NOMBRE_DB="appGeneric.db";
    private static final int VERSION_DB = 4;
    private static final String TABLA_DESCARGOS=
            "CREATE TABLE DESCARGOS(ID_ODT TEXT PRIMARY KEY NOT NULL," +
                    "ID_USUARIO TEXT,"+
                    " INICIO_FECHA TEXT," +
                    " FIN_FECHA TEXT)";

    public Database(@Nullable Context context) {
        super(context, NOMBRE_DB, null, VERSION_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLA_DESCARGOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS"+TABLA_DESCARGOS);
        db.execSQL(TABLA_DESCARGOS);
    }

    public void agregarDescargos(String idUsuario,String odtID, String inicioFecha, String finFecha){
        SQLiteDatabase db = getWritableDatabase();
        if (db != null){
            db.execSQL("INSERT INTO DESCARGOS VALUES('"+odtID+"','"+idUsuario+"','"+inicioFecha+"','"+finFecha+"')");
            db.close();
        }
    }

    public List<Descargo> obtenerDescargos(String idUsuario){
        SQLiteDatabase bd = getReadableDatabase();
        Cursor cursor = bd.rawQuery("SELECT * FROM DESCARGOS WHERE ID_USUARIO='"+idUsuario+"'",null);
        List<Descargo> descargos = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                descargos.add(new Descargo(cursor.getString(0),cursor.getString(2),
                        cursor.getString(3)));
            }while (cursor.moveToNext());
        }
        return descargos;
    }

    public void eliminarDescargo(String odtId){
        SQLiteDatabase bd = getWritableDatabase();
        if (bd != null){
            bd.execSQL("DELETE FROM DESCARGOS WHERE ID_ODT='"+odtId+"'");
            bd.close();
        }
    }
}

