package com.perspective.movilidad.transito;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.perspective.movilidad.R;
import com.perspective.movilidad.core.Conexion;
import com.perspective.movilidad.core.JSONParser;
import com.perspective.movilidad.core.TransitoDS;
import com.perspective.movilidad.gs.Agentes;
import com.perspective.movilidad.gs.Infraccion;

public class CorregirTableta extends Activity implements OnClickListener, OnItemSelectedListener {
	
	private Spinner spAgente;
	private Button btnEnviar;
	private ArrayAdapter<String> adapter;
	private List<Agentes> agentes;
	private List<String> agente = new ArrayList<String>();
	private List<String> agente1 = new ArrayList<String>();
	private List<Integer> idAgente = new ArrayList<Integer>();
	private TransitoDS ds;
	private ProgressDialog pd;
	private JSONParser jsonParser;
	private int fol  = 0,count = 0;
	private String result = "",age = "";
	private ContentValues cv = null;
	private List<Infraccion> in;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_corregir_tableta);
		
		spAgente = (Spinner)findViewById(R.id.spAgente);
		btnEnviar = (Button)findViewById(R.id.btnEnviar);
		
		ds = new TransitoDS(getApplicationContext());
		
		ds.open();
		
		agentes = ds.getAllAgentes("");
		
		agente.add("");
		agente1.add("");
		idAgente.add(0);
		
		System.err.println(agentes.size());
		for (int i = 0; i < agentes.size(); i++) {
			agente.add(agentes.get(i).getCveAgente() + " " + agentes.get(i).getNombreAgente() + " " + agentes.get(i).getPaternoAgente() + " " + agentes.get(i).getMaternoAgente());
			agente1.add(agentes.get(i).getCveAgente());
			idAgente.add(agentes.get(i).getIdCAgentes());
		}
		
		adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_dropdown_item, agente);
		
		spAgente.setAdapter(adapter);
		
		adapter.notifyDataSetChanged();
		
		btnEnviar.setOnClickListener(this);
		
		jsonParser = new JSONParser();
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.corregir_tableta, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btnEnviar:
			if(Conexion.validarConexion(getApplicationContext())){
				System.err.println(spAgente.getSelectedItemPosition());
				age = "";
				if(!spAgente.getSelectedItem().toString().equalsIgnoreCase(""))
					new Enviar().execute(agente1.get(spAgente.getSelectedItemPosition()).trim());
				else {
					Toast toast = Toast.makeText(getApplicationContext(), "Seleccione el agente", Toast.LENGTH_LONG);
					toast.setGravity(0, 0, 15);
					toast.show();
				}
			} else {
				Toast toast = Toast.makeText(getApplicationContext(), "No se tiene conexión a internet", Toast.LENGTH_LONG);
				toast.setGravity(0, 0, 15);
				toast.show();
			}
			break;

		default:
			break;
		}
		
	}
	
	public class Enviar extends AsyncTask<String, String, String> {
		
		@Override
		protected void onPreExecute() {
		
			super.onPreExecute();
			pd = new ProgressDialog(CorregirTableta.this);
			pd.setCancelable(false);
			pd.setTitle("Mensaje");
			pd.setMessage("Corrigiendo...");
			pd.show();
		}

		@Override
		protected String doInBackground(String... params) {
			
			ArrayList<NameValuePair> cve = new ArrayList<NameValuePair>();
			
			System.err.println(params[0] + " pa");
			cve.add(new BasicNameValuePair("cve_agente", params[0]));
			cve.add(new BasicNameValuePair("foco","rojo"));
			
			age = params[0];
			
			
			JSONObject jobject = null;
			try{
				jobject = jsonParser.realizarHttpRequest("http://apidiv.guadalajara.gob.mx:8085/serversql/configurar_tableta.php", "GET", cve);
			} catch (Exception e) {
				System.err.println(e.getMessage() + " mensaje");
			}
			fol = 0;
			try{
				fol = jobject.getInt("status");
			}catch(JSONException e) {
				System.err.println(e.getMessage());
			}
			if(fol > 0) {
				result = "ok";
				
			}else {
				result = "no";
			}
			
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			
			super.onPostExecute(result);
			
			pd.dismiss();
			
			if(result.equalsIgnoreCase("ok")){
					
				if(fol > 0){
					System.err.println(fol);
					
					cv = new ContentValues();
					
					cv.put("estatus", "N");
					
					count = ds.database.update("infracciones", cv, "folio > '"+fol+"' and cve_agente = '"+age+"'", null);
					
					System.err.println(count + " update");
					//System.err.println(ds.database.update("infracciones", cv, "folio > '3742940'", null) + " update");*/
					
					//in = ds.getAllInfraccion(" folio = '3742940' ");
					in = ds.getAllInfraccion("folio > '"+fol+"' and cve_agente = '"+age+"' order by id_infracciones desc limit 10");
					
					for (int j = 0; j < in.size(); j++) {
						//System.err.println(String.valueOf(in.get(j).getLatitud()) + " " + String.valueOf(in.get(j).getLongitud()));
						System.err.println(in.get(j).getFolio() + " " + in.get(j).getPlaca() + " " + in.get(j).getFecha_multa() + " " + in.get(j).getStat());
					}
				}
				
				if(count > 0) {
					Toast toast = Toast.makeText(getApplicationContext(), "Se actualizo " + count + " registro(s)", Toast.LENGTH_LONG);
					toast.setGravity(0, 0, 15);
					toast.show();
					
					onDestroy();
				}
			
			}
		}
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
		System.err.println(agente.get(position));
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.finish();
	}
}
