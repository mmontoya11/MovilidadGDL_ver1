package com.perspective.movilidad.transito;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.bixolon.printer.BixolonPrinter;
import com.perspective.movilidad.R;
import com.perspective.movilidad.core.JSONParser;
import com.perspective.movilidad.gs.Justificar;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ImprimirTicket extends Activity implements OnClickListener {
	
	private EditText etFolio,etfecha,etPlaca,etProcedencia,etMarca,etColor,etZona,etLugar,etEntre,etConcepto,etObservacion,etHora;
	private Button btnBuscar,btnImprimir;
	private ProgressDialog pd;
	private JSONParser jsonParser;
	private int estatus = 0,clave = 0,dia = 0,me = 0,axo = 0;
	private double importe = 0;
	private String art = "",cveI = "",mConnectedDeviceName = "",elemento = "",h = "",infra = "",completo = "",cveAgente= ""; 
	public static BixolonPrinter mBixolonPrinter;
	public static final String TAG = "BixolonPrinterSample";
	private AlertDialog mSampleDialog;
	private DecimalFormat format = new DecimalFormat("###,###.##");
	private String [] arti,hr;
	private String placa,fecha,procedencia,marca,color,zona,lugar,entre,obs;
	private RelativeLayout rl1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imprimir_ticket);
		
		etFolio = (EditText)findViewById(R.id.etFolio);
		btnBuscar = (Button)findViewById(R.id.btnBuscar);
		etfecha = (EditText)findViewById(R.id.etFecha);
		etPlaca = (EditText)findViewById(R.id.etPlacas);
		etProcedencia = (EditText)findViewById(R.id.etProcedencia);
		etMarca = (EditText)findViewById(R.id.etMarca);
		etColor = (EditText)findViewById(R.id.etColor);
		etZona = (EditText)findViewById(R.id.etZona);
		etLugar = (EditText)findViewById(R.id.etLugar);
		etEntre = (EditText)findViewById(R.id.etEntre);
		etConcepto = (EditText)findViewById(R.id.etInf);
		etObservacion = (EditText)findViewById(R.id.etObservaciones);
		btnImprimir = (Button)findViewById(R.id.btnImprimir);
		etHora = (EditText)findViewById(R.id.etHr);
		rl1 = (RelativeLayout)findViewById(R.id.rl1);
		
		btnBuscar.setOnClickListener(this);
		btnImprimir.setOnClickListener(this);
		
		jsonParser = new JSONParser();
		
		rl1.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBuscar:
			if(!etFolio.getText().toString().isEmpty()) {
				etPlaca.setText("");
				etfecha.setText("");
				etProcedencia.setText("");
				etMarca.setText("");
				etColor.setText("");
				etZona.setText("");
				etLugar.setText("");
				etEntre.setText("");
				etObservacion.setText("");
				new BuscarFolio().execute(etFolio.getText().toString());
			} else { 
				Toast toast = Toast.makeText(getApplicationContext(), "Ingrese el folio", Toast.LENGTH_LONG);
				toast.setGravity(15, 0, 0);
				toast.show();
			}
			break;
			
		case R.id.btnImprimir:
			mBixolonPrinter = new BixolonPrinter(this, mHandler, null);
			mBixolonPrinter.findBluetoothPrinters();
			
			printSampleGDL();
			break;

		default:
			break;
		}
	}
	
	class BuscarFolio extends AsyncTask<String, Void, Boolean> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(ImprimirTicket.this);
			pd.setTitle("Mensaje");
			pd.setMessage("Buscando...");
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			ArrayList<NameValuePair> fol = new ArrayList<NameValuePair>();
			fol.add(new BasicNameValuePair("folio", params[0]));
			JSONObject jobject = null;
			try{
				jobject = jsonParser.realizarHttpRequest("http://apidiv.guadalajara.gob.mx:8085/serversql/getFolio.php", "GET", fol);
			} catch (Exception e) {
				System.err.println(e.getMessage() + " mensaje");
			}
			try{
				estatus = jobject.getInt("status");
				if(estatus > 0) {
					System.err.println(jobject.getString("placa") + " p");
					placa = jobject.getString("placa").trim();
					fecha = jobject.getString("fecha").trim();
					procedencia = jobject.getString("procedencia").trim();
					marca = jobject.getString("marca").trim();
					color  = jobject.getString("color").trim();
					zona = jobject.getString("zona").trim();
					lugar = jobject.getString("lugar_infraccion").trim();
					entre = jobject.getString("entre_calles").trim();
					obs = jobject.getString("observaciones_tablet").trim();
					importe = jobject.getDouble("importe_infraccion");
					art = jobject.getString("articulos").trim();
					clave = jobject.getInt("clave");
					cveI = jobject.getString("cve_infraccion").trim();
					elemento = jobject.getString("agente").trim();
					dia = jobject.getInt("d");
					me = jobject.getInt("m");
					axo = jobject.getInt("a");
					h = jobject.getString("fecha").split(" ")[1];
					hr = h.split(":");
					h = hr[0] + ":" + hr[1];
					arti = art.split("[(]");
					
					completo = arti[1] + arti[2];
					completo = completo.replace(")", ""); 
					infra = arti[0];
					cveAgente = jobject.getString("cve_agente");
					
					System.err.println(clave);
					
				}
			}catch(JSONException e) {
				System.err.println(e.getMessage());
			}
			return estatus > 0;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pd.dismiss();
			if(result) {
				rl1.setVisibility(View.VISIBLE);
				etPlaca.setText(placa);
				etfecha.setText(fecha);
				etProcedencia.setText(procedencia);
				etMarca.setText(marca);
				etColor.setText(color);
				etZona.setText(zona);
				etLugar.setText(lugar);
				etEntre.setText(entre);
				etObservacion.setText(obs);
				etHora.setText(h);
				etConcepto.setText(infra.trim());
			} else {
				Toast toast = Toast.makeText(getApplicationContext(), "Nose encontro folio", Toast.LENGTH_LONG);
				toast.setGravity(15, 0, 0);
				toast.show();
			}
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
						DialogManager.showBluetoothDialog2(ImprimirTicket.this, (Set<BluetoothDevice>) msg.obj);
					}
					return true;
				
				case BixolonPrinter.MESSAGE_STATE_CHANGE:
					
					if(msg.arg1 == BixolonPrinter.STATE_CONNECTED) {
						
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
					
					break;
				case BixolonPrinter.MESSAGE_PRINT_COMPLETE:
					//mBixolonPrinter.disconnect();
					//mHandler.sendEmptyMessage(0);
					break;
			}
			
			
			return true;
		}
	});
	
	@SuppressLint({ "InflateParams", "DefaultLocale" })
	private void printSampleGDL() {
		if (mSampleDialog == null) {
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
			tvClave.setText(cveI+"");
			tvSan.setText("$ " + String.valueOf(format.format(importe)));
			
			tvServidor.setText(elemento);
			
			System.err.println(elemento);
			
			tvFolio.setText(etFolio.getText().toString());//32682972 32682976
			
			final String cuerpo = "En la Ciudad de Guadalajara, Jalisco, siendo las " + h + " horas del d�a " + dia + " del mes de " + MainActivity.getMes(me) +
					" del a�o " + axo + ", el servidor p�blico " + elemento + " adscrito al �rea de Gesti�n del Estacionamiento" +
					", dependiente de la Direcci�n de Movilidad y Transporte, con n�mero de empleado " + cveAgente + ", acreditando mi personalidad" +
					" con la credencial oficial vigente, expedida por el Director de Recursos Humanos de este H. Ayuntamiento Constitucional" +
					" de Guadalajara, y en cumplimiento de mis funciones, encargado de vigilar el cumplimiento de los reglamentos municipales" +
					", en t�rminos de los Art�culos 132, 133 pen�ltimo p�rrafo, 139 fracciones XXXI, XXXII y Octavo" +
					" Transitorio del Reglamento de la Administraci�n P�blica Municipal y de los Art�culos 1, 23, 67 punto 1 y 2, 69, 73" +
					", del Reglamento de Estacionamientos en el Municipio de Guadalajara, en la calle " + etLugar.getText().toString().toUpperCase(Locale.getDefault()) + " a su cruce con la(s)" +
					" Calle(s) " + etEntre.getText().toString().toUpperCase(Locale.getDefault()) + " observ� la siguiente falta al Reglamento citado " + infra.trim().toUpperCase() + " " + cveI.trim().toUpperCase() + " acto seguido con el fundamento" +
					" en el art�culo 123 fracci�n II de la Ley del Procedimiento Administrativo del Estado de Jalisco y sus Municipios se" +
					" le INFRACCIONA al propietario del veh�culo marca " + etMarca.getText().toString().toUpperCase(Locale.getDefault()) + ", color " + etColor.getText().toString().toUpperCase() + " con placas de circulaci�n " + etPlaca.getText().toString().toUpperCase() + " haci�ndose acreedor" +
					" a la sanci�n prevista por el " + completo.trim() +
					" Bajo el mismo tenor, se le informa al ciudadano que esta medida es con el fin de invitar a favorecer e incrementar" +
					" la cultura vial de Guadalajara.";
			
			tvMulta.setText("En la Ciudad de Guadalajara, Jalisco, siendo las " + h + " horas del d�a " + dia + " del mes de " + MainActivity.getMes(me) +
					" del a�o " + axo + ", el servidor p�blico " + elemento + " adscrito al �rea de Gesti�n del Estacionamiento" +
					", dependiente de la Direcci�n de Movilidad y Transporte, con n�mero de empleado " + cveAgente + ", acreditando mi personalidad" +
					" con la credencial oficial vigente, expedida por el Director de Recursos Humanos de este H. Ayuntamiento Constitucional" +
					" de Guadalajara, y en cumplimiento de mis funciones, encargado de vigilar el cumplimiento de los reglamentos municipales" +
					", en t�rminos de los Art�culos 132, 133 pen�ltimo p�rrafo, 139 fracciones XXXI, XXXII y Octavo" +
					" Transitorio del Reglamento de la Administraci�n P�blica Municipal y de los Art�culos 1, 23, 67 punto 1 y 2, 69, 73" +
					", del Reglamento de Estacionamientos en el Municipio de Guadalajara, en la calle " + etLugar.getText().toString().toUpperCase(Locale.getDefault()) + " a su cruce con la(s)" +
					" Calle(s) " + etEntre.getText().toString().toUpperCase(Locale.getDefault()) + " observ� la siguiente falta al reglamento citado " + infra.trim().toUpperCase() + " " + cveI.trim().toUpperCase() + " acto seguido con el fundamento" +
					" en el art�culo 123 fracci�n II de la Ley del Procedimiento Administrativo del Estado de Jalisco y sus Municipios se" +
					" le INFRACCIONA al propietario del veh�culo marca " + etMarca.getText().toString().toUpperCase(Locale.getDefault()) + ", color " + etColor.getText().toString().toUpperCase() + " con placas de circulaci�n " + etPlaca.getText().toString().toUpperCase() + " haci�ndose acreedor" +
					" a la sanci�n prevista por el " + completo +
					" \n" +
					"Bajo el mismo tenor, se le informa al ciudadano que esta medida es con el fin de invitar a favorecer e incrementar" +
					" la cultura vial de Guadalajara.");
					
			//concepto += spInf.getSelectedItem().toString();
			
			mSampleDialog = new AlertDialog.Builder(ImprimirTicket.this).setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@SuppressLint("NewApi")
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					String [] str;
					
					
					mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_1252_LATIN1);
					
					mBixolonPrinter.setPageMode();
					ImprimirTicket.mBixolonPrinter.setPrintArea(0, 0, 576, 200);

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
					mBixolonPrinter.printText(getResources().getString(R.string.folio) + " " + etFolio.getText().toString().trim() + "\n", 
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
					ImprimirTicket.mBixolonPrinter.setPrintArea(0, 0, 576, 430);

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
					mBixolonPrinter.printText(cveI, BixolonPrinter.ALIGNMENT_LEFT,
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
					mBixolonPrinter.printText("$ " + String.valueOf(format.format(importe)), BixolonPrinter.ALIGNMENT_LEFT,
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
	
}
