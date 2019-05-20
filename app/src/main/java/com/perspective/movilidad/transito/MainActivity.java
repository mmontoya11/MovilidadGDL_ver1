package com.perspective.movilidad.transito;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import com.bixolon.printer.BixolonPrinter;
import com.parkimovil.contract.ParkimovilContract;
import com.perspective.movilidad.R;
import com.perspective.movilidad.core.TransitoDS;
import com.perspective.movilidad.gs.Agentes;
import com.perspective.movilidad.gs.Colores;
import com.perspective.movilidad.gs.Estados;
import com.perspective.movilidad.gs.Fotografia;
import com.perspective.movilidad.gs.Infraccion;
import com.perspective.movilidad.gs.Infracciones;
import com.perspective.movilidad.gs.Justificar;
import com.perspective.movilidad.gs.Marcas;
import com.perspective.movilidad.gs.Placas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, OnItemSelectedListener, TextWatcher, Runnable {
	//marca color 
	private EditText etfecha,ethr,etPlacas,/*etVigencia,*/etProcedencia,etMarca,/*etTipo,etModelo,*/etColor,/*etSerie,etNombre,etApPaterno,etApMaterno,etDomicilio,etColonia,etCiudad,etLicencia,etNombrePopie,etDomicilioPopie,etColoniaPropie,etCiudadPopie,*/etLugar,etConcepto/*,etTC,etPla,etLic_,etVEC,etINVENT*/,etArticulo,etObservaciones,/*etLinea,*/etEntre,etIDP;
	//private RadioGroup rgTipo;
	private Calendar cal = Calendar.getInstance();
	private String usuario,fecha = "",hr,tipo_licencia = "",ip,folio = "",clave,dia,mes,axo,fec,cveI = "",cveI1 = "",cveI2 = "",cveI3 = "",cveI4 = "",cveI5 = "",h,m,config = "",concepto = "",concepto1 = "",elemento = "",_path,lang = "eng",name ="", completo = "",infra = "",cla = "",nFoto,mConnectedDeviceName = "";
	private String arti [];
	private int idU, res,cons, id_c_inf, id_placa = 0,countInf = 0,p = 0,foto = 0,me,tomo = 0,imp = 0,folioMinimo = 0,folioMaximo = 0;
	private Button btnGuardar,btnImprimir,btnBuscar,btnScann,btnFoto,btnver1,btnver2,btnver3,btnver4,btnver5,btnImprimir1,btnBuscarP;
	private TransitoDS ds;
	public static BixolonPrinter mBixolonPrinter;
	private AlertDialog mSampleDialog,mSampleDialog1;
	private SharedPreferences sp;
	//private Formatter fmt = new Formatter();
	private Spinner spInf/*,spInf1,spInf2,spInf3,spInf4*/,spZona,spColor,spMarca,spEstado;
	private List<Infracciones> inf = new ArrayList<Infracciones>();
	private List<Placas> placa = new ArrayList<Placas>();
	private List<Agentes> agente = new ArrayList<Agentes>();
	private List<Agentes> agente1 = new ArrayList<Agentes>();
	private List<String> infracciones = new ArrayList<String>();
	private List<String> infracciones1 = new ArrayList<String>();
	private List<Infraccion> infraccion = new ArrayList<Infraccion>();
	private List<String> articulo = new ArrayList<String>();
	private List<String> cve = new ArrayList<String>();
	private List<String> placas = new ArrayList<String>();
	private List<String> column = new ArrayList<String>();
	private List<Integer> idCI = new ArrayList<Integer>();
	private List<Double> importes = new ArrayList<Double>();
	private List<Colores> color = new ArrayList<Colores>();
	private List<Marcas> marca = new ArrayList<Marcas>();
	private List<Estados> estado = new ArrayList<Estados>();
	private List<String> colores= new ArrayList<String>();
	private List<String> marcas = new ArrayList<String>();
	private List<String> estados = new ArrayList<String>();
	private double latitud = 0, longitud = 0, mayor = 0,san = 0;
	private LocationManager mLocationManager;
	private Location currentLocation = null;
	private MyLocationListener mLocationListener;
	private Thread thread = null;
	private TextView tvAgente;
	//private final double result = 123.1234567;
	public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/Placas/";
	protected boolean _taken;
	private boolean guardo = false,isConected = false,isSelected = false;
	private Geocoder geocoder = null;
	//private GeoPoint geoPoint = null;
	private List<Address> address;
	private Fotografia fotos;
	
	static final int MESSAGE_START_WORK = Integer.MAX_VALUE - 2;
	static final int MESSAGE_END_WORK = Integer.MAX_VALUE - 3;
	
	public static final String TAG = "BixolonPrinterSample";
	static final String ACTION_COMPLETE_PROCESS_BITMAP = "com.bixolon.anction.COMPLETE_PROCESS_BITMAP";
	static final String EXTRA_NAME_BITMAP_WIDTH = "BitmapWidth";
	static final String EXTRA_NAME_BITMAP_HEIGHT = "BitmapHeight";
	static final String EXTRA_NAME_BITMAP_PIXELS = "BitmapPixels";
	
	private int id_c_infraccion [] = new int [5]; 
	private String articulos [] = new String [5]; 
	private Double importe [] = new Double [5]; 
	private String cve_infraccion [] = new String[5]; 
	private DecimalFormat format = new DecimalFormat("###,###.##");
	
	
	private int mAlignment = BixolonPrinter.ALIGNMENT_CENTER;
	/*private int width = 300;
	private int level = 50;*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		idU = getIntent().getExtras().getInt("id");
		usuario = getIntent().getExtras().getString("us");
		clave = getIntent().getExtras().getString("clave").trim();
		config = getIntent().getExtras().getString("num");
		
		//System.out.println(String.valueOf(result));
		/*System.out.println(idU);
		System.out.println(usuario);
		System.out.println(config);*/
		
		double lat =   20.68136398;
		double lon = -103.30959041;
		
		geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
		
		/*try {
			address = geocoder.getFromLocation(lat, lon, 1);
			if(address.size() > 0)
				System.out.println(address.get(0).getAddressLine(0));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}*/
		
		for (int i = 0; i < id_c_infraccion.length; i++) {
			id_c_infraccion[i] = 0;
			articulos[i] = "";
			importe[i] = 0d;
			cve_infraccion[i] = "";
		}
		
		etfecha = (EditText)findViewById(R.id.etFecha);
		ethr = (EditText)findViewById(R.id.etHr);
		this.etPlacas = (EditText)findViewById(R.id.etPlacas);
		//this.etVigencia = (EditText)findViewById(R.id.etVigencia);
		this.etProcedencia = (EditText)findViewById(R.id.etProcedencia);
		this.etMarca = (EditText)findViewById(R.id.etMarca);
		//this.etTipo = (EditText)findViewById(R.id.etTipo);
		//this.etModelo = (EditText)findViewById(R.id.etModelo);
		this.etColor = (EditText)findViewById(R.id.etColor);
		//this.etSerie = (EditText)findViewById(R.id.etSerie);
		//this.etNombre = (EditText)findViewById(R.id.etNombre);
		//this.etApPaterno = (EditText)findViewById(R.id.etApPaterno);
		//this.etApMaterno = (EditText)findViewById(R.id.etApMaterno);
		//this.etDomicilio = (EditText)findViewById(R.id.etDomicilio);
		//this.etColonia = (EditText)findViewById(R.id.etColonia);
		//this.etCiudad = (EditText)findViewById(R.id.etCiudad);
		//this.etLicencia = (EditText)findViewById(R.id.etLicencia);
		//this.etNombrePopie = (EditText)findViewById(R.id.etNombrePropie);
		//this.etDomicilioPopie = (EditText)findViewById(R.id.etDomicilioPropie);
		//this.etColoniaPropie = (EditText)findViewById(R.id.etColoniaPropie);
		//this.etCiudadPopie = (EditText)findViewById(R.id.etCiudadPropie);
		this.etLugar = (EditText)findViewById(R.id.etLugar);
		//this.etConcepto = (EditText)findViewById(R.id.etConcepto);
		/*this.etTC = (EditText)findViewById(R.id.etTC);
		this.etPla = (EditText)findViewById(R.id.etPla);
		this.etLic_ = (EditText)findViewById(R.id.etLic_);
		this.etVEC = (EditText)findViewById(R.id.etVEH);
		this.etINVENT = (EditText)findViewById(R.id.etINVENT);*/
		this.etArticulo = (EditText)findViewById(R.id.etArticulo);
		this.etObservaciones = (EditText)findViewById(R.id.etObservaciones);
		//this.rgTipo = (RadioGroup)findViewById(R.id.rgTipo);
		btnGuardar = (Button)findViewById(R.id.btnGuardar);
		btnImprimir = (Button)findViewById(R.id.btnImprimir);
		//btnImprimir1 = (Button)findViewById(R.id.btnImprimir1);
		this.spInf = (Spinner)findViewById(R.id.spInfraccion);
		/*this.spInf1 = (Spinner)findViewById(R.id.spInfraccion1);
		this.spInf2 = (Spinner)findViewById(R.id.spInfraccion2);
		this.spInf3 = (Spinner)findViewById(R.id.spInfraccion3);
		this.spInf4 = (Spinner)findViewById(R.id.spInfraccion4);*/
		//this.etLinea = (EditText)findViewById(R.id.etLinea);
		this.tvAgente = (TextView)findViewById(R.id.tvAgente);
		this.btnBuscar = (Button)findViewById(R.id.btnBuscarP);
		this.btnScann = (Button)findViewById(R.id.btnEscanear);
		this.btnFoto = (Button)findViewById(R.id.btnFoto);
		this.etEntre = (EditText)findViewById(R.id.etEntre);
		
		this.btnver1 = (Button)findViewById(R.id.btnVer1);
		this.btnver2 = (Button)findViewById(R.id.btnVer2);
		this.btnver3 = (Button)findViewById(R.id.btnVer3);
		this.btnver4 = (Button)findViewById(R.id.btnVer4);
		this.btnver5 = (Button)findViewById(R.id.btnVer5);
		
		spMarca = (Spinner)findViewById(R.id.spMarca);
		spColor = (Spinner)findViewById(R.id.spColor);
		spEstado = (Spinner)findViewById(R.id.spEstado);
		
		this.spColor.setOnItemSelectedListener(this);
		this.spEstado.setOnItemSelectedListener(this);
		this.spMarca.setOnItemSelectedListener(this);
		
		this.btnBuscarP = (Button)findViewById(R.id.btnBuscarP1);
		this.etIDP = (EditText)findViewById(R.id.etID);
		
		this.btnBuscarP.setOnClickListener(this);
		
		
		this.spZona = (Spinner)findViewById(R.id.spZona);
		
		spZona.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.zonas)));
		
		tvAgente.setText(tvAgente.getText().toString() + usuario);
		
		
		fecha = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
		
		h = (cal.get(Calendar.HOUR_OF_DAY) >= 0 & cal.get(Calendar.HOUR_OF_DAY) < 10) ? "0" + cal.get(Calendar.HOUR_OF_DAY) : cal.get(Calendar.HOUR_OF_DAY) + ""; 
		m = (cal.get(Calendar.MINUTE) >= 0 & cal.get(Calendar.MINUTE) < 10) ? "0" + cal.get(Calendar.MINUTE) : cal.get(Calendar.MINUTE) + "";
		hr = h + "" + m;
		ethr.setText(hr);
		
		hr = h + ":" + m;
		etfecha.setText(fecha + " " + hr);
		
		
		h = h + ":" + m;
		
		this.btnGuardar.setOnClickListener(this);
		btnImprimir.setOnClickListener(this);
		//btnImprimir1.setOnClickListener(this);
		//this.rgTipo.setOnCheckedChangeListener(this);
		this.spInf.setOnItemSelectedListener(this);
		/*this.spInf1.setOnItemSelectedListener(this);
		this.spInf2.setOnItemSelectedListener(this);
		this.spInf3.setOnItemSelectedListener(this);
		this.spInf4.setOnItemSelectedListener(this);*/
		//this.etPlacas.addTextChangedListener(this);
		this.btnBuscar.setOnClickListener(this);
		this.btnScann.setOnClickListener(this);
		this.btnFoto.setOnClickListener(this);
		this.btnver1.setOnClickListener(this);
		this.btnver2.setOnClickListener(this);
		this.btnver3.setOnClickListener(this);
		this.btnver4.setOnClickListener(this);
		this.btnver5.setOnClickListener(this);
		
		btnImprimir.setEnabled(false);
		btnGuardar.setEnabled(false);
		
		ds = new TransitoDS(getApplicationContext());
		ds.open();
		
		estado = ds.getAllEstado("");
		marca = ds.getAllMarca("");
		color = ds.getAllColor("");
		
		for (int i = 0; i < estado.size(); i++) {
			estados.add(estado.get(i).getEstados().trim());
		}
		marcas.add("Seleccione marca...");
		for (int i = 0; i < marca.size(); i++) {
			marcas.add(marca.get(i).getMarca().trim());
		}
		colores.add("Seleccione color...");
		for (int i = 0; i < color.size(); i++) {
			colores.add(color.get(i).getColor().trim());
		}
		
		spEstado.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_dropdown_item, estados));
		spColor.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_dropdown_item, colores));
		spMarca.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.simple_spinner_dropdown_item, marcas));
		
		selectValue(spEstado, "JALISCO");
		
		sp = getSharedPreferences("transito", Context.MODE_PRIVATE);
		ip = sp.getString("ip", "");
		
		
		if(ds.validarCampo("c_agentes", "folio_minimo") > 0)
			agente1 = ds.getAllFolio(" id_c_agentes = " + idU);
		
		for (int i = 0; i < agente1.size(); i++) {
			folioMinimo = Integer.parseInt(agente1.get(i).getFolioMinimo());
			folioMaximo = Integer.parseInt(agente1.get(i).getFolioMaximo());
		}
		
		cons = sp.getInt("cons", 1);
		System.err.println(cons);
		//fmt.format("%04d", cons);
		/*if(cons < folioMinimo) {
			SharedPreferences.Editor editor = sp.edit();
			cons += 1;
			editor.putInt("cons", folioMinimo);
			editor.commit();
		}*/
		cons = sp.getInt("cons", 1);
		System.err.println(cons);
		
		if(cons == folioMaximo) {
			mostrarMsjFolio();
		}
		
		if(!(cons >= folioMinimo & cons <= folioMaximo)) {
			System.err.println(folioMinimo + " " + cons + " " + folioMaximo);
			mostrarMsjSincronizar();
		}
		
		dia = (cal.get(Calendar.DATE) >= 0 & cal.get(Calendar.DATE) < 10) ? "0" + cal.get(Calendar.DATE) : cal.get(Calendar.DATE) + "";
		mes = ((cal.get(Calendar.MONTH) + 1) >= 0 & (cal.get(Calendar.MONTH) + 1) < 10)  ? "0" + (cal.get(Calendar.MONTH) + 1) : (cal.get(Calendar.MONTH) + 1) + "";
		axo = cal.get(Calendar.YEAR) + "";
		
		me = (cal.get(Calendar.MONTH));
		//System.out.println(dia + mes + axo.substring(2, 4));
		
		fec= dia + mes + axo.substring(2, 4);
		
		folio = cons + "";
		System.err.println(folio + " folio");
		
		infraccion = ds.getAllInfraccion(" folio = '" + folio +"'");
		
		if(infraccion.isEmpty()) {
			System.err.println("folio valido");
		}else {
			Toast toas = Toast.makeText(getApplicationContext(), "El folio ya se capturo, favor de descargar datos", Toast.LENGTH_SHORT);
			toas.setGravity(0, 0, 15);
			toas.show();
			onDestroy();
		}
		//folio = clave + fec + fmt;
		
		//System.out.println(folio);
		
		//System.out.println(ip);
		
		
		infracciones.add("");
		infracciones1.add("");
		//placa = ds.getAllPlacas("");
		idCI.add(0);
		articulo.add("");
		cve.add("");
		importes.add(0d);
		inf=ds.getAllInfracciones("");
		agente = ds.getAllAgentes(" id_c_agentes = '" + idU + "'");
		
		//System.err.println(agente.size());
		
		for (int i = 0; i < placa.size(); i++) {
			System.err.println(placa.get(i).getPlaca());
		}
		
		for (int i = 0; i < agente.size(); i++) {
			elemento = agente.get(i).getNombreAgente().trim() + " " + agente.get(i).getPaternoAgente().trim() + " " + agente.get(i).getMaternoAgente().trim(); 
			//System.err.println(elemento);
		}
		
		column = ds.getAllInfraccionesColumn("");
		
		for (int i = 0; i < column.size(); i++) {
			//System.err.println(column.get(i) + " " + i);
		}
	
		
		
		for (int i = 0; i < inf.size(); i++) {
			infracciones.add(inf.get(i).getCveInfracciones() + " " +  inf.get(i).getDescripcion());
			infracciones1.add(inf.get(i).getDescripcion());
			idCI.add(inf.get(i).getIdCInfracciones());
			//System.out.println(inf.get(i).getArticulo());
			cve.add(inf.get(i).getCveInfracciones());
			importes.add(inf.get(i).getImporteInfraccion());
			if(!inf.get(i).equals("null"))
				articulo.add(inf.get(i).getArticulo());
			else
				articulo.add("");
			//System.out.println(inf.get(i).getArticulo());
		}
		
		spInf.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_dropdown_item, infracciones));
		/*spInf1.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_dropdown_item, infracciones));
		spInf2.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_dropdown_item, infracciones));
		spInf3.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_dropdown_item, infracciones));
		spInf4.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_dropdown_item, infracciones));*/
		
		/*IntentFilter filter = new IntentFilter();
		filter.addAction(MainActivity.ACTION_COMPLETE_PROCESS_BITMAP);
		registerReceiver(mReceiver, filter);*/
		
		
		mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			thread = new Thread(MainActivity.this);
			thread.start();
		} else {
			builAlert();
		}
		File f = new File(DATA_PATH);
		if(!f.exists())
			f.mkdirs();
		
		_path = DATA_PATH + "/ocr.jpg";
		
		//mBixolonPrinter = new BixolonPrinter(this, mHandler, null);
		
	}
	
	
	
	private void selectValue(Spinner spinner, String val) {
	    for (int i = 0; i < spinner.getCount(); i++) {
	        if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(val.trim())) {
	            spinner.setSelection(i);
	            break;
	        }
	    }
	}
	
	private void mostrarMsjSincronizar() {
		AlertDialog.Builder al = new AlertDialog.Builder(this)
		.setTitle("Mensaje")
		.setMessage("FAVOR DE SINCRONIZAR NUMERO DE INFRACCIÓN.")
		.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				onDestroy();
			}
		});
		AlertDialog alert = al.create();
		alert.show();
	}
	
	
	
	public void builAlert() {
		final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
		alert.setMessage("El GPS  esta deshabilitado. Entrar a configuración")
		.setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),0);
				onDestroy();
			}
		}).setNegativeButton("No", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		AlertDialog al = alert.create();
		al.show();
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		/*unregisterReceiver(mReceiver);
		try {
			unregisterReceiver(mUsbReceiver);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}*/
		/*if(mHandler != null) {
			mHandler.sendEmptyMessage(0);
			mBixolonPrinter.disconnect();
		}*/
		if(thread != null)
			handler.sendEmptyMessage(0);
		
		this.finish();
		
		//mBixolonPrinter.disconnect();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
		
		name = "";
		name = Environment.getExternalStorageDirectory()+"/Infracciones/fotografias/"+folio.replace("/", "_")+"/";
		File folder = new File(name);
		folder.mkdirs();
		String nom;
		
		switch (v.getId()) {
		case R.id.btnGuardar:
			mostrarMsj();
			break;
			
		case R.id.btnImprimir:
			mBixolonPrinter = new BixolonPrinter(this, mHandler, null);
			//if(!isConected)
				mBixolonPrinter.findBluetoothPrinters();
			
			
			//printSample21();
			
			printSampleGDL();
			
			
			/*if(imp == 0) {
				System.out.println(imp);
				printSampleGDL();
			}
			else if(imp == 1) {
				System.out.println(imp);
				printSampleGDL1();
			}*/
			
			
			/*if(imp == 0) {
				System.out.println(imp);
				printSample2();
			}
			else if(imp == 1) {
				System.out.println(imp);
				printSample21();
			}*/
			break;
			
		/*case R.id.btnImprimir1:
			mBixolonPrinter = new BixolonPrinter(this, mHandler, null);
			mBixolonPrinter.findBluetoothPrinters();
			printSampleGDL1();
			break;*/
		case R.id.btnBuscarP:
			if(!etPlacas.getText().toString().equalsIgnoreCase(""))
				buscarPlaca(etPlacas.getText().toString().trim());
			else {
				Toast toast = Toast.makeText(getApplicationContext(), "Ingrese la placa", Toast.LENGTH_LONG);
				toast.setGravity(0, 0, 15);
				toast.show();
			}
			break;
			
		case R.id.btnEscanear:
			System.err.println("qwertyuiop");
			startCameraActivity();
			break;
			
		case R.id.btnFoto:
			System.err.println("qwertyuiop");
			tomarFoto();
			break;
			
			
		case R.id.btnVer1:
			nom = folio+"-1.jpg";
			name+=nom.replace("/", "_");
			mostrarImagen(name);
			//new DialogoPersonalizado();
			break;
		case R.id.btnVer2:
			nom = folio+"-2.jpg";
			name+=nom.replace("/", "_");
			mostrarImagen(name);
			break;
		case R.id.btnVer3:
			nom = folio+"-3.jpg";
			name+=nom.replace("/", "_");
			mostrarImagen(name);
			break;
		case R.id.btnVer4:
			nom = folio+"-4.jpg";
			name+=nom.replace("/", "_");
			mostrarImagen(name);
			break;
			
		case R.id.btnVer5:
			nom = folio+"-5.jpg";
			name+=nom.replace("/", "_");
			mostrarImagen(name);
			break;
			
		case R.id.btnBuscarP1:
			if(!etIDP.getText().toString().isEmpty()) {
				String[] mSelectionArgs = {""};
				String mSelectionClause = ParkimovilContract.Infractions._ID + " = ?";
				//mSelectionArgs[0] = "ABC1234";
				//mSelectionArgs[0] = "ULD5681";
				mSelectionArgs[0] = etIDP.getText().toString();
				
				Cursor cursor = getContentResolver().query(ParkimovilContract.Infractions.CONTENT_URI,
		                ParkimovilContract.Infractions.ALL_COLUMNS,
		                mSelectionClause,
		                mSelectionArgs,
		                null);
	
		        if (cursor != null) {
		            Log.i("Count: ", String.valueOf(cursor.getCount()));
		        }
		        
		        if(cursor.getCount() == 0) {
		        	Toast toast = Toast.makeText(getApplicationContext(), "No se encontro el registro" , Toast.LENGTH_LONG);
					toast.setGravity(0, 0, 15);
					toast.show();
		        	/*etPlacas.setText("JND4040");
		        	etProcedencia.setText("Jalisco");
		        	etMarca.setText("Ford");
		        	etColor.setText("Blanco");
		        	
		        	selectValue(spEstado, "Jalisco");
		        	selectValue(spMarca, "Ford");
		        	selectValue(spColor, "Blanco");*/
		        }
	
		        while (cursor != null && cursor.moveToNext()) {
		        	
		        	/*ParkimovilContract.Infractions._ID;
		        	ParkimovilContract.Infractions.INFRACTION_NUMBER;
		        	ParkimovilContract.Infractions.INFRACTION_DATE;
		        	ParkimovilContract.Infractions.STATE;
		        	ParkimovilContract.Infractions.BRAND;
		        	ParkimovilContract.Infractions.MODEL;
		       		ParkimovilContract.Infractions.COLOR;
		       		ParkimovilContract.Infractions.LICENSE_PLATE;
	      			*/
		        	
		        	etPlacas.setText(cursor.getString(cursor.getColumnIndex(ParkimovilContract.Infractions.LICENSE_PLATE)));
		        	etProcedencia.setText(cursor.getString(cursor.getColumnIndex(ParkimovilContract.Infractions.STATE)));
		        	etMarca.setText(cursor.getString(cursor.getColumnIndex(ParkimovilContract.Infractions.BRAND)));
		        	etColor.setText(cursor.getString(cursor.getColumnIndex(ParkimovilContract.Infractions.COLOR)));
		        	
		        	selectValue(spEstado, cursor.getString(cursor.getColumnIndex(ParkimovilContract.Infractions.STATE)));
		        	selectValue(spMarca, cursor.getString(cursor.getColumnIndex(ParkimovilContract.Infractions.BRAND)));
		        	selectValue(spColor, cursor.getString(cursor.getColumnIndex(ParkimovilContract.Infractions.COLOR)));
		        	
		            System.err.println(cursor.getString(cursor.getColumnIndex(ParkimovilContract.Infractions.LICENSE_PLATE)) + "\n");
		        }
	
		        if (cursor != null) {
		            cursor.close();
		        }
			}else {
				Toast toast = Toast.makeText(getApplicationContext(), "Ingrese el " + getResources().getString(R.string.id_p) , Toast.LENGTH_LONG);
				toast.setGravity(0, 0, 15);
				toast.show();
			}
			break;
			

		default:
			break;
		}
		
	}
	
	
	public void mostrarImagen(String foto){
		
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
		dialog.setTitle("Imagen");
		ImageView iv = new ImageView(getApplicationContext());
		
		Bitmap bm = BitmapFactory.decodeFile(foto);
		iv.setImageBitmap(bm);
		
		dialog.setView(iv);
		dialog.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		AlertDialog alert = dialog.create();
		alert.show();

		
	}
	
	
	public void tomarFoto() {
		try {
			foto ++;
			Log.i("fot", foto+"");
			/*
			 * String archivo = "";
				archivo = Environment.getExternalStorageDirectory()+"/"+etNumeroActa.getText().toString().replace("/", "_")+"/";
				File folder = new File(archivo);
				folder.mkdirs();
			 */
			name = "";//03-13 13:47:48.600: fotografias/2019/03/2631347/2631347-1.jpg
			name = "sdcard/Infracciones/fotografias/"+folio+"/";
			File folder = new File(name);
			folder.mkdirs();
			String nom = folio+"-"+foto+".jpg";
			name+=nom.replace("/", "_");
			
			System.err.println(name);
			Log.i("Foto", name);
			if (foto == 5) {
				btnFoto.setVisibility(View.GONE);
			}
			try{
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				Uri out = Uri.parse(name);

				intent.putExtra(MediaStore.EXTRA_OUTPUT,out);
				startActivityForResult(intent, 1);
			}catch (Exception e) {
				Toast toast = Toast.makeText(MainActivity.this, "Hubo un error con la camara", Toast.LENGTH_SHORT);
				Log.e("Exception", e.getMessage());
				toast.setGravity(0, 0, 15);
				toast.show();
			}
		}catch(Exception e) {
			Log.e("ExceptionAbajo", e.getMessage());

			Toast toast = Toast.makeText(MainActivity.this, "Hubo un error con la camara", Toast.LENGTH_SHORT);
			toast.setGravity(0, 0, 15);
			toast.show();
		}
	
	}
	
	protected void startCameraActivity() {
		File file = new File(_path);
		Uri outputFileUri = Uri.fromFile(file);

		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

		startActivityForResult(intent, 0);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(!guardo)
				almacenar();
			//salir();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void almacenar() {
		Toast toast;
		System.err.println(folio);
		//res = (int) ds.crearInfraciones("", "", etNombre.getText().toString().toUpperCase(), etfecha.getText().toString(), cveI.toUpperCase(), ethr.getText().toString(), clave.toUpperCase(), etSerie.getText().toString().toUpperCase(), "", id_c_inf, etMarca.getText().toString().toUpperCase(), etTipo.getText().toString().toUpperCase(), etColor.getText().toString().toUpperCase(), etProcedencia.getText().toString().toUpperCase(), etDomicilio.getText().toString().toUpperCase(), etColonia.getText().toString().toUpperCase(), etCiudadPopie.getText().toString().toUpperCase(), etLicencia.getText().toString().toUpperCase(), tipo_licencia.toUpperCase(), etLugar.getText().toString().toUpperCase(), etNombrePopie.getText().toString().toUpperCase(), etDomicilioPopie.getText().toString().toUpperCase(), etColoniaPropie.getText().toString().toUpperCase(), etCiudadPopie.getText().toString().toUpperCase(), etArticulo.getText().toString().toUpperCase(), etObservaciones.getText().toString().toUpperCase(),latitud,longitud,etPlacas.getText().toString().toUpperCase(),id_placa,folio.toUpperCase(),etLinea.getText().toString().toUpperCase(),etModelo.getText().toString().toUpperCase(), "N", etVigencia.getText().toString().toUpperCase(),cveI1.toUpperCase(),cveI2.toUpperCase(),cveI3.toUpperCase(),cveI4.toUpperCase(),cveI5.toUpperCase());
		res = (int) ds.crearInfraciones("", "", "", etfecha.getText().toString(), cveI.toUpperCase(Locale.getDefault()), ethr.getText().toString(), clave.toUpperCase(), "", "", id_c_inf, etMarca.getText().toString().toUpperCase(), "", etColor.getText().toString().toUpperCase(), etProcedencia.getText().toString().toUpperCase(), "", "", "", "", tipo_licencia.toUpperCase(), etLugar.getText().toString().toUpperCase(), "", "", "", "", etArticulo.getText().toString().toUpperCase(), etObservaciones.getText().toString().toUpperCase(),latitud,longitud,etPlacas.getText().toString().toUpperCase(),id_placa,folio.toUpperCase(),"","", "N", "",cveI1.toUpperCase(),cveI2.toUpperCase(),cveI3.toUpperCase(),cveI4.toUpperCase(),cveI5.toUpperCase(),etEntre.getText().toString(),spZona.getSelectedItem().toString(),"N");
		//res = (int) ds.crearInfraciones("", "", "", etfecha.getText().toString(), cveI.toUpperCase(Locale.getDefault()), ethr.getText().toString(), clave.toUpperCase(), "", "", id_c_inf, etMarca.getText().toString().toUpperCase(), "", etColor.getText().toString().toUpperCase(), etProcedencia.getText().toString().toUpperCase(), "", "", "", "", tipo_licencia.toUpperCase(), etLugar.getText().toString().toUpperCase(), "", "", "", "", etArticulo.getText().toString().toUpperCase(), etObservaciones.getText().toString().toUpperCase(),latitud,longitud,etPlacas.getText().toString().toUpperCase(),id_placa,folio.toUpperCase(),"","", "N", "",cveI1.toUpperCase(),cveI2.toUpperCase(),cveI3.toUpperCase(),cveI4.toUpperCase(),cveI5.toUpperCase(),etEntre.getText().toString(),spZona.getSelectedItem().toString(),"S");
		if(res > 0) {
			//System.out.println("inserto");
			toast = Toast.makeText(getApplicationContext(), "Guardo correctamente", Toast.LENGTH_SHORT);
			btnGuardar.setEnabled(false);
			btnImprimir.setEnabled(true);
			SharedPreferences.Editor editor = sp.edit();
			cons += 1;
			editor.putInt("cons", cons);
			editor.commit();
		} else {
			//System.out.println("no inserto");
			toast = Toast.makeText(getApplicationContext(), "Hubo un error", Toast.LENGTH_SHORT);
			btnGuardar.setEnabled(true);
		}
		toast.setGravity(0, 0, 15);
		toast.show();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.i(TAG, "resultCode: " + resultCode);
		
		if(requestCode == 0) {
			if (resultCode == -1) {
				//onPhotoTaken();
			} else {
				Log.v(TAG, "User cancelled");
			}
		}
		
		if(requestCode == 1){
	    	if(resultCode == RESULT_OK) {
	    		File fl = null;	
	    		Toast toast = Toast.makeText(getApplicationContext(), "La fotografia debe ser de menor calidad, el tama�o maximo permitido es de 3MB", Toast.LENGTH_LONG);
	    		if (foto == 1) {
	    			fl = new File(Environment.getExternalStorageDirectory()+"/Infracciones/fotografias/"+folio+"/" + folio+"-1.jpg");
	    			System.err.println(fl.length()/1024);
	    			System.err.println(fl.length());
	    			if(fl.length()/1024 <= 3000) {
		    			//etDFoto.setVisibility(View.VISIBLE);nom = folio+"-5.jpg";
		    			fotos = new Fotografia(folio, folio+"-1.jpg", 'N');
		    			System.out.println("FOTO " + ds.crearFotografia(fotos));
		    			btnver1.setVisibility(View.VISIBLE);
	    			} else {
	    				foto--;
	    				toast.setGravity(0, 0, 15);
	    				toast.show();
	    			}
	    		}
	    		else if (foto == 2) {
	    			fl = new File(Environment.getExternalStorageDirectory()+"/Infracciones/fotografias/"+folio+"/" + folio+"-2.jpg");
	    			if(fl.length()/1024 <= 3000) {
		   				//etDFoto1.setVisibility(View.VISIBLE);
		    			fotos = new Fotografia(folio, folio+"-2.jpg", 'N');
		    			System.out.println("FOTO " + ds.crearFotografia(fotos));
		   				btnver2.setVisibility(View.VISIBLE);
	    			} else {
	    				foto--;
	    				toast.setGravity(0, 0, 15);
	    				toast.show();
	    			}
	   			}
	    		else if (foto == 3) {
	    			fl = new File(Environment.getExternalStorageDirectory()+"/Infracciones/fotografias/"+folio+"/" + folio+"-3.jpg");
	    			if(fl.length()/1024 <= 3000) {
		   				//etDFoto2.setVisibility(View.VISIBLE);
		    			fotos = new Fotografia(folio, folio+"-3.jpg", 'N');
		    			System.out.println("FOTO " + ds.crearFotografia(fotos));
		  				btnver3.setVisibility(View.VISIBLE);
	    			} else {
	    				foto--;
	    				toast.setGravity(0, 0, 15);
	    				toast.show();
	    			}
	   			}
	   			else if(foto == 4){
	   				fl = new File(Environment.getExternalStorageDirectory()+"/Infracciones/fotografias/"+folio+"/" + folio+"-4.jpg");
	    			if(fl.length()/1024 <= 3000) {
		    			//etDFoto3.setVisibility(View.VISIBLE);
		   				fotos = new Fotografia(folio, folio+"-4.jpg", 'N');
		   				System.out.println("FOTO " + ds.crearFotografia(fotos));
		   				btnver4.setVisibility(View.VISIBLE);
	    			} else {
	    				foto--;
	    				toast.setGravity(0, 0, 15);
	    				toast.show();
	    			}
	   			}
	   			else if(foto == 5){
	   				fl = new File(Environment.getExternalStorageDirectory()+"/Infracciones/fotografias/"+folio+"/" + folio+"-4.jpg");
	    			if(fl.length()/1024 <= 3000) {
		    			//etDFoto4.setVisibility(View.VISIBLE);
		   				fotos = new Fotografia(folio, folio+"-5.jpg", 'N');
		   				System.out.println("FOTO " + ds.crearFotografia(fotos));
		    			btnver5.setVisibility(View.VISIBLE);
	    			} else {
	    				foto--;
	    				toast.setGravity(0, 0, 15);
	    				toast.show();
	    			}
	   			}
	    				
	   			if(foto == 5)
	   				btnFoto.setVisibility(View.GONE);
	   			else
    				btnFoto.setVisibility(View.VISIBLE);
	   			
	   			if(foto > 0) {
    				btnGuardar.setEnabled(true);
    			}
    			else 
    				btnGuardar.setEnabled(false);
	    		
			}else if(resultCode == RESULT_CANCELED) {
    			System.err.println(RESULT_CANCELED);
    			foto--;
    			if(foto == 5)
    				btnFoto.setVisibility(View.GONE);
    			else
    				btnFoto.setVisibility(View.VISIBLE);
    				
    			if(foto > 0) {
    				btnGuardar.setEnabled(true);
    			}
    			else 
    				btnGuardar.setEnabled(false);
    			
			}
		}
	}
	
	
	
	/*protected void onPhotoTaken() {
		_taken = true;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;

		Bitmap bitmap = BitmapFactory.decodeFile(_path, options);

		try {
			ExifInterface exif = new ExifInterface(_path);
			int exifOrientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			Log.v(TAG, "Orient: " + exifOrientation);

			int rotate = 0;

			switch (exifOrientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			}

			Log.v(TAG, "Rotation: " + rotate);
			rotate = 1;
			if (rotate != 0) {

				// Getting width & height of the given image.
				int w = bitmap.getWidth();
				int h = bitmap.getHeight();
				
				System.err.println("w " + w + " h " + h);

				// Setting pre rotate
				Matrix mtx = new Matrix();
				mtx.preRotate(rotate);

				// Rotating Bitmap
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, 60, mtx, false);
			}

			// Convert to ARGB_8888, required by tess
			bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

		} catch (IOException e) {
			Log.e(TAG, "Couldn't correct orientation: " + e.toString());
		}

		// _image.setImageBitmap( bitmap );
		
		Log.v(TAG, "Before baseApi");

		TessBaseAPI baseApi = new TessBaseAPI();
		baseApi.setDebug(true);
		baseApi.init(DATA_PATH, lang);
		baseApi.setImage(bitmap);
		
		String recognizedText = baseApi.getUTF8Text();
		
		baseApi.end();

		// You now have the text in recognizedText var, you can do anything with it.
		// We will display a stripped out trimmed alpha-numeric version of it (if lang is eng)
		// so that garbage doesn't make it to the display.

		Log.v(TAG, "OCRED TEXT: " + recognizedText);

		if ( lang.equalsIgnoreCase("eng") ) {
			recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
		}
		
		recognizedText = recognizedText.trim();

		if ( recognizedText.length() != 0 ) {
			etPlacas.setText(etPlacas.getText().toString().length() == 0 ? recognizedText : etPlacas.getText() + " " + recognizedText);
			etPlacas.setSelection(etPlacas.getText().toString().length());
		}
		
		// Cycle done.
	}*/
	

	/*@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		
		switch (checkedId) {
		case R.id.rbA:
			this.tipo_licencia = getResources().getString(R.string.a);
			//System.out.println(rgTipo.getCheckedRadioButtonId());
			break;
		case R.id.rbCH:
			this.tipo_licencia = getResources().getString(R.string.ch);
			//System.out.println(rgTipo.getCheckedRadioButtonId());
			break;
		case R.id.rbEXT:
			this.tipo_licencia = getResources().getString(R.string.ext);
			//System.out.println(rgTipo.getCheckedRadioButtonId());
			break;
		case R.id.rbMT:
			this.tipo_licencia = getResources().getString(R.string.mt);
			//System.out.println(rgTipo.getCheckedRadioButtonId());
			break;
		case R.id.rbO:
			this.tipo_licencia = getResources().getString(R.string.o);
			//System.out.println(rgTipo.getCheckedRadioButtonId());
			break;
		case R.id.rbTPF:
			this.tipo_licencia = getResources().getString(R.string.tpf);
			//System.out.println(rgTipo.getCheckedRadioButtonId());
			break;

		default:
			break;
		}
		
	}*/
	
	private boolean isPlacaValid(String password) {
		return password.length() < 5;
	}
	
	public void attemptLogin() {
		/*if (mAuthTask != null) {
			return;
		}
		
		
		// Reset errors.
		//mEmailView.setError(null);
		//mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		//String email = mEmailView.getText().toString();
		//password = mPasswordView.getText().toString();*/

		etPlacas.setError(null);
		etLugar.setError(null);
		etMarca.setError(null);
		etColor.setError(null);
		
		boolean cancel = false;
		View focusView = null;
		
		if(TextUtils.isEmpty(etPlacas.getText().toString())) {
			etPlacas.setError(getString(R.string.error_field_required));
			focusView = etPlacas;
			isSelected = false;
			cancel = true;
		}
		if(TextUtils.isEmpty(etLugar.getText().toString())) {
			etLugar.setError(getString(R.string.error_field_required));
			focusView = etLugar;
			isSelected = false;
			cancel = true;
		}
		if(TextUtils.isEmpty(etColor.getText().toString())) {
			etColor.setError(getString(R.string.error_field_required));
			focusView = etColor;
			isSelected = false;
			cancel = true;
		}
		if(TextUtils.isEmpty(etMarca.getText().toString())) {
			etMarca.setError(getString(R.string.error_field_required));
			focusView = etMarca;
			isSelected = false;
			cancel = true;
		}
		
		if(isPlacaValid(etPlacas.getText().toString().trim())) {
			etPlacas.setError(getString(R.string.error_invalid_placa));
			focusView = etPlacas;
			isSelected = false;
			cancel = true;
		}

		// Check for a valid password, if the user entered one.
		/*if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}*/

		// Check for a valid email address.
		/*if (TextUtils.isEmpty(email)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!isEmailValid(email)) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}*/
		if(countInf == 0) {
			Toast toast = Toast.makeText(getApplicationContext(), "Debe seleccionar al menos una infracción", Toast.LENGTH_SHORT);
			toast.setGravity(0, 0, 15);
			toast.show();
			cancel = true;
			isSelected = true;
		}
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			//if(TextUtils.isEmpty(etPlacas.getText().toString())){
			if(!isSelected)
				focusView.requestFocus();
			//}
		} else {
			
			mayor = importe[0];
			for (int i = 0; i < importe.length; i++) {
				//System.out.println(importe[i]);
				if(mayor < importe[i]) {
					mayor = importe[i];
					p = i;
				}
			}
			cveI = cve_infraccion[p];
			id_c_inf = id_c_infraccion[p];
			//System.err.println(mayor);
			Toast toast;
			infraccion = ds.getAllInfraccion(" folio = '" + folio +"'");
			for (int i = 0; i < infraccion.size(); i++) {
				System.err.println(infraccion.get(i).getFolio());
			}
			if(infraccion.isEmpty()) {
				//res = (int) ds.crearInfraciones("", "", etNombre.getText().toString().toUpperCase(), etfecha.getText().toString(), cveI.toUpperCase(), ethr.getText().toString(), clave.toUpperCase(), etSerie.getText().toString().toUpperCase(), "", id_c_inf, etMarca.getText().toString().toUpperCase(), etTipo.getText().toString().toUpperCase(), etColor.getText().toString().toUpperCase(), etProcedencia.getText().toString().toUpperCase(), etDomicilio.getText().toString().toUpperCase(), etColonia.getText().toString().toUpperCase(), etCiudadPopie.getText().toString().toUpperCase(), etLicencia.getText().toString().toUpperCase(), tipo_licencia.toUpperCase(), etLugar.getText().toString().toUpperCase(), etNombrePopie.getText().toString().toUpperCase(), etDomicilioPopie.getText().toString().toUpperCase(), etColoniaPropie.getText().toString().toUpperCase(), etCiudadPopie.getText().toString().toUpperCase(), etArticulo.getText().toString().toUpperCase(), etObservaciones.getText().toString().toUpperCase(),latitud,longitud,etPlacas.getText().toString().toUpperCase(),id_placa,folio.toUpperCase(),etLinea.getText().toString().toUpperCase(),etModelo.getText().toString().toUpperCase(), "N", etVigencia.getText().toString().toUpperCase(),cveI1.toUpperCase(),cveI2.toUpperCase(),cveI3.toUpperCase(),cveI4.toUpperCase(),cveI5.toUpperCase());
				res = (int) ds.crearInfraciones("", "", "", etfecha.getText().toString(), cveI.toUpperCase(Locale.getDefault()), ethr.getText().toString(), clave.toUpperCase(), "", "", id_c_inf, etMarca.getText().toString().toUpperCase(), "", etColor.getText().toString().toUpperCase(), etProcedencia.getText().toString().toUpperCase(), "", "", "", "", tipo_licencia.toUpperCase(), etLugar.getText().toString().toUpperCase(), "", "", "", "", etArticulo.getText().toString().toUpperCase(), etObservaciones.getText().toString().toUpperCase(),latitud,longitud,etPlacas.getText().toString().toUpperCase(),id_placa,folio.toUpperCase(),"","", "N", "",cveI1.toUpperCase(),cveI2.toUpperCase(),cveI3.toUpperCase(),cveI4.toUpperCase(),cveI5.toUpperCase(),etEntre.getText().toString(),spZona.getSelectedItem().toString(),"S");
				
				if(res > 0) {
					//System.out.println("inserto");
					toast = Toast.makeText(getApplicationContext(), "Guardo correctamente", Toast.LENGTH_SHORT);
					btnGuardar.setEnabled(false);
					//if(foto > 0)
					btnImprimir.setEnabled(true);
					SharedPreferences.Editor editor = sp.edit();
					cons += 1;
					editor.putInt("cons", cons);
					editor.commit();
					guardo = true;
					etPlacas.setEnabled(false);
					btnFoto.setEnabled(false);
					spEstado.setEnabled(false);
					etProcedencia.setEnabled(false);
					spMarca.setEnabled(false);
					etMarca.setEnabled(false);
					spColor.setEnabled(false);
					etColor.setEnabled(false);
					spZona.setEnabled(false);
					etLugar.setEnabled(false);
					etEntre.setEnabled(false);
					spInf.setEnabled(false);
					etObservaciones.setEnabled(false);
					etIDP.setEnabled(false);
					btnBuscarP.setEnabled(false);
				} else {
					//System.out.println("no inserto");
					toast = Toast.makeText(getApplicationContext(), "Hubo un error", Toast.LENGTH_SHORT);
					btnGuardar.setEnabled(true);
				}
				
			} else {
				toast = Toast.makeText(getApplicationContext(), "El folio ya se capturo, favor de descargar datos", Toast.LENGTH_LONG);
				//toast = Toast.makeText(getApplicationContext(), "No se puede generar la infracción, favor de descargar los datos", Toast.LENGTH_SHORT);
			}
			toast.setGravity(0, 0, 15);
			toast.show();
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			/*showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);*/
		}
	}
	
	public final Handler mHandler = new Handler(new Handler.Callback() {
		
		@SuppressWarnings("unchecked")
		@Override
		public boolean handleMessage(Message msg) {
			Log.d(TAG, "mHandler.handleMessage(" + msg + ")");
			
			switch (msg.what) {
			
			case BixolonPrinter.MESSAGE_DEVICE_NAME:
				mConnectedDeviceName = msg.getData().getString(BixolonPrinter.KEY_STRING_DEVICE_NAME);
				Toast.makeText(getApplicationContext(), mConnectedDeviceName, Toast.LENGTH_LONG).show();
				return true;
				
				case BixolonPrinter.MESSAGE_BLUETOOTH_DEVICE_SET:
					/*Set<BluetoothDevice> bluetoothDevicesSet = (Set<BluetoothDevice>)msg.obj;
					for (BluetoothDevice device : bluetoothDevicesSet) {
						if(device.getName().equals("SPP-R200II")) {
							mBixolonPrinter.connect(device.getAddress());
							break;
						}
					}*/
					if (msg.obj == null) {
						Toast.makeText(getApplicationContext(), "No paired device", Toast.LENGTH_SHORT).show();
					} else {
						DialogManager.showBluetoothDialog(MainActivity.this, (Set<BluetoothDevice>) msg.obj);
					}
					return true;
				
				case BixolonPrinter.MESSAGE_STATE_CHANGE:
					
					if(msg.arg1 == BixolonPrinter.STATE_CONNECTED) {
						isConected = true;
						
						/*BitmapDrawable drawable = (BitmapDrawable)getResources().getDrawable(com.pgm.transito.R.drawable.logo1);
						Bitmap bitmap = drawable.getBitmap();
						mBixolonPrinter.printBitmap(bitmap, BixolonPrinter.ALIGNMENT_CENTER, 150, 50, true,true,true);
						String texto = "hola";
						mBixolonPrinter.printText(texto, BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.TEXT_ATTRIBUTE_FONT_A | BixolonPrinter.TEXT_ATTRIBUTE_UNDERLINE1, BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, true);
						mBixolonPrinter.printText(texto, BixolonPrinter.ALIGNMENT_LEFT, BixolonPrinter.TEXT_ATTRIBUTE_FONT_B | BixolonPrinter.TEXT_ATTRIBUTE_UNDERLINE2, BixolonPrinter.TEXT_SIZE_HORIZONTAL2 | BixolonPrinter.TEXT_SIZE_VERTICAL2, true);
						mBixolonPrinter.cutPaper(true);
						System.out.println("conectado");*/
					}
					break;
				case BixolonPrinter.STATE_NONE:
					isConected = false;
					break;
				case BixolonPrinter.MESSAGE_PRINT_COMPLETE:
					//mBixolonPrinter.disconnect();
					//mHandler.sendEmptyMessage(0);
					break;
			}
			
			
			return true;
			}
	});


	/*private BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
	
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, "mUsbReceiver.onReceive(" + context + ", " + intent + ")");
			String action = intent.getAction();
	
			if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
				mBixolonPrinter.connect();
				Toast.makeText(getApplicationContext(), "Found USB device", Toast.LENGTH_SHORT).show();
			} else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
				mBixolonPrinter.disconnect();
				Toast.makeText(getApplicationContext(), "USB device removed", Toast.LENGTH_SHORT).show();
			}
	
		}
	};*/
	
	private void printBitmap(byte[] pixels, int width, int height) {
		mBixolonPrinter.printBitmap(pixels, mAlignment, width, height, false);
		mBixolonPrinter.cutPaper(0, true);
	}
	
	BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(MainActivity.ACTION_COMPLETE_PROCESS_BITMAP)) {
				byte[] pixels = intent.getByteArrayExtra(MainActivity.EXTRA_NAME_BITMAP_PIXELS);
				int width = intent.getIntExtra(MainActivity.EXTRA_NAME_BITMAP_WIDTH, 0);
				int height = intent.getIntExtra(MainActivity.EXTRA_NAME_BITMAP_HEIGHT, 0);
				
				printBitmap(pixels, width, height);
			}

		}
	};
	
	
	/*@SuppressLint("InflateParams")
	private void printSample2() {
		if (mSampleDialog == null) {
			concepto = "Concepto 1:";
			LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			final View view = inflater.inflate(R.layout.impresion_ticket1, null);
			
			TextView tvfolio = (TextView)view.findViewById(R.id.tvFolio1);
			TextView tvFecha = (TextView)view.findViewById(R.id.tvFecha1);
			TextView tvPlacas = (TextView)view.findViewById(R.id.tvPlaca1);
			//TextView tvVigencia = (TextView)view.findViewById(R.id.tvVigencia1);
			TextView tvProcedencia = (TextView)view.findViewById(R.id.tvProcedencia1);
			TextView tvMarca = (TextView)view.findViewById(R.id.tvMarca1);
			TextView tvTipo = (TextView)view.findViewById(R.id.tvTipo1);
			TextView tvMod = (TextView)view.findViewById(R.id.tvMod1);
			TextView tvColor = (TextView)view.findViewById(R.id.tvColor1);
			TextView tvSerie = (TextView)view.findViewById(R.id.tvSerie1);
			TextView tvLinea = (TextView)view.findViewById(R.id.tvLinea1);
			TextView tvNombre = (TextView)view.findViewById(R.id.tvNombre1);
			TextView tvDomicilio = (TextView)view.findViewById(R.id.tvDomicilio1);
			TextView tvColonia = (TextView)view.findViewById(R.id.tvColonia1);
			//TextView tvCiudad = (TextView)view.findViewById(R.id.tvCiudad1);
			TextView tvLicencia = (TextView)view.findViewById(R.id.tvLicencia1);
			TextView tvTipoL = (TextView)view.findViewById(R.id.tvTip1);
			
			TextView tvLugar = (TextView)view.findViewById(R.id.tvLugar1);
			TextView tvConcepto = (TextView)view.findViewById(R.id.tvConcepto1);
			TextView tvArticulo = (TextView)view.findViewById(R.id.tvArticulos1);
			TextView tvObservacione = (TextView)view.findViewById(R.id.tvObservaciones1);
			
			TextView tvClave = (TextView)view.findViewById(R.id.tvClave1);//tvConcepto2
			
			*TextView tvConcepto2 = (TextView)view.findViewById(R.id.tvConcepto2);
			TextView tvConcepto3 = (TextView)view.findViewById(R.id.tvConcepto3);
			TextView tvConcepto4 = (TextView)view.findViewById(R.id.tvConcepto4);
			TextView tvConcepto5 = (TextView)view.findViewById(R.id.tvConcepto5);
			
			TextView tvConcepto12 = (TextView)view.findViewById(R.id.tvConcepto12);
			TextView tvConcepto13 = (TextView)view.findViewById(R.id.tvConcepto13);
			TextView tvConcepto14 = (TextView)view.findViewById(R.id.tvConcepto14);
			TextView tvConcepto15 = (TextView)view.findViewById(R.id.tvConcepto15);*
			
			concepto += spInf.getSelectedItem().toString();
			
			/*if(spInf1.getSelectedItem().toString().trim() == "") {
				tvConcepto2.setVisibility(View.GONE);
				tvConcepto12.setVisibility(View.GONE);
			} else {
				concepto += ", Concepto 2: " + spInf1.getSelectedItem().toString();
				tvConcepto12.setText(spInf1.getSelectedItem().toString());
			}
			//System.err.println(spInf2.getSelectedItem().toString() + "|");
			if(spInf2.getSelectedItem().toString() == "") {
				tvConcepto3.setVisibility(View.GONE);
				tvConcepto13.setVisibility(View.GONE);
			}
			else {
				concepto += ", Concepto 3:" + spInf2.getSelectedItem().toString();
				tvConcepto13.setText(spInf2.getSelectedItem().toString());
			}
			
			if(spInf3.getSelectedItem().toString() == "") {
				tvConcepto4.setVisibility(View.GONE);
				tvConcepto14.setVisibility(View.GONE);
			}
			else {
				concepto += ", Concepto 4:" + spInf3.getSelectedItem().toString();
				tvConcepto14.setText(spInf3.getSelectedItem().toString());
			}
			
			if(spInf4.getSelectedItem().toString() == "") {
				tvConcepto5.setVisibility(View.GONE);
				tvConcepto15.setVisibility(View.GONE);
			}
			else {
				concepto += ", Concepto 5:" + spInf4.getSelectedItem().toString() +  ", ";
				tvConcepto15.setText(spInf4.getSelectedItem().toString());
			}*
			
			tvfolio.setText(folio);
			tvFecha.setText(fecha + " " + h);
			tvPlacas.setText(etPlacas.getText().toString());
			tvProcedencia.setText(etProcedencia.getText().toString());
			tvMarca.setText(etMarca.getText().toString());
			tvTipo.setText(etTipo.getText().toString());
			tvMod.setText(etModelo.getText().toString());
			tvColor.setText(etColor.getText().toString());
			tvSerie.setText(etSerie.getText().toString());
			tvLinea.setText(etLinea.getText().toString());
			tvNombre.setText(etNombre.getText().toString());
			tvDomicilio.setText(etDomicilio.getText().toString());
			tvColonia.setText(etColonia.getText().toString());
			tvLicencia.setText(etLicencia.getText().toString());
			tvTipoL.setText(tipo_licencia);
			
			tvLugar.setText(etLugar.getText().toString());
			tvConcepto.setText(spInf.getSelectedItem().toString());
			tvObservacione.setText(etObservaciones.getText().toString());
			tvArticulo.setText(etArticulo.getText().toString().trim());
			
			tvClave.setText(clave);
			
			mSampleDialog = new AlertDialog.Builder(MainActivity.this).setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.relativeLayout1);
					layout.buildDrawingCache();
					layout.setDrawingCacheEnabled(true);
					Bitmap bitmap = layout.getDrawingCache();
					
					*int height = layout.getChildAt(0).getHeight();
					int width = layout.getWidth();
					Bitmap bitma = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
					
					Canvas c = new Canvas(bitma);
					layout.layout(0, 0, layout.getLayoutParams().width, layout.getLayoutParams().height);
					layout.draw(c);
					
					Bitmap bitmap = layout.getDrawingCache();*
					
					mBixolonPrinter.setPageMode();
					mBixolonPrinter.setPrintDirection(BixolonPrinter.DIRECTION_0_DEGREE_ROTATION);
					mBixolonPrinter.setPrintArea(0, 0, 384, 970);
					mBixolonPrinter.setAbsoluteVerticalPrintPosition(950);
					mBixolonPrinter.printBitmap(bitmap, BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.BITMAP_WIDTH_FULL, 88, false);
					mBixolonPrinter.formFeed(true);
					*try {
						Thread.sleep(5000);
						//printSample21();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					printSample21();*
				}
			}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			}).create();
		}
		
		mSampleDialog.show();
	}*/
	
	/*@SuppressLint("InflateParams")
	private void printSample2Copy() {
		if (mSampleDialog == null) {
			concepto = "Concepto 1:";
			LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			final View view = inflater.inflate(R.layout.impresion_ticket1copy, null);
			
			TextView tvfolio = (TextView)view.findViewById(R.id.tvFolio1);
			TextView tvFecha = (TextView)view.findViewById(R.id.tvFecha1);
			TextView tvPlacas = (TextView)view.findViewById(R.id.tvPlaca1);
			//TextView tvVigencia = (TextView)view.findViewById(R.id.tvVigencia1);
			TextView tvProcedencia = (TextView)view.findViewById(R.id.tvProcedencia1);
			TextView tvMarca = (TextView)view.findViewById(R.id.tvMarca1);
			TextView tvTipo = (TextView)view.findViewById(R.id.tvTipo1);
			TextView tvMod = (TextView)view.findViewById(R.id.tvMod1);
			TextView tvColor = (TextView)view.findViewById(R.id.tvColor1);
			TextView tvSerie = (TextView)view.findViewById(R.id.tvSerie1);
			TextView tvLinea = (TextView)view.findViewById(R.id.tvLinea1);
			TextView tvNombre = (TextView)view.findViewById(R.id.tvNombre1);
			TextView tvDomicilio = (TextView)view.findViewById(R.id.tvDomicilio1);
			TextView tvColonia = (TextView)view.findViewById(R.id.tvColonia1);
			//TextView tvCiudad = (TextView)view.findViewById(R.id.tvCiudad1);
			TextView tvLicencia = (TextView)view.findViewById(R.id.tvLicencia1);
			TextView tvTipoL = (TextView)view.findViewById(R.id.tvTip1);
			
			TextView tvLugar = (TextView)view.findViewById(R.id.tvLugar1);
			TextView tvConcepto = (TextView)view.findViewById(R.id.tvConcepto1);
			TextView tvArticulo = (TextView)view.findViewById(R.id.tvArticulos1);
			TextView tvObservacione = (TextView)view.findViewById(R.id.tvObservaciones1);
			TextView tvobservaciones = (TextView)view.findViewById(R.id.tvObservaciones);
			
			TextView tvClave = (TextView)view.findViewById(R.id.tvClave1);//tvConcepto2
			
			/*TextView tvConcepto2 = (TextView)view.findViewById(R.id.tvConcepto2);
			TextView tvConcepto3 = (TextView)view.findViewById(R.id.tvConcepto3);
			TextView tvConcepto4 = (TextView)view.findViewById(R.id.tvConcepto4);
			TextView tvConcepto5 = (TextView)view.findViewById(R.id.tvConcepto5);
			
			TextView tvConcepto12 = (TextView)view.findViewById(R.id.tvConcepto12);
			TextView tvConcepto13 = (TextView)view.findViewById(R.id.tvConcepto13);
			TextView tvConcepto14 = (TextView)view.findViewById(R.id.tvConcepto14);
			TextView tvConcepto15 = (TextView)view.findViewById(R.id.tvConcepto15);*
			
			TextView tvElemento = (TextView)view.findViewById(R.id.tvElemento1);
					
			concepto += spInf.getSelectedItem().toString();
			
			*if(spInf1.getSelectedItem().toString().trim() == "") {
				tvConcepto2.setVisibility(View.GONE);
				tvConcepto12.setVisibility(View.GONE);
			} else {
				concepto += ", Concepto 2: " + spInf1.getSelectedItem().toString();
				tvConcepto12.setText(spInf1.getSelectedItem().toString());
			}
			//System.err.println(spInf2.getSelectedItem().toString() + "|");
			if(spInf2.getSelectedItem().toString() == "") {
				tvConcepto3.setVisibility(View.GONE);
				tvConcepto13.setVisibility(View.GONE);
			}
			else {
				concepto += ", Concepto 3:" + spInf2.getSelectedItem().toString();
				tvConcepto13.setText(spInf2.getSelectedItem().toString());
			}
			
			if(spInf3.getSelectedItem().toString() == "") {
				tvConcepto4.setVisibility(View.GONE);
				tvConcepto14.setVisibility(View.GONE);
			}
			else {
				concepto += ", Concepto 4:" + spInf3.getSelectedItem().toString();
				tvConcepto14.setText(spInf3.getSelectedItem().toString());
			}
			
			if(spInf4.getSelectedItem().toString() == "") {
				tvConcepto5.setVisibility(View.GONE);
				tvConcepto15.setVisibility(View.GONE);
			}
			else {
				concepto += ", Concepto 5:" + spInf4.getSelectedItem().toString() +  ", ";
				tvConcepto15.setText(spInf4.getSelectedItem().toString());
			}*
			
			tvfolio.setText(folio.toUpperCase());
			tvFecha.setText(fecha + " " + h);
			tvPlacas.setText(etPlacas.getText().toString().toUpperCase());
			tvProcedencia.setText(etProcedencia.getText().toString().toUpperCase());
			tvMarca.setText(etMarca.getText().toString().toUpperCase());
			tvTipo.setText(etTipo.getText().toString().toUpperCase());
			tvMod.setText(etModelo.getText().toString().toUpperCase());
			tvColor.setText(etColor.getText().toString().toUpperCase());
			tvSerie.setText(etSerie.getText().toString().toUpperCase());
			tvLinea.setText(etLinea.getText().toString().toUpperCase());
			tvNombre.setText(etNombre.getText().toString().toUpperCase());
			tvDomicilio.setText(etDomicilio.getText().toString().toUpperCase());
			tvColonia.setText(etColonia.getText().toString().toUpperCase());
			tvLicencia.setText(etLicencia.getText().toString().toUpperCase());
			tvTipoL.setText(tipo_licencia.toUpperCase());
			
			tvLugar.setText(etLugar.getText().toString().toUpperCase());
			tvConcepto.setText(spInf.getSelectedItem().toString().toUpperCase());
			//tvConcepto.setText(concepto);
			tvObservacione.setText(etObservaciones.getText().toString().toUpperCase());
			tvArticulo.setText(etArticulo.getText().toString().trim().toUpperCase());
			tvElemento.setText(elemento.toUpperCase() + "\n");
			
			if(etObservaciones.getText().toString().equalsIgnoreCase("")) {
				tvobservaciones.setVisibility(View.GONE);
				tvObservacione.setVisibility(View.GONE);
			}
			
			tvClave.setText(clave.toUpperCase());
			
			mSampleDialog = new AlertDialog.Builder(MainActivity.this).setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.relativeLayout1);
					
					Bitmap bitmap = Bitmap.createBitmap(layout.getWidth(), layout.getHeight(), Bitmap.Config.ARGB_8888);
					Canvas canvas = new Canvas(bitmap);
					layout.draw(canvas);
					
					
					*layout.setDrawingCacheEnabled(true);
					layout.buildDrawingCache();
					Bitmap bitmap = layout.getDrawingCache();
					
					
					
					int height = layout.getChildAt(0).getHeight();
					int width = layout.getWidth();
					
					Bitmap bitma = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
					
					Canvas c = new Canvas(bitma);
					layout.layout(0, 0, layout.getLayoutParams().width, layout.getLayoutParams().height);
					layout.draw(c);
					
					Bitmap bitmap = layout.getDrawingCache();*
					
					mBixolonPrinter.setPageMode();
					mBixolonPrinter.setPrintDirection(BixolonPrinter.DIRECTION_0_DEGREE_ROTATION);
					mBixolonPrinter.setPrintArea(0, 0, 390, 970);
					mBixolonPrinter.setAbsoluteVerticalPrintPosition(970);
					mBixolonPrinter.printBitmap(bitmap, BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.BITMAP_WIDTH_FULL, 88, false);
					mBixolonPrinter.formFeed(true);
					*try {
						Thread.sleep(5000);
						//printSample21();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					printSample21();*
				}
			}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			}).create();
		}
		
		mSampleDialog.show();
	}*/
	
	/*@SuppressLint("InflateParams")
	private void printSample21() {
		if (mSampleDialog1 == null) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			final View view = inflater.inflate(R.layout.impresion_ticket12, null);
			
			TextView tvNombrePro = (TextView)view.findViewById(R.id.tvNombrePro1);
			TextView tvDomicilioPro = (TextView)view.findViewById(R.id.tvDomicilioPro1);
			TextView tvColoniaPro = (TextView)view.findViewById(R.id.tvColoniaPro1);
			TextView tvCiudadPro = (TextView)view.findViewById(R.id.tvCiudadPro1);
			
			TextView tvLugar = (TextView)view.findViewById(R.id.tvLugar1);
			TextView tvHora = (TextView)view.findViewById(R.id.tvHora1);
			TextView tvConcepto = (TextView)view.findViewById(R.id.tvConcepto1);
			
		
			TextView tvArticulo = (TextView)view.findViewById(R.id.tvArticulos1);
			TextView tvObservacione = (TextView)view.findViewById(R.id.tvObservaciones1);
			
			TextView tvClave = (TextView)view.findViewById(R.id.tvClave1);
			//TextView tvTipoL = (TextView)view.findViewById(R.id.tvTip1);
			
			
			tvNombrePro.setText(etNombrePopie.getText().toString());
			tvDomicilioPro.setText(etDomicilioPopie.getText().toString());
			tvColoniaPro.setText(etColoniaPropie.getText().toString());
			tvCiudadPro.setText(etCiudadPopie.getText().toString());
			
			tvArticulo.setText(etArticulo.getText().toString().trim());
			
			tvLugar.setText(etLugar.getText().toString());
			tvHora.setText(h);
			tvConcepto.setText(spInf.getSelectedItem().toString());
			tvObservacione.setText(etObservaciones.getText().toString());
			
			tvClave.setText(clave);
			//tvTipoL.setText(tipo_licencia);
			
			
			mSampleDialog1 = new AlertDialog.Builder(MainActivity.this).setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.relativeLayout1);
					layout.buildDrawingCache();
					layout.setDrawingCacheEnabled(true);
					Bitmap bitmap = layout.getDrawingCache();
					
					mBixolonPrinter.setPageMode();
					mBixolonPrinter.setPrintDirection(BixolonPrinter.DIRECTION_0_DEGREE_ROTATION);
					mBixolonPrinter.setPrintArea(0, 0, 384, 750);
					mBixolonPrinter.setAbsoluteVerticalPrintPosition(710);
					mBixolonPrinter.printBitmap(bitmap, BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.BITMAP_WIDTH_FULL, 88, false);
					mBixolonPrinter.formFeed(true);
				}
			}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			}).create();
			
			mSampleDialog1.show();
			RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.relativeLayout1);
			layout.buildDrawingCache();
			layout.setDrawingCacheEnabled(true);
			Bitmap bitmap = layout.getDrawingCache();
			
			mBixolonPrinter.setPageMode();
			mBixolonPrinter.setPrintDirection(BixolonPrinter.DIRECTION_0_DEGREE_ROTATION);
			mBixolonPrinter.setPrintArea(0, 0, 384, 750);
			mBixolonPrinter.setAbsoluteVerticalPrintPosition(710);
			mBixolonPrinter.printBitmap(bitmap, BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.BITMAP_WIDTH_FULL, 88, false);
			mBixolonPrinter.formFeed(true);
			//mSampleDialog1.dismiss();
			
			
		}
		
		//mSampleDialog1.show();
	}*/
	
	@SuppressLint("InflateParams")
	private void printSampleGDL1() {
		if (mSampleDialog1 == null) {
			concepto = "Concepto 1:";
			LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			final View view = inflater.inflate(R.layout.multa_gdl1, null);
			
			
			mSampleDialog1 = new AlertDialog.Builder(MainActivity.this).setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					imp = 0;
					
					RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.relativeLayout11);
					
					System.err.println(layout.getWidth() + " h " + layout.getHeight());
					
					Bitmap bitmap = Bitmap.createBitmap(layout.getWidth(), layout.getHeight(), Bitmap.Config.ARGB_8888);
					Canvas canvas = new Canvas(bitmap);
					layout.draw(canvas);
					
					mBixolonPrinter.setPageMode();
					mBixolonPrinter.setPrintDirection(BixolonPrinter.DIRECTION_0_DEGREE_ROTATION);
					mBixolonPrinter.setPrintArea(0, 0, 565, 1180);
					mBixolonPrinter.setAbsoluteVerticalPrintPosition(1100);
					mBixolonPrinter.printBitmap(bitmap, BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.BITMAP_WIDTH_FULL, 88, false);
					mBixolonPrinter.formFeed(false);
					
					
					mBixolonPrinter.printText("50% DE DESCUENTO", BixolonPrinter.ALIGNMENT_LEFT, BixolonPrinter.TEXT_ATTRIBUTE_FONT_C, BixolonPrinter.TEXT_SIZE_HORIZONTAL1, false);
					
					//mBixolonPrinter.cutPaper(0, true);
					
				}
			}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			}).create();
		}
		
		mSampleDialog1.show();
	}
	
	
	@SuppressLint({ "InflateParams", "DefaultLocale" })
	private void printSampleGDL() {
		if (mSampleDialog == null) {
			concepto = "Concepto 1:";
			LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			final View view = inflater.inflate(R.layout.multa_gdl, null);
			
			TextView tvMulta = (TextView)view.findViewById(R.id.tvMulta);
			TextView tvFolio = (TextView)view.findViewById(R.id.tvFolio1);
			//TextView tvZona = (TextView)view.findViewById(R.id.tvZona1);
			TextView tvMotivo = (TextView)view.findViewById(R.id.tvMotivo1);
			TextView tvClave = (TextView)view.findViewById(R.id.tvClave1);
			TextView tvSan = (TextView)view.findViewById(R.id.tvSan1);
			TextView tvServidor = (TextView)view.findViewById(R.id.tvServidor);
			
			//tvZona.setText(spZona.getSelectedItem().toString());
			tvMotivo.setText(infra);
			tvClave.setText(cla);
			tvSan.setText("$ " + String.valueOf(format.format(san)));
			
			tvServidor.setText(elemento);
			
			System.err.println(elemento);
			
			tvFolio.setText(folio);//32682972 32682976
			
			final String cuerpo = "En la Ciudad de Guadalajara, Jalisco, siendo las " + h + " horas del d�a " + dia + " del mes de " + getMes(me) +
					" del a�o " + axo + ", el servidor p�blico " + elemento + " adscrito al �rea de Gesti�n del Estacionamiento" +
					", dependiente de la Direcci�n de Movilidad y Transporte, con n�mero de empleado " +  clave + ", acreditando mi personalidad" +
					" con la credencial oficial vigente, expedida por el Director de Recursos Humanos de este H. Ayuntamiento Constitucional" +
					" de Guadalajara, y en cumplimiento de mis funciones, encargado de vigilar el cumplimiento de los reglamentos municipales" +
					", en t�rminos de los Art�culos 132, 133 pen�ltimo p�rrafo, 139 fracciones XXXI, XXXII y Octavo" +
					" Transitorio del Reglamento de la Administraci�n P�blica Municipal y de los Art�culos 1, 23, 67 punto 1 y 2, 69, 73" +
					", del Reglamento de Estacionamientos en el Municipio de Guadalajara, en la calle " + etLugar.getText().toString().toUpperCase(Locale.getDefault()) + " a su cruce con la(s)" +
					" Calle(s) " + etEntre.getText().toString().toUpperCase(Locale.getDefault()) + " observ� la siguiente falta al Reglamento citado " + infra.trim().toUpperCase() + " " + cla.trim().toUpperCase() + " acto seguido con el fundamento" +
					" en el art�culo 123 fracci�n II de la Ley del Procedimiento Administrativo del Estado de Jalisco y sus Municipios se" +
					" le INFRACCIONA al propietario del veh�culo marca " + etMarca.getText().toString().toUpperCase(Locale.getDefault()) + ", color " + etColor.getText().toString().toUpperCase() + " con placas de circulaci�n " + etPlacas.getText().toString().toUpperCase() + " haci�ndose acreedor" +
					" a la sanci�n prevista por el " + completo.trim() +
					" Bajo el mismo tenor, se le informa al ciudadano que esta medida es con el fin de invitar a favorecer e incrementar" +
					" la cultura vial de Guadalajara.";
			
			tvMulta.setText("En la Ciudad de Guadalajara, Jalisco, siendo las " + h + " horas del d�a " + dia + " del mes de " + getMes(me) +
					" del a�o " + axo + ", el servidor p�blico " + elemento + " adscrito al �rea de Gesti�n del Estacionamiento" +
					", dependiente de la Direcci�n de Movilidad y Transporte, con n�mero de empleado " +  clave + ", acreditando mi personalidad" +
					" con la credencial oficial vigente, expedida por el Director de Recursos Humanos de este H. Ayuntamiento Constitucional" +
					" de Guadalajara, y en cumplimiento de mis funciones, encargado de vigilar el cumplimiento de los reglamentos municipales" +
					", en t�rminos de los Art�culos 132, 133 pen�ltimo p�rrafo, 139 fracciones XXXI, XXXII y Octavo" +
					" Transitorio del Reglamento de la Administraci�n P�blica Municipal y de los Art�culos 1, 23, 67 punto 1 y 2, 69, 73" +
					", del Reglamento de Estacionamientos en el Municipio de Guadalajara, en la calle " + etLugar.getText().toString().toUpperCase(Locale.getDefault()) + " a su cruce con la(s)" +
					" Calle(s) " + etEntre.getText().toString().toUpperCase(Locale.getDefault()) + " observ� la siguiente falta al reglamento citado " + infra.trim().toUpperCase() + " " + cla.trim().toUpperCase() + " acto seguido con el fundamento" +
					" en el art�culo 123 fracci�n II de la Ley del Procedimiento Administrativo del Estado de Jalisco y sus Municipios se" +
					" le INFRACCIONA al propietario del veh�culo marca " + etMarca.getText().toString().toUpperCase(Locale.getDefault()) + ", color " + etColor.getText().toString().toUpperCase() + " con placas de circulaci�n " + etPlacas.getText().toString().toUpperCase() + " haci�ndose acreedor" +
					" a la sanci�n prevista por el " + completo +
					" \n" +
					"Bajo el mismo tenor, se le informa al ciudadano que esta medida es con el fin de invitar a favorecer e incrementar" +
					" la cultura vial de Guadalajara.");
					
			concepto += spInf.getSelectedItem().toString();
			
			mSampleDialog = new AlertDialog.Builder(MainActivity.this).setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@SuppressLint("NewApi")
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					String [] str;
					
					
					mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_1252_LATIN1);
					
					mBixolonPrinter.setPageMode();
					MainActivity.mBixolonPrinter.setPrintArea(0, 0, 576, 200);

					Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.log_movilidad);

					mBixolonPrinter.setPrintDirection(BixolonPrinter.DIRECTION_0_DEGREE_ROTATION);
					mBixolonPrinter.setAbsoluteVerticalPrintPosition(200);
					mBixolonPrinter.setAbsolutePrintPosition(20);
					mBixolonPrinter.printBitmap(bm, BixolonPrinter.ALIGNMENT_CENTER, 200, 65, false);

					bm = BitmapFactory.decodeResource(getResources(), R.drawable.log_g);
					mBixolonPrinter.setPrintDirection(BixolonPrinter.DIRECTION_0_DEGREE_ROTATION);
					mBixolonPrinter.setAbsoluteVerticalPrintPosition(200);
					mBixolonPrinter.setAbsolutePrintPosition(400);
					mBixolonPrinter.printBitmap(bm, BixolonPrinter.ALIGNMENT_CENTER, 150, 65, false);

					mBixolonPrinter.formFeed(true);

					//normal mode
					
					mBixolonPrinter.lineFeed(2, false);
					mBixolonPrinter.printText(getResources().getString(R.string.folio) + " " + folio + "\n", 
							BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_A | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, 
							false);

					mBixolonPrinter.printText(getResources().getString(R.string.cedula)+ "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					//mBixolonPrinter.lineFeed(2, false);
					
					mBixolonPrinter.printText(getResources().getString(R.string.area)+ "\n\n", BixolonPrinter.ALIGNMENT_CENTER,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					
					mBixolonPrinter.printText(getResources().getString(R.string.dom)+ "\n", BixolonPrinter.ALIGNMENT_CENTER,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);

					str = Justificar.justifocarTexto(cuerpo, "a");
					
					String n = "";
					
					for (int i = 0; i < str.length; i++) {
						System.out.println(str[i]);
						n += str[i] + "\n";
						
					}
					System.err.println("n " + n);
					mBixolonPrinter.printText(n + "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_A, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					/*mBixolonPrinter.printText(cuerpo+ "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_A, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);*/
					
					
					//pagemode
					mBixolonPrinter.setPageMode();
					MainActivity.mBixolonPrinter.setPrintArea(0, 0, 576, 430);

					mBixolonPrinter.setPrintDirection(BixolonPrinter.DIRECTION_0_DEGREE_ROTATION);
					mBixolonPrinter.setAbsoluteVerticalPrintPosition(40);
					mBixolonPrinter.setAbsolutePrintPosition(40);
					mBixolonPrinter.printBox(0, 0, 576, 330, 1, false);
					
					mBixolonPrinter.setAbsoluteVerticalPrintPosition(50);
					mBixolonPrinter.setAbsolutePrintPosition(200);
					mBixolonPrinter.printText(getResources().getString(R.string.motivo), BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printBox(0, 80, 576, 81, 1, false);
					
					mBixolonPrinter.setAbsoluteVerticalPrintPosition(120);
					mBixolonPrinter.setAbsolutePrintPosition(30);
					mBixolonPrinter.printText(infra, BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printBox(0, 160, 576, 161, 1, false);
					
					/*mBixolonPrinter.setAbsoluteVerticalPrintPosition(220);
					mBixolonPrinter.setAbsolutePrintPosition(130);
					mBixolonPrinter.printText(spZona.getSelectedItem().toString(), BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);*/
					
					mBixolonPrinter.setAbsoluteVerticalPrintPosition(220);
					mBixolonPrinter.setAbsolutePrintPosition(130);
					mBixolonPrinter.printText(cla, BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printBox(0, 240, 576, 241, 1, false);
					
					mBixolonPrinter.setAbsoluteVerticalPrintPosition(300);
					mBixolonPrinter.setAbsolutePrintPosition(130);
					mBixolonPrinter.printText(getResources().getString(R.string.sancion), BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					
					mBixolonPrinter.printBox(400, 240, 400, 325, 1, false);
					
					/*mBixolonPrinter.setAbsoluteVerticalPrintPosition(220);
					mBixolonPrinter.setAbsolutePrintPosition(450);
					mBixolonPrinter.printText(cla, BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);*/
					
					mBixolonPrinter.setAbsoluteVerticalPrintPosition(300);
					mBixolonPrinter.setAbsolutePrintPosition(450);
					mBixolonPrinter.printText("$ " + String.valueOf(format.format(san)), BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.formFeed(true);
					
					mBixolonPrinter.lineFeed(1, false);
					mBixolonPrinter.printText(getResources().getString(R.string.nombre_a) +  " " + getResources().getString(R.string.firma) + "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.lineFeed(1, false);
					mBixolonPrinter.printText(elemento + "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.lineFeed(1, false);
					mBixolonPrinter.printText(getResources().getString(R.string.despues)+ "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B , 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printText(getResources().getString(R.string.descuento) + "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					str = Justificar.justifocarTexto(getResources().getString(R.string.sanciones), "b");
					
					n = "";
					
					for (int i = 0; i < str.length; i++) {
						System.out.println(str[i]);
						n += str[i] + "\n";
						
					}
					
					/*mBixolonPrinter.printText(getResources().getString(R.string.sanciones)+ "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B , 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);*/
					
					mBixolonPrinter.printText(n, BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B , 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printText(getResources().getString(R.string.constitucion) + "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					str = Justificar.justifocarTexto(getResources().getString(R.string.constitucion1), "b");
					
					n = "";
					
					for (int i = 0; i < str.length; i++) {
						System.out.println(str[i]);
						n += str[i] + "\n";
						
					}
					
					/*mBixolonPrinter.printText(getResources().getString(R.string.constitucion1)+ "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B , 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);*/
					
					mBixolonPrinter.printText(n, BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B , 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printText(getResources().getString(R.string.consti_jal) + "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					str = Justificar.justifocarTexto(getResources().getString(R.string.consti_jal1), "b");
					
					n = "";
					
					for (int i = 0; i < str.length; i++) {
						System.out.println(str[i]);
						n += str[i] + "\n";
						
					}
					
					/*mBixolonPrinter.printText(getResources().getString(R.string.consti_jal1)+ "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B , 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);*/
					
					mBixolonPrinter.printText(n, BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B , 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printText(getResources().getString(R.string.lpa) + "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					str = Justificar.justifocarTexto(getResources().getString(R.string.lpa1), "b");
					
					n = "";
					
					for (int i = 0; i < str.length; i++) {
						System.out.println(str[i]);
						n += str[i] + "\n";
						
					}
					
					/*mBixolonPrinter.printText(getResources().getString(R.string.lpa1)+ "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B , 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);*/
					
					mBixolonPrinter.printText(n, BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B , 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printText(getResources().getString(R.string.lmt) + "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					str = Justificar.justifocarTexto(getResources().getString(R.string.lmt1), "b");
					
					n = "";
					
					for (int i = 0; i < str.length; i++) {
						System.out.println(str[i]);
						n += str[i] + "\n";
						
					}
					
					/*mBixolonPrinter.printText(getResources().getString(R.string.lmt1)+ "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B , 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);*/
					
					mBixolonPrinter.printText(n, BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B , 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printText(getResources().getString(R.string.lugar3) + "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printText(getResources().getString(R.string.dir) + " " + getResources().getString(R.string.dir_) + "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printText(getResources().getString(R.string.dir1) + " " + getResources().getString(R.string.dir11) + "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printText(getResources().getString(R.string.dir2) + " " + getResources().getString(R.string.dir21) + "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printText(getResources().getString(R.string.dir3) + " " + getResources().getString(R.string.dir31) + "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printText(getResources().getString(R.string.dir4) + " " + getResources().getString(R.string.dir41) + "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printText(getResources().getString(R.string.cla) + "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printText("Tambi�n puede realizar pago online en https://modulos.guadalajara.gob.mx/estacionometros/" + "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					
					mBixolonPrinter.lineFeed(3, false);
					
					
					
					/*mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_1252_LATIN1);
					
					mBixolonPrinter.setPageMode();
					MainActivity.mBixolonPrinter.setPrintArea(0, 0, 576, 200);

					Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.mov05062016);

					mBixolonPrinter.setPrintDirection(BixolonPrinter.DIRECTION_0_DEGREE_ROTATION);
					mBixolonPrinter.setAbsoluteVerticalPrintPosition(200);
					mBixolonPrinter.setAbsolutePrintPosition(20);
					mBixolonPrinter.printBitmap(bm, BixolonPrinter.ALIGNMENT_CENTER, 150, 80, false);

					bm = BitmapFactory.decodeResource(getResources(), R.drawable.esc05062016);
					mBixolonPrinter.setPrintDirection(BixolonPrinter.DIRECTION_0_DEGREE_ROTATION);
					mBixolonPrinter.setAbsoluteVerticalPrintPosition(200);
					mBixolonPrinter.setAbsolutePrintPosition(400);
					mBixolonPrinter.printBitmap(bm, BixolonPrinter.ALIGNMENT_CENTER, 150, 80, false);

					mBixolonPrinter.formFeed(true);
					

					mBixolonPrinter.lineFeed(2, false);
					mBixolonPrinter.printText(getResources().getString(R.string.folio) + " " + folio + "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_A | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);

					mBixolonPrinter.printText(getResources().getString(R.string.cedula) , BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_A | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.lineFeed(2, false);
					
					mBixolonPrinter.printText(getResources().getString(R.string.area) + "\n", BixolonPrinter.ALIGNMENT_CENTER,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_A | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printText(getResources().getString(R.string.dom) + "\n", BixolonPrinter.ALIGNMENT_CENTER,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_A | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printText(cuerpo + "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_A | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.setPageMode();
					MainActivity.mBixolonPrinter.setPrintArea(0, 0, 576, 400);
					mBixolonPrinter.setPrintDirection(BixolonPrinter.DIRECTION_0_DEGREE_ROTATION);
					mBixolonPrinter.setAbsoluteVerticalPrintPosition(30);
					mBixolonPrinter.setAbsolutePrintPosition(30);
					mBixolonPrinter.printBox(0, 0, 576, 320, 1, false);
					
					mBixolonPrinter.setAbsoluteVerticalPrintPosition(50);
					mBixolonPrinter.setAbsolutePrintPosition(260);
					mBixolonPrinter.printText(getResources().getString(R.string.motivo).trim(), BixolonPrinter.ALIGNMENT_CENTER,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_A | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printBox(0, 80, 576, 81, 1, false);
					
					mBixolonPrinter.setAbsoluteVerticalPrintPosition(140);
					mBixolonPrinter.setAbsolutePrintPosition(30);
					mBixolonPrinter.printText(infra.trim(), BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_A | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					mBixolonPrinter.printBox(0, 160, 576, 161, 1, false);
					
					mBixolonPrinter.setAbsoluteVerticalPrintPosition(220);
					mBixolonPrinter.setAbsolutePrintPosition(130);
					mBixolonPrinter.printText(getResources().getString(R.string.clave), BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_A | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printBox(0, 240, 576, 241, 1, false);
					
					mBixolonPrinter.setAbsoluteVerticalPrintPosition(300);
					mBixolonPrinter.setAbsolutePrintPosition(130);
					mBixolonPrinter.printText(getResources().getString(R.string.sancion), BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_A | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					
					mBixolonPrinter.printBox(400, 160, 400, 320, 1, false);
					mBixolonPrinter.setAbsoluteVerticalPrintPosition(220);
					mBixolonPrinter.setAbsolutePrintPosition(450);
					mBixolonPrinter.printText(cla, BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_A | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.setAbsoluteVerticalPrintPosition(300);
					mBixolonPrinter.setAbsolutePrintPosition(450);
					mBixolonPrinter.printText("$ " + String.valueOf(format.format(san)), BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_A | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.formFeed(true);
					
					mBixolonPrinter.lineFeed(2, false);*/
					
					mBixolonPrinter.disconnect();
				}
			}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			}).create();
		}
		
		mSampleDialog.show();
	}
	
	private String textData(){
		String data = "";
		data += "En la Ciudad de Guadalajara, Jalisco, siendo las " + "h" + " horas del dia " + "dia" + " del mes de " + "Octubre" +
				" del año 2016, el servidor público " + "elemento" + " adscrito al Área de Gestión del Estacionamiento" +
				", dependiente de la Dirección de Movilidad y Transporte, con número de empleado " + " clave" + ", acreditando mi personalidad" +
				" con la credencial oficial vigente, expedida por el Director de Recursos Humanos de este H. Ayuntamiento Constitucional" +
				" de Guadalajara, y en cumplimiento de mis funciones, encargado de vigilar el cumplimiento de los reglamentos municipales" +
				", en términos de los Artículos 132, 133 penúltimo párrafo, 139 fracciones XXXI, XXXII, XXXIII, XXXIV, XXXV y Octavo" +
				" Transitorio del Reglamento de la Administración Pública Municipal y de los Artículos 1, 23, 67 punto 1 y 2, 69, 73" +
				", del Reglamento de Estacionamientos en el Municipio de Guadalajara, en la calle " +
				"etLugar" + " a su cruce con la(s)" +
				" Calle(s) " + "etEntre" + " observé la siguiente falta al reglamento citado " + "infra" + " " + "cla"+ " acto seguido con el fundamento" +
				" en el artículo 123 fracción II de la Ley del Procedimiento Administrativo del Estado de Jalisco y sus Municipios se" +
				" le INFRACCIONA al propietario del vehículo marca " + "etMarca" + ", color " + "etColor" + " con placas de circulación " + 
				"etPlacas"+ " haciéndose acreedor" +
				" a la sanción prevista por el " + "completo" +
				". Bajo el mismo tenor, se le informa al ciudadano que esta medida es con el fin de invitar a favorecer e incrementar" +
				" la cultura vial de Guadalajara.";
		return data;
	}
	

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		String str = "";
		//str = etArticulo.getText().toString();
		switch (parent.getId()) {
		case R.id.spInfraccion:
			if(position != 0) {
				//str += articulo.get(position) + ", ";
				
				articulos[0] = articulo.get(position);
				id_c_infraccion[0] = idCI.get(position);
				importe[0] = importes.get(position);
				
				cve_infraccion[0]  = cve.get(position);
				
				cveI1 = cve.get(position);
				countInf++;
				
				arti = articulo.get(position).split("[(]");
				
				completo = arti[1] + arti[2];
				completo = completo.replace(")", ""); 
				cla = cve.get(position);
				infra = infracciones1.get(position);
				san = importes.get(position);
				System.err.println(arti.length + " " + arti[1] + " " + arti[2]);
				
			}
			break;
		/*case R.id.spInfraccion1:
			if(position != 0) {
				//str += articulo.get(position) + ", ";
				
				articulos[1] = articulo.get(position);
				id_c_infraccion[1] = idCI.get(position);
				importe[1] = importes.get(position);
				
				cve_infraccion[1]  = cve.get(position);
				
				cveI2 = cve.get(position);
				countInf++;
				
			}
			break;
		case R.id.spInfraccion2:
			if(position != 0) {
				//str += articulo.get(position) + ", ";
				
				articulos[2] = articulo.get(position);
				id_c_infraccion[2] = idCI.get(position);
				importe[2] = importes.get(position);
				
				cve_infraccion[2]  = cve.get(position);
				
				cveI3 = cve.get(position);
				countInf++;
				
			}
			break;
		case R.id.spInfraccion3:
			if(position != 0) {
				//str += articulo.get(position) + ", "; 
				
				articulos[3] = articulo.get(position);
				id_c_infraccion[3] = idCI.get(position);
				importe[3] = importes.get(position);
				
				cve_infraccion[3]  = cve.get(position);
				
				cveI4 = cve.get(position);
				countInf++;
				
			}
			break;
		case R.id.spInfraccion4:
			if(position != 0) {
				//str += articulo.get(position) + ", "; 
				
				articulos[4] = articulo.get(position);
				id_c_infraccion[4] = idCI.get(position);
				importe[4] = importes.get(position);
				
				cve_infraccion[4]  = cve.get(position);
				
				cveI5 = cve.get(position);
				countInf++;
				
			}
			break;*/
			
		case R.id.spEstado:
			if(!spEstado.getSelectedItem().toString().equalsIgnoreCase("")) {
				etProcedencia.setText(spEstado.getSelectedItem().toString());
			}
			break;
			
		case R.id.spColor:
			if(!spColor.getSelectedItem().toString().equalsIgnoreCase("")) {
				if(!spColor.getSelectedItem().toString().equalsIgnoreCase("Seleccione color..."))
					etColor.setText(spColor.getSelectedItem().toString());
			}
			break;
			
		case R.id.spMarca:
			if(!spMarca.getSelectedItem().toString().equalsIgnoreCase("")) {
				if (!spMarca.getSelectedItem().toString().equalsIgnoreCase("Seleccione marca...")) 
					etMarca.setText(spMarca.getSelectedItem().toString());
			}
			break;

		default:
			break;
		}
		for (int i = 0; i < articulos.length; i++) {
			if(!articulos[i].isEmpty())
				str += articulos[i].trim() + ", ";
		}
		//etArticulo.setText(str.trim());
		etArticulo.setText("");
		
	}
	
	public void buscarPlaca(String pla) {
		placas.clear();
		placa = ds.getAllPlacas(" placa like '%" + pla + "%'");
		for (int i = 0; i < placa.size(); i++) {
			System.err.println(placa.get(i).getPlaca().trim());
			placas.add(placa.get(i).getPlaca().trim());
		}
		
		for (int i = 0; i < placas.size(); i++) {
			System.err.println(placas.get(i).equalsIgnoreCase(pla.trim()));
			if(placas.get(i).equalsIgnoreCase(pla.trim())){
				//System.out.println(placas.get(i).equalsIgnoreCase(s.toString()));
				//etNombre.setText(placa.get(i).getPropiePlaca().trim());
				//etNombre.setText(placa.get(i).getPropiePlaca().trim());
				/*etApPaterno.setText(placa.get(i).getPaternoPalca());
				etApMaterno.setText(placa.get(i).getMaternoPlaca());*/
				//etColonia.setText(placa.get(i).getColonia().trim());
				//etDomicilio.setText(placa.get(i).getDireccion().trim());
				etMarca.setText(placa.get(i).getMarca().trim());
				//etSerie.setText(placa.get(i).getSerie().trim());
				//etTipo.setText(placa.get(i).getTipo().trim());
				//etModelo.setText(placa.get(i).getAxo() + "");
				etColor.setText(placa.get(i).getColor());
				
				/*etNombrePopie.setText(placa.get(i).getPropiePlaca());
				etDomicilioPopie.setText(placa.get(i).getDireccion());
				etColoniaPropie.setText(placa.get(i).getColonia());*/
				
				id_placa = placa.get(i).getIdPlaca();
				
				/*etNombre.setEnabled(false);
				etColonia.setEnabled(false);
				etDomicilio.setEnabled(false);
				etMarca.setEnabled(false);
				etSerie.setEnabled(false);
				etTipo.setEnabled(false);
				etModelo.setEnabled(false);*/
				
				break;
			} else {
				/*if(!etNombre.getText().toString().isEmpty()) {
					//System.out.println(false);
					etNombre.setText("");
					etColonia.setText("");
					etDomicilio.setText("");
					etMarca.setText("");
					etSerie.setText("");
					etTipo.setText("");
					etModelo.setText("");
					etColor.setText("");
					
					etNombrePopie.setText("");
					etDomicilioPopie.setText("");
					etColoniaPropie.setText("");
					
					id_placa = 0;
					
					/*etNombre.setEnabled(true);
					etColonia.setEnabled(true);
					etDomicilio.setEnabled(true);
					etMarca.setEnabled(true);
					etSerie.setEnabled(true);
					etTipo.setEnabled(true);
					etModelo.setEnabled(true);*
					
					break;
				}*/
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if(!s.toString().isEmpty()) {
			placa = ds.getAllPlacas(" placa like '%" + s.toString() + "%'");
			for (int i = 0; i < placa.size(); i++) {
				System.err.println(placa.get(i).getPlaca().trim());
				placas.add(placa.get(i).getPlaca().trim());
			}
			for (int i = 0; i < placas.size(); i++) {
				if(placas.get(i).equalsIgnoreCase(s.toString().trim())){
					//System.out.println(placas.get(i).equalsIgnoreCase(s.toString()));
					//etNombre.setText(placa.get(i).getPropiePlaca().trim());
					//etNombre.setText(placa.get(i).getPropiePlaca().trim());
					/*etApPaterno.setText(placa.get(i).getPaternoPalca());
					etApMaterno.setText(placa.get(i).getMaternoPlaca());*/
					//etColonia.setText(placa.get(i).getColonia().trim());
					//etDomicilio.setText(placa.get(i).getDireccion().trim());
					etMarca.setText(placa.get(i).getMarca().trim());
					//etSerie.setText(placa.get(i).getSerie().trim());
					//etTipo.setText(placa.get(i).getTipo().trim());
					//etModelo.setText(placa.get(i).getAxo() + "");
					etColor.setText(placa.get(i).getColor());
					
					/*etNombrePopie.setText(placa.get(i).getPropiePlaca());
					etDomicilioPopie.setText(placa.get(i).getDireccion());
					etColoniaPropie.setText(placa.get(i).getColonia());*/
					
					id_placa = placa.get(i).getIdPlaca();
					
					/*etNombre.setEnabled(false);
					etColonia.setEnabled(false);
					etDomicilio.setEnabled(false);
					etMarca.setEnabled(false);
					etSerie.setEnabled(false);
					etTipo.setEnabled(false);
					etModelo.setEnabled(false);*/
					
					break;
				} else {
					/*if(!etNombre.getText().toString().isEmpty()) {
						//System.out.println(false);
						etNombre.setText("");
						etColonia.setText("");
						etDomicilio.setText("");
						etMarca.setText("");
						etSerie.setText("");
						etTipo.setText("");
						etModelo.setText("");
						etColor.setText("");
						
						etNombrePopie.setText("");
						etDomicilioPopie.setText("");
						etColoniaPropie.setText("");
						
						id_placa = 0;
						
						/*etNombre.setEnabled(true);
						etColonia.setEnabled(true);
						etDomicilio.setEnabled(true);
						etMarca.setEnabled(true);
						etSerie.setEnabled(true);
						etTipo.setEnabled(true);
						etModelo.setEnabled(true);*
						
						break;
					}*/
				}
			}
			
			/*if(placas.contains(s.toString())) {
				System.out.println(true);
			} else {
				//System.out.println(false);
			}*/
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		
	}
	
	private void setCurrentLocation(Location loc) {
    	currentLocation = loc;
    }
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mLocationManager.removeUpdates(mLocationListener);
	    	if (currentLocation!=null) {
	    		//Toast.makeText(getBaseContext(), "Latitude: " + currentLocation.getLatitude() + " Longitude: " + currentLocation.getLongitude(), Toast.LENGTH_LONG).show();
	    		Log.i("Coordenadas", "Latitude: " + currentLocation.getLatitude() * 1E6 + " Longitude: " + (int)currentLocation.getLongitude() * 1E6);
	    		latitud = currentLocation.getLatitude();
	    		longitud = currentLocation.getLongitude();
	    		//System.out.println("lat " + currentLocation.getLatitude() + " lon " + currentLocation.getLongitude());
	    		
	    		/*geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
	    		if(Conexion.validarConexion(getApplicationContext())) {
		    		try {
		    			address = geocoder.getFromLocation(latitud, longitud, 1);
		    			if(address.size() > 0) {
		    				System.out.println(address.get(0).getAddressLine(0));
		    				etLugar.setText(address.get(0).getAddressLine(0).toUpperCase(Locale.getDefault()));
		    			}
		    		} catch (IOException e) {
		    			e.printStackTrace();
		    			System.err.println(e.getMessage());
		    		}
	    		}*/
	    	}
		}
	};
	
	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {
			if (location != null) {
                Toast toast =Toast.makeText(getBaseContext(), "Señal GPS encontrada", Toast.LENGTH_LONG);
                toast.setGravity(0, 0, 15);
				toast.show();
                setCurrentLocation(location);
                handler.sendEmptyMessage(0);
            }
		}

		public void onProviderDisabled(String provider) {
			
		}

		public void onProviderEnabled(String provider) {
			
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
        
    }
	
	@Override
	public void run() {
		mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		try{
			if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			
				Looper.prepare();
			
				mLocationListener = new MyLocationListener();
			/*	if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED ||ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
					ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION}, 9999);
*/
				mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
				Looper.loop(); 
				Looper.myLooper().quit(); 
			
			} else {
				Looper.prepare();
				Toast toast = Toast.makeText(MainActivity.this,"Señal GPS no encontrada", Toast.LENGTH_LONG);
				toast.setGravity(0, 0, 15);
				toast.show();
				//builAlert();
			}
		}catch (Exception e) {
			Log.e("gps", e.getMessage());
		}
	}
	
	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			salir();
		}
		return super.onKeyDown(keyCode, event);
	}*/
	
	public void salir() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
		dialog.setTitle("�Esta seguro de salir?");
		dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		dialog.setMessage("¿Esta seguro de salir del llenado de la infracción?").setPositiveButton("SI", new DialogInterface.OnClickListener() {
		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				MainActivity.this.onDestroy();
				if(thread != null)
					handler.sendEmptyMessage(0);
			}
		});
		AlertDialog alert = dialog.create();
		alert.show();
	}
	
	public void conceptos() {
		concepto1 = "";
		concepto1 += "El concepto es el siguiente: ";
		concepto1 += spInf.getSelectedItem().toString() + "\n";
		concepto1 += "La placa es la siguiente \n";
		concepto1 += etPlacas.getText().toString().trim() + "\n";
		concepto1 += "La marca es la siguiente \n";
		concepto1 += etMarca.getText().toString().trim() + "\n";
		concepto1 += "El color es el siguiente \n";
		concepto1 += etColor.getText().toString().trim() + "\n";
		
		/*if(spInf1.getSelectedItem().toString().trim() != "") {
			concepto1 += ", Concepto 2: " + spInf1.getSelectedItem().toString();
		}
		
		if(spInf2.getSelectedItem().toString() != "") {
			concepto1 += ", Concepto 3:" + spInf2.getSelectedItem().toString();
		}
		
		if(spInf3.getSelectedItem().toString() != "") {
			concepto1 += ", Concepto 4:" + spInf3.getSelectedItem().toString();
		}

		
		if(spInf4.getSelectedItem().toString() != "") {
			concepto1 += ", Concepto 5:" + spInf4.getSelectedItem().toString() +  ", ";
		}*/
		
		concepto1 += " �Es correcto?" ;

	}
	
	private void mostrarMsj() {
		conceptos();
		AlertDialog.Builder al = new AlertDialog.Builder(this)
		.setTitle("Mensaje")
		.setMessage(concepto1)
		.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				attemptLogin();
			}
		}).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		
		AlertDialog alert = al.create();
		alert.show();
	}
	
	
	public static String getMes(int mes) {
		switch (mes) {
		case 0:
			return "Enero";
			
		case 1:
			return "Febrero";
			
		case 2:
			return "Marzo";
			
		case 3:
			return "Abril";
			
		case 4:
			return "Mayo";
			
		case 5:
			return "Junio";
			
		case 6:
			return "Julio";
			
		case 7:
			return "Agosto";
			
		case 8:
			return "Septiembre";
			
		case 9:
			return "Octubre";
			
		case 10:
			return "Noviembre";

		default:
			return "Diciembre";
		}
	}
	
	private void mostrarMsjFolio() {
		AlertDialog.Builder al = new AlertDialog.Builder(this)
		.setTitle("Mensaje")
		.setMessage("Favor de asignar un nuevo folio de infracciones.")
		.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				onDestroy();
			}
		});
		AlertDialog alert = al.create();
		alert.show();
	}
	
	
}
