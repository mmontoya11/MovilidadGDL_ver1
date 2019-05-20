package com.perspective.movilidad.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.util.Log;

public class Conexion {
	
	private static String msj;
	private static JSONParser jsonParser = new JSONParser();
	private Context context;
	private InputStream is;
	private String result;
	private JSONArray jArray;
	private JSONObject json_data;
	private ArrayList<String> tablas = new ArrayList<String>();
	
	public Conexion() {
		
	}
	
	public Conexion(Context context) {
		this.context = context;
	}

	public static String infraccion(String paterno_infraccion,String materno_infraccion,String nombres_infraccion,String fecha_multa, String cve_infraccion,String hora_infraccion,String cve_agente,String serie_infracciones,int c_id_infracciones,String marca,String tipo,String color, String procedencia,String domicilio_conductor,String colonia_conductor,String ciudad_conductor,String licencia_conductor,String tipo_licencia,String nombre_propietario,String domicilio_propietario,String colonia_propietario,String ciudad_propietario,String articulos,String observaciones_tablet,String url){
		
		try {
			ArrayList<NameValuePair> infraccion = new ArrayList<NameValuePair>();
		 
			infraccion.add(new BasicNameValuePair("paterno_infraccion", String.valueOf(paterno_infraccion)));
			infraccion.add(new BasicNameValuePair("materno_infraccion", String.valueOf(materno_infraccion))); 
			infraccion.add(new BasicNameValuePair("nombres_infraccion", String.valueOf(nombres_infraccion))); 
			infraccion.add(new BasicNameValuePair("fecha_multa", String.valueOf(fecha_multa))); 
			infraccion.add(new BasicNameValuePair("cve_infraccion", String.valueOf(cve_infraccion))); 
			infraccion.add(new BasicNameValuePair("hora_infraccion", String.valueOf(hora_infraccion)));
			infraccion.add(new BasicNameValuePair("cve_agente", String.valueOf(cve_agente))); 
			infraccion.add(new BasicNameValuePair("serie_infracciones", String.valueOf(serie_infracciones))); 
			infraccion.add(new BasicNameValuePair("c_id_infracciones", String.valueOf(c_id_infracciones))); 
			infraccion.add(new BasicNameValuePair("marca", String.valueOf(marca))); 
			infraccion.add(new BasicNameValuePair("color", String.valueOf(color))); 
			infraccion.add(new BasicNameValuePair("procedencia", String.valueOf(procedencia))); 
			infraccion.add(new BasicNameValuePair("domicilio_conductor", String.valueOf(domicilio_conductor))); 
			infraccion.add(new BasicNameValuePair("colonia_conductor", String.valueOf(colonia_conductor))); 
			infraccion.add(new BasicNameValuePair("ciudad_conductor", String.valueOf(ciudad_conductor))); 
			infraccion.add(new BasicNameValuePair("licencia_conductor", String.valueOf(licencia_conductor))); 
			infraccion.add(new BasicNameValuePair("tipo_licencia", String.valueOf(tipo_licencia))); 
			infraccion.add(new BasicNameValuePair("nombre_propietario", String.valueOf(nombre_propietario))); 
			infraccion.add(new BasicNameValuePair("domicilio_propietario", String.valueOf(domicilio_propietario))); 
			infraccion.add(new BasicNameValuePair("colonia_propietario", String.valueOf(colonia_propietario))); 
			infraccion.add(new BasicNameValuePair("ciudad_propietario", String.valueOf(ciudad_propietario))); 
			infraccion.add(new BasicNameValuePair("articulos", String.valueOf(articulos))); 
			infraccion.add(new BasicNameValuePair("observaciones_tablet", String.valueOf(observaciones_tablet)));
			
			
			JSONObject json = jsonParser.realizarHttpRequest(url, "POST", infraccion);
			int status = json.getInt("status");
			System.out.println(status);
			msj = (status == 1) ? "S" : "N";
			
		} 
	    catch (JSONException e) {
	    	Log.i("JSONException", e.getMessage());
	    	return "N";
	    }catch (Exception e) {
			Log.e("e", e.getMessage() + " k");
			return "N";
		}
		return msj;
	}
	
	public static boolean validarConexion(Context context){
		boolean conexion = false;
		ConnectivityManager conn = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		if (conn.getActiveNetworkInfo() != null && conn.getActiveNetworkInfo().isAvailable() && conn.getActiveNetworkInfo().isConnected()) 
			conexion = true;
		
		return conexion;
	}
	
