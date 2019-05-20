package com.perspective.movilidad.transito;

import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.perspective.movilidad.R;
import com.perspective.movilidad.core.Conexion;
import com.perspective.movilidad.core.JSONParser;

public class Configurar extends Activity implements OnClickListener {
	
	private EditText etSerie,etSerie1;
	private int res = 0,idU = 0;
	private String serie = "",serie1 = "",usuario = "",clave = "";
	private boolean isSelected,comm;
	private ProgressDialog pd;
	private JSONParser jParser = new JSONParser();;
	private Button btnEnvia;
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private Bundle bundle = new Bundle();
	private Conexion conn = new Conexion();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configurar);
		
		etSerie = (EditText)findViewById(R.id.etSerie);
		etSerie1 = (EditText)findViewById(R.id.etSerie1);
		//spDir = (Spinner)findViewById(R.id.spDir);
		btnEnvia = (Button)findViewById(R.id.btnEnvia);
		
		btnEnvia.setOnClickListener(this);
	
		sp = getSharedPreferences("transito", Context.MODE_PRIVATE);
		
		editor = sp.edit();
		
		savedInstanceState = getIntent().getExtras();
		
		idU = getIntent().getExtras().getInt("id");
		usuario = getIntent().getExtras().getString("us");
		clave = getIntent().getExtras().getString("clave").trim();
		
		
	}
	
	
	public void validarFrom() {
		
		etSerie.setError(null);
		etSerie1.setError(null);
		
		boolean cancel = false;
		View focusView = null;
		
		serie = etSerie.getText().toString();
		serie1 = etSerie1.getText().toString();
		
		if(TextUtils.isEmpty(serie)) {
			etSerie.setError(getString(R.string.serie));
			focusView = etSerie;
			cancel = true;
			isSelected = false;
		}
		
		if(TextUtils.isEmpty(serie1)) {
			etSerie1.setError(getString(R.string.serie));
			focusView = etSerie1;
			cancel = true;
			isSelected = false;
		}
		
		
		
		if(cancel) {
			if(!isSelected)
				focusView.requestFocus();
		}else {
			
			if(conn.validarConexion(getApplicationContext()))
				new GetNum().execute(serie,serie1);
			else{
				Toast toast = Toast.makeText(getApplicationContext(), "No tiene conexión a internet", Toast.LENGTH_LONG);
				toast.setGravity(0, 0, 15);
				toast.show();
			}
		}
		
	}


	public class GetNum extends AsyncTask<String, String, String> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(Configurar.this);
			pd.setCancelable(false);
			pd.setTitle("Mensaje");
			pd.setMessage("Conectando...");
			pd.show();
		}

		@Override
		protected String doInBackground(String... params) {
			
			ArrayList<NameValuePair> conf = new ArrayList<NameValuePair>();
			
			conf.add(new BasicNameValuePair("serie", params[0]));
			conf.add(new BasicNameValuePair("serie1", params[1]));
			
			
			JSONObject jObject = jParser.realizarHttpRequest("http://apidiv.guadalajara.gob.mx:8085/serversql/getTableta.php", "POST", conf);
			
			try {
				res = jObject.getInt("status");
				
				if(res > 0)
					return "S";
				else 
					return "N";
			} catch(Exception e) {
				System.err.println(e.getMessage() + " j ");
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pd.dismiss();
			Toast toast;
			
			bundle.putInt("id", idU);
			bundle.putString("us", usuario);
			bundle.putString("clave", clave);
			
			if(result.equalsIgnoreCase("S")) {
				//String loc = String.format(Locale.getDefault(), "%04d", res);
				System.err.println(res + " num");
				editor.putInt("config", res);
				comm = editor.commit();
				if(comm) 
					toast = Toast.makeText(getApplicationContext(), "Se registro número de tableta", Toast.LENGTH_LONG);
				else
					toast = Toast.makeText(getApplicationContext(), "No se registro número de tableta", Toast.LENGTH_LONG);
				
				startActivity(new Intent(getApplicationContext(), com.perspective.movilidad.transito.Menu.class).putExtras(bundle));
				onDestroy();
			}
			else {
				if(res == -2)
					toast = Toast.makeText(getApplicationContext(), "No coinciden los números de serie", Toast.LENGTH_LONG);
				else
					toast = Toast.makeText(getApplicationContext(), "No se encontro", Toast.LENGTH_LONG);
			}
			toast.setGravity(0, 0, 15);
			toast.show();
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.configurar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnEnvia:
			validarFrom();
			break;

		default:
			break;
		}
	}
}
