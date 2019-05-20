package com.perspective.movilidad.core;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by indatcom on 6/04/16.
 */
public class Update extends AsyncTask<String,String,String> {
    Context context;
    ProgressDialog dialog;
    String token,id_user;
    String liga,version;
    HttpURLConnection urlConnection;
    
    public Update(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute (){
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Buscando Actualizaciones, Espere");
        dialog.setTitle("Conectando al servidor");
        dialog.show();
        dialog.setCancelable(false);

        try {
            /*Cursor c = db.rawQuery("SELECT * FROM usuario", null);
            c.moveToLast();
            token = c.getString(1);
            id_user = c.getString(2);
            Log.e("TOKEN",token);*/
        }catch (CursorIndexOutOfBoundsException e){
            Log.e("", "error al obtener token");
        }

    }

    @SuppressLint("SdCardPath")
	protected String doInBackground(String... sUrl) {

        String path = "/sdcard/Archivo.apk";
        StringBuilder result = new StringBuilder();
        //String url_base = "";
        try {
            //Realizamos la consulta a la API

            URL link = new URL("http://apidiv.guadalajara.gob.mx:8085/infracciones/sistema/updateTransito.php?foco=rojo");
            urlConnection = (HttpURLConnection) link.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            JSONObject obj = new JSONObject(result.toString());
            JSONArray jsonArray = obj.optJSONArray("data");
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                 liga = jsonObject.optString("link").toString();
                 version = jsonObject.optString("version").toString();
                 System.err.println("Mensaje con datos " + liga+" "+version + " " + sUrl[0]);

            }
            if (new Double(sUrl[0]).compareTo(new Double(version))==-1){
                Log.e("Mensaje con datos",liga+" "+version +sUrl[0]);
                try {
                    URL url = new URL(liga);
                    URLConnection connection = url.openConnection();
                    connection.connect();

                    int fileLength = connection.getContentLength();

                    // download the file
                    InputStream input = new BufferedInputStream(url.openStream());
                    OutputStream output = new FileOutputStream(path);

                    byte data[] = new byte[1024];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != -1) {
                        total += count;
                        publishProgress(String.valueOf((int) (total * 100 / fileLength)));
                        output.write(data, 0, count);
                    }

                    output.flush();
                    output.close();
                    input.close();
                } catch (Exception e) {
                    Log.d("Cargando", "Fallo conexion");
                    path = "No";
                }
            }else {
                path ="No";
            }


    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){

        }
        return path;
        }
        // begin the installation by opening the resulting file
    
    @Override
    protected void onPostExecute(String path) {
        dialog.dismiss();
        if (path.equals("No")){
            Toast toast = Toast.makeText(context, "No existen Actualizaciones", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_VIEW);
            i.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
            Log.d("Cargando", "Se instalara el nuevo APK");
            this.context.startActivity(i);
        }

    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}