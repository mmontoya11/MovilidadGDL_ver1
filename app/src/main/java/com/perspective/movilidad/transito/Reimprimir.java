package com.perspective.movilidad.transito;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.bixolon.printer.BixolonPrinter;
import com.perspective.movilidad.R;
import com.perspective.movilidad.core.TransitoDS;
import com.perspective.movilidad.gs.Agentes;
import com.perspective.movilidad.gs.Infraccion;
import com.perspective.movilidad.gs.Infracciones;
import com.perspective.movilidad.gs.Justificar;


import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Reimprimir extends Activity implements OnClickListener {

	private int idI;
	private List<Infraccion> infraccion = new ArrayList<Infraccion>();
	private List<Agentes> agente = new ArrayList<Agentes>();
	private TransitoDS ds;
	private EditText etfecha,ethr,etPlacas,etProcedencia,etMarca,etColor,etObservaciones,etLugar,etCruce;
	private Button btnImprimir;
	private TextView tvAgente;
	public static BixolonPrinter mBixolonPrinter;
	private AlertDialog mSampleDialog;
	public static final String TAG = "BixolonPrinterSample";
	private Spinner spInf,spInf1,spInf2,spInf3,spInf4;
	private List<Infracciones> inf = new ArrayList<Infracciones>();
	private List<String> infracciones = new ArrayList<String>();
	private int mAlignment = BixolonPrinter.ALIGNMENT_CENTER;
	private BaseAdapter adapter;
	private String concepto = "",infra = "",cla = "",elemento = "",folio = "",clave = "",fecha = "",h = "",dia="",me = "",axo,completo,zona = "";
	private boolean isConected = false;
	private double san;
	private DecimalFormat format = new DecimalFormat("###,###.##");
	private String [] arti ;
	
	static final String ACTION_COMPLETE_PROCESS_BITMAP = "com.bixolon.anction.COMPLETE_PROCESS_BITMAP";
	static final String EXTRA_NAME_BITMAP_WIDTH = "BitmapWidth";
	static final String EXTRA_NAME_BITMAP_HEIGHT = "BitmapHeight";
	static final String EXTRA_NAME_BITMAP_PIXELS = "BitmapPixels";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reimprimir);
		
		idI = getIntent().getExtras().getInt("ide");
		System.err.println(idI);
		
		ds = new TransitoDS(getApplicationContext());
		
		ds.open();
		
		infraccion = ds.getAllInfraccion("id_infracciones = " + idI);
		
		this.etfecha = (EditText)findViewById(R.id.etFecha);
		this.ethr = (EditText)findViewById(R.id.etHr);
		this.etPlacas = (EditText)findViewById(R.id.etPlacas);
		this.etProcedencia = (EditText)findViewById(R.id.etProcedencia);
		this.etMarca = (EditText)findViewById(R.id.etMarca);
		this.etColor = (EditText)findViewById(R.id.etColor);
		this.etObservaciones = (EditText)findViewById(R.id.etObservaciones);
		this.btnImprimir = (Button)findViewById(R.id.btnImprimir);
		this.spInf = (Spinner)findViewById(R.id.spInfraccion);
		this.tvAgente = (TextView)findViewById(R.id.tvAgente);
		etLugar = (EditText)findViewById(R.id.etLugar);
		etCruce = (EditText)findViewById(R.id.etEntre);
		
		this.btnImprimir.setOnClickListener(this);
		
		
		
		for (int i = 0; i < infraccion.size(); i++) {
			System.err.println(infraccion.get(i).getFolio());
		}
		
		etfecha.setText(infraccion.get(0).getFecha_multa());
		etPlacas.setText(infraccion.get(0).getPlaca());
		etProcedencia.setText(infraccion.get(0).getProcedencia());
		etMarca.setText(infraccion.get(0).getMarca());
		etColor.setText(infraccion.get(0).getColor());
		ethr.setText(infraccion.get(0).getHora_infraccion());
		etObservaciones.setText(infraccion.get(0).getObservaciones());
		etLugar.setText(infraccion.get(0).getLugar_infaccion());
		etCruce.setText(infraccion.get(0).getEntre_calle());
		
		
		tvAgente.setText(infraccion.get(0).getCve_agente());
		
		cla = infraccion.get(0).getCve_infraccion();
		
		clave = infraccion.get(0).getCve_agente();
		
		fecha = infraccion.get(0).getFecha_multa();
		
		String [] fechas = fecha.split("/"); 
		
		dia = fechas[0];
		me = fechas[1];
		axo = fechas[2];
		
		int m = Integer.valueOf(me)-1;
		
		me = String.valueOf(m);
		
		h = infraccion.get(0).getHora_infraccion();
		
		h = h.substring(0, 2) + ":" + h.substring(2, h.length());
		
		System.err.println(fecha + " fecha");
		
		agente = ds.getAllAgentes("cve_agente like '%" +infraccion.get(0).getCve_agente()+"%'");
		
		san = 0;
		
		folio = infraccion.get(0).getFolio();
		
		elemento = agente.get(0).getNombreAgente() + " " + agente.get(0).getPaternoAgente() + " " + agente.get(0).getMaternoAgente();
		
		
		infracciones.clear();
		infracciones.add("");
		adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_dropdown_item, infracciones);
		spInf.setAdapter(adapter);
		
		etfecha.setEnabled(false);
		etPlacas.setEnabled(false);
		etProcedencia.setEnabled(false);
		etMarca.setEnabled(false);
		etColor.setEnabled(false);
		ethr.setEnabled(false);
		etObservaciones.setEnabled(false);
		etLugar.setEnabled(false);
		etCruce.setEnabled(false);
		spInf.setEnabled(false);
		
		
		
		if(!infraccion.get(0).getCve_infraccion().equalsIgnoreCase("")) {
			try {
				inf=ds.getAllInfracciones("id_c_infracciones = '" + infraccion.get(0).getC_id_infracciones() + "'");
				
				infracciones.clear();
				System.err.println(inf.size());
				for (int i = 0; i < inf.size(); i++) {
					infra = inf.get(i).getDescripcion().trim();
					san = inf.get(0).getImporteInfraccion();
					infracciones.add(inf.get(i).getCveInfracciones() + " " +  inf.get(i).getDescripcion().trim());
					System.err.println(inf.get(i).getCveInfracciones() + " " +  inf.get(i).getDescripcion().trim());
					
					arti = inf.get(i).getArticulo().split("[(]");
					
					zona = infraccion.get(0).getZona();
					
					completo = arti[1] + arti[2];
					completo = completo.replace(")", ""); 
					
					System.err.println(completo + " completo");
					
				}
			} catch(Exception e) {
				System.err.println(e.getMessage() + " aqui ");
			}
		}
		if(infracciones.size() > 0)
			adapter.notifyDataSetChanged();
		
		mBixolonPrinter = new BixolonPrinter(this, mHandler, null);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reimprimir, menu);
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
		case R.id.btnImprimir:
			mBixolonPrinter = new BixolonPrinter(this, mHandler, null);
			//if(!isConected)
				mBixolonPrinter.findBluetoothPrinters();
			
			printSampleGDL();
			/*try {*/
				/*mBixolonPrinter = new BixolonPrinter(getApplicationContext(), mHandler, null);
				mBixolonPrinter.findBluetoothPrinters();
				
				printSample2Copy();*/
			/*} catch (Exception e) {
				System.err.println(e.getMessage() + "  ll ");
			}*/
			
			break;

		default:
			break;
		}
	}
	
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
			
			final String cuerpo = "En la Ciudad de Guadalajara, Jalisco, siendo las " + h + " horas del dia " + dia + " del mes de " + MainActivity.getMes(Integer.valueOf(me)) +
					" del a�o " + axo + ", el servidor p�blico " + elemento + " adscrito al �rea de Gesti�n del Estacionamiento" +
					", dependiente de la Direcci�n de Movilidad y Transporte, con n�mero de empleado " +  clave + ", acreditando mi personalidad" +
					" con la credencial oficial vigente, expedida por el Director de Recursos Humanos de este H. Ayuntamiento Constitucional" +
					" de Guadalajara, y en cumplimiento de mis funciones, encargado de vigilar el cumplimiento de los reglamentos municipales" +
					", en t�rminos de los Art�culos 132, 133 pen�ltimo p�rrafo, 139 fracciones XXXI, XXXII y Octavo" +
					" Transitorio del Reglamento de la Administraci�n P�blica Municipal y de los Art�culos 1, 23, 67 punto 1 y 2, 69, 73" +
					", del Reglamento de Estacionamientos en el Municipio de Guadalajara, en la calle " + etLugar.getText().toString().toUpperCase(Locale.getDefault()) + " a su cruce con la(s)" +
					" Calle(s) " + etCruce.getText().toString().toUpperCase(Locale.getDefault()) + " observ� la siguiente falta al reglamento citado " + infra.trim().toUpperCase() + " " + cla.trim().toUpperCase() + " acto seguido con el fundamento" +
					" en el art�culo 123 fracci�n II de la Ley del Procedimiento Administrativo del Estado de Jalisco y sus Municipios se" +
					" le INFRACCIONA al propietario del veh�culo marca " + etMarca.getText().toString().toUpperCase(Locale.getDefault()) + ", color " + etColor.getText().toString().toUpperCase() + " con placas de circulaci�n " + etPlacas.getText().toString().toUpperCase() + " haci�ndose acreedor" +
					" a la sanci�n prevista por el " + completo +
					//" a la sanci�n prevista por el " +
					"Bajo el mismo tenor, se le informa al ciudadano que esta medida es con el fin de invitar a favorecer e incrementar" +
					" la cultura vial de Guadalajara.";
			
			tvMulta.setText("En la Ciudad de Guadalajara, Jalisco, siendo las " + h + " horas del dia " + dia + " del mes de " + MainActivity.getMes(Integer.valueOf(me)) +
					" del a�o " + axo + ", el servidor p�blico " + elemento + " adscrito al �rea de Gesti�n del Estacionamiento" +
					", dependiente de la Direcci�n de Movilidad y Transporte, con n�mero de empleado " +  clave + ", acreditando mi personalidad" +
					" con la credencial oficial vigente, expedida por el Director de Recursos Humanos de este H. Ayuntamiento Constitucional" +
					" de Guadalajara, y en cumplimiento de mis funciones, encargado de vigilar el cumplimiento de los reglamentos municipales" +
					", en términos de los Art�culos 132, 133 penúltimo p�rrafo, 139 fracciones XXXI, XXXII y Octavo" +
					" Transitorio del Reglamento de la Administraci�n P�blica Municipal y de los Art�culos 1, 23, 67 punto 1 y 2, 69, 73" +
					", del Reglamento de Estacionamientos en el Municipio de Guadalajara, en la calle " + etLugar.getText().toString().toUpperCase(Locale.getDefault()) + " a su cruce con la(s)" +
					" Calle(s) " + etCruce.getText().toString().toUpperCase(Locale.getDefault()) + " observé la siguiente falta al reglamento citado " + infra.trim().toUpperCase() + " " + cla.trim().toUpperCase() + " acto seguido con el fundamento" +
					" en el art�culo 123 fracci�n II de la Ley del Procedimiento Administrativo del Estado de Jalisco y sus Municipios se" +
					" le INFRACCIONA al propietario del veh�culo marca " + etMarca.getText().toString().toUpperCase(Locale.getDefault()) + ", color " + etColor.getText().toString().toUpperCase() + " con placas de circulaci�n " + etPlacas.getText().toString().toUpperCase() + " haciéndose acreedor" +
					" a la sanci�n prevista por el " + completo +
					"\n" +
					"Bajo el mismo tenor, se le informa al ciudadano que esta medida es con el fin de invitar a favorecer e incrementar" +
					" la cultura vial de Guadalajara.");
					
			concepto += spInf.getSelectedItem().toString();
			
			mSampleDialog = new AlertDialog.Builder(Reimprimir.this).setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					String [] str;
					
					
					mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_1252_LATIN1);
					
					mBixolonPrinter.setPageMode();
					mBixolonPrinter.setPrintArea(0, 0, 576, 200);

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
					mBixolonPrinter.printText(getResources().getString(R.string.folio) + " " + folio + "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_A | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);

					mBixolonPrinter.printText(getResources().getString(R.string.cedula)+ "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.lineFeed(2, false);
					
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
					mBixolonPrinter.setPrintArea(0, 0, 576, 430);

					mBixolonPrinter.setPrintDirection(BixolonPrinter.DIRECTION_0_DEGREE_ROTATION);
					mBixolonPrinter.setAbsoluteVerticalPrintPosition(30);
					mBixolonPrinter.setAbsolutePrintPosition(30);
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
					mBixolonPrinter.printText(zona, BixolonPrinter.ALIGNMENT_LEFT,
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
					
					mBixolonPrinter.lineFeed(2, false);
					mBixolonPrinter.printText(getResources().getString(R.string.despues)+ "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B , 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printText(getResources().getString(R.string.descuento) + "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printText(getResources().getString(R.string.sanciones)+ "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B , 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printText(getResources().getString(R.string.constitucion) + "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printText(getResources().getString(R.string.constitucion1)+ "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B , 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printText(getResources().getString(R.string.consti_jal) + "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printText(getResources().getString(R.string.consti_jal1)+ "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B , 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printText(getResources().getString(R.string.lpa) + "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printText(getResources().getString(R.string.lpa1)+ "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B , 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printText(getResources().getString(R.string.lmt) + "\n", BixolonPrinter.ALIGNMENT_LEFT,
							BixolonPrinter.TEXT_ATTRIBUTE_FONT_B | BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED, 
							BixolonPrinter.TEXT_SIZE_HORIZONTAL1 | BixolonPrinter.TEXT_SIZE_VERTICAL1, false);
					
					mBixolonPrinter.printText(getResources().getString(R.string.lmt1)+ "\n", BixolonPrinter.ALIGNMENT_LEFT,
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
			
			TextView tvConcepto2 = (TextView)view.findViewById(R.id.tvConcepto2);
			TextView tvConcepto3 = (TextView)view.findViewById(R.id.tvConcepto3);
			TextView tvConcepto4 = (TextView)view.findViewById(R.id.tvConcepto4);
			TextView tvConcepto5 = (TextView)view.findViewById(R.id.tvConcepto5);
			
			TextView tvConcepto12 = (TextView)view.findViewById(R.id.tvConcepto12);
			TextView tvConcepto13 = (TextView)view.findViewById(R.id.tvConcepto13);
			TextView tvConcepto14 = (TextView)view.findViewById(R.id.tvConcepto14);
			TextView tvConcepto15 = (TextView)view.findViewById(R.id.tvConcepto15);
			
			TextView tvElemento = (TextView)view.findViewById(R.id.tvElemento1);
					
			concepto += spInf.getSelectedItem().toString();
			
			if(spInf1.getSelectedItem().toString().trim() == "") {
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
			}
			
			tvfolio.setText(infraccion.get(0).getFolio());
			tvFecha.setText(infraccion.get(0).getFecha_multa());
			tvPlacas.setText(etPlacas.getText().toString().toUpperCase());
			tvProcedencia.setText(etProcedencia.getText().toString().toUpperCase());
			tvMarca.setText(etMarca.getText().toString().toUpperCase());
			tvTipoL.setText(infraccion.get(0).getTipo_lic());
			
			tvObservacione.setText(etObservaciones.getText().toString().toUpperCase());
			tvElemento.setText(infraccion.get(0).getCve_agente() + "\n");
			
			if(etObservaciones.getText().toString().equalsIgnoreCase("")) {
				tvobservaciones.setVisibility(View.GONE);
				tvObservacione.setVisibility(View.GONE);
			}
			
			tvClave.setText(infraccion.get(0).getCve_agente());
			
			mSampleDialog = new AlertDialog.Builder(Reimprimir.this).setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.relativeLayout1);
					
					Bitmap bitmap = Bitmap.createBitmap(layout.getWidth(), layout.getHeight(), Bitmap.Config.ARGB_8888);
					Canvas canvas = new Canvas(bitmap);
					layout.draw(canvas);
					
					
					/*layout.setDrawingCacheEnabled(true);
					layout.buildDrawingCache();
					Bitmap bitmap = layout.getDrawingCache();
					
					
					
					int height = layout.getChildAt(0).getHeight();
					int width = layout.getWidth();
					
					Bitmap bitma = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
					
					Canvas c = new Canvas(bitma);
					layout.layout(0, 0, layout.getLayoutParams().width, layout.getLayoutParams().height);
					layout.draw(c);
					
					Bitmap bitmap = layout.getDrawingCache();*/
					
					mBixolonPrinter.setPageMode();
					mBixolonPrinter.setPrintDirection(BixolonPrinter.DIRECTION_0_DEGREE_ROTATION);
					mBixolonPrinter.setPrintArea(0, 0, 390, 970);
					mBixolonPrinter.setAbsoluteVerticalPrintPosition(970);
					mBixolonPrinter.printBitmap(bitmap, BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.BITMAP_WIDTH_FULL, 88, false);
					mBixolonPrinter.formFeed(true);
					/*try {
						Thread.sleep(5000);
						//printSample21();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					printSample21();*/
				}
			}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			}).create();
		}
		
		mSampleDialog.show();
	}
	
	
	private final Handler mHandler = new Handler(new Handler.Callback() {
		
		@SuppressWarnings("unchecked")
		@Override
		public boolean handleMessage(Message msg) {
			Log.d(TAG, "mHandler.handleMessage(" + msg + ")");
			
			switch (msg.what) {
				
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
						DialogManager.showBluetoothDialog1(Reimprimir.this, (Set<BluetoothDevice>) msg.obj);
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
				case BixolonPrinter.MESSAGE_PRINT_COMPLETE:
					mBixolonPrinter.disconnect();
					
					break;
			}
			
			
			return true;
			}
	});
	
	private void printBitmap(byte[] pixels, int width, int height) {
		mBixolonPrinter.printBitmap(pixels, mAlignment, width, height, false);
		mBixolonPrinter.cutPaper(0, true);
	}
	
	BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Reimprimir.ACTION_COMPLETE_PROCESS_BITMAP)) {
				byte[] pixels = intent.getByteArrayExtra(Reimprimir.EXTRA_NAME_BITMAP_PIXELS);
				int width = intent.getIntExtra(Reimprimir.EXTRA_NAME_BITMAP_WIDTH, 0);
				int height = intent.getIntExtra(Reimprimir.EXTRA_NAME_BITMAP_HEIGHT, 0);
				
				printBitmap(pixels, width, height);
			}

		}
	};
	
	
}
