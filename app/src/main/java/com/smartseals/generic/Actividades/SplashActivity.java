package com.smartseals.generic.Actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.smartseals.generic.Modelo.UsuarioBean;
import com.smartseals.generic.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    ImageView splash;
    FirebaseDatabase database;
    DatabaseReference reference;
    String username, email, rol, idArea,idUser;
    UsuarioBean usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splash = findViewById(R.id.splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
                boolean sesion = preferences.getBoolean("sesion",false);
                username = preferences.getString("username","No existe informacion");
                email = preferences.getString("email","No existe informacion");
                rol = preferences.getString("role","No existe informacion");
                idArea = preferences.getString("area_id","No existe informacion");
                idUser = preferences.getString("_id","No existe informacion");
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("Operador");
                if (sesion) {
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.child(idUser).exists()){
                             usuario = new UsuarioBean(username,email,rol,idArea,idUser);
                            reference.child(idUser).setValue(usuario);
                            Intent intent = new Intent(SplashActivity.this, InicioActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Intent intent = new Intent(SplashActivity.this, InicioActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                }else{
                    Intent intent = new Intent(SplashActivity.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        },2000);
    }
}