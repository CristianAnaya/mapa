package com.smartseals.generic.Actividades;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.smartseals.generic.Basedato.Database;
import com.smartseals.generic.Modelo.ResponseEndPointBean;
import com.smartseals.generic.Modelo.TrabajoModel;
import com.smartseals.generic.NetworkUtilities.ConexionServer;
import com.smartseals.generic.R;
import com.google.gson.Gson;

public class InformacionDelTrabajo extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = InformacionDelTrabajo.class.getSimpleName();

    private int posicion;
    TextView txtFechaInicioPlanificada, txtFechaFinPlanificada, txtOdt, totalTrabajo;
    Button btnIniciarTrabajo, btnVerMapa, btnDetalle, btnFinalizar;
    private String id;
    private Context mContext;
    private String odtId;
    private iniciarTrabajoTask mInicioTrabajo = null;
    private finalizarTrabajoTask mFinalizarTrabajo = null;
    private String trabajoId;
    private int finalizar;
    Database database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_del_trabajo);
        mContext = this;
        txtFechaFinPlanificada = findViewById(R.id.txtFechaFinPlanificada);
        txtFechaInicioPlanificada = findViewById(R.id.txtFechaInicioPlanificada);
        txtOdt = findViewById(R.id.txtOdtInformacion);
        totalTrabajo = findViewById(R.id.txtTrabajosRealizados);

        btnIniciarTrabajo = findViewById(R.id.btnIniciarTrabajo);
        btnVerMapa = findViewById(R.id.btnVerMapa);
        btnDetalle = findViewById(R.id.btnDetalles);
        btnFinalizar = findViewById(R.id.btnFinalizar);

        database = new Database(getApplicationContext());
        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setCustomView(R.layout.actionbar_editar_perfil_cuenta);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        Button btn_atras_editar_perfil = (Button) actionBar.getCustomView().findViewById(R.id.btn_atras_seguir_usuario);
        TextView txt_action_bar_moderno =(TextView)actionBar.getCustomView().findViewById(R.id.txt_actionbar_moderno);
        txt_action_bar_moderno.setText("Información del trabajo");

        btn_atras_editar_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnIniciarTrabajo.setOnClickListener(this);
        btnVerMapa.setOnClickListener(this);
        btnFinalizar.setOnClickListener(this);
        btnDetalle.setOnClickListener(this);

        posicion = getIntent().getIntExtra("posicion",0);
        txtOdt.setText(InicioActivity.descargoList.get(posicion).getOdt());
        txtFechaInicioPlanificada.setText(InicioActivity.descargoList.get(posicion).getFechaInicio());
        txtFechaFinPlanificada.setText(InicioActivity.descargoList.get(posicion).getFechaFin());

        SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        SharedPreferences preferencesDescargo = getSharedPreferences("preferenciasDescargos", Context.MODE_PRIVATE);

        id = preferences.getString("_id","No existe la informacion");

         odtId = InicioActivity.descargoList.get(posicion).getOdt();

        Toast.makeText(mContext, ""+odtId, Toast.LENGTH_SHORT).show();

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {

        if (v == btnIniciarTrabajo)
        {
            mInicioTrabajo= new iniciarTrabajoTask(odtId,id);
            mInicioTrabajo.execute((Void) null);

            btnFinalizar.setBackgroundResource(R.drawable.background_boton_fin_descargo);
            btnFinalizar.setTextColor(getColor(R.color.white_smoke));
            btnIniciarTrabajo.setBackgroundResource(R.drawable.btn_inhabilitado);
            btnIniciarTrabajo.setEnabled(false);
            btnFinalizar.setEnabled(true);

        }

        if (v == btnDetalle) {

        }
        if (v == btnVerMapa)
        {

        }

        if (v == btnFinalizar)
        {
            mFinalizarTrabajo = new finalizarTrabajoTask(trabajoId,odtId,id);
            mFinalizarTrabajo.execute((Void) null);
            database.eliminarDescargo(odtId);
            startActivity(new Intent(InformacionDelTrabajo.this,InicioActivity.class));
            finish();

        }


    }

    public class iniciarTrabajoTask extends AsyncTask<Void,Void,ResponseEndPointBean>{

        private final String mUsuarioId;
        private final String mOdtId;

        iniciarTrabajoTask(String mOdtId, String mUsuarioId) {
            this.mUsuarioId = mUsuarioId;
            this.mOdtId = mOdtId;
        }

        @Override
        protected ResponseEndPointBean doInBackground(Void... voids) {
            return ConexionServer.iniciarDescargoFromServer(mOdtId,mUsuarioId);
        }


        @Override
        protected void onPostExecute(ResponseEndPointBean responseEndPointBean) {
            super.onPostExecute(responseEndPointBean);

            if (responseEndPointBean != null){
                if (responseEndPointBean.isSuccess()){

                    Gson gson = new Gson();
                    TrabajoModel trabajoModel = gson.fromJson(responseEndPointBean.getData(), TrabajoModel.class);
                    if (trabajoModel != null)
                    {
                        trabajoId = trabajoModel.getCode();
                    }
                    Log.d(TAG, "onPostExecute: "+responseEndPointBean.getData());

                }
                else{
                    Toast.makeText(InformacionDelTrabajo.this, ""+responseEndPointBean.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    public class finalizarTrabajoTask extends AsyncTask<Void,Void,ResponseEndPointBean>
    {

       private final String mTrabajoId, mOdtId, mUserId;

        public finalizarTrabajoTask(String mTrabajoId, String mOdtId, String mUserId) {
            this.mTrabajoId = mTrabajoId;
            this.mOdtId = mOdtId;
            this.mUserId = mUserId;
        }


        @Override
        protected ResponseEndPointBean doInBackground(Void... voids) {
            return ConexionServer.finalizarTrabajoFromServer(mTrabajoId,mOdtId,mUserId);
        }

        @Override
        protected void onPostExecute(ResponseEndPointBean responseEndPointBean) {
            super.onPostExecute(responseEndPointBean);

            if (responseEndPointBean != null){
                if (responseEndPointBean.isSuccess()){
                    Toast.makeText(mContext, ""+responseEndPointBean.getMessage(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(mContext, ""+responseEndPointBean.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}






