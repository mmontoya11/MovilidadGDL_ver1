package com.perspective.movilidad.transito;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.perspective.movilidad.R;
import com.perspective.movilidad.core.Conexion;
import com.perspective.movilidad.core.GestionBD;
import com.perspective.movilidad.core.JSONParser;
import com.perspective.movilidad.core.TransitoDS;
import com.perspective.movilidad.core.Update;
import com.perspective.movilidad.gs.Agentes;
import com.perspective.movilidad.gs.Fotografia;
import com.perspective.movilidad.gs.Infraccion;
import com.perspective.movilidad.gs.Placas;
import com.perspective.movilidad.gs.Tabletas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class Menu extends Activity implements OnClickListener {
	//g45tr0park
	private Button btnMulta,btnDescarga,btnDescargaCopy,btnActualizar,btnIP,btnIPFTP,btnActualizarAgente,btnConfigT,btnActualizarPlaca,btnBusqueda,btnUpdate,btnFoto,btnSincronizar,btnCorregirTableta,btnImprimirTicketLinea;
	private int idU,count,countF = 0,cons = 0,folioMinimo, folioMaximo, ve = 0,fol = 0,bol = 0,con1,val = 0;
	private String usuario,msj = "",ip,clave,dia,mes,axo,hr,min,seg,cla,ipftp,config,cve = "",result = "",fecha,ag = "";
	private Bundle bundle = null;
	private ProgressDialog pdialogo;
	private Conexion con = null;
	private List<Infraccion> infraccion = null;
	private List<Tabletas> tab = null;
	private List<Agentes> agente = new ArrayList<Agentes>();
	private List<Infraccion> infracciones = new ArrayList<Infraccion>();
	private int nos = 0,totalM = 0,totalE = 0,totalF = 0;
	private TextView tvM;
	
	private TransitoDS ds = null;
	private SharedPreferences sp;
	private EditText mEdittText;
	//private final String mFTP = "201.130.205.141",user = "pgm", pass = "pgm2012";
	private final String mFTP = "189.198.128.67",mFTPCopy = "192.168.254.2",user = "infracciones", pass = "123456";//192.168.254.2:50100
	private Calendar cal = Calendar.getInstance();
	private boolean res = false,comm = false;
	private String [] tablet;
	private JSONParser jsonParser = new JSONParser();
	private File exportDir = null;
	private File file = null;
	private List<Fotografia> fotografia = new ArrayList<Fotografia>();
	private Toast toast = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		System.out.println(getIntent().getExtras().getInt("id"));
		System.out.println(getIntent().getExtras().getString("us").trim());
		
		idU = getIntent().getExtras().getInt("id");
		usuario = getIntent().getExtras().getString("us");
		clave = getIntent().getExtras().getString("clave").trim();
		
		System.err.println(usuario);
		
		/*idU = 0;
		usuario = "ej";
		clave = "ej";*/
		
		this.btnMulta = (Button)findViewById(R.id.btnMulta);
		this.btnDescarga = (Button)findViewById(R.id.btnDescargaDatos);
		this.btnActualizar = (Button)findViewById(R.id.btnActualizarBD);
		this.btnIP = (Button)findViewById(R.id.btnIP);
		this.btnDescargaCopy = (Button)findViewById(R.id.btnDescargaDatosCopy);
		this.btnIPFTP = (Button)findViewById(R.id.btnIPFTP);
		this.btnActualizarAgente = (Button)findViewById(R.id.btnActualizarAgente);
		this.btnConfigT = (Button)findViewById(R.id.btnConfigTab);
		this.btnActualizarPlaca = (Button)findViewById(R.id.btnActualizarPlacas);
		this.btnBusqueda = (Button)findViewById(R.id.btnBusqueda);
		this.btnUpdate = (Button)findViewById(R.id.btnDescargaApp);
		this.btnFoto = (Button)findViewById(R.id.btnDescargaFoto);
		this.btnSincronizar = (Button)findViewById(R.id.btnSincronizar);
		btnCorregirTableta = (Button)findViewById(R.id.btnCorregirTableta);
		btnImprimirTicketLinea = (Button)findViewById(R.id.btnImprimirTicketLinea);
		tvM = (TextView)findViewById(R.id.tvMsg);
		
		/*btnMulta.setFocusable(true);
		btnMulta.setFocusableInTouchMode(true);
		btnMulta.requestFocus();*/
		
		
		this.btnMulta.setOnClickListener(this);
		this.btnDescarga.setOnClickListener(this);
		this.btnActualizar.setOnClickListener(this);
		this.btnIP.setOnClickListener(this);
		this.btnIPFTP.setOnClickListener(this);
		this.btnDescargaCopy.setOnClickListener(this);
		this.btnActualizarAgente.setOnClickListener(this);
		this.btnConfigT.setOnClickListener(this);
		this.btnActualizarPlaca.setOnClickListener(this);
		this.btnBusqueda.setOnClickListener(this);
		this.btnUpdate.setOnClickListener(this);
		this.btnFoto.setOnClickListener(this);
		this.btnSincronizar.setOnClickListener(this);
		btnCorregirTableta.setOnClickListener(this);
		btnImprimirTicketLinea.setOnClickListener(this);
		
		
		System.err.println(usuario + "| |" + "sistemas");
		
		//if(usuario.trim().equalsIgnoreCase("admin")) {
		if(usuario.trim().equalsIgnoreCase("administrador") | usuario.trim().equalsIgnoreCase("admin")) {
			btnDescarga.setEnabled(true);
			btnDescargaCopy.setEnabled(false);
			btnActualizar.setEnabled(false);
			btnIP.setEnabled(false);
			btnIPFTP.setEnabled(false);
			btnActualizarAgente.setEnabled(false);
			btnConfigT.setEnabled(false);
			btnMulta.setEnabled(false);
			//btnActualizarPlaca.setEnabled(false);
			btnCorregirTableta.setVisibility(View.VISIBLE);
			btnImprimirTicketLinea.setEnabled(true);
		} else if (usuario.trim().equalsIgnoreCase("siste") | usuario.trim().equalsIgnoreCase("sistemas")) {
			btnDescarga.setEnabled(false);
			btnDescargaCopy.setEnabled(true);
			btnActualizar.setEnabled(true);
			btnIP.setEnabled(true);
			btnIPFTP.setEnabled(true);
			btnActualizarAgente.setEnabled(true);
			btnConfigT.setEnabled(true);
			btnMulta.setEnabled(false);
			btnCorregirTableta.setVisibility(View.VISIBLE);
			btnImprimirTicketLinea.setEnabled(false);
		} else {
			btnDescarga.setEnabled(true);
			btnDescargaCopy.setEnabled(false);
			btnActualizar.setEnabled(false);
			btnIP.setEnabled(false);
			btnIPFTP.setEnabled(false);
			btnActualizarAgente.setEnabled(false);
			btnConfigT.setEnabled(false);
			btnMulta.setEnabled(true);
			btnActualizarPlaca.setEnabled(false);
			btnUpdate.setEnabled(false);
			btnCorregirTableta.setVisibility(View.GONE);
			btnImprimirTicketLinea.setEnabled(false);
		}
		
		if(clave.equalsIgnoreCase("28991")) {
			btnImprimirTicketLinea.setEnabled(true);
		}
		
		con = new Conexion(getApplicationContext());
		
		Log.e("error", con.toString());
		
		infraccion = new ArrayList<Infraccion>();
		
		sp = getSharedPreferences("transito", Context.MODE_PRIVATE);
		cons = sp.getInt("cons", 0);
		
		con1 = sp.getInt("config", 0);
		ag = sp.getString("agente", "");
		val = sp.getInt("val", 0);
		
		System.err.println(val);
		
		if(val == 0) {
			SharedPreferences.Editor editor;
			editor = sp.edit();
			editor.putInt("cons", 0);
			comm = editor.commit();
			if(comm) {
				val ++;
				editor.putInt("val", val);
				editor.commit();
			}
		}
		
		cons = sp.getInt("cons", 0);
		
		System.err.println(con1 + " c");
		
		if(cons == 0) {
			mostrarMsjSincronizar();
		}
		
		if(con1 == 0) {
			btnMulta.setEnabled(false);
			
		} else {
			if((usuario.trim().equalsIgnoreCase("administrador") | usuario.trim().equalsIgnoreCase("admin")) | (usuario.trim().equalsIgnoreCase("siste") | usuario.trim().equalsIgnoreCase("sistemas"))) {
				btnMulta.setEnabled(false);
			}else {
				btnMulta.setEnabled(true);
			}
			
		}
		
		System.err.println(cons);
		
		tab = new ArrayList<Tabletas>();
		ds = new TransitoDS(getApplicationContext());
		
		ds.open();
		
		if(ds.validarCampo("c_agentes", "folio_minimo") > 0)
			agente = ds.getAllFolio(" id_c_agentes = " + idU);
		
		for (int i = 0; i < agente.size(); i++) {
				System.out.println(agente.get(i).getFolioMinimo() + " " + agente.get(i).getFolioMaximo());
				folioMinimo = Integer.parseInt(agente.get(i).getFolioMinimo());
				folioMaximo = Integer.parseInt(agente.get(i).getFolioMaximo());
		}
		
		infracciones = ds.getUltimoinfracciones("");
		for (int i = 0; i < infracciones.size(); i++) {
			System.err.println(infracciones.get(i).getCve_agente());
			cve = infracciones.get(i).getCve_agente();
		}
		btnSincronizar.setEnabled(false);
		System.err.println(cve + " cve " + clave);
		System.err.println(!cve.equalsIgnoreCase(clave));
		System.err.println(!TextUtils.isEmpty(cve));
		if(!TextUtils.isEmpty(cve)) {
			//if(!cve.equalsIgnoreCase(clave)) {
			if(!clave.equalsIgnoreCase(ag)) {
				btnMulta.setEnabled(false);
				mostrarMsjSincronizar();
				btnSincronizar.setEnabled(true);
			}else {
				mostrarMsjResto(cons, folioMaximo);
			}
		}else {
			btnMulta.setEnabled(false);
			mostrarMsjSincronizar();
			btnSincronizar.setEnabled(true);
		} 
		
		try {
			PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
			ve = info.versionCode;
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
		
		if(folioMaximo == cons) {
			mostrarMsjFolio();
		}
		
		infraccion = ds.getAllInfraccion("");
		for (int i = 0; i < infraccion.size(); i++) {
			//System.err.println(infraccion.get(i).getLatitud() + " " + infraccion.get(i).getLongitud());
		}
		//System.err.println(ds.getCountPlacas("") + " placa");
		
		tab = ds.getAllTabletas("");
		
		tablet = new String[tab.size()];
		
		for (int i = 0; i < tab.size(); i++) {
			//System.out.println("tableta " + tab.get(i).getTableta());
			tablet[i] = tab.get(i).getTableta().trim();
			
		}
		
		
		ip = sp.getString("ip", "");
		ipftp = sp.getString("ipftp", "");
		config = sp.getString("numt", "");
		
		/*if(config.equalsIgnoreCase("")) {
			mostrarMsj();
		}*/
		btnConfigT.setVisibility(View.VISIBLE);
		//editor.putString("numt", mEdittText.getText().toString());
		
		dia = (cal.get(Calendar.DATE) >= 0 & cal.get(Calendar.DATE) < 10) ? "0" + cal.get(Calendar.DATE) : cal.get(Calendar.DATE) + "";
		mes = ((cal.get(Calendar.MONTH) + 1) >= 0 & (cal.get(Calendar.MONTH) + 1) < 10) ? "0" + (cal.get(Calendar.MONTH) + 1) : (cal.get(Calendar.MONTH) + 1) + "";
		axo = cal.get(Calendar.YEAR) + "";
		hr = (cal.get(Calendar.HOUR_OF_DAY) >= 0 & cal.get(Calendar.HOUR_OF_DAY) < 10) ? "0" + cal.get(Calendar.HOUR_OF_DAY) : cal.get(Calendar.HOUR_OF_DAY) + "";
		min = (cal.get(Calendar.MINUTE) >= 0 & cal.get(Calendar.MINUTE) < 10) ? "0" + cal.get(Calendar.MINUTE) : cal.get(Calendar.MINUTE) + "";
		seg = (cal.get(Calendar.SECOND) >= 0 & cal.get(Calendar.SECOND) < 10) ? "0" + cal.get(Calendar.SECOND) : cal.get(Calendar.SECOND) + "";
		
		//System.out.println(clave + dia + mes + axo.substring(2,4) + hr + min + seg);
		cla = clave + dia + mes + axo.substring(2,4) + hr + min + seg;
		
		System.out.println(cla + ".csv");
		
		System.out.println(ip);
		System.out.println(ipftp);
		System.out.println(config);
		
		//mFTP = ipftp;
		
		crearIndex();
		
		System.err.println(msj);
		
		infracciones = ds.getAllInfraccion(" estatus = 'N' or estatus = 1");
		
		System.out.println(infracciones.size());
		
		if(!infracciones.isEmpty())
			mostrarMsj(infracciones.size());
		
		System.err.println((folioMaximo - cons) + " resto");
		
	}
	
	private void mostrarMsj(String folio, String fecha) {
		AlertDialog.Builder al = new AlertDialog.Builder(this)
		.setTitle("Se sincronizo la tableta")
		.setMessage("Ultimo folio utilizado: " + folio)
		.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//onDestroy();
				//btnMulta.setEnabled(false);
			}
		});
		AlertDialog alert = al.create();
		alert.show();
	}
	
	private void mostrarMsjResto(int folio,int folioMaximo) {
		AlertDialog.Builder al = new AlertDialog.Builder(this)
		.setTitle("Mensaje")
		.setMessage("Le restan: " + (folioMaximo - folio) + " folios")
		.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//onDestroy();
				//btnMulta.setEnabled(false);
			}
		});
		AlertDialog alert = al.create();
		alert.show();
	}
	
	private void mostrarMsj(int folio) {
		AlertDialog.Builder al = new AlertDialog.Builder(this)
		.setTitle("Mensaje")
		.setMessage("No ha descargado los datos, tiene " + folio + " infraccion(es) pendientes por descargar")
		.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//onDestroy();
				//btnMulta.setEnabled(false);
			}
		});
		AlertDialog alert = al.create();
		alert.show();
	}
	
	private void mostrarMsj(String msj) {
		AlertDialog.Builder al = new AlertDialog.Builder(this)
		.setTitle("Mensaje")
		.setMessage(msj)
		.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//onDestroy();
				//btnMulta.setEnabled(false);
			}
		});
		AlertDialog alert = al.create();
		alert.show();
	}
	
	
	public void crearIndex() {
		String sql = "";
		
		try {
			sql = "CREATE INDEX IF NOT EXISTS placa_index ON placas(placa)";
			ds.database.execSQL(sql);
		} catch(SQLiteException e) {
			System.err.println("SQLiteException " + e.getMessage());
		}
		
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, (android.view.Menu) menu);
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
		
		bundle = new Bundle();
		bundle.putInt("id", idU);
		bundle.putString("us", usuario);
		bundle.putString("clave", clave);
		bundle.putString("num", config);
		
		switch (v.getId()) {
		case R.id.btnMulta:
			startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtras(bundle));
			break;
			
		case R.id.btnDescargaDatos:
			int d = ds.getAllInfraccion("estatus = 'N' or estatus = '1'").size();
			if(d > 0)
				d=1;
			if(validar()) {
				if(Conexion.validarConexion(Menu.this)) {
					//new SincronizarInfraccion().execute(clave);
					totalM = 0;
					totalF = ds.getAllFotografia(" estatus = 'N'").size();
					totalM = (totalF + d);
					exportar();
					new SubirArchivo().execute();
						//new Conectar().execute();
					//new CargaInfracciones().execute();
					
				} else {
					Toast toast = Toast.makeText(getApplicationContext(), "No se encontro conexion internet", Toast.LENGTH_SHORT);
					toast.setGravity(0, 0, 15);
					toast.show();
				}
			} else 
				mostrarMsjRegistro();
			break;
			
		case R.id.btnDescargaDatosCopy:
			if(validar()) {
				exportar();
				new ConectarCopy().execute();
				new CargaInfraccionesLocal().execute();
				//new CrearInfracion().execute();
			} else 
				mostrarMsjRegistro();
			break;
			
		case R.id.btnActualizarBD:
			new CrearBD().execute();
			break;
			
		case R.id.btnIP:
			mostrarVentana(1);
			break;
			
		case R.id.btnIPFTP:
			mostrarVentana(2);
			break;
			
		case R.id.btnActualizarAgente:
			new ActualizarAgentes().execute();
			break;
			
		case R.id.btnConfigTab:
			//mostrarVentana();
			 startActivity(new Intent(getApplicationContext(), com.perspective.movilidad.transito.Configurar.class ).putExtras(bundle));
			 onDestroy();
			break;
			
		case R.id.btnActualizarPlacas:
			exportDir = new File(Environment.getExternalStorageDirectory() + "/Placas/", "");
			if(!exportDir.exists())
				exportDir.mkdirs();
			file = new File(exportDir, "placas.txt");
			
			if(file.exists()) {
				new ImportarPlacas().execute();
			}else {
				Toast toast = Toast.makeText(getApplicationContext(), "No existe el archivo", Toast.LENGTH_LONG);
				toast.setGravity(15, 0, 0);
				toast.show();
			}
			break;
			
		case R.id.btnBusqueda:
			startActivity(new Intent(getApplicationContext(), Busqueda.class).putExtras(bundle));
			break;
			
		case R.id.btnDescargaApp:
			if(Conexion.validarConexion(getApplicationContext()))
				new Update(Menu.this).execute(String.valueOf(ve));
			else {
				Toast toast = Toast.makeText(getApplicationContext(), "No cuenta con conexión a internet", Toast.LENGTH_LONG);
				toast.setGravity(0, 0, 15);
				toast.show();
			}
			break;
			
		case R.id.btnDescargaFoto:
			//if(con.validarConexion(Menu.this))
			new EnviarFoto().execute();
			break;
			
		case R.id.btnSincronizar:
			if(Conexion.validarConexion(Menu.this)) {
				new SincronizarInfraccion1().execute(clave);
			} else {
				Toast toast = Toast.makeText(getApplicationContext(), "No se encontro conexion internet", Toast.LENGTH_SHORT);
				toast.setGravity(0, 0, 15);
				toast.show();
			}
			
			break;
			
		case R.id.btnCorregirTableta:
			startActivity(new Intent(getApplicationContext(), CorregirTableta.class));
			break;
			
		case R.id.btnImprimirTicketLinea:
			startActivity(new Intent(getApplicationContext(), ImprimirTicket.class));
			break;

		default:
			break;
		}
		
	}
	
	class SincronizarInfraccion extends AsyncTask<String, String, String>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			/*pdialogo = new ProgressDialog(Menu.this);
			pdialogo.setMessage("Importando datos..");
			pdialogo.setIndeterminate(false);
			pdialogo.setCancelable(false);
			pdialogo.show();*/
		}

		@Override
		protected String doInBackground(String... params) {
			
			ArrayList<NameValuePair> cve = new ArrayList<NameValuePair>();
			cve.add(new BasicNameValuePair("cve_agente", params[0]));
			
			
			JSONObject jobject = null;
			try{
				jobject = jsonParser.realizarHttpRequest("http://apidiv.guadalajara.gob.mx:8085/serversql/sincronizar.php", "GET", cve);
			} catch (Exception e) {
				System.err.println(e.getMessage() + " mensaje");
			}
			fol = 0;
			
			String fecha = "";
			try{
				fol = jobject.getInt("status");
				fecha = jobject.getString("fecha");
			}catch(JSONException e) {
				System.err.println(e.getMessage());
			}
			System.err.println(fecha);
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
			pdialogo.dismiss();
			String fecha1;
			bol = ds.getAllAgentes1("cve_agente = '" + clave + "'");
			fecha1 = ds.getAllAgentes2( " folio = '" + bol + "'");
			System.err.println(fecha1);
			System.err.println(result + " " + bol + " " + folioMinimo + " " + fol);
			SharedPreferences.Editor editor = sp.edit();
			
			boolean r = false;
			editor.putString("agente", clave);
			if(result.equalsIgnoreCase("ok")){
				/*if(fol == folioMinimo) {
					System.err.println(folioMinimo + " minimo");
					if(folioMinimo != 0 | folioMinimo != 1) {
						editor.putInt("cons", folioMinimo+1);
						//ag = sp.getString("agente", "");
						
						r = editor.commit();
						if(r)
							mostrarMsj(String.valueOf(folioMinimo), "");
					} else {
						mostrarMsj("Error de comunicación, favor de volver a sincronizar");
					}
				} else if(fol < bol) {
					System.err.println((bol + 1) + " bol" + fecha1 );
					if(bol != 0 | bol != 1) {
						editor.putInt("cons", bol+1);
						r = editor.commit();
						if(r)
							mostrarMsj(String.valueOf(bol), " de fecha " + fecha1);
					} else {
						mostrarMsj("Error de comunicación, favor de volver a sincronizar");
					}
				} else {*/
					System.err.println((fol + 1) + " fol " + fecha);
					if(fol != 0 | fol != 1) {
						editor.putInt("cons", fol+1);
						r = editor.commit();
						if(r) {
							mostrarMsj(String.valueOf(fol), " de fecha " + fecha);
							/*List<Infraccion> in = new ArrayList<Infraccion>();
							in = ds.getAllInfraccion(" folio > '"+fol+"' order by id_infracciones desc limit 10");
							String a = "";
							System.err.println(in.size() + " antes");
							if(!in.isEmpty()) {
								a = in.get(0).getCve_agente();
								in = ds.getAllInfraccion(" folio > '3862940' and cve_agente = '"+a+"' order by id_infracciones desc limit 10");
								System.err.println(in.size() + " entro");
								if(!in.isEmpty()) {
									System.err.println("update");
								} else {
									System.err.println("bien");
								}
							}else {
								System.err.println("vacio");
							}*/
							
						}
					}
					else {
						mostrarMsj("Error de comunicaci�n, favor de volver a sincronizar");
					}
				//}
			}else {
				
				System.err.println(folioMinimo + " minimo");
				if(folioMinimo != 0 | folioMinimo != 1) {
					editor.putInt("cons", folioMinimo+1);
					r = editor.commit();
					if(r)
						mostrarMsj(String.valueOf(folioMinimo), "");
				} else {
					mostrarMsj("Error de comunicaci�n, favor de volver a sincronizar");
				}
				
				/*System.err.println(folioMinimo);
				editor.putInt("cons", folioMinimo+1);
				r = editor.commit();*/
				
				/*if(fol == folioMinimo) {
					System.err.println(folioMinimo);
					editor.putInt("cons", folioMinimo);
					r = editor.commit();
				} else if(fol < bol) {
					System.err.println(bol + 1);
					editor.putInt("cons", bol);
					r = editor.commit();
				} else {
					System.err.println(fol + 1);
					editor.putInt("cons", fol);
					r = editor.commit();
				}*/
			}
			Toast toast;
			if(r) {
				toast = Toast.makeText(getApplicationContext(), "Se sincronizo la infraccion", Toast.LENGTH_LONG);
				if(con1 != 0) {
					if((usuario.trim().equalsIgnoreCase("administrador") | usuario.trim().equalsIgnoreCase("admin")) | (usuario.trim().equalsIgnoreCase("siste") | usuario.trim().equalsIgnoreCase("sistemas"))) {
						btnMulta.setEnabled(false);
					}else {
						btnMulta.setEnabled(true);
					}
				} else  {
					btnMulta.setEnabled(false);
				}
			} else {
				toast = Toast.makeText(getApplicationContext(), "No se pudo sincronizar, intente más tarde", Toast.LENGTH_LONG);
			}
			toast.setGravity(0, 0, 15);
			toast.show();
				
		}
		
	}
	
	class SincronizarInfraccion1 extends AsyncTask<String, String, String>{
		String a;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pdialogo = new ProgressDialog(Menu.this);
			pdialogo.setMessage("Sincronizando infracción..");
			pdialogo.setIndeterminate(false);
			pdialogo.setCancelable(false);
			pdialogo.show();
		}

		@Override
		protected String doInBackground(String... params) {
			
			ArrayList<NameValuePair> cve = new ArrayList<NameValuePair>();
			cve.add(new BasicNameValuePair("cve_agente", params[0]));
			
			 a = params[0];
			
			JSONObject jobject = null;
			try{
				jobject = jsonParser.realizarHttpRequest("http://apidiv.guadalajara.gob.mx:8085/serversql/sincronizar.php", "GET", cve);
			} catch (Exception e) {
				System.err.println(e.getMessage() + " mensaje");
			}
			fol = 0;
			
			String fecha = "";
			try{
				fol = jobject.getInt("status");
				fecha = jobject.getString("fecha");
			}catch(JSONException e) {
				System.err.println(e.getMessage());
			}
			System.err.println(fecha);
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
			pdialogo.dismiss();
			String fecha1;
			bol = ds.getAllAgentes1("cve_agente = '" + clave + "'");
			fecha1 = ds.getAllAgentes2( " folio = '" + bol + "'");
			System.err.println(fecha1);
			System.err.println(result + " " + bol + " " + folioMinimo + " " + fol);
			SharedPreferences.Editor editor = sp.edit();
			
			boolean r = false;
			editor.putString("agente", clave);
			if(result.equalsIgnoreCase("ok")){
				/*if(fol == folioMinimo) {
					System.err.println(folioMinimo + " minimo");
					if(folioMinimo != 0 | folioMinimo != 1) {
						editor.putInt("cons", folioMinimo+1);
						//ag = sp.getString("agente", "");
						
						r = editor.commit();
						if(r)
							mostrarMsj(String.valueOf(folioMinimo), "");
					} else {
						mostrarMsj("Error de comunicación, favor de volver a sincronizar");
					}
				} else if(fol < bol) {
					System.err.println((bol + 1) + " bol" + fecha1 );
					if(bol != 0 | bol != 1) {
						editor.putInt("cons", bol+1);
						r = editor.commit();
						if(r)
							mostrarMsj(String.valueOf(bol), " de fecha " + fecha1);
					} else {
						mostrarMsj("Error de comunicación, favor de volver a sincronizar");
					}
				} else {*/
					System.err.println((fol + 1) + " fol " + fecha);
					if(fol != 0 | fol != 1) {
						editor.putInt("cons", fol+1);
						r = editor.commit();
						if(r) {
							mostrarMsj(String.valueOf(fol), " de fecha " + fecha);
							List<Infraccion> in = new ArrayList<Infraccion>();
							in = ds.getAllInfraccion(" CAST(folio AS INTEGER) > " + fol + " and cve_agente = '"+a+"' order by id_infracciones desc limit 10");
							
							if(!in.isEmpty()) {
								System.err.println("update");
								ContentValues cv = new ContentValues();
								
								cv.put("estatus", "N");
								
								System.err.println(ds.database.update("infracciones", cv, " CAST(folio AS INTEGER) > " + fol + " and cve_agente = '"+a+"'", null) + " update");
								//System.err.println(ds.database.update("infracciones", cv, "folio > '3742940'", null) + " update");
							} else {
								System.err.println("bien");
							}
							
						}
					}
					else {
						mostrarMsj("Error de comunicaci�n, favor de volver a sincronizar");
					}
				//}
			}else {
				
				System.err.println(folioMinimo + " minimo");
				if(folioMinimo != 0 | folioMinimo != 1) {
					editor.putInt("cons", folioMinimo+1);
					r = editor.commit();
					if(r)
						mostrarMsj(String.valueOf(folioMinimo), "");
				} else {
					mostrarMsj("Error de comunicación, favor de volver a sincronizar");
				}
				
				/*System.err.println(folioMinimo);
				editor.putInt("cons", folioMinimo+1);
				r = editor.commit();*/
				
				/*if(fol == folioMinimo) {
					System.err.println(folioMinimo);
					editor.putInt("cons", folioMinimo);
					r = editor.commit();
				} else if(fol < bol) {
					System.err.println(bol + 1);
					editor.putInt("cons", bol);
					r = editor.commit();
				} else {
					System.err.println(fol + 1);
					editor.putInt("cons", fol);
					r = editor.commit();
				}*/
			}
			Toast toast;
			if(r) {
				toast = Toast.makeText(getApplicationContext(), "Se sincronizo la infraccion", Toast.LENGTH_LONG);
				if(con1 != 0) {
					if((usuario.trim().equalsIgnoreCase("administrador") | usuario.trim().equalsIgnoreCase("admin")) | (usuario.trim().equalsIgnoreCase("siste") | usuario.trim().equalsIgnoreCase("sistemas"))) {
						btnMulta.setEnabled(false);
					}else {
						btnMulta.setEnabled(true);
					}
				} else  {
					btnMulta.setEnabled(false);
				}
			} else {
				toast = Toast.makeText(getApplicationContext(), "No se pudo sincronizar, intente más tarde", Toast.LENGTH_LONG);
			}
			toast.setGravity(0, 0, 15);
			toast.show();
				
		}
		
	}
	
	public boolean importar(File path,File file) {
		
		try {
			//if(file.exists())  {
				
			eliminaRegistrosTodo("placas");
				
			CSVReader reader=new CSVReader(new InputStreamReader(new FileInputStream(file), "ISO-8859-1"), '|', '\'', 1);
				
			String [] nextLine;
				
			Placas placas = null;
			String axo1 = String.valueOf(0);
			long startTime = System.currentTimeMillis();
			ds.database.beginTransaction();
						
			while((nextLine = reader.readNext()) != null) {
				//System.err.println(nextLine[9].trim().replace("'", ""));
				String placa = nextLine[0].trim().replace("'", "");
				String propie_placas = nextLine[1].trim().replace("'", "");
				String direccion_placas = nextLine[2].trim().replace("'", "");
				String colonia_placas = nextLine[3].trim().replace("'", "");
				String serie = nextLine[4].trim().replace("'", "");
				String marca = nextLine[5].trim().replace("'", "");
				String tipo = nextLine[6].trim().replace("'", "");
				String linea = nextLine[7].trim().replace("'", "");
				String color = nextLine[8].trim().replace("'", "");
				//String tipo_vehiculo = nextLine[9].trim();
				String axo_modelo = nextLine[10].trim().replace("'", "");
					
				if(axo_modelo.equalsIgnoreCase("null"))
					axo1 = String.valueOf(0);
						
				placas = new Placas(placa, propie_placas, direccion_placas, colonia_placas, serie, marca, tipo, linea, Integer.parseInt(axo1), color);
				//System.err.println(placas.getPlaca() + " placa");
				ds.crearPlaca(placas);
								
			}
			reader.close();
			ds.database.setTransactionSuccessful();
			long diff = System.currentTimeMillis() - startTime;
			System.err.println(Long.toString(diff) + " ms");
			return true;
		/*}
		else 
			return false;*/
			
		}catch (UnsupportedEncodingException e) {
			System.out.println("UnsupportedEncodingException " +e.getMessage());
			return false;
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException " + e.getMessage());
			return false;
		} catch (IOException e) {
			System.out.println("IOException " + e.getMessage());
			return false;
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
			return false;
		}finally {
			ds.database.endTransaction();
		}
	}
	
	public class ImportarPlacas extends AsyncTask<String, String, String> {
		boolean res=false;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pdialogo = new ProgressDialog(Menu.this);
			pdialogo.setMessage("Importando datos..");
			pdialogo.setIndeterminate(false);
			pdialogo.setCancelable(false);
			pdialogo.show();
		}

		@Override
		protected String doInBackground(String... params) {
			
			res = importar(exportDir, file);
			System.err.println(res);
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pdialogo.dismiss();
			Toast toast;
			if(res) 
				toast = Toast.makeText(getApplicationContext(), "Datos importados", Toast.LENGTH_LONG);
			 else 
				toast = Toast.makeText(getApplicationContext(), "Ocurrio algo al importar los datos", Toast.LENGTH_LONG);
			
			toast.setGravity(0, 0, 15);
			toast.show();
		}
		
	}
	
	public void eliminaRegistros(String tabla) {
    	GestionBD gestion = new GestionBD(getApplicationContext(),"transito.db",null,1);
    	SQLiteDatabase db = gestion.getReadableDatabase();
    	try {
    		Cursor c = db.rawQuery("SELECT * FROM " + tabla, null);
			if (c.moveToFirst()) {
				do {
					//System.out.println(c.getString(1));
					db.delete(tabla, c.getColumnName(0) + " = " + c.getString(0), null);
				} while (c.moveToNext());
			}
			c.close();
		} catch (SQLiteException e) {
			Log.e("SQLiteException ", e.getMessage());
		}
    	finally {
    		db.close();
    	}
    }
	
	public void eliminaRegistrosTodo(String tabla) {
    	GestionBD gestion = new GestionBD(getApplicationContext(),"transito.db",null,1);
    	SQLiteDatabase db = gestion.getReadableDatabase();
    	try {
			//if (c.moveToFirst()) {
				//do {
					//System.out.println(c.getString(1));
					db.delete(tabla, null, null);
				//} while (c.moveToNext());
			//}
			//c.close();
		} catch (SQLiteException e) {
			Log.e("SQLiteException ", e.getMessage());
		}
    	finally {
    		db.close();
    	}
    }
	
	public void inserta() {
		System.out.println("http://"+ip+"/serverSQL/getc_infracciones.php");
		//if(!con.search("http://"+ip+"/sitios/apa_cat/serverSQL/getInfraccion.php").trim().equalsIgnoreCase("No se pudo conectar con el servidor")) {
		if(!con.search("http://"+ip+"/serverSQL/getc_infracciones.php").trim().equalsIgnoreCase("No se pudo conectar con el servidor")) {
			if(!con.search("http://"+ip+"/serverSQL/getc_infracciones.php").trim().equalsIgnoreCase("null")) {
			//if(!con.search("http://"+ip+"/sitios/apa_cat/serverSQL/getInfraccion.php").trim().equalsIgnoreCase("null")) {
				eliminaRegistrosTodo("c_infracciones");
				//con.insetarRegistros("http://"+ip+"/sitios/apa_cat/serverSQL/getInfraccion.php", "c_infracciones");
				con.insetarRegistros("http://"+ip+"/serverSQL/getc_infracciones.php", "c_infracciones");
			}
			
			/*if(!con.search("http://"+ip+"/sitios/apa_cat/serverSQL/getCAgentes.php").trim().equalsIgnoreCase("null")) {
				eliminaRegistrosTodo("c_agentes");
				con.insetarRegistros("http://"+ip+"/sitios/apa_cat/serverSQL/getCAgentes.php", "c_agentes");
			}*/
			
			if(!con.search("http://"+ip+"/serverSQL/getCAgentes.php").trim().equalsIgnoreCase("null")) {
				eliminaRegistrosTodo("c_agentes");
				con.insetarRegistros("http://"+ip+"/serverSQL/getCAgentes.php", "c_agentes");
			}
			
			/*if(!con.search("http://"+ip+"/sitios/apa_cat/serverSQL/getPlacas.php").trim().equalsIgnoreCase("null")) {
			//if(!con.search("http://"+ip+"/transito/serverSQL/getPlacas.php").trim().equalsIgnoreCase("null")) {
				eliminaRegistros("placas");
				con.insetarRegistros("http://"+ip+"/sitios/apa_cat/serverSQL/getPlacas.php", "placas");
				//con.insetarRegistros("http://"+ip+"/transito/serverSQL/getPlacas.php", "placas");
			}*/
			/*if(!con.search("http://192.168.0.16:8080/sitios/apa_cat/serverSQL/getTabletas.php").trim().equalsIgnoreCase("null")) {
				//if(!con.search("http://"+ip+"/transito/serverSQL/getPlacas.php").trim().equalsIgnoreCase("null")) {
				eliminaRegistros("c_tabletas");
				con.insetarRegistros("http://192.168.0.16:8080/sitios/apa_cat/serverSQL/getTabletas.php", "c_tabletas");
				//con.insetarRegistros("http://"+ip+"/transito/serverSQL/getPlacas.php", "placas");
			}
			if(!con.search("http://"+ip+"/sitios/apa_cat/serverSQL/getTabletas.php").trim().equalsIgnoreCase("null")) {
				//if(!con.search("http://"+ip+"/transito/serverSQL/getPlacas.php").trim().equalsIgnoreCase("null")) {
				eliminaRegistrosTodo("c_tabletas");
				con.insetarRegistros("http://"+ip+"/sitios/apa_cat/serverSQL/getTabletas.php", "c_tabletas");
				//con.insetarRegistros("http://"+ip+"/transito/serverSQL/getPlacas.php", "placas");
			}*/
			
			if(!con.search("http://"+ip+"/serverSQL/getc_colores.php").trim().equalsIgnoreCase("null")) {
				eliminaRegistrosTodo("c_colores");
				con.insetarRegistros("http://"+ip+"/serverSQL/getc_colores.php", "c_colores");
			}
			if(!con.search("http://"+ip+"/serverSQL/getc_estados.php").trim().equalsIgnoreCase("null")) {
				eliminaRegistrosTodo("c_estados");
				con.insetarRegistros("http://"+ip+"/serverSQL/getc_estados.php", "c_estados");
			}
			if(!con.search("http://"+ip+"/serverSQL/getc_marcas.php").trim().equalsIgnoreCase("null")) {
				eliminaRegistrosTodo("c_marcas");
				con.insetarRegistros("http://"+ip+"/serverSQL/getc_marcas.php", "c_marcas");
			}
			msj = "Datos Actualizados";
		}else
			msj = "No se pudo conectar con el servidor";
	}
	
	public void insertaAgentes() {
		System.out.println("http://"+ip+"/serverSQL/getInfracciones.php");
		if(!con.search("http://"+ip+"/serverSQL/getInfraccion.php").trim().equalsIgnoreCase("No se pudo conectar con el servidor")) {
			if(!con.search("http://"+ip+"/serverSQL/getCAgentes.php").trim().equalsIgnoreCase("null")) {
				eliminaRegistrosTodo("c_agentes");
				con.insetarRegistros("http://"+ip+"/serverSQL/getCAgentes.php", "c_agentes");
			}
			msj = "Datos Actualizados";
		}else
			msj = "No se pudo conectar con el servidor";
		/*System.out.println("http://"+ip+"/transito/serverSQL/getInfracciones.php");
		if(!con.search("http://"+ip+"/sitios/apa_cat/serverSQL/getInfraccion.php").trim().equalsIgnoreCase("No se pudo conectar con el servidor")) {
			if(!con.search("http://"+ip+"/sitios/apa_cat/serverSQL/getCAgentes.php").trim().equalsIgnoreCase("null")) {
				eliminaRegistrosTodo("c_agentes");
				con.insetarRegistros("http://"+ip+"/sitios/apa_cat/serverSQL/getCAgentes.php", "c_agentes");
			}
			msj = "Datos Actualizados";
		}else
			msj = "No se pudo conectar con el servidor";*/
	}
	
	public void descargarInfraccion() {
		try {
			System.out.println("si " + ip );
			if(ip != "") {
			//if(con.validarConexion(getApplicationContext())) {
				System.out.println("si1");
				infraccion = ds.getAllInfraccion("");
				System.err.println(infraccion.size() + " lectura");
				System.out.println("http://" + ip + "/serverSQL/insertInfracciones.php");
				for (int i = 0; i < infraccion.size(); i++) {
					if(Conexion.infraccion(infraccion.get(i).getPaterno_infraccion(), infraccion.get(i).getMaterno_infraccion(), infraccion.get(i).getNombres_infraccion(), infraccion.get(i).getFecha_multa(), infraccion.get(i).getCve_infraccion(), infraccion.get(i).getHora_infraccion(), infraccion.get(i).getCve_agente(), infraccion.get(i).getSerie_infracciones(), infraccion.get(i).getC_id_infracciones(), infraccion.get(i).getMarca(), infraccion.get(i).getTipo(), infraccion.get(i).getColor(), infraccion.get(i).getProcedencia(), infraccion.get(i).getDomicilio_conductor(), infraccion.get(i).getColonia_conductor(), infraccion.get(i).getCiudad_conductor(), infraccion.get(i).getLicencia_conductor(), infraccion.get(i).getTipo_lic(), infraccion.get(i).getNombres_infraccion(), infraccion.get(i).getDomicilio_propietario(), infraccion.get(i).getColonia_propietario(), infraccion.get(i).getCiudad_propietario(), infraccion.get(i).getArticulos(), infraccion.get(i).getObservaciones_tablet(), "http://" + ip + "/serverSQL/insertInfracciones.php").equalsIgnoreCase("")) {
					//if(Conexion.insertaLecturas(lec.get(i).getLecturaReal(), lec.get(i).getConsumoReal(), lec.get(i).getCalculada(), lec.get(i).getCapturo(), lec.get(i).getId_apa(), lec.get(i).getPeriodo(), "http://192.168.0.11/apa_cat/serverSQL/insertLecturas.php").equalsIgnoreCase("S"))
						System.out.println("Inserto");
					}
					else
						System.out.println("No inserto");
				}
			/*}else {
				Log.e("msj", "ho hay conexión");
			}*/
			}else {
				Toast toast = Toast.makeText(getApplicationContext(), "No ha ingresado la IP", Toast.LENGTH_SHORT);
				toast.setGravity(0, 0, 15);
				toast.show();
			}
		} catch (Exception e) {
			
		}
		finally {
			
		}
	}
	
	
	public void conectarCopy() throws SocketException {
		System.out.println("conectar");
		FTPClient client = null;
		
		
		try {
			res = false;
			client = new FTPClient();
			client.connect(InetAddress.getByName(mFTPCopy), 21);
			System.out.println(client.login(user, pass) + " login");
			System.out.println(client.isConnected() + " conectado");
			System.out.println(client.printWorkingDirectory() + " dir");
			System.out.println(client.mkd("/prueba/") + " crear");
			System.out.println(client.changeWorkingDirectory("/prueba/"));
			System.out.println(client.printWorkingDirectory() + " prueba");
			client.setFileType(FTP.BINARY_FILE_TYPE);
			BufferedInputStream buffer = null;
			buffer = new BufferedInputStream(new FileInputStream(Environment.getExternalStorageDirectory() + "/prueba/" + cla + ".csv"));
			client.enterLocalPassiveMode();
			res = client.storeFile(cla + ".csv", buffer);
			client.disconnect();
			System.out.println(client.isConnected()  + " conectado");
		} catch (Exception e) {
			System.out.println(e.getMessage() + " 1");
		}
	}
	
	public void conectar() throws SocketException {
		System.out.println("conectar");
		FTPClient client = null;
		
		try {
			res = false;
			System.out.println("conectar1");
			client = new FTPClient();
			System.out.println("conectar2");
			client.connect(InetAddress.getByName(mFTP), 21);
			System.out.println("conectar3");
			System.out.println(client.login(user, pass) + " login");
			System.out.println(client.isConnected() + " conectado");
			System.out.println(client.printWorkingDirectory() + " dir");
			System.out.println(client.mkd("/prueba/") + " crear");
			System.out.println(client.changeWorkingDirectory("/prueba/"));
			System.out.println(client.printWorkingDirectory() + " prueba");
			client.setFileType(FTP.BINARY_FILE_TYPE);
			BufferedInputStream buffer = null;
			buffer = new BufferedInputStream(new FileInputStream(Environment.getExternalStorageDirectory() + "/prueba/" + cla + ".csv"));
			client.enterLocalPassiveMode();
			res = client.storeFile(cla + ".csv", buffer);
			client.disconnect();
			System.out.println(client.isConnected()  + " conectado");
		} catch (Exception e) {
			System.out.println(e.getMessage() + " 1");
		}
	}
	
	class CrearBD extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pdialogo = new ProgressDialog(Menu.this);
			pdialogo.setMessage("Descargando datos...");
			pdialogo.setIndeterminate(false);
			pdialogo.show();
		}
		
		@Override
		protected String doInBackground(String... params) {
			inserta();
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pdialogo.dismiss();
			
			try {
				Thread.sleep(3000);
			} catch (Exception e) {
				
			}
			
			mostrarMsjA();
			/*Toast toast = Toast.makeText(getApplicationContext(), msj, Toast.LENGTH_LONG);
			toast.setGravity(0, 0, 15);
			toast.show();*/
		}
	}
	
	class ActualizarAgentes extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pdialogo = new ProgressDialog(Menu.this);
			pdialogo.setMessage("Actualizando Agentes...");
			pdialogo.setIndeterminate(false);
			pdialogo.show();
		}
		
		@Override
		protected String doInBackground(String... params) {
			insertaAgentes();
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pdialogo.dismiss();
			
			try {
				mostrarMsjAg();
			} catch (Exception e) {
				
			}
			
			/*Toast toast = Toast.makeText(getApplicationContext(), msj, Toast.LENGTH_LONG);
			toast.setGravity(0, 0, 15);
			toast.show();*/
		}
	}
	
	class CrearInfracion extends AsyncTask<String, String, String> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pdialogo = new ProgressDialog(Menu.this);
			pdialogo.setMessage("Descargando datos..");
			pdialogo.setIndeterminate(false);
			pdialogo.show();
		}

		@Override
		protected String doInBackground(String... params) {
			descargarInfraccion();
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pdialogo.dismiss();
		}
		
	}
	
	/*public void conectarA()  {
		System.out.println("conectarA");
		
		File f;
		f = new File(Environment.getExternalStorageDirectory() + "/prueba/" + cla + ".csv");
		File file = new File(f.getAbsolutePath());
		
		try {
			
			res = false;
			
			MultipartEntity mpEntity = new MultipartEntity();
			ContentBody foto = new FileBody(file, "text/csv");
			
			mpEntity.addPart("aUp", foto);
			mpEntity.addPart("archivo", new StringBody(cla + ".csv"));
			
			//JSONObject json = jsonParser.subirImage("http://apidiv.guadalajara.gob.mx:8085/infracciones/sistema/pruebaI.php", "POST", mpEntity);
			
			//JSONObject json = jsonParser.subirImage("http://192.168.0.14:8080/sitios/JuecesG/arvhivo_up.php", "POST", mpEntity);
			//JSONObject json = jsonParser.subirImage("http://apidiv.guadalajara.gob.mx:8085/infracciones/sistema/arvhivo_up.php", "POST", mpEntity);
			JSONObject json = jsonParser.subirImage("http://"+ip+"/infracciones/sistema/arvhivo_up.php", "POST", mpEntity);
			
			int success = json.getInt("status");
			System.out.println(success + " success");
			
			if(success > 0) {
				System.out.println("envio movio");
				//validar que se almaceno
				res = true;
			}else {
				res = false;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage() + " 1");
		}
	}*/
	
	class SubirArchivo extends AsyncTask<Integer, Integer, Integer> {
		
		int ii = 1;
		int fo= 0;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pdialogo = new ProgressDialog(Menu.this);
			pdialogo.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pdialogo.setMessage("Enviando archivo");
			pdialogo.setIndeterminate(false);
			pdialogo.setCancelable(false);
			pdialogo.setMax(totalM);
			pdialogo.setProgress(0);
			pdialogo.show();
			
		}
		
		@Override
		protected Integer doInBackground(Integer... params) {
			conectarA();
			//fotografias();
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			pdialogo.setProgress(values[0]);
		}
		
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			pdialogo.dismiss();
			if(res || (totalF > 0)) {
				new CargaInfracciones().execute();
				String sql = "select * from infracciones WHERE estatus = '1'";
				System.out.println(sql);
				Cursor cursor = ds.database.rawQuery(sql, null);
				
				if (cursor.moveToFirst()) {
					do {
						ContentValues cv = new ContentValues();
						cv.put("estatus", "S");
						System.out.println(ds.database.update("infracciones", cv, "id_infracciones='" + cursor.getInt(0) + "' ", null) + " actualizo");
					} while (cursor.moveToNext());
				}
				toast = Toast.makeText(getApplicationContext(), "Se enviaron " + count + " registros \nSe enviaron " + totalE + " fotos de "+totalM +" m�s el archivo de datos", Toast.LENGTH_SHORT);
				new SincronizarInfraccion().execute(clave);
				tvM.setText("Se enviaron " + count + " registros \nSe enviaron " + totalE + " fotos de "+totalM +" m�s el archivo de datos");
			}
			else
				toast = Toast.makeText(getApplicationContext(), "Problemas de comunicaci�n, favor de intentarlo m�s tade", Toast.LENGTH_LONG);
			toast.setGravity(0, 0, 15);
			toast.show();
			//mostrarMsj();
		}
		
		public void conectarA()  {
			System.out.println("conectarA");
			
			File f;
			f = new File(Environment.getExternalStorageDirectory() + "/prueba/" + cla + ".csv");
			File file = new File(f.getAbsolutePath());
			
			try {
				
				res = false;
				
				MultipartEntity mpEntity = new MultipartEntity();
				ContentBody foto = new FileBody(file, "text/csv");
				
				mpEntity.addPart("aUp", foto);
				mpEntity.addPart("archivo", new StringBody(cla + ".csv"));
				
				//JSONObject json = jsonParser.subirImage("http://apidiv.guadalajara.gob.mx:8085/infracciones/sistema/pruebaI.php", "POST", mpEntity);
				
				//JSONObject json = jsonParser.subirImage("http://192.168.0.14:8080/sitios/JuecesG/arvhivo_up.php", "POST", mpEntity);
				//JSONObject json = jsonParser.subirImage("http://apidiv.guadalajara.gob.mx:8085/infracciones/sistema/arvhivo_up.php", "POST", mpEntity);
				JSONObject json = jsonParser.subirImage("http://"+ip+"/infracciones/sistema/arvhivo_up.php", "POST", mpEntity);
				
				int success = json.getInt("status");
				System.out.println(success + " success");
				
				if(success > 0) {
					System.out.println("envio movio");
					publishProgress(ii);
					ii++;
					//validar que se almaceno
					res = true;
				}else {
					res = false;
				}
				
				nos = 0;
				countF = 0;
				int totalf = 0;
				String s,dir,ar;
				dir = Environment.getExternalStorageDirectory() + "/Infracciones/fotografias/";
				File f1;
				fotografia = ds.getAllFotografia(" estatus = 'N'");
				totalf = fotografia.size();
				try {
					for (int i = 0; i < fotografia.size(); i++) {
						s = fotografia.get(i).getNumero_acta();
						ar = fotografia.get(i).getArchivo();
						Log.i("Dato", s + " " + ar);
						f1 = new File(dir + s + "/" +  ar);
						System.out.println(f1.exists() + f1.getAbsolutePath());
						
						if (f1.exists()) {
							Log.i("Mes", "if exist");
							File file1 = new File(f1.getAbsolutePath());
							//if(f.length()/1024000 <= 2) { 
							MultipartEntity mpEntity1 = new MultipartEntity();
								
							ContentBody foto1 = new FileBody(file1, "image/jpeg");
								
							mpEntity1.addPart("fotoUp", foto1);
							mpEntity1.addPart("foto", new StringBody(s));
							mpEntity1.addPart("archivo",new StringBody(ar));
								
							//JSONObject json = jsonParser.subirImage("http://apidiv.guadalajara.gob.mx:8085/infracciones/sistema/fotoinfraccion.php", "POST", mpEntity);
							JSONObject json1 = jsonParser.subirImage("http://" + ip + "/infracciones/sistema/fotoinfraccion1.php", "POST", mpEntity1);
							//JSONObject json = jsonParser.subirImage("http://192.168.0.14:8080/sitios/JuecesG/fotoinfraccion.php", "POST", mpEntity);
								
							int success1 = json1.getInt("status");
							System.out.println(success1 + " success");
								
							ContentValues cv = new ContentValues();
							if(success1 < 3)
								fo++;
								
							if(success1 == 1) {
								System.out.println("envio movio");
								cv.put("estatus", "S");
								//sql = "update Fotografia set ";
								ds.database.update("Fotografia", cv, " id_fotografia = " + fotografia.get(i).getIdFotofrafia(), null);
								//db.update("Fotografia", cv, " id_fotografia = " + c.getInt(0) , null);
								countF++;
								totalE++;
							}
							else if(success1 == 0) {
								System.out.println("envio no movio");
								/*cv.put("estatus", "S");
								ds.database.update("Fotografia", cv, " id_fotografia = " + fotografia.get(i).getIdFotofrafia(), null);*/
							}else if(success1 == 3) {
								System.out.println("existe");
								cv.put("estatus", "S");
								ds.database.update("Fotografia", cv, " id_fotografia = " + fotografia.get(i).getIdFotofrafia(), null);
								//db.update("Fotografia", cv, " id_fotografia = " + c.getInt(0) , null);
								//totalE++;
							}
							else
								System.out.println("no envio f");
							/*}
							else 
								nos++;*/
							
						} 
						nos = totalf-nos;
						
						//publishProgress((int) i * 100/totalf);
						publishProgress(ii);
						ii++;
						
						System.err.println("publishProgress(1)");
					
						Thread.sleep(4000);
						if(countF > 0) {
							try {
								
								ArrayList<NameValuePair> carga = new ArrayList<NameValuePair>();
								
								carga.add(new BasicNameValuePair("tableta", config));
								carga.add(new BasicNameValuePair("registros", String.valueOf(0)));
								carga.add(new BasicNameValuePair("fotos", String.valueOf(countF)));
								
								//JSONObject json = jsonParser.realizarHttpRequest("http://apidiv.guadalajara.gob.mx:8085/serverSQL/insertCarga.php", "POST", carga);
								JSONObject json1 = jsonParser.realizarHttpRequest("http://"+ip+"/serverSQL/insertCarga.php", "POST", carga);
								
								//JSONObject json = jsonParser.realizarHttpRequest("http://apidiv.guadalajara.gob.mx:8085/serverSQL/insertCarga.php", "POST", carga);
								
								int estatus = json1.getInt("status");
								
								if(estatus == 1)
									System.err.println("inserto");
								else
									System.err.println("no inserto");
							
							}catch(JSONException e) {
								System.out.println(e.getMessage() + " mm");
							}
						}
					} 
				} catch(Exception e) {
					System.err.println(e.getMessage());
				}
				
				
			} catch (Exception e) {
				System.out.println(e.getMessage() + " 1");
			}
		}
		
	}
	
	class Conectar extends AsyncTask<String, String, String> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pdialogo = new ProgressDialog(Menu.this);
			pdialogo.setMessage("Enviando archivo");
			pdialogo.setIndeterminate(false);
			pdialogo.setCancelable(false);
			pdialogo.show();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				conectar();
			} catch (Exception e) {
				
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pdialogo.dismiss();
			Toast toast;
			if(res) {
				String sql = "select * from infracciones WHERE estatus = '1'";
				System.out.println(sql);
				Cursor cursor = ds.database.rawQuery(sql, null);
				
				if (cursor.moveToFirst()) {
					do {
						ContentValues cv = new ContentValues();
						cv.put("estatus", "S");
						System.out.println(ds.database.update("infracciones", cv, "id_infracciones='" + cursor.getInt(0) + "' ", null) + " actualizo");
					} while (cursor.moveToNext());
				}
				toast = Toast.makeText(getApplicationContext(), "Se envio \nSe enviaron " + count + " registros", Toast.LENGTH_SHORT);
			}
			else
				toast = Toast.makeText(getApplicationContext(), "No se almaceno", Toast.LENGTH_SHORT);
			toast.setGravity(0, 0, 15);
			toast.show();
			//mostrarMsj();
		}
		
	}
	
	/*public void mostrarMsj() {
		AlertDialog.Builder msj = new AlertDialog.Builder(Menu.this);
		msj.setTitle("Mensaje").setMessage("Se enviaron " + count + " registros").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
	}*/
	
	class ConectarCopy extends AsyncTask<String, String, String> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pdialogo = new ProgressDialog(Menu.this);
			pdialogo.setMessage("Enviando archivo");
			pdialogo.setIndeterminate(false);
			pdialogo.setCancelable(false);
			pdialogo.show();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				conectarCopy();
			} catch (Exception e) {
				
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pdialogo.dismiss();
			Toast toast;
			if(res)
				toast = Toast.makeText(getApplicationContext(), "Se envio \nSe enviaron " + count + " registros", Toast.LENGTH_SHORT);
			else
				toast = Toast.makeText(getApplicationContext(), "No se almaceno", Toast.LENGTH_SHORT);
			toast.setGravity(0, 0, 15);
			toast.show();
		}
		
	}
	
	
	class CargaInfracciones extends AsyncTask<String, String, String> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			/*pdialogo = new ProgressDialog(Menu.this);
			pdialogo.setMessage("Enviando archivo");
			pdialogo.setIndeterminate(false);
			pdialogo.setCancelable(false);
			pdialogo.show();*/
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				String archivo = cla;
				
				ArrayList<NameValuePair> arc = new ArrayList<NameValuePair>();
				arc.add(new BasicNameValuePair("a", archivo));
				arc.add(new BasicNameValuePair("foco", "rojo"));
				arc.add(new BasicNameValuePair("b", con1 + ""));
				arc.add(new BasicNameValuePair("c", String.valueOf(count)));
				
				
				System.out.println("http://" + ip + "/infracciones/sistema/carga_infracciones.php");
				
				//JSONObject json =*/ jsonParser.realizarHttpRequest("http://" + ip + "/sitios/apa_cat/serverSQL/carga_infracciones.php", "GET", arc);
				if(res) {
					System.err.println("envio");
					jsonParser.realizarHttpRequest("http://"+ip+"/infracciones/sistema/carga_infracciones.php", "GET", arc);
				} else {
					System.err.println("no envio");
				}
				
				/*int success = json.getInt("success");
				if(success == 1)
					System.out.println("recibio");
				else 
					System.out.println("no recibio");*/
				
			} catch (Exception e) {
				
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			//pdialogo.dismiss();
		}
		
	}
	
	class CargaInfraccionesLocal extends AsyncTask<String, String, String> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			/*pdialogo = new ProgressDialog(Menu.this);
			pdialogo.setMessage("Enviando archivo");
			pdialogo.setIndeterminate(false);
			pdialogo.setCancelable(false);
			pdialogo.show();*/
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				String archivo = cla;
				
				ArrayList<NameValuePair> arc = new ArrayList<NameValuePair>();
				arc.add(new BasicNameValuePair("a", archivo));
				arc.add(new BasicNameValuePair("foco", "rojo"));
				arc.add(new BasicNameValuePair("b", config));
				arc.add(new BasicNameValuePair("c", String.valueOf(count)));
				
				/*JSONObject json =*/ jsonParser.realizarHttpRequest("http://192.168.254.2:50100/sitios/apa_cat/serverSQL/carga_infracciones.php", "GET", arc);
				
				
				/*int success = json.getInt("success");
				if(success == 1)
					System.out.println("recibio");
				else 
					System.out.println("no recibio");*/
				
			} catch (Exception e) {
				
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			//pdialogo.dismiss();
		}
		
	}
	
	public void mostrarVentana() {
		final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(getResources().getString(R.string.config_tab));
		
		dialog.setItems(tablet, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				bundle = new Bundle();
				bundle.putString("us", usuario);
				//bundle.putInt("id", ids[spUser.getSelectedItemPosition()]);
				bundle.putString("clave", clave);
				bundle.putInt("id", idU);
				
				SharedPreferences.Editor editor = sp.edit();
				
				System.out.println(tablet[which]);
				editor.putString("numt", tablet[which]);
				editor.commit();
				
				startActivity(new Intent(getApplicationContext(), Menu.class).putExtras(bundle));
				onDestroy();
			}
			
		});
		/*mEdittText = new EditText(getApplicationContext());
		mEdittText.setInputType(InputType.TYPE_CLASS_TEXT);
		mEdittText.setHint(getResources().getString(R.string.config_tab));
		mEdittText.setTextColor(Color.BLACK);
		dialog.setView(mEdittText);
		
		dialog.setTitle(getResources().getString(R.string.config_tab)).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(!mEdittText.getText().toString().isEmpty()) {
					bundle = new Bundle();
					bundle.putString("us", usuario);
					//bundle.putInt("id", ids[spUser.getSelectedItemPosition()]);
					bundle.putString("clave", clave);
					bundle.putInt("id", idU);
					SharedPreferences.Editor editor = sp.edit();
					System.out.println(mEdittText.getText().toString());
					editor.putString("numt", mEdittText.getText().toString());
					editor.commit();
					
					startActivity(new Intent(getApplicationContext(), Menu.class).putExtras(bundle));
					onDestroy();
				}
			}
		});*/
		dialog.create().show();
		
	}
	
	public void mostrarVentana(final int opc) {
		final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(getResources().getString(R.string.ip_a));
		mEdittText = new EditText(getApplicationContext());
		mEdittText.setInputType(InputType.TYPE_CLASS_TEXT);
		mEdittText.setHint(getResources().getString(R.string.ip_a));
		mEdittText.setTextColor(Color.BLACK);
		dialog.setView(mEdittText);
		
		dialog.setTitle(getResources().getString(R.string.ip_a)).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(!mEdittText.getText().toString().isEmpty()) {
					bundle = new Bundle();
					bundle.putString("us", usuario);
					//bundle.putInt("id", ids[spUser.getSelectedItemPosition()]);
					bundle.putString("clave", clave);
					bundle.putInt("id", idU);
					SharedPreferences.Editor editor = sp.edit();
					System.out.println(mEdittText.getText().toString());
					if(opc == 1)
						editor.putString("ip", mEdittText.getText().toString());
					else
						editor.putString("ipftp", mEdittText.getText().toString());
					editor.commit();
					startActivity(new Intent(getApplicationContext(), Menu.class).putExtras(bundle));
					onDestroy();
				}
			}
		});
		dialog.create().show();
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.finish();
	}
	
	public void exportar() {
		File exportDirC = new File(Environment.getExternalStorageDirectory() + "/Prueba/", "");
		
		cal = Calendar.getInstance();
		
		dia = (cal.get(Calendar.DATE) >= 0 & cal.get(Calendar.DATE) < 10) ? "0" + cal.get(Calendar.DATE) : cal.get(Calendar.DATE) + "";
		mes = ((cal.get(Calendar.MONTH) + 1) >= 0 & (cal.get(Calendar.MONTH) + 1) < 10) ? "0" + (cal.get(Calendar.MONTH) + 1) : (cal.get(Calendar.MONTH) + 1) + "";
		axo = cal.get(Calendar.YEAR) + "";
		hr = (cal.get(Calendar.HOUR_OF_DAY) >= 0 & cal.get(Calendar.HOUR_OF_DAY) < 10) ? "0" + cal.get(Calendar.HOUR_OF_DAY) : cal.get(Calendar.HOUR_OF_DAY) + "";
		min = (cal.get(Calendar.MINUTE) >= 0 & cal.get(Calendar.MINUTE) < 10) ? "0" + cal.get(Calendar.MINUTE) : cal.get(Calendar.MINUTE) + "";
		seg = (cal.get(Calendar.SECOND) >= 0 & cal.get(Calendar.SECOND) < 10) ? "0" + cal.get(Calendar.SECOND) : cal.get(Calendar.SECOND) + "";
		
		cla = clave + dia + mes + axo.substring(2,4) + hr + min + seg;
		
		if (!exportDirC.exists())
			exportDirC.mkdirs();
		File fileC = new File(exportDirC,cla + ".csv");
		try {
			fileC.createNewFile();
			
			CSVWriter writer = new CSVWriter(new FileWriter(fileC), '|');
			String sql = "select * from infracciones WHERE estatus = 'N' or estatus = '1'";
			System.out.println(sql);
			Cursor cursor = ds.database.rawQuery(sql, null);
			
			/*String arrExp1[] = {cursor.getColumnName(1), cursor.getColumnName(2), cursor.getColumnName(3), cursor.getColumnName(4), cursor.getColumnName(5), cursor.getColumnName(6), cursor.getColumnName(7), cursor.getColumnName(8), cursor.getColumnName(9), cursor.getColumnName(11), cursor.getColumnName(12), cursor.getColumnName(13), cursor.getColumnName(14), cursor.getColumnName(15), cursor.getColumnName(16), cursor.getColumnName(17), cursor.getColumnName(18), cursor.getColumnName(19), cursor.getColumnName(20), cursor.getColumnName(21), cursor.getColumnName(22), cursor.getColumnName(23), cursor.getColumnName(24), cursor.getColumnName(25), cursor.getColumnName(26), cursor.getColumnName(10),cursor.getColumnName(27),cursor.getColumnName(28)};
			writer.writeNext(arrExp1);*/
			
			if (cursor.moveToFirst()) {
				do {
					count = cursor.getCount();
					String arrExp[] = {cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(11), cursor.getString(32), cursor.getString(13), cursor.getString(14), cursor.getString(15), cursor.getString(16), cursor.getString(17), cursor.getString(18), cursor.getString(19), cursor.getString(20), cursor.getString(21), cursor.getString(22), cursor.getString(23), cursor.getString(24), cursor.getString(25), cursor.getString(26), cursor.getString(9),cursor.getString(10), String.valueOf(cursor.getDouble(27)), String.valueOf(cursor.getDouble(28)), String.valueOf(cursor.getString(29)),cursor.getString(30),cursor.getString(31),cursor.getString(33),cursor.getString(12), "S",cursor.getString(35),cursor.getString(36),cursor.getString(37),cursor.getString(38),cursor.getString(39), String.valueOf(con1), cla + ".csv",cursor.getString(cursor.getColumnIndex("entre_calle")),cursor.getString(cursor.getColumnIndex("zona")),cursor.getString(cursor.getColumnIndex("estat"))};
					writer.writeNext(arrExp);
					ContentValues cv = new ContentValues();
					cv.put("estatus", "1");
					System.out.println(ds.database.update("infracciones", cv, "id_infracciones='" + cursor.getInt(0) + "' ", null) + " actualizo");
				} while (cursor.moveToNext());
			}
			/*String arrFinal[] = {"\u001a"}; 
			writer.writeNext(arrFinal);*/
			writer.close();
			cursor.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/*private void mostrarMsj() {
		AlertDialog.Builder al = new AlertDialog.Builder(this)
		.setMessage("No esta configurada la tableta")
		.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//onDestroy();
				btnMulta.setEnabled(false);
			}
		});
		AlertDialog alert = al.create();
		alert.show();
	}*/
	
	private void mostrarMsjA() {
		AlertDialog.Builder al = new AlertDialog.Builder(this)
		.setTitle("Mensaje")
		.setMessage("Se actualizo la base de datos.")
		.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				bundle = new Bundle();
				bundle.putString("us", usuario);
				bundle.putString("clave", clave);
				bundle.putInt("id", idU);
				
				startActivity(new Intent(getApplicationContext(), Menu.class).putExtras(bundle));
				onDestroy();
			}
		});
		AlertDialog alert = al.create();
		alert.show();
	}
	
	private void mostrarMsjAg() {
		AlertDialog.Builder al = new AlertDialog.Builder(this)
		.setTitle("Mensaje")
		.setMessage("Se actualizo los agentes.")
		.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//onDestroy();
				//btnMulta.setEnabled(false);
			}
		});
		AlertDialog alert = al.create();
		alert.show();
	}
	
	private void mostrarMsjRegistro() {
		AlertDialog.Builder al = new AlertDialog.Builder(this)
		.setTitle("Mensaje")
		.setMessage("No hay registros nuevos.")
		.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//onDestroy();
				//btnMulta.setEnabled(false);
			}
		});
		AlertDialog alert = al.create();
		alert.show();
	}
	
	private void mostrarMsjFolio() {
		AlertDialog.Builder al = new AlertDialog.Builder(this)
		.setTitle("Mensaje")
		.setMessage("Favor de asignar un nuevo folio de infracciones.")
		.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//onDestroy();
				btnMulta.setEnabled(false);
				btnSincronizar.setEnabled(true);
			}
		});
		AlertDialog alert = al.create();
		alert.show();
	}
	
	private void mostrarMsjSincronizar() {
		AlertDialog.Builder al = new AlertDialog.Builder(this)
		.setTitle("Mensaje")
		.setMessage("FAVOR DE SINCRONIZAR NUMERO DE INFRACCI�N.")
		.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//onDestroy();
				btnSincronizar.setEnabled(true);
				btnMulta.setEnabled(false);
			}
		});
		AlertDialog alert = al.create();
		alert.show();
	}
	
	@Override
	public void onBackPressed() {
		moveTaskToBack(true); // I don't think you're looking for this.
		startActivity(new Intent(getApplicationContext(), LoginActivity.class));
		onDestroy();
		super.onBackPressed();
	}
	
	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		startActivity(new Intent(getApplicationContext(), LoginActivity.class));
		onDestroy();
		return super.onKeyDown(keyCode, event);
	}*/
	
	
	public boolean validar() {
		String sql = "select * from infracciones WHERE estatus = 'N' or estatus = '1'";
		System.out.println(sql);
		Cursor cursor = ds.database.rawQuery(sql, null);
		boolean r = false;
		int v = 0;
		if (cursor.moveToFirst()) {
			if(cursor.getCount() > 0) 
				v++;
		}
		sql = "select * from Fotografia WHERE estatus = 'N'";
		System.out.println(sql);
		cursor = ds.database.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			if(cursor.getCount() > 0) 
				v++;
		}
		r = v > 0 ? true : false;
		return r;
	}
	
	
	public class EnviarFoto extends AsyncTask<Integer, Integer, Integer>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pdialogo = new ProgressDialog(Menu.this);
			pdialogo.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pdialogo.setMessage("Enviando Fotograf�as");
			pdialogo.setIndeterminate(false);
			pdialogo.setCancelable(false);
			pdialogo.setMax(100);
			pdialogo.setProgress(0);
			pdialogo.show();
		}
		
		
		@Override
		protected Integer doInBackground(Integer... params) {
			int res = 1;
			
			fotografias();
			
			return res;
		}
		
		public void fotografias() {
			nos = 0;
			countF = 0;
			int totalf = 0;
			String s,dir,ar;
			dir = Environment.getExternalStorageDirectory() + "/Infracciones/fotografias/";
			File f;
			fotografia = ds.getAllFotografia(" estatus = 'N'");
			totalf = fotografia.size();
			try {
				for (int i = 0; i < fotografia.size(); i++) {
					s = fotografia.get(i).getNumero_acta();
					ar = fotografia.get(i).getArchivo();
					Log.i("Dato", s + " " + ar);
					f = new File(dir + s + "/" +  ar);
					System.out.println(f.exists() + f.getAbsolutePath());
					
					if (f.exists()) {
						Log.i("Mes", "if exist");
						File file = new File(f.getAbsolutePath());
						//if(f.length()/1024000 <= 2) { 
						MultipartEntity mpEntity = new MultipartEntity();
							
						ContentBody foto = new FileBody(file, "image/jpeg");
							
						mpEntity.addPart("fotoUp", foto);
						mpEntity.addPart("foto", new StringBody(s));
						mpEntity.addPart("archivo",new StringBody(ar));
							
						//JSONObject json = jsonParser.subirImage("http://apidiv.guadalajara.gob.mx:8085/infracciones/sistema/fotoinfraccion.php", "POST", mpEntity);
						JSONObject json = jsonParser.subirImage("http://" + ip + "/infracciones/sistema/fotoinfraccion1.php", "POST", mpEntity);
						//JSONObject json = jsonParser.subirImage("http://192.168.0.14:8080/sitios/JuecesG/fotoinfraccion.php", "POST", mpEntity);
							
						int success = json.getInt("status");
						System.out.println(success + " success");
							
						ContentValues cv = new ContentValues();
							
						if(success == 1) {
							System.out.println("envio movio");
							cv.put("estatus", "S");
							//sql = "update Fotografia set ";
							ds.database.update("Fotografia", cv, " id_fotografia = " + fotografia.get(i).getIdFotofrafia(), null);
							//db.update("Fotografia", cv, " id_fotografia = " + c.getInt(0) , null);
							countF++;
						}
						else if(success == 0) 
							System.out.println("envio no movio");
						else if(success == 3) {
							System.out.println("existe");
							cv.put("estatus", "S");
							ds.database.update("Fotografia", cv, " id_fotografia = " + fotografia.get(i).getIdFotofrafia(), null);
							//db.update("Fotografia", cv, " id_fotografia = " + c.getInt(0) , null);
						}
						else
							System.out.println("no envio f");
						/*}
						else 
							nos++;*/
						
					} 
					nos = totalf-nos;
					
					publishProgress((int) i * 100/totalf);
				
					Thread.sleep(4000);
					if(countF > 0) {
						try {
							
							ArrayList<NameValuePair> carga = new ArrayList<NameValuePair>();
							
							carga.add(new BasicNameValuePair("tableta", config));
							carga.add(new BasicNameValuePair("registros", String.valueOf(0)));
							carga.add(new BasicNameValuePair("fotos", String.valueOf(countF)));
							
							//JSONObject json = jsonParser.realizarHttpRequest("http://apidiv.guadalajara.gob.mx:8085/serverSQL/insertCarga.php", "POST", carga);
							JSONObject json = jsonParser.realizarHttpRequest("http://"+ip+"/serverSQL/insertCarga.php", "POST", carga);
							
							//JSONObject json = jsonParser.realizarHttpRequest("http://apidiv.guadalajara.gob.mx:8085/serverSQL/insertCarga.php", "POST", carga);
							
							int estatus = json.getInt("status");
							
							if(estatus == 1)
								System.err.println("inserto");
							else
								System.err.println("no inserto");
						
						}catch(JSONException e) {
							System.out.println(e.getMessage() + " mm");
						}
					}
				} 
			} catch(Exception e) {
				System.err.println(e.getMessage());
			}
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			pdialogo.setProgress(values[0]);
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			pdialogo.dismiss();
			if(nos > 0) {
				Toast toast = Toast.makeText(getApplicationContext(), "No se enviaron " + nos + " fotografia(s) por el tama�o de la resoluci ", Toast.LENGTH_LONG);
				toast.setGravity(0, 0, 15);
				toast.show();
			}
		}
	}
	
	/*public void fotografias() {
		countF = 0;
		String s,dir,ar;
		dir = Environment.getExternalStorageDirectory() + "/Infracciones/fotografias/";
		File f;
		fotografia = ds.getAllFotografia(" estatus = 'N'");
		try {
			for (int i = 0; i < fotografia.size(); i++) {
				s = fotografia.get(i).getNumero_acta();
				ar = fotografia.get(i).getArchivo();
				Log.i("Dato", s + " " + ar);
				f = new File(dir + s + "/" +  ar);
				System.out.println(f.exists() + f.getAbsolutePath());
				
				if (f.exists()) {
					Log.i("Mes", "if exist");
					File file = new File(f.getAbsolutePath());
					MultipartEntity mpEntity = new MultipartEntity();
					
					ContentBody foto = new FileBody(file, "image/jpeg");
					
					mpEntity.addPart("fotoUp", foto);
					mpEntity.addPart("foto", new StringBody(s));
					mpEntity.addPart("archivo",new StringBody(ar));
					
					//JSONObject json = jsonParser.subirImage("http://apidiv.guadalajara.gob.mx:8085/infracciones/sistema/fotoinfraccion.php", "POST", mpEntity);
					JSONObject json = jsonParser.subirImage("http://" + ip + "/infracciones/sistema/fotoinfraccion1.php", "POST", mpEntity);
					//JSONObject json = jsonParser.subirImage("http://192.168.0.14:8080/sitios/JuecesG/fotoinfraccion.php", "POST", mpEntity);
					
					int success = json.getInt("status");
					System.out.println(success + " success");
					
					ContentValues cv = new ContentValues();
					
					if(success == 1) {
						System.out.println("envio movio");
						cv.put("estatus", "S");
						//sql = "update Fotografia set ";
						ds.database.update("Fotografia", cv, " id_fotografia = " + fotografia.get(i).getIdFotofrafia(), null);
						//db.update("Fotografia", cv, " id_fotografia = " + c.getInt(0) , null);
						countF++;
					}
					else if(success == 0) 
						System.out.println("envio no movio");
					else if(success == 3) {
						System.out.println("existe");
						cv.put("estatus", "S");
						ds.database.update("Fotografia", cv, " id_fotografia = " + fotografia.get(i).getIdFotofrafia(), null);
						//db.update("Fotografia", cv, " id_fotografia = " + c.getInt(0) , null);
					}
					else
						System.out.println("no envio f");
					
				}
			
				Thread.sleep(4000);
				if(countF > 0) {
					try {
						
						ArrayList<NameValuePair> carga = new ArrayList<NameValuePair>();
						
						carga.add(new BasicNameValuePair("tableta", config));
						carga.add(new BasicNameValuePair("registros", String.valueOf(0)));
						carga.add(new BasicNameValuePair("fotos", String.valueOf(countF)));
						
						//JSONObject json = jsonParser.realizarHttpRequest("http://apidiv.guadalajara.gob.mx:8085/serverSQL/insertCarga.php", "POST", carga);
						JSONObject json = jsonParser.realizarHttpRequest("http://"+ip+"/serverSQL/insertCarga.php", "POST", carga);
						
						//JSONObject json = jsonParser.realizarHttpRequest("http://apidiv.guadalajara.gob.mx:8085/serverSQL/insertCarga.php", "POST", carga);
						
						int estatus = json.getInt("status");
						
						if(estatus == 1)
							System.err.println("inserto");
						else
							System.err.println("no inserto");
					
					}catch(JSONException e) {
						System.out.println(e.getMessage() + " mm");
					}
				}
			} 
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
			
		/*try {
			if (db != null) {
				Cursor c = db.rawQuery("SELECT * FROM Fotografia where estatus='N'", null);
				if(c.moveToFirst()) {
					foto.clear();
					archivo.clear();
					do {
						foto.add(c.getString(2));
						archivo.add(c.getString(3));
						s = c.getString(2).replace("/", "_");
						ar = c.getString(3);
						Log.i("Dato", s + " " + ar);
						f = new File(dir + s + "/" +  ar);
						System.out.println(f.exists());
						if (f.exists()) {
							Log.i("Mes", "if exist");
							File file = new File(f.getAbsolutePath());
							MultipartEntity mpEntity = new MultipartEntity();
							ContentBody foto = new FileBody(file, "image/jpeg");
							
							mpEntity.addPart("fotoUp", foto);
							mpEntity.addPart("foto", new StringBody(s));
							
							JSONObject json = jsonParser.subirImage("http://apidiv.guadalajara.gob.mx:8085/infracciones/sistema/pruebaI.php", "POST", mpEntity);
							
							//JSONObject json = jsonParser.subirImage("http://192.168.0.16:8080/sitios/pruebas/pruebaI.php", "POST", mpEntity);
							
							int success = json.getInt("status");
							System.out.println(success + " success");
							
							ContentValues cv = new ContentValues();
							String sql;
							
							if(success == 1) {
								System.out.println("envio movio");
								cv.put("estatus", "S");
								//sql = "update Fotografia set ";
								db.update("Fotografia", cv, " id_fotografia = " + c.getInt(0) , null);
							}
							else if(success == 0) 
								System.out.println("envio no movio");
							else if(success == 3) {
								System.out.println("existe");
								cv.put("estatus", "S");
								db.update("Fotografia", cv, " id_fotografia = " + c.getInt(0) , null);
							}
							else
								System.out.println("no envio f");
							
						}
						//db.delete("Fotografia", c.getColumnName(0) + " = '" + c.getString(0) + "'", null);
					} while (c.moveToNext());
					c.close();
					Thread.sleep(4000);
					if(countF > 0) {
						try {
							
							ArrayList<NameValuePair> carga = new ArrayList<NameValuePair>();
							
							carga.add(new BasicNameValuePair("tableta", config));
							carga.add(new BasicNameValuePair("registros", String.valueOf(0)));
							carga.add(new BasicNameValuePair("fotos", String.valueOf(countF)));
							
							//JSONObject json = jsonParser.realizarHttpRequest("http://apidiv.guadalajara.gob.mx:8085/serverSQL/insertCarga.php", "POST", carga);
							
							JSONObject json = jsonParser.realizarHttpRequest("http://apidiv.guadalajara.gob.mx:8085/serverSQL/insertCarga.php", "POST", carga);
							
							int estatus = json.getInt("status");
							
							if(estatus == 1)
								System.err.println("inserto");
							else
								System.err.println("no inserto");
						
						}catch(JSONException e) {
							System.out.println(e.getMessage() + " mm");
						}
					}
				}
				
			}
		} catch (Exception e) {
			
		}*
	}*/
	
}
