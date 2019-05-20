package com.perspective.movilidad.transito;

import java.util.ArrayList;
import java.util.List;
//import com.pgm.core.TransitoDS;
//import com.pgm.gs.Infraccion;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.perspective.movilidad.R;
import com.perspective.movilidad.core.TransitoDS;
import com.perspective.movilidad.gs.Infraccion;

public class Busqueda extends Activity implements OnClickListener, OnItemClickListener {
	
	private ImageButton btnFechaI,btnFechaF;
	private Button btnBuscar;
	private EditText etFechaI,etFechaF;
	private static int id,dia,mes,ano;
	private static final int DATE_DIALOG_ID = 0,DATE_DIALOG_ID1 = 1;
	private List<Infraccion> infraccion = new ArrayList<Infraccion>();
	private TransitoDS ds;
	private ListView lv;
	private MostrarListaCategoria listaAdapter;
	private ArrayList<String> datosList = new ArrayList<String>();
	private ArrayList<Integer> idInfraccion = new ArrayList<Integer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_busqueda);
		
		btnFechaI = (ImageButton)findViewById(R.id.ibFechaI);
		btnFechaF = (ImageButton)findViewById(R.id.ibFechaF);
		etFechaI = (EditText)findViewById(R.id.etFechaI);
		etFechaF = (EditText)findViewById(R.id.etFechaF);
		btnBuscar = (Button)findViewById(R.id.btnBuscar);
		lv = (ListView)findViewById(R.id.lvDatos);
		
		btnFechaI.setOnClickListener(this);
		btnFechaF.setOnClickListener(this);
		btnBuscar.setOnClickListener(this);
		lv.setOnItemClickListener(this);
		
		ds = new TransitoDS(getApplicationContext());
		
		ds.open();
		
		listaAdapter = new MostrarListaCategoria(getApplicationContext(), R.layout.view_datos, datosList);
		lv.setAdapter(listaAdapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.busqueda, menu);
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
		case R.id.ibFechaI:
			id = DATE_DIALOG_ID;
			new DatePickerFragment().show(getFragmentManager(), "dialog");
			break;
			
		case R.id.ibFechaF:
			id = DATE_DIALOG_ID1;
			new DatePickerFragment1().show(getFragmentManager(), "dialog");
			break;
		case R.id.btnBuscar:
			infraccion = ds.getAllInfraccion("fecha_multa between '" + etFechaI.getText().toString() + " 00:00" + "' and '" + etFechaF.getText().toString() + " 23:59" + "'");
			idInfraccion.clear();
			datosList.clear();
			for (int i = 0; i < infraccion.size(); i++) {
				idInfraccion.add(infraccion.get(i).getId_infracciones());
				datosList.add("Placa: " + infraccion.get(i).getPlaca() + " fecha: " + infraccion.get(i).getFecha_multa() + " Agente: " + infraccion.get(i).getCve_agente() + " folio: " + infraccion.get(i).getFolio() + " estatus " + infraccion.get(i).getStat());
			}
			listaAdapter.notifyDataSetChanged();
			break;

		default:
			break;
		}
		
	}
	
	
	@SuppressLint("ValidFragment")
    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener, OnTimeSetListener {
		   

		   @Override
		   public Dialog onCreateDialog(Bundle savedInstanceState) {
			   int year = ano;
			   int month = mes;
			   int day = dia;
			   switch (id) {
			   		case DATE_DIALOG_ID:
			   			return new DatePickerDialog(getActivity(), this, year, month, day);

			   		default:
			   			return null;
			   }
			   
		   }
		   
		   private void actualizarFecha() {
			   mes+=1;
			   etFechaI.setText(new StringBuilder().append(dia).append("/") .append(mes).append("/").append(ano).toString());
			   mes-=1;
	       }
	   	
	   	/*private void actualizarHora() {
	   		String minuto;
	   		minuto = (min > -1 & min < 10) ? "0" + min : "" + min ;
	   		etHora.setText(new StringBuilder().append(hr).append(":").append(minuto).toString());
	   	}*/

		   public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			   ano = year;
			   mes = monthOfYear;
			   dia = dayOfMonth;
		       actualizarFecha();
		   }

		   @Override
		   public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			   //hr = hourOfDay;
			   //min = minute;
			   //actualizarHora();
		   }
	 }
	
	@SuppressLint("ValidFragment")
    public class DatePickerFragment1 extends DialogFragment implements DatePickerDialog.OnDateSetListener, OnTimeSetListener {
		   

		   @Override
		   public Dialog onCreateDialog(Bundle savedInstanceState) {
			   int year = ano;
			   int month = mes;
			   int day = dia;
			   switch (id) {
			   		case DATE_DIALOG_ID1:
			   			return new DatePickerDialog(getActivity(), this, year, month, day);

			   		default:
			   			return null;
			   }
			   
		   }
		   
		   private void actualizarFecha() {
			   mes+=1;
			   etFechaF.setText(new StringBuilder().append(dia).append("/") .append(mes).append("/").append(ano).toString());
			   mes-=1;
	       }
	   	
	   	/*private void actualizarHora() {
	   		String minuto;
	   		minuto = (min > -1 & min < 10) ? "0" + min : "" + min ;
	   		etHora.setText(new StringBuilder().append(hr).append(":").append(minuto).toString());
	   	}*/

		   public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			   ano = year;
			   mes = monthOfYear;
			   dia = dayOfMonth;
		       actualizarFecha();
		   }

		   @Override
		   public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			   //hr = hourOfDay;
			   //min = minute;
			   //actualizarHora();
		   }
	 }
	
	
	
	public class MostrarListaCategoria extends BaseAdapter {
		
		//private Context context;
		private int layout;
		private ArrayList<String> category = new ArrayList<String>();
		private LayoutInflater inflater;
		
		public MostrarListaCategoria(Context context, int layout, ArrayList<String> category) {
			this.layout = layout;
			this.category = category;
			
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			if (datosList.isEmpty()) 
				return 0;
			else
				return datosList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View row = convertView;
			//ImageView iv;
			TextView tv;
			
			if(row == null) {
				
				row = inflater.inflate(layout, null, false);
				
			}
			
			tv = (TextView) row.findViewById(R.id.tvCategoria);
			System.err.println(category.get(position));
			tv.setText(category.get(position));
			
			return row;
		}
		
	}



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Bundle bundle = new Bundle();
		bundle.putInt("ide", idInfraccion.get(position));
		startActivity(new Intent(getApplicationContext(), Reimprimir.class).putExtras(bundle));
	}
	
}
