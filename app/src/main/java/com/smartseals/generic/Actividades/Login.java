package com.smartseals.generic.Actividades;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.smartseals.generic.Modelo.ResponseEndPointBean;
import com.smartseals.generic.Modelo.UsuarioBean;
import com.smartseals.generic.MyAccountAuthenticatorActivity;
import com.smartseals.generic.NetworkUtilities.ConexionServer;
import com.smartseals.generic.Parametro.Parametro;
import com.smartseals.generic.R;
import com.smartseals.generic.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends MyAccountAuthenticatorActivity {
    private static final String TAG = Login.class.getSimpleName();

    public static final int LOGIN = 201;
    public  final int DEBUG = 1;
    public  final int INFO = 2;
    public  final int ERROR = 3;
    /**
     * El indicador de intención para confirmar las credenciales.
     */
    public static final String PARAM_CONFIRM_CREDENTIALS = "confirmCredentials";

    /**
     * La intención extra para almacenar el nombre de usuario.
     */
    public static final String PARAMETRO_USUARIO = "usuario";

     /**
     * Haga un seguimiento de la tarea de inicio de sesión para asegurarse de que podemos cancelarla si así lo solicita.
     */

     private UserLoginTask mAuthTask = null;

    private UsuarioBean usuarioBean;

    // UI references.
    private EditText mUsuarioView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView lblEmpresa;
    private Spinner spEmpresa;
    private String  password, username, name,email, area_id,_id,role;
    //Valores para nombre de usuario y contraseña en el momento del intento de inicio de sesión.
    private String mUsuario;
    private String mPassword;
    private String mEmpresa;

    /**
    * ¿La persona que llamaba originalmente pedía una cuenta completamente nueva?
     */
    protected boolean mRequestNewAccount = false;

    /**
   *Si está configurado, solo estamos verificando que el usuario conozca sus credenciales; esta
   *no hace que la contraseña del usuario o authToken se cambie en el
   *dispositivo.
   */
    private Boolean mConfirmCredentials = false;

    private AccountManager mAccountManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Configure el formulario de inicio de sesión.
        final Intent intent = getIntent();
        mUsuario = intent.getStringExtra(PARAMETRO_USUARIO);

        mRequestNewAccount = (mUsuario == null);

        mAccountManager = AccountManager.get(this);

        //Configure el formulario de inicio de sesión.

        mUsuarioView = (EditText) findViewById(R.id.user);
//        mUsuarioView.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();

            }
        });


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mConfirmCredentials = intent.getBooleanExtra(PARAM_CONFIRM_CREDENTIALS, false);
        recuperarPreferencias();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (myPreferencia != null) {
            myPreferencia.setVersionActualizada(true);
            myPreferencia.setVersionMensaje("");
        }
    }




    /**
    * Intenta iniciar sesión o registrar la cuenta especificada por el formulario de inicio de sesión.
    * Si hay errores de formulario (usuario no válido, campos faltantes, etc.), el
    * se presentan errores y no se realiza ningún intento de inicio de sesión real.
    */

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        // Restablecer errores.
        mUsuarioView.setError(null);
        mPasswordView.setError(null);

        // Almacenar valores en el momento del intento de inicio de sesión.
        mUsuario = mUsuarioView.getText().toString().trim();

        mPassword = mPasswordView.getText().toString().trim();


        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(mUsuario)) {
            mUsuarioView.setError("Error");
            focusView = mUsuarioView;
            cancel = true;
        }

        // Verificamos si hay una contraseña válida, si el usuario ingresó una.
        if (!isPasswordValid(mPassword)) {
            mPasswordView.setError("Error");
            focusView = mPasswordView;
            cancel = true;
        }

        // Verifique si hay una contraseña válida, si el usuario ingresó una.

        if (TextUtils.isEmpty(mPassword)) {
            mPasswordView.setError("Error");
            focusView = mPasswordView;
            cancel = true;
        }



        ocultarTeclado();

        if (cancel) {
            // Hubo un error; no intentes iniciar sesión y enfoca el primero
            // campo de formulario con un error.
            focusView.requestFocus();
        } else {
            // Muestra un control de progreso y comienza una tarea en segundo plano para
            // realiza el intento de inicio de sesión del usuario.
            showProgress(true);
            mAuthTask = new UserLoginTask(mUsuario, mPassword);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 3;
    }

    /**
    *Muestra la IU de progreso y oculta el formulario de inicio de sesión.
     */

    private void showProgress(final boolean show) {
        // En Honeycomb MR2 tenemos las API ViewPropertyAnimator, que permiten
        // para animaciones muy fáciles. Si está disponible, use estas API para desvanecerse
        // el spinner de progreso.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
                }
            });

        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
            mLoginFormView.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
        }
    }

    public void log(int nombreLog, Exception e) {
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

    public void ocultarTeclado() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mUsuarioView.getWindowToken(), 0);

        InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm2.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);
    }

        /**
        * Representa una tarea de inicio de sesión asincrónica utilizada para autenticar
        * el usuario.
         */

        public class UserLoginTask extends AsyncTask<Void,Void, ResponseEndPointBean>{


            private final String mUsuario;
            private final String mPassword;

            UserLoginTask(String mUsuario, String password) {
                this.mUsuario = mUsuario;
                mPassword = password;
            }

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected ResponseEndPointBean doInBackground(Void... voids) {
                usuarioBean = null;
                return ConexionServer.authenticate(mUsuario, mPassword);
            }

            @Override
            protected void onPostExecute(ResponseEndPointBean responseEndPointBean) {
                mAuthTask = null;
                showProgress(false);


                   if (responseEndPointBean.isSuccess()) {
                       try {
                           JSONArray jsonArray = new JSONArray(responseEndPointBean.getData());
                           for (int i = 0; i < jsonArray.length(); i++) {
                               JSONObject jsonObject = jsonArray.getJSONObject(i);

                               username = jsonObject.getString("username");
                                password = jsonObject.getString("password");
                                name = jsonObject.getString("name");
                                email = jsonObject.getString("email");
                                role = jsonObject.getString("role");
                                area_id = jsonObject.getString("areaId");
                                _id = jsonObject.getString("_id");

                           }

                       } catch (JSONException e) {
                           e.printStackTrace();
                       }

                       guardarPreferencias(name,email,role,area_id,_id);

                       Intent intent = new Intent(Login.this, SplashActivity.class);
                       startActivity(intent);
                       finish();
                       }
                   else
                   {
                       Toast.makeText(Login.this, ""+responseEndPointBean.getMessage(), Toast.LENGTH_SHORT).show();
                   }

         }
            @Override
            protected void onCancelled() {
                mAuthTask = null;
                showProgress(false);
            }
     }


     private void guardarPreferencias(String name, String email, String role, String area_id, String _id){
         SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
         SharedPreferences.Editor editor = preferences.edit();
         editor.putString("username",mUsuario);
         editor.putString("password",mPassword);
         editor.putString("email",email);
         editor.putString("name",name);
         editor.putString("area_id",area_id);
         editor.putString("_id",_id);
         editor.putString("role",role);

         editor.putBoolean("sesion",true);
         editor.commit();
    }

    private void recuperarPreferencias()
    {
        SharedPreferences preferences = getSharedPreferences("preferenciasLogin",Context.MODE_PRIVATE);
        mUsuarioView.setText(preferences.getString("username",""));
        mPasswordView.setText(preferences.getString("password",""));
    }




    private void finishConfirmCredentials(boolean result) {
        final Account account = new Account(mUsuario, SyncStateContract.Constants.ACCOUNT_TYPE);
        mAccountManager.setPassword(account, mPassword);

        /*final Intent intent = new Intent(this, SplashScreenActivity.class);
        intent.putExtra(AccountManager.KEY_BOOLEAN_RESULT, result);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);

        startActivity(intent);*/
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case LOGIN:
                    if (data != null) {
                        mUsuario = data.getStringExtra(Parametro.PARAMETRO_USERNAME);
                        mPassword = data.getStringExtra(Parametro.PARAMETRO_PASSWORD);
                        mUsuarioView.setText(mUsuario);
                        mPasswordView.setText(mPassword);
                        showProgress(true);
                        mAuthTask = new UserLoginTask(mUsuario, mPassword);
                        mAuthTask.execute((Void) null);
                    }
                    break;
            }
        }

    }
}




