package com.smartseals.generic.Actividades;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.smartseals.generic.GenericApi.MyLoginApi;
import com.smartseals.generic.Modelo.UserModel;
import com.smartseals.generic.NetworkUtilities.ConexionServer;
import com.smartseals.generic.R;
import com.smartseals.generic.RetrofitClient.RetrofitClient;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    MyLoginApi myLoginApi;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Button btnLogin,btnRegistrar;
    EditText edtUsuario, edtPassword;
    RelativeLayout rootLayout;


    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        init();
        btnLogin = (Button) findViewById(R.id.btnIniciarSesion);
        edtUsuario = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);

        btnRegistrar = findViewById(R.id.btnRegistrarUsuario);



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<UserModel> call = myLoginApi.PostUser(edtUsuario.getText().toString(),edtPassword.getText().toString());
                call.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        UserModel userModel = response.body();
                        if (response.isSuccessful())
                        {
                                Toast.makeText(LoginActivity.this, "" , Toast.LENGTH_SHORT).show();

                        }else 
                        {
                            Toast.makeText(LoginActivity.this, ""+userModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {

                    }
                });
            }
        });


    }

    private void init() {
        myLoginApi = RetrofitClient.getInstance(ConexionServer.URL_LOGIN).create(MyLoginApi.class);

    }

   /* private void consumirServicios() {
        //Ejecutaremos el hilo creado
        String usuario = edtUsuario.getText().toString();
        String password = edtPassword.getText().toString();

        ServiciosTask serviciosTask = new ServiciosTask(this,"http://34.221.109.117/auth/login",usuario,password);
        serviciosTask.execute();
    }*/


}