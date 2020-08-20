package com.smartseals.generic.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;

import com.smartseals.generic.Basedato.GenericAppBaseDato;
import com.smartseals.generic.Basedato.GenericAppContract.LogColumns;

import com.smartseals.generic.Basedato.GenericAppContract;
import com.smartseals.generic.GenericApp;
import com.smartseals.generic.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class LogDao extends GenericDao{
	private static final String TAG = LogDao.class.getSimpleName();
	
	private static LogDao mInstance = null;
	
	private Context mContext = GenericApp.getContext();
	
	private static final String COLUMNS = LogColumns.ID + ","
			+ LogColumns.CAUSA + "," 
			+ LogColumns.DEVICEID + "," 
			+ LogColumns.EXCEPTION + ","
			+ LogColumns.GUID + "," 
			+ LogColumns.MENSAJE + "," 
			+ LogColumns.SINCRONIZADO + "," 
			+ LogColumns.TAG + ","
			+ LogColumns.USERNAME + "," 
			+ LogColumns.VERSION_ANDROID + "," 
			+ LogColumns.VERSION_CODE + "," 
			+ LogColumns.VERSION_NAME + "," 
			+ LogColumns.ELIMINADO + "," 
			+ LogColumns.FECHA_SISTEMA;

	private LogDao(Context c) {
		super(c);
		mContext = c;
	}
	
	public static LogDao getInstance(Context ctx) {
		if (mInstance == null) {
			mInstance = new LogDao(ctx.getApplicationContext());
		}
		
		return mInstance;
	}
	
	private ContentValues toInsert(String tag, Throwable exception) {
		ContentValues contenido = new ContentValues();
		try {

			contenido.put(GenericAppContract.LogColumns.TAG, tag);

			try {
				if (exception.getCause() != null) {
					contenido.put(GenericAppContract.LogColumns.EXCEPTION, exception.getCause().toString());
				} else {
					if (exception.getLocalizedMessage() != null) {
						contenido.put(GenericAppContract.LogColumns.EXCEPTION, exception.getLocalizedMessage());
					} else {
						contenido.put(GenericAppContract.LogColumns.EXCEPTION, "Vacia");
					}
				}
			} catch (Exception e) {
				contenido.put(LogColumns.EXCEPTION, "Error");
			}

			Writer writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			exception.printStackTrace(printWriter);
			contenido.put(LogColumns.CAUSA, writer.toString());
			if (exception.getMessage() != null) {
				contenido.put(LogColumns.MENSAJE, exception.getMessage());
			} else {
				contenido.put(LogColumns.MENSAJE, "No mensaje");
			}
			contenido.put(LogColumns.DEVICEID, GenericApp.getDeviceId());
			contenido.put(LogColumns.USERNAME, usuarioBean.getUsername());
			contenido.put(LogColumns.VERSION_ANDROID, Build.VERSION.SDK_INT);
			contenido.put(LogColumns.VERSION_CODE, mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode);
			contenido.put(LogColumns.VERSION_NAME, mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName);
			contenido.put(LogColumns.GUID, UUID.randomUUID().toString());
			contenido.put(LogColumns.FECHA_SISTEMA, Utils.dateToString(new Date()));
			contenido.put(LogColumns.ELIMINADO, 0);
		} catch (NullPointerException e) {
			Utils.log(TAG, e.getMessage());
		} catch (Exception e) {
			Utils.log(TAG, e.getMessage());
		}
		return contenido;
	}

	public int insertarLog(String tag, Throwable exception) {
		int rowId = -1;
		ContentValues contenido = null;
		try {
			contenido = toInsert(tag, exception);

			if (!ourDatabase.inTransaction()) {
				rowId = (int) ourDatabase.insert(GenericAppBaseDato.Tables.LOG, null, contenido);
			}

			if (rowId >= 500) {
				deleteAllDataSincronizado();
			}
		} catch (Exception e) {
			insertarLog(contenido);
		}
		return rowId;
	}

	public int insertarLog(ContentValues contenido) {
		int rowId = -1;
		try {
			if (contenido != null) {
				rowId = (int) ourDatabase.insert(GenericAppBaseDato.Tables.LOG, null, contenido);
			}

			if (rowId >= 500) {
				deleteAllDataSincronizado();
			}
		} catch (Exception e) {
			Utils.log(TAG, e.getMessage());
		}
		return rowId;
	}

	public void deleteAllDataSincronizado() {
		try {
			ourDatabase.delete(GenericAppBaseDato.Tables.LOG, LogColumns.SINCRONIZADO + " = 0", null);
		} catch (Exception e) {
			Utils.log(TAG, e.getMessage());
		}
	}

	public String ejecutarScrip(String scrip) {
		String swEjecutado = "OK";
		try {
			beginTransaction();
			ourDatabase.execSQL(scrip);
			setTransactionSuccessful();
		} catch (SQLiteException e) {
			Utils.log(TAG, e.getMessage());
			swEjecutado = e.getMessage();
		} catch (Exception e) {
			Utils.log(TAG, e.getMessage());
			swEjecutado = e.getMessage();
		} finally {
			endTransaction();
		}
		return swEjecutado;
	}

	public Cursor getNoSync() {
		Cursor cursor = null;
		try {
			String sql = "select " + COLUMNS + " from " + GenericAppBaseDato.Tables.LOG + " where " + LogColumns.SINCRONIZADO + " = 0 LIMIT 100";
			cursor = ourDatabase.rawQuery(sql, null);
		} catch (Exception e) {
			Utils.log(TAG, e.getMessage());
		}
		return cursor;
	}

	public boolean marcarErrorSincronizadoWhitBulkInsert(JSONArray listaGuidSincronizados) {
		boolean swResultado = false;
		try {
			ourDatabase.beginTransaction();
			String sql = "update " + GenericAppBaseDato.Tables.LOG + " set " + LogColumns.SINCRONIZADO + "=1 where " + LogColumns.GUID + "=?";
			SQLiteStatement insert = ourDatabase.compileStatement(sql);
			int tamano = listaGuidSincronizados.length();
			for (int i = 0; i < tamano; i++) {
				insert.bindString(1, listaGuidSincronizados.getString(i));
				insert.execute();
			}
			ourDatabase.setTransactionSuccessful();
			swResultado = true;
		} catch (SQLiteException e) {
			Utils.log(TAG, e.getMessage());
			swResultado = false;
		} catch (JSONException e) {
			Utils.log(TAG, e.getMessage());
			swResultado = false;
		} catch (Exception e) {
			Utils.log(TAG, e.getMessage());
			swResultado = false;
		} finally {
			ourDatabase.endTransaction();
		}
		return swResultado;
	}
	
	public void eliminarDatosViejos() throws SQLiteException, Exception {
		try {
			ourDatabase.delete(GenericAppBaseDato.Tables.LOG, LogColumns.SINCRONIZADO + "=?", new String[] { "1" });
		} catch (Exception e) {
			Utils.log(TAG, e.getMessage());
		}
	}

	public Cursor ejecutarConsultaByServer(String sql) {
		Cursor cursor = null;
		try {
			cursor = ourDatabase.rawQuery(sql, null);
		} catch (SQLiteException e) {
			Utils.log(TAG, e.getMessage());
		}
		return cursor;
	}

	@Override
	public Object toEntity(Cursor cursor) {
		return null;
	}

	@Override
	public ArrayList toListOfEntities(Cursor cursor) {
		return null;
	}
}