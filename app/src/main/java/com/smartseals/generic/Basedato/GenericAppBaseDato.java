package com.smartseals.generic.Basedato;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.smartseals.generic.Basedato.GenericAppContract.UsuarioColumn;
import com.smartseals.generic.Basedato.GenericAppContract.DescargoColumn;
import com.smartseals.generic.Basedato.GenericAppContract.LogColumns;
import com.smartseals.generic.Basedato.GenericAppContract.OrdenDeTrabajoColumn;
import com.smartseals.generic.utils.Utils;


public class GenericAppBaseDato extends SQLiteOpenHelper {
    private static final String TAG = GenericAppBaseDato.class.getSimpleName();

    private static Context mContext;
    private static SQLiteDatabase myWritableDb;

    public static final String DATABASE_NAME = "SmartSealGeneric.db";
    private static final int DATABASE_VERSION = 1;

    private static GenericAppBaseDato mInstance = null;

    public interface Tables {
        String USUARIO = "Usuario";
        String ORDEN_DE_TRABAJO = "OrdenDeTrabajo";
        String DESCARGO = "Descargo";
        String ELEMENTO = "Elemento";
        String VISITA_ELEMENTO = "VisitaElemento";
        String ESTADO_ELEMENTO = "EstadoElemento";
        String NOMBRE_ARBOL = "NombreArboles";
        String ALERTAS = "Alertas";
        String ELEMENTO_REPORTE = "ElementoReporte";
        String VISITA_ELEMENTO_REPORTE = "VisitaElementoReporte";
        String APOYO_PODA = "ApoyoPoda";
        String LOG = "Log";
    }

