package com.smartseals.generic.Actividades;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.smartseals.generic.Adapter.DescargosAdapter;
import com.smartseals.generic.BaseActionBar;
import com.smartseals.generic.Basedato.Database;
import com.smartseals.generic.Common.Common;
import com.smartseals.generic.Modelo.Descargo;
import com.smartseals.generic.Modelo.ResponseEndPointBean;
import com.smartseals.generic.NetworkUtilities.ConexionServer;
import com.smartseals.generic.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class InicioActivity extends BaseActionBar implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = InicioActivity.class.getSimpleName();



    ImageView img_perfil;
    TextView txtNombreOperador, txtRolOperador;
    CardView cardOdt;

    public static List<Descargo> descargoList = new ArrayList<>();
    DescargosAdapter descargosAdapter = null;
    ListView listView;
    private String user,rol,idUsuario;
    String fechaInicio,fechaFinal;
    String odt,odtId,fechaInicia,fechaFin, gps;
    DatabaseReference reference;

    Date fechaI,fechaF;
    Thread t;
    int count = 0;
    volatile boolean ejecutar = true;
    private FusedLocationProviderClient mfusedLocationClient;

    ProgressDialog loadingBar;
    Descargo descargoObject;
    private getDescargosTask mDescargosTask = null;
    private Database database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.md_black_1000));
        toolbar.setTitle("Gestion de trabajos");

        setSupportActionBar(toolbar);
        database = new Database(getApplicationContext());
        final SharedPreferences preferences = getSharedPreferences("preferenciasLogin",Context.MODE_PRIVATE);
        user = preferences.getString("username","No existe la informacion");
        rol = preferences.getString("role","No existe la informacion");
        idUsuario = preferences.getString("_id","No existe la informacion");
        reference = FirebaseDatabase.getInstance().getReference("Operador");
        mfusedLocationClient = LocationServices.getFusedLocationProviderClient(this);



        listView = findViewById(R.id.listTrabajo);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        loadingBar = new ProgressDialog(this);


        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.md_black_1000));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(Gravity.START)) {
                    drawer.closeDrawer(Gravity.START);
                } else {
                    drawer.openDrawer(Gravity.START);
                }
            }
        });

        View headerView = navigationView.getHeaderView(0);
         img_perfil = (ImageView)headerView.findViewById(R.id.imgOperador);
         txtNombreOperador = headerView.findViewById(R.id.txtNombreOperador);
         txtRolOperador = headerView.findViewById(R.id.txtRolOperador);

        txtNombreOperador.setText(user);
        txtRolOperador.setText(rol);

        navigationView.setNavigationItemSelectedListener(this);
        SharedPreferences preferencesDescargo = getSharedPreferences("preferenciasDescargos", Context.MODE_PRIVATE);
        odtId = preferencesDescargo.getString("odtId","No existe la informacion");
        fechaInicia = preferences.getString("fechaInicia","No existe la informacion");
        fechaFin = preferences.getString("fechaFin","No existe la informacion");

        //database = new Database(getApplicationContext());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(),InformacionDelTrabajo.class);
                intent.putExtra("posicion",position);
                startActivity(intent);
                finish();

            }
        });
        if (estaConectado()) {
            validarDescargos();
        }else {
            validarDescargosOffline();
        }

        /*t = new Thread(){
            @Override
            public void run() {
                while (ejecutar){
                    try {
                        Thread.sleep(4000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                subirLatLng();
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        t.start();*/


    }

    private void subirLatLng() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);

        }

        mfusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {


                        if (location != null) {
                            try {
                                Geocoder geocoder = new Geocoder(InicioActivity.this, Locale.getDefault());
                                List<Address> list = geocoder.getFromLocation(
                                        location.getLatitude(), location.getLongitude(), 1);
                                if (!list.isEmpty()) {
                                    Address DirCalle = list.get(0);

                                    Map<String, Object> latLng = new HashMap<>();
                                    latLng.put(idUsuario+ "/" + "latitud", location.getLatitude());
                                    latLng.put(idUsuario + "/" + "longitud", location.getLongitude());
                                    latLng.put(idUsuario + "/" + "direccion", DirCalle.getAddressLine(0));
                                    reference.updateChildren(latLng);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //Log.e("Latitud: ",+location.getLatitude()+"Longitud: "+location.getLongitude());

                        }

                    }
                });
    }

    private void validarDescargosOffline() {
        descargosAdapter = new DescargosAdapter(InicioActivity.this, database.obtenerDescargos(idUsuario));
        listView.setAdapter(descargosAdapter);
        Toast.makeText(mContext, "No hay internet", Toast.LENGTH_SHORT).show();
    }

    private void validarDescargos() {
        mDescargosTask = new getDescargosTask(idUsuario);
        mDescargosTask.execute((Void) null);
        descargosAdapter = new DescargosAdapter(InicioActivity.this, database.obtenerDescargos(idUsuario));
        listView.setAdapter(descargosAdapter);

    }




    public class getDescargosTask extends AsyncTask<Void,Void, ResponseEndPointBean> {

        private String  usuarioId;

        public getDescargosTask(String usuarioId) {
            this.usuarioId = usuarioId;

        }

        @Override
        protected ResponseEndPointBean doInBackground(Void... voids) {

            return ConexionServer.getPlanificacionBrigadaFromServer(usuarioId);
        }


        @Override
        protected void onPostExecute(ResponseEndPointBean responseEndPointBean) {

            mDescargosTask = null;
            descargoObject = null;
            if (responseEndPointBean != null) {
                if (responseEndPointBean.isSuccess()) {

                   try {

                        JSONArray jsonArray = new JSONArray(responseEndPointBean.getData());

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (isCancelled())
                                break;
                             odt = jsonObject.getString("code");
                             fechaInicio = jsonObject.getString("startDate");
                            fechaFinal = jsonObject.getString("endDate");
                            gps = jsonObject.getString("gps");
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            java.util.Date nFecha = format.parse(fechaInicio);
                            java.util.Date nFechaF = format.parse(fechaFinal);
                            fechaI = new Date(nFecha.getTime());
                            fechaF = new Date(nFechaF.getTime());
                            descargoObject = new Descargo(odt,fechaI.toString(),fechaF.toString());
                            descargoList.add(descargoObject);
                            database.agregarDescargos(idUsuario,odt,fechaI.toString(),fechaF.toString());

                            if (gps.isEmpty())
                            {
                                irActividadSinGps(descargoObject);
                            }else{
                                irActividad(descargoObject);
                            }
                        }
                        descargosAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "onPostExecute: "+responseEndPointBean.getData());
                    loadingBar.dismiss();

                }
                else {
                    Toast.makeText(mContext, ""+responseEndPointBean.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }

        @Override
        protected void onCancelled() {

        }
    }

    private void irActividadSinGps(Descargo descargo) {
        Common.descargo = descargo;//importante, siempre debe asignarle un valor antes de usar
        startActivity(new Intent(InicioActivity.this,InformacionDelTrabajo.class));
        finish();
    }


    private void irActividad(Descargo descargoModel) {
        Common.descargo = descargoModel;//importante, siempre debe asignarle un valor antes de usar
        startActivity(new Intent(InicioActivity.this,InformacionDelTrabajo.class));
        finish();
    }
    protected Boolean estaConectado() {
        if (conectadoWifi()) {

            return true;
        } else {
            if (conectadoRedMovil()) {

                return true;
            } else {

                return false;
            }
        }
    }

    protected Boolean conectadoWifi() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected Boolean conectadoRedMovil() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.refresh:



        }

        return super.onOptionsItemSelected(item);

    }


    @Override
    protected void onResume() {
        super.onResume();
        validarDescargos();
        ejecutar = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        validarDescargos();
        ejecutar = true;
    }



    @Override
    protected void onPause() {
        super.onPause();
        ejecutar = false;
        validarDescargos();
        Toast.makeText(mContext, "Ejecutando en segundo plano", Toast.LENGTH_SHORT).show();
    }

    public void detener() {
        ejecutar = false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home)
        {

            loadingBar.setMessage("Validando base de datos...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            mDescargosTask = new getDescargosTask(idUsuario);
            mDescargosTask.execute((Void) null);

            descargosAdapter = new DescargosAdapter(InicioActivity.this, database.obtenerDescargos(idUsuario));
            listView.setAdapter(descargosAdapter);

        }
        else if (id == R.id.cerrar_sesion){
            SharedPreferences preferences = getSharedPreferences("preferenciasLogin",Context.MODE_PRIVATE);
            preferences.edit().clear().commit();
            detener();

            Intent intent = new Intent(InicioActivity.this,Login.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
}