	public String search(String url) {
		result = null;
		ArrayList<NameValuePair> dat = new ArrayList<NameValuePair>();
		dat.add(new BasicNameValuePair("id", "0"));
		dat.add(new BasicNameValuePair("foco", "rojo"));
		
		try {
			
			DefaultHttpClient httpClient = new DefaultHttpClient();
			String parametros = URLEncodedUtils.format(dat, "utf-8");
			url += "?" + parametros;
			HttpGet httpGet = new HttpGet(url);
			
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			is= httpEntity.getContent();	
			
			/*HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(dat));
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			
			this.is = entity.getContent();*/
			
			Log.i("is", is.toString());
		} catch (Exception e) {
			Log.e("ERROR", e.getMessage());
			return "No se pudo conectar con el servidor";
		}
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(this.is, "iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			
			this.is.close();
			result = sb.toString();
		} catch (ClientProtocolException e) {
        	Log.e("ClientProtocolException", e.getMessage());
        	return "null";
        }
		catch (IOException e) {
			Log.e("IOException", e.getMessage());
			return "null";
		}
		catch (Exception e) {
			Log.e("Exception", e.getMessage());
			return "null";
		}
		return result;
	}
	
	public boolean insetarRegistros(String url, String tabla) {
	
		System.out.println(tabla + " " + url);
		GestionBD gestion = new GestionBD(context,"transito.db",null, 1);
		SQLiteDatabase db = gestion.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		
		Cursor c = db.rawQuery("SELECT * FROM " + tabla, null);
		
		
		ArrayList<NameValuePair> dat = new ArrayList<NameValuePair>();
		dat.add(new BasicNameValuePair("id", "0"));
		dat.add(new BasicNameValuePair("foco", "rojo"));
		
		try {
			
			DefaultHttpClient httpClient = new DefaultHttpClient();
			String parametros = URLEncodedUtils.format(dat, "utf-8");
			url += "?" + parametros;
			HttpGet httpGet = new HttpGet(url);
			
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			is= httpEntity.getContent();
			
			/*HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(dat));
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			this.is = entity.getContent();*/
		} catch (Exception e) {
			Log.e("ERROR1", e.getMessage());
		}
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(this.is, "iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			
			this.is.close();
			result = sb.toString();
			System.out.println(result);
			this.jArray = new JSONArray(result);
			this.json_data = this.jArray.getJSONObject(0);
			@SuppressWarnings("unchecked")
			Iterator<String> itr = json_data.keys();
			tablas.clear();
			while (itr.hasNext()) {
				tablas.add(itr.next());
			}
			
			for (int i = 0; i < tablas.size(); i++) {
				System.out.println("t " + tablas.get(i));
				if (c.getColumnIndex(tablas.get(i)) > -1) 
					System.out.println("si existe " );
				else {
					db.execSQL("ALTER TABLE " + tabla + " ADD COLUMN " + tablas.get(i) + " TEXT ");
					System.out.println("ALTER TABLE " + tabla + " ADD COLUMN " + tablas.get(i) + " TEXT ");
				}
			}
			c.close();
			for (int i = 0; i < tablas.size(); i++) {
				System.out.println("t " + tablas.get(i));
			}
			for (int i = 0; i < jArray.length(); i++) {
				this.json_data = this.jArray.getJSONObject(i);
				
				for (int j = 0; j < tablas.size(); j++) {
					if (!json_data.isNull(tablas.get(j))) {
							cv.put(tablas.get(j), json_data.getString(tablas.get(j).trim()));
							System.out.println("registro" + i + " " + tablas.get(j) + " " + json_data.getString(tablas.get(j).trim()).trim());
					}
					else {
						cv.put(tablas.get(j), "");
						System.out.println("registro" + i + " " + tablas.get(j) + " ");
					}
				}
				db.insert(tabla, null, cv);
			}
			
		}catch (SQLiteException sqlite) {
			Log.e("SQLiteException", sqlite.getMessage());
			return false;
		}catch (JSONException j) {
			Log.e("JSONException", j.getMessage());
			return false;
		}
		catch (ClientProtocolException e) {
        	Log.e("ClientProtocolException", e.getMessage());
        	return false;
        }
		catch (IOException e) {
			Log.e("IOException", e.getMessage());
			return false;
		}
		finally {
			db.close();
		}
		return true;
	}

}