    private GenericAppBaseDato(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    public static GenericAppBaseDato getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new GenericAppBaseDato(ctx);
        }
        mContext = ctx;
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /** Creando las tablas de la base de datos **/
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.USUARIO + " ("
                    + UsuarioColumn.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + UsuarioColumn.USUARIO_ID + " INTEGER NOT NULL, "
                    + UsuarioColumn.BRIGADA_ID + " INTEGER, "
                    + UsuarioColumn.USUARIO + " INTEGER NOT NULL, "
                    + UsuarioColumn.NOMBRE_USUARIO + " TEXT NOT NULL, "
                    + UsuarioColumn.APELLIDO_USUARIO + " TEXT, "
                    + UsuarioColumn.ROL_ID + " INTEGER NOT NULL, "
                    + UsuarioColumn.NOMBRE_ROL + " TEXT NOT NULL, "
                    + UsuarioColumn.SECTOR_ID + " INTEGER NOT NULL, "
                    + UsuarioColumn.NOMBRE_SECTOR + " TEXT NOT NULL, "
                    + UsuarioColumn.SIGLA_SECTOR + " TEXT NOT NULL, "
                    + UsuarioColumn.ELIMINADO + " INTEGER DEFAULT 0, "
                    + UsuarioColumn.EMPRESA + " TEXT NOT NULL, "
                    + UsuarioColumn.FECHA_SISTEMA + " INTEGER)"
            );


            db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.ORDEN_DE_TRABAJO + " ("
                    + OrdenDeTrabajoColumn.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + OrdenDeTrabajoColumn.ODT_ID + " INTEGER NOT NULL, "
                    + OrdenDeTrabajoColumn.CODIGO_ODT + " TEXT NOT NULL, "
                    + OrdenDeTrabajoColumn.DESCARGO + " TEXT, "
                    + OrdenDeTrabajoColumn.CIRCUITO_ID + " INTEGER NOT NULL, "
                    + OrdenDeTrabajoColumn.MATRICULA_CIRCUITO + " TEXT, "
                    + OrdenDeTrabajoColumn.USUARIO_ID + " INTEGER NOT NULL, "
                    + OrdenDeTrabajoColumn.FECHA_INICIO_PLANIFICADA + " INTEGER, "
                    + OrdenDeTrabajoColumn.FECHA_FIN_PLANIFICADA + " INTEGER, "
                    + OrdenDeTrabajoColumn.FECHA_INICIO_REAL + " INTEGER, "
                    + OrdenDeTrabajoColumn.FECHA_FIN_REAL + " INTEGER, "
                    + OrdenDeTrabajoColumn.ELEMENTO_PLANIFICADO + " INTEGER DEFAULT 0, "
                    + OrdenDeTrabajoColumn.SINCRONIZADO + " INTEGER DEFAULT 0, "
                    + OrdenDeTrabajoColumn.IS_PLANIFICADO + " INTEGER DEFAULT 0, "
                    + OrdenDeTrabajoColumn.USUARIO_ID_ASIGANDO + " INTEGER DEFAULT 0, "
                    + OrdenDeTrabajoColumn.REPORTADO + " INTEGER DEFAULT 0, "
                    + OrdenDeTrabajoColumn.ELIMINADO + " INTEGER DEFAULT 0, "
                    + OrdenDeTrabajoColumn.FECHA_SISTEMA + " INTEGER)"
            );

            db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.DESCARGO + " ("
                    + DescargoColumn.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DescargoColumn.DESCARGO_ID + " INTEGER NOT NULL, "
                    + DescargoColumn.ODT_ID + " INTEGER NOT NULL, "
                    + DescargoColumn.FECHA_TRABAJO + " INTEGER, "
                    + DescargoColumn.HORA_INICIO + " TEXT, "
                    + DescargoColumn.HORA_FIN + " TEXT, "
                    + DescargoColumn.CODIGO_DESCARGO + " TEXT, "
                    + DescargoColumn.ELIMINADO + " INTEGER DEFAULT 0, "
                    + DescargoColumn.USUARIO_ID_ASIGANDO + " INTEGER DEFAULT 0, "
                    + DescargoColumn.PERSONAL_INSCRITO + " INTEGER DEFAULT 0, "
                    + DescargoColumn.ELEMENTOS_DE_SEGURIDAD + " INTEGER DEFAULT 0, "
                    + DescargoColumn.DESCARGO_FINALIZADO + " INTEGER DEFAULT 0, "
                    + DescargoColumn.CONTADOR_GPS + " INTEGER DEFAULT 0, "
                    + DescargoColumn.FECHA_SISTEMA + " INTEGER)"
            );


            db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.LOG + " ("
                    + LogColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + LogColumns.TAG + " TEXT,"
                    + LogColumns.EXCEPTION + " TEXT,"
                    + LogColumns.CAUSA + " TEXT,"
                    + LogColumns.MENSAJE + " TEXT,"
                    + LogColumns.DEVICEID + " TEXT,"
                    + LogColumns.USERNAME + " TEXT,"
                    + LogColumns.VERSION_ANDROID + " TEXT,"
                    + LogColumns.VERSION_CODE + " TEXT,"
                    + LogColumns.VERSION_NAME + " TEXT,"
                    + LogColumns.GUID + " TEXT NOT NULL,"
                    + LogColumns.FECHA_SISTEMA + " TEXT NOT NULL,"
                    + LogColumns.ELIMINADO + " INTEGER NOT NULL DEFAULT 0,"
                    + LogColumns.SINCRONIZADO + " INTEGER  DEFAULT 0)");
        }catch(Exception e){
        Utils.log(TAG, e);
    }

}
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Actualizacion de la base de datos

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Para cuando se degrade la version de la base de datos
    }

    /**
     * Retorna una instancia de escritura hacia la base de datos con el fin
     * de no estar abriendo objetos SQLiteDatabase de forma simultanea.
     */
    public SQLiteDatabase getMyWritableDatabase() {
        if ((myWritableDb == null) || (!myWritableDb.isOpen())) {
            try {
                myWritableDb = mInstance.getWritableDatabase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return myWritableDb;
    }

    @Override
    public synchronized void close() {
        super.close();

        if (myWritableDb != null) {
            myWritableDb.close();
            myWritableDb = null;
        }
    }
}