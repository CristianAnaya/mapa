package com.smartseals.generic.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smartseals.generic.GenericApp;
import com.smartseals.generic.Modelo.ResponseEndPointBean;
import com.smartseals.generic.Modelo.UsuarioBean;
import com.smartseals.generic.NetworkUtilities.ConexionServer;
import com.smartseals.generic.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FormularioDinamico extends AppCompatActivity {


    private formularioTask mFormularioTask= null;
    public UsuarioBean usuarioBean;
    LinearLayout linearFormulario;
    String dato1, dato2,dato3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_dinamico);

        linearFormulario = (LinearLayout)findViewById(R.id.layoutFormulario);


    }

    public class formularioTask extends AsyncTask<Void,Void, ResponseEndPointBean> {

        private Context mContext;

        private long usuarioId;
        private String empresa;
        public formularioTask(long usuarioId, String empresa) {
            this.usuarioId = usuarioId;
            this.empresa = empresa;

            mContext = GenericApp.getContext();
        }
        @Override
        protected ResponseEndPointBean doInBackground(Void... voids) {
            usuarioBean = null;
            return  ConexionServer.getFormularioFromServer(empresa);
        }

        @Override
        protected void onPostExecute(ResponseEndPointBean responseEndPointBean) {
            mFormularioTask = null;
            if (responseEndPointBean != null) {
                if (responseEndPointBean.isSuccess()) {
                    try {
                        JSONArray jsonArray = new JSONArray(responseEndPointBean.getData());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            dato1 = jsonObject.getString("");//Version
                            dato2 = jsonObject.getString("");//Componentes
                            dato3 = jsonObject.getString("");


                        }

                        consultarVersion(dato1,dato2);

                    /*Gson gson = new Gson();
                    Formulario formularioDin = gson.fromJson(responseEndPointBean.getData(), Formulario.class);
                    if (formularioDin != null) {
                        if (formularioDin.getEmpresaId().equals(usuarioBean.getEmpresaId())) {
                            for (int i = 0; i < formularioDin.getVersion(); i++) {
                                if (formularioDin.getVersion() == i) {

                                    addViewDinamico(formularioDin.getName(),formularioDin.getComponente(),formularioDin.getValidationReference(),formularioDin.getValues());
                                }
                            }
                        }

                    }*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else
                    {
                    Toast.makeText(mContext, responseEndPointBean.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }
        @Override
        protected void onCancelled() {
            mFormularioTask = null;
        }
    }

    private void consultarVersion(String dato1,String componentes) {
        int version = Integer.parseInt(dato1);
        for (int i = 0; i < version; i++){
            if (version == i){
                addCompontentes(componentes);
            }else{

            }
        }
    }

    private void addCompontentes(String componentes) {
        switch (componentes){
            case "text":
                // cantidad de texto que escoge el administrador
                for (int i=0; i<=5; i++){
                    TextView textView = new TextView(this);
                    textView.setText(String.valueOf(i));
                    linearFormulario.addView(textView);
                }

            case "input":
                // cantidad de texto que escoge el administrador
                for (int i=0; i<=5; i++){
                    EditText editText = new EditText(this);
                    editText.setText(String.valueOf(i));
                    linearFormulario.addView(editText);
                }
        }
    }

}