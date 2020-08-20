package com.smartseals.generic.Dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.smartseals.generic.Basedato.GenericAppBaseDato;
import com.smartseals.generic.Basedato.GenericAppContract;
import com.smartseals.generic.Modelo.UsuarioBean;
import com.smartseals.generic.utils.Utils;

import java.util.ArrayList;

public abstract class GenericDao<T> implements GenericActionDao {
	protected static String TAG;

	protected  final int DEBUG = 1;
	protected  final int INFO = 2;
	protected  final int ERROR = 3;

	protected GenericAppBaseDato dbProvider;
	protected SQLiteDatabase ourDatabase;
	protected Context mContext;

	protected UsuarioBean usuarioBean;

	protected GenericDao(Context ctx) {
		dbProvider = GenericAppBaseDato.getInstance(ctx);
		ourDatabase = dbProvider.getMyWritableDatabase();
		mContext = ctx;

		TAG = ctx.getClass().getSimpleName();

		usuarioBean = getUserBean();
	}

	public void beginTransaction() {
		ourDatabase.beginTransaction();
	}

	public void beginTransactionNonExclusive() {
		ourDatabase.beginTransactionNonExclusive();
	}

	public void setTransactionSuccessful() {
		ourDatabase.setTransactionSuccessful();
	}

	public void endTransaction() {
		ourDatabase.endTransaction();
	}

	public boolean enableWriteAheadLogging() {
		return ourDatabase.enableWriteAheadLogging();
	}

	public void disableWriteAheadLogging() {
		ourDatabase.disableWriteAheadLogging();
	}

	public boolean isWriteAheadLoggingEnabled() {
		return ourDatabase.isWriteAheadLoggingEnabled();
	}

	public boolean isDbLockedByCurrentThread() {
		return ourDatabase.isDbLockedByCurrentThread();
	}

	public boolean inTransaction() {
		return ourDatabase.inTransaction();
	}

	protected void log(int nombreLog, Exception e) {
		switch (nombreLog) {
			case DEBUG:
				Log.d(TAG, e.toString(), e);
				break;
			case INFO:
				Log.i(TAG, e.toString(), e);
				break;
			case ERROR:
				Utils.log(TAG, e);
				break;
		}
	}

	protected boolean isTextVacio(String texto) {
		return (texto == null || texto.isEmpty());
	}

	public UsuarioBean getUserBean() {
		UsuarioBean usuarioBean = null;
		try {
			String sql = "SELECT * FROM " + GenericAppBaseDato.Tables.USUARIO;

			Cursor cursor = ourDatabase.rawQuery(sql, null);

			ArrayList<UsuarioBean> listaUsuarioBean = new ArrayList<>();

			if (cursor != null) {
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
					usuarioBean = new UsuarioBean();

					usuarioBean.setUsername(cursor.getString(cursor.getColumnIndex(GenericAppContract.UsuarioColumn.NOMBRE_USUARIO)));


					listaUsuarioBean.add(usuarioBean);
				}
				if (!cursor.isClosed()) {
					cursor.close();
				}

				if (listaUsuarioBean != null && listaUsuarioBean.size() == 1) {
					usuarioBean = listaUsuarioBean.get(0);
				}
			}
		} catch (Exception e) {
			log(ERROR, e);
		}
		return usuarioBean;
	}

    @Override
    public boolean existe(String tabla, String column, String dato) {
        boolean existe = false;

        String sql = "select " + GenericAppContract.BaseColumn.ID + " from " + tabla + " where " + column + " = '" + dato + "'";

        Cursor cursor = ourDatabase.rawQuery(sql, null);

        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                long id = cursor.getInt(cursor.getColumnIndex(GenericAppContract.BaseColumn.ID));
                if (id > 0) {
                    existe = true;
                }
            }
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        return existe;
    }

	@Override
	public synchronized boolean existe(String tabla, String column, long dato) {
		boolean existe = false;

		String sql = "select " + GenericAppContract.BaseColumn.ID + " from " + tabla + " where " + column + " = ?";

		Cursor cursor = ourDatabase.rawQuery(sql, new String[] {String.valueOf(dato)});

		if (cursor != null) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				long id = cursor.getInt(cursor.getColumnIndex(GenericAppContract.BaseColumn.ID));
				if (id > 0) {
					existe = true;
				}
			}
			if (!cursor.isClosed()) {
				cursor.close();
			}
		}
		return existe;
	}

	public void dummyData() {

	}

	public void clearInstance() {

	}
}