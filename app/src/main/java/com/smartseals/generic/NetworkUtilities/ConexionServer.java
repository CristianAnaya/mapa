package com.smartseals.generic.NetworkUtilities;

import android.util.Log;

import com.smartseals.generic.GenericApp;
import com.smartseals.generic.Modelo.ResponseEndPointBean;
import com.smartseals.generic.utils.HttpsTrustManager;
import com.smartseals.generic.utils.MyPreferencia;
import com.smartseals.generic.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConexionServer {
    private static final String TAG = ConexionServer.class.getSimpleName();




    public static final int HTTP_REQUEST_TIMEOUT_MS = 30 * 1000;
    public static String URL_LOGIN = "http://54.185.55.169/auth/login";
    public static String URL_INICIO_TRABAJO = "http://54.185.55.169/works/beginwork";
    public static String URL_FINALIZAR_TRABAJO = "http://54.185.55.169/works/endwork";
    public static String GET_PLANIFICACION_OPERADOR_FROM_SERVER = "http://54.185.55.169/odts/";




    private static Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy h:mm:ss a").disableHtmlEscaping().serializeNulls().create();


    private static void getIp(){
        URL_LOGIN = "http://54.185.55.169/auth/login";
        URL_INICIO_TRABAJO = "http://54.185.55.169/works/beginwork";
        URL_FINALIZAR_TRABAJO = "http://54.185.55.169/works/endwork";
        GET_PLANIFICACION_OPERADOR_FROM_SERVER =  "http://54.185.55.169/odts/";

    }



    private static HttpURLConnection postHttpURLConnection(String urlNombre, boolean output, boolean input, int contentLength, int http_requet_time_out) throws Exception {
        java.net.URL url = new java.net.URL(urlNombre);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setDoOutput(output);
        urlConnection.setDoInput(input);

        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setRequestProperty("Content-Length", "" + Integer.toString(contentLength));
        urlConnection.setUseCaches(false);
        urlConnection.setConnectTimeout(http_requet_time_out);
        urlConnection.setReadTimeout(http_requet_time_out);

        HttpsTrustManager.allowAllSSL();

        return urlConnection;
    }


    /**
     * Conecta con el servidor principal, autentica el usuario y contrasena
     * proporcionado
     *
     * @param username El usuario de la aplicacion.
     * @param password La contrasena del usuario de aplicacion.
     * @return String El identificador unico asignado por el servidor a al usuario o -1 si no lo
     * encuentra
     */
    public static ResponseEndPointBean authenticate(String username, String password) {
        getIp();

        ResponseEndPointBean responseEndPointBean = null;
        HttpURLConnection urlConnection = null;

            try {
                JSONObject jsonUsuario = new JSONObject();
                jsonUsuario.put("username", username);
                jsonUsuario.put("password", password);


                    urlConnection = postHttpURLConnectionLogin(URL_LOGIN, true, true, jsonUsuario.toString().getBytes().length, HTTP_REQUEST_TIMEOUT_MS);

                urlConnection.connect();

                // Post Json
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8");
                outputStreamWriter.write(jsonUsuario.toString());
                outputStreamWriter.close();

                // Receive Response from server
                int statusCode = urlConnection.getResponseCode();

                /* 200 represents HTTP OK */
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    StringBuilder sb = new StringBuilder();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                    String line;
                    sb.setLength(0);
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }
                    JSONObject jsonObjectResultado = new JSONObject(sb.toString());

                    responseEndPointBean = gson.fromJson(jsonObjectResultado.toString(), ResponseEndPointBean.class);
                } else {
                    String mensaje = convertStreamToString(urlConnection.getErrorStream());
                    Log.e(TAG, mensaje);

                    responseEndPointBean = new ResponseEndPointBean();
                    responseEndPointBean.setSuccess(false);
                    responseEndPointBean.setMessage(mensaje);
                }
            } catch (Exception e) {
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

        return responseEndPointBean;
    }

    public static ResponseEndPointBean getPlanificacionBrigadaFromServer(String usuarioId) {

        getIp();

        ResponseEndPointBean responseEndPointBean = null;
        HttpURLConnection urlConnection = null;

        String url =GET_PLANIFICACION_OPERADOR_FROM_SERVER + usuarioId;
        try {
            urlConnection = getHttpURLConnection(url, true, HTTP_REQUEST_TIMEOUT_MS);

            urlConnection.connect();



            // Receive Response from server
            int statusCode = urlConnection.getResponseCode();

            /* 200 represents HTTP OK */
            if (statusCode == HttpURLConnection.HTTP_OK) {
                StringBuilder sb = new StringBuilder();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                String line;
                sb.setLength(0);
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                JSONObject jsonObjectResultado = new JSONObject(sb.toString());

                responseEndPointBean = gson.fromJson(jsonObjectResultado.toString(), ResponseEndPointBean.class);
            } else {
                String mensaje = convertStreamToString(urlConnection.getErrorStream());
                Log.e(TAG, mensaje);

                responseEndPointBean = new ResponseEndPointBean();
                responseEndPointBean.setSuccess(false);
                responseEndPointBean.setMessage(mensaje);
            }
        } catch (Exception e) {
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return responseEndPointBean;
    }

    public static ResponseEndPointBean getFormularioFromServer(String usuarioId) {
        getIp();

        ResponseEndPointBean responseEndPointBean = null;
        HttpURLConnection urlConnection = null;

        try {
            JSONObject jsonUsuario = new JSONObject();
            jsonUsuario.put("_id", usuarioId);




            urlConnection = postHttpURLConnectionLogin(URL_FINALIZAR_TRABAJO, true, true, jsonUsuario.toString().getBytes().length, HTTP_REQUEST_TIMEOUT_MS);

            urlConnection.connect();

            // Post Json
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8");
            outputStreamWriter.write(jsonUsuario.toString());
            outputStreamWriter.close();

            // Receive Response from server
            int statusCode = urlConnection.getResponseCode();

            /* 200 represents HTTP OK */
            if (statusCode == HttpURLConnection.HTTP_OK) {
                StringBuilder sb = new StringBuilder();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                String line;
                sb.setLength(0);
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                JSONObject jsonObjectResultado = new JSONObject(sb.toString());

                responseEndPointBean = gson.fromJson(jsonObjectResultado.toString(), ResponseEndPointBean.class);
            } else {
                String mensaje = convertStreamToString(urlConnection.getErrorStream());
                Log.e(TAG, mensaje);

                responseEndPointBean = new ResponseEndPointBean();
                responseEndPointBean.setSuccess(false);
                responseEndPointBean.setMessage(mensaje);
            }
        } catch (Exception e) {
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return responseEndPointBean;
    }


    public static ResponseEndPointBean iniciarDescargoFromServer(String odt, String usuarioId) {
        getIp();

        ResponseEndPointBean responseEndPointBean = null;
        HttpURLConnection urlConnection = null;

        try {
            JSONObject jsonUsuario = new JSONObject();
            jsonUsuario.put("userId", usuarioId);
            jsonUsuario.put("odtCode", odt);

            urlConnection = postHttpURLConnectionLogin(URL_INICIO_TRABAJO, true, true, jsonUsuario.toString().getBytes().length, HTTP_REQUEST_TIMEOUT_MS);

            urlConnection.connect();

            // Post Json
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8");
            outputStreamWriter.write(jsonUsuario.toString());
            outputStreamWriter.close();

            // Receive Response from server
            int statusCode = urlConnection.getResponseCode();

            /* 200 represents HTTP OK */
            if (statusCode == HttpURLConnection.HTTP_OK) {
                StringBuilder sb = new StringBuilder();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                String line;
                sb.setLength(0);
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                JSONObject jsonObjectResultado = new JSONObject(sb.toString());

                responseEndPointBean = gson.fromJson(jsonObjectResultado.toString(), ResponseEndPointBean.class);
            } else {
                String mensaje = convertStreamToString(urlConnection.getErrorStream());
                Log.e(TAG, mensaje);

                responseEndPointBean = new ResponseEndPointBean();
                responseEndPointBean.setSuccess(false);
                responseEndPointBean.setMessage(mensaje);
            }
        } catch (Exception e) {
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return responseEndPointBean;
    }


    public static ResponseEndPointBean finalizarTrabajoFromServer(String trabajoId,String odtId,String usuarioId) {
        getIp();

        ResponseEndPointBean responseEndPointBean = null;
        HttpURLConnection urlConnection = null;

        try {
            JSONObject jsonUsuario = new JSONObject();
            jsonUsuario.put("userIdd", usuarioId);
            jsonUsuario.put("odtCode", odtId);
            jsonUsuario.put("workCode", trabajoId);

            urlConnection = postHttpURLConnectionLogin(URL_FINALIZAR_TRABAJO, true, true, jsonUsuario.toString().getBytes().length, HTTP_REQUEST_TIMEOUT_MS);

            urlConnection.connect();

            // Post Json
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8");
            outputStreamWriter.write(jsonUsuario.toString());
            outputStreamWriter.close();

            // Receive Response from server
            int statusCode = urlConnection.getResponseCode();

            /* 200 represents HTTP OK */
            if (statusCode == HttpURLConnection.HTTP_OK) {
                StringBuilder sb = new StringBuilder();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                String line;
                sb.setLength(0);
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                JSONObject jsonObjectResultado = new JSONObject(sb.toString());

                responseEndPointBean = gson.fromJson(jsonObjectResultado.toString(), ResponseEndPointBean.class);
            } else {
                String mensaje = convertStreamToString(urlConnection.getErrorStream());
                Log.e(TAG, mensaje);

                responseEndPointBean = new ResponseEndPointBean();
                responseEndPointBean.setSuccess(false);
                responseEndPointBean.setMessage(mensaje);
            }
        } catch (Exception e) {
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return responseEndPointBean;
    }


    private static HttpURLConnection postHttpURLConnectionLogin(String urlNombre, boolean output, boolean input, int contentLength, int http_requet_time_out) throws Exception {
        java.net.URL url = new java.net.URL(urlNombre);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setDoOutput(output);
        urlConnection.setDoInput(input);

        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setRequestProperty("Content-Length", "" + Integer.toString(contentLength));
        urlConnection.setRequestProperty("token","smartsealsco");
        urlConnection.setUseCaches(false);
        urlConnection.setConnectTimeout(http_requet_time_out);
        urlConnection.setReadTimeout(http_requet_time_out);

        HttpsTrustManager.allowAllSSL();

        return urlConnection;
    }


    private static HttpURLConnection getHttpURLConnection(String urlNombre, boolean input, int http_requet_time_out) throws Exception {
        URL url = new URL(urlNombre);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setDoInput(input);
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("token", "smartsealsco");
        urlConnection.setConnectTimeout(http_requet_time_out);
        urlConnection.setReadTimeout(http_requet_time_out);

        HttpsTrustManager.allowAllSSL();

        return urlConnection;
    }


    private static String convertStreamToString(java.io.InputStream is) {
        try {
            if (is != null) {
                return new java.util.Scanner(is).useDelimiter("\\A").next();
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }


    private static int networkSend(HttpURLConnection urlConnection, String stringJson) {
        OutputStreamWriter outputStreamWriter = null;
        try {

            if (!stringJson.isEmpty()) {
                outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8");
                outputStreamWriter.write(stringJson);
                outputStreamWriter.close();

                MyPreferencia myPreferencia = MyPreferencia.getInstance(GenericApp.getContext());
                myPreferencia.setBytesSent(stringJson.getBytes().length);
            }

            return urlConnection.getResponseCode();
        } catch (Exception e) {
            Utils.log(TAG, e);
        }
        return 408;
    }

}
