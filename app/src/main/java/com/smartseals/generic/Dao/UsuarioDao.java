package com.smartseals.generic.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


import com.smartseals.generic.Basedato.GenericAppBaseDato;
import com.smartseals.generic.Basedato.GenericAppContract;
import com.smartseals.generic.Modelo.UsuarioBean;

import java.util.ArrayList;

/**
 * Created by Zait Paulo on 25/01/2019.
 */
public class UsuarioDao extends GenericDao {

    private static UsuarioDao mInstance = null;

    protected UsuarioDao(Context context) {
        super(context);
        mContext = context;
    }

    public static UsuarioDao getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new UsuarioDao(context.getApplicationContext());
        }
        return mInstance;
    }

    /**
     * Mapea los datos del agente al ContentValues
     * proporcionado
     *
     * @param usuarioBean El Agente logeado en la aplicacion con los datos que envio el servidor.
     * @return ContentValues con los datos del agente listos para enviar a la base de datos
     * encuentra
     */
    private ContentValues toContentValues(UsuarioBean usuarioBean) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(GenericAppContract.UsuarioColumn.USUARIO, usuarioBean.getUsername());


        return contentValues;
    }


    public void deleteAllData() {
        try {
            ourDatabase.delete(GenericAppBaseDato.Tables.USUARIO, null, null);
        } catch (Exception e) {
            log(ERROR, e);
        }
    }

    public long insert(UsuarioBean UserBean) {
        long rowId = -1;
        try {
            ContentValues contentValues = toContentValues(UserBean);

            rowId = ourDatabase.insert(GenericAppBaseDato.Tables.USUARIO, null, contentValues);
        } catch (Exception e) {
            log(ERROR, e);
        }
        return rowId;
    }

    public UsuarioBean getUserBean() {
        UsuarioBean usuarioBean = null;
        try {
            String sql = "SELECT * FROM " + GenericAppBaseDato.Tables.USUARIO;
            Cursor cursor = ourDatabase.rawQuery(sql, null);
            usuarioBean = toEntity(cursor);
        } catch (Exception e) {
            log(ERROR, e);
        }
        return usuarioBean;
    }

    public UsuarioBean toEntity(Cursor cursor) {
        UsuarioBean usuarioBean = null;
        if (cursor != null) {
            ArrayList<UsuarioBean> listaUsuario = toListOfEntities(cursor);
            if (listaUsuario != null && listaUsuario.size() == 1) {
                usuarioBean = listaUsuario.get(0);
            }
        }
        return usuarioBean;
    }

    @Override
    public ArrayList<UsuarioBean> toListOfEntities(Cursor cursor) {
        UsuarioBean usuarioBean;
        ArrayList<UsuarioBean> listaUsuarioBean = new ArrayList<>();
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                usuarioBean = new UsuarioBean();

                usuarioBean.setUsername(cursor.getString(cursor.getColumnIndex(GenericAppContract.UsuarioColumn.USUARIO)));

                listaUsuarioBean.add(usuarioBean);
            }
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        return listaUsuarioBean;
    }

    @Override
    public void clearInstance() {
        super.clearInstance();

        mInstance = null;
    }
}