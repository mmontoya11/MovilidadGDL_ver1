package com.perspective.movilidad.transito;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import com.parkimovil.contract.ParkimovilContract;
import com.perspective.movilidad.R;
import com.perspective.movilidad.core.TransitoDS;
import com.perspective.movilidad.gs.Agentes;
import com.perspective.movilidad.gs.Fotografia;
import com.perspective.movilidad.gs.Infraccion;
import com.perspective.movilidad.gs.Infracciones;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements  OnItemSelectedListener {

	//private static final String[] DUMMY_CREDENTIALS = new String[] {
			//"foo@example.com:hello", "bar@example.com:world" };
	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// UI references.
	//private AutoCompleteTextView mEmailView;
	private EditText mPasswordView;
	private View mProgressView;
	private View mLoginFormView;
	private String password,clave = "",v= "",config = "";
	private boolean status;
	private Bundle bundle = null;
	private Spinner spUser;
	private int con = 0;
	private int [] ids = null;
	private List<Infracciones> inf = new ArrayList<Infracciones>();
	private List<Agentes> agente = new ArrayList<Agentes>();
	private List<Infraccion> infraccion = new ArrayList<Infraccion>();
	private TransitoDS ds = null;
	private List<String> agentes = new ArrayList<String>();
	private List<Integer> idAgente = new ArrayList<Integer>();
	private List<String> infracciones = new ArrayList<String>();
	private List<Infraccion> infra = new ArrayList<Infraccion>();
	private Agentes ag;
	private TextView tvVersion,tvNombre;
	private SharedPreferences sp;
	private static int MY_TARGET_WRITE_PERMISSION_REQUEST = 1983;
	private int res = 0;
	private List<Fotografia> foto;



	@SuppressLint("DefaultLocale")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		if(ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
			ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.CAMERA} , 1);

		Log.e("e", "e");
		
		tvVersion = (TextView)findViewById(R.id.tvVersion);
		tvNombre = (TextView)findViewById(R.id.tvNombre);
		
		sp = getSharedPreferences("transito", Context.MODE_PRIVATE);
		config = sp.getString("numt", "");
		
		con = sp.getInt("config", 0);
		
		tvNombre.setText("Dispositivo Codigo: " + con);
		
		TypedArray arreglo = getResources().obtainTypedArray(R.array.ids);
		int longitud = arreglo.length();
		ids = new int [longitud];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = i + 1;
		}
		
		try {
			PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
			v= info.versionName;
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
		//System.out.println("version " + v);
		
		tvVersion.setText("Mov-000011.4 \n05142019");
		
		arreglo.recycle();

		// Set up the login form.
		//mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
		//populateAutoComplete();

		mPasswordView = (EditText) findViewById(R.id.etPass);
		spUser = (Spinner)findViewById(R.id.spUser);
		spUser.setOnItemSelectedListener(this);
		
		
		/*mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});*/

		Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
		mEmailSignInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});

		mLoginFormView = findViewById(R.id.login_form);
		mProgressView = findViewById(R.id.login_progress);
		
		ds = new TransitoDS(getApplicationContext());
		ds.open();
		
		/*List<String> fot = ds.getAllFotografia("a.estatus = 'N' and b.fecha_multa >= '08/05/2019'", 0);
		
		System.err.println(fot.size());
		
		for (int i = 0; i < fot.size(); i++) {
			System.err.println(fot.get(0));
		}
		
		//foto = ds.getAllFotografia(" estatus = 'N' and numero_acta = (select folio from infracciones where fecha_multa between '01/04/2019 00:00' and '22/04/2019 23:59')");
		/foto = ds.getAllFotografia(" estatus = 'N'");
		
		System.err.println(foto.size() + " foto");
		
		for (int i = 0; i < foto.size(); i++) {
			System.err.println("archivo " + foto.get(i).getArchivo() + " numero_a " + foto.get(i).getNumero_acta());
		}
		
		/*foto = ds.getAllFotografia("estatus = 'N' and a.fecha_multa >= '01/04/2019')");
		
		System.err.println(foto.size() + " total no subidos");
		
		for (int i = 0; i < foto.size(); i++) {
			foto.get(i).getNumero_acta();
		}*/
		
		//select * from infracciones WHERE estatus = 'N'
		ContentValues cv = new ContentValues();
		cv.put("estatus", "N");
		//System.err.println(ds.database.update("infracciones", cv, "fecha_multa between '11/01/2017 00:00' and '25/01/2017 23:59'", null) + " update");
		//System.err.println(ds.database.update("infracciones", cv, "folio > 3160307", null) + " update");
		
		if(validarCampo("infracciones", "entre_calle") == 0) {
			String sql = "alter table infracciones add entre_calle TEXT";
			ds.database.execSQL(sql);
		}
		if(validarCampo("infracciones", "zona") == 0) {
			String sql = "alter table infracciones add zona TEXT";
			ds.database.execSQL(sql);
		}
		
		if(validarCampo("infracciones", "estat") == 0) {
			String sql = "alter table infracciones add estat TEXT";
			ds.database.execSQL(sql);
		}
		
		List<Infraccion> in = new ArrayList<Infraccion>();
		
		
		
		//in = ds.getAllInfraccion("estatus = 'N' or estatus = '1' and fecha_multa between '03/03/2017 00:00' and '03/03/2017 23:59'");
		//in = ds.getAllInfraccion("fecha_multa between '1/3/2017 00:00' and '3/3/2017 23:59'");
		
		
		
		//System.err.println(ds.database.update("Fotografia", cv, "estatus = 'N'", null) + " update");
		
		//System.err.println(ds.database.update("infracciones", cv, "folio > '3992518' and cve_agente = '31656'", null) + " update");
		//System.err.println(ds.database.update("infracciones", cv, "folio > '3742940'", null) + " update");
		
		//in = ds.getAllInfraccion(" folio = '3742940' ");
		//in = ds.getAllInfraccion(" folio > '4095844' and cve_agente = '31906' order by id_infracciones desc");
		
		for (int j = 0; j < in.size(); j++) {
			//System.err.println(String.valueOf(in.get(j).getLatitud()) + " " + String.valueOf(in.get(j).getLongitud()));
			//System.err.println(in.get(j).getFolio() + " " + in.get(j).getPlaca() + " " + in.get(j).getFecha_multa() + " " + in.get(j).getStat());
		}
		
		/*infra = ds.getAllInfraccion(" placa like 'JHJ1129' or placa like 'JL63802' or placa like 'JMU5490' or placa like 'JLG7671' or placa like 'JLB8446' or placa like 'MCJ5122' or placa like 'JBH5972'" +
				"or placa like 'JLH8686' or placa like 'JLP9137' or placa like 'PFD306E' or placa like 'PFD 306E' ");
		
		System.err.println(infra.size());
		for (int i = 0; i < infra.size(); i++) {
			System.err.println(infra.get(0).getPlaca() + " " +infra.get(i).getFecha_multa() + " " + infra.get(i).getStat());
		}*/
		
		//System.err.println(ds.updateInfraccion("placa like 'JHJ1129' or placa like 'JL63802' or placa like 'JMU5490' or placa like 'JLG7671' or placa like 'JLB8446' or placa like 'MCJ5122' or placa like 'JBH5972'" +
				//"or placa like 'JLH8686' or placa like 'JLP9137' or placa like 'PFD306E' or placa like 'PFD 306E'"));
		if(!isTableExists("Fotografia")) {
			
			System.err.println("no existe");
			try {
				ds.database.execSQL("Create table Fotografia(id_fotografia INTEGER PRIMARY KEY AUTOINCREMENT, id_levantamiento integer,numero_acta text,archivo text,descripcion text,estatus TEXT)");
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
			
		}else 
			System.err.println("si existe");
		
		cv = new ContentValues();
		
		cv.put("estatus", "S");
		
		/*List<Fotografia> ft = new ArrayList<Fotografia>();
		
		ft = ds.getAllFotografia("estatus = 'N'");
		
		System.err.println(ft.size() + " fotos sin sub");*/
		
		/*for (int i = 0; i < ft.size(); i++) {
			System.err.println(ft.get(i).getIdInfraccion() + " id " + ft.get(i).getNumero_acta());
		}*/

		if(!isTableExists("c_tabletas")) {
			System.out.println("false");
			
			try {
				
				ds.database.execSQL("create table c_tabletas(id_c_tabletas integer PRIMARY KEY AUTOINCREMENT,tableta TEXT, usuario_tableta TEXT, capturo TEXT, fecha numeric)");
			
			} catch(SQLiteException e) {
				System.err.println(e.getMessage());
			}
		} else
			System.out.println("true");
		
		inf = ds.getAllInfracciones("");
		ds.getAllInfracciones1("");
		agente = ds.getAllAgentes("");
		infraccion = ds.getAllInfraccion("");
		
		for (int i = 0; i < inf.size(); i++) {
			//System.out.println(inf.get(i).getVigencia()  + " " + i);
			infracciones.add(inf.get(i).getDescripcion());
		}
		for (int i = 0; i < agente.size(); i++) {
			//System.out.println(agente.get(i).getCveAgente() + " " + i);
			agentes.add(agente.get(i).getCveAgente());
			//agentes.add(agente.get(i).getNombreAgente() + " " + agente.get(i).getPaternoAgente() + " " + agente.get(i).getMaternoAgente());
			idAgente.add(agente.get(i).getIdCAgentes());
		}
		
		for (int i = 0; i < infraccion.size(); i++) {
			//System.out.println(infraccion.get(i).getFecha_multa() + " " + i);
		}
		
		
		try{
			spUser.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_dropdown_item, agentes));
			//spUser.setAdapter(ArrayAdapter.createFromResource(getApplicationContext(), R.array.usuario, R.layout.simple_spinner_dropdown_item));
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		
		
		if(!isTableExists("c_colores")) {
			System.err.println("no existe");
			try {
				ds.database.execSQL("create table c_colores(id_c_colores integer,color text,capturo text, fecha numeric)");
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		} else 
			System.err.println("si existe");
		
		if(!isTableExists("c_estados")) {
			System.err.println("no existe");
			try {
				ds.database.execSQL("create table c_estados(id_c_estados integer,estado text,capturo text, fecha numeric)");
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		} else 
			System.err.println("si existe");
		
		if(!isTableExists("c_marcas")) {
			System.err.println("no existe");
			try {
				ds.database.execSQL("create table c_marcas(id_c_marcas integer,marca text,capturo text, fecha numeric)");
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		} else 
			System.err.println("si existe");
		
		/*ActivityCompat.requestPermissions(this,new String[]{"com.parquimovil.verificador.provider.permission.READ_PROVIDER"}, 
				MY_TARGET_WRITE_PERMISSION_REQUEST);*/
		
		String[] mSelectionArgs = {""};
		String mSelectionClause = ParkimovilContract.Infractions.LICENSE_PLATE + " = ?";
		//mSelectionArgs[0] = "ABC1234";
		mSelectionArgs[0] = "8E379";
		
		Cursor cursor = getContentResolver().query(ParkimovilContract.Infractions.CONTENT_URI,
                ParkimovilContract.Infractions.ALL_COLUMNS,
                mSelectionClause,
                mSelectionArgs,
                null);

        if (cursor != null) {
            Log.i("Count: ", String.valueOf(cursor.getCount()));
        }

        while (cursor != null && cursor.moveToNext()) {
        	
            System.err.println(cursor.getString(cursor.getColumnIndex(ParkimovilContract.Infractions._ID)) + "\n");
        }

        if (cursor != null) {
            cursor.close();
        }
		
		/*String[] mSelectionArgs = {""};

		// Constructs a selection clause that matches the license plate

		String mSelectionClause = ParkimovilContract.Infractions.LICENSE_PLATE + " = ?";

		// Set the license plates to query to args

		mSelectionArgs[0] = "ABC1234";

		// Queries the infractions and returns results

		Cursor mCursor = getContentResolver().query(

		ParkimovilContract.Infractions.CONTENT_URI, // The content URI of the Infractions table

		ParkimovilContract.Infractions.ALL_COLUMNS, // The columns to return for each row

		mSelectionClause, // Selection criteria

		mSelectionArgs, // Selection criteria

		null); // The sort order for the returned rows
		//ParkimovilContract.Infractions.
		if (mCursor != null) {
			while(mCursor.moveToNext()) {
				System.err.println(ParkimovilContract.Infractions._ID + " id");
			}
		}
		
		//mCursor.close();*/
		
		
	}
	
	public boolean isTableExists(String tableName) {

	    Cursor cursor = ds.database.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
	    if(cursor!=null) {
	        if(cursor.getCount()>0) {
	        	cursor.close();
	            return true;
	        }
	        cursor.close();
	    }
	    return false;
	}
	
	public int validarCampo(String tabla , String campo){
		Cursor c = ds.database.rawQuery("SELECT * FROM " + tabla, null);
		try {
			if(c != null){
				//if (c.moveToFirst()) {
				if(c.getColumnIndex(campo) > 0)
					return c.getColumnIndex(campo);
				//}
				
			}
		} catch (SQLiteException e) {
			Log.e("SQLite", e.getMessage());
		}
		return 0;
		
	}

	/*private void populateAutoComplete() {
		if (VERSION.SDK_INT >= 14) {
			// Use ContactsContract.Profile (API 14+)
			getLoaderManager().initLoader(0, null, this);
		} else if (VERSION.SDK_INT >= 8) {
			// Use AccountManager (API 8+)
			new SetupEmailAutoCompleteTask().execute(null, null);
		}
	}*/

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		//mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		//String email = mEmailView.getText().toString();
		password = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password, if the user entered one.
		if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

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

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/*private boolean isEmailValid(String email) {
		return email.contains("@");
	}*/

	private boolean isPasswordValid(String password) {
		return password.length() > 0;
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});

			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mProgressView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mProgressView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/*@Override
	public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
		return new CursorLoader(this,
				// Retrieve data rows for the device user's 'profile' contact.
				Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
						ContactsContract.Contacts.Data.CONTENT_DIRECTORY),
				ProfileQuery.PROJECTION,

				// Select only email addresses.
				ContactsContract.Contacts.Data.MIMETYPE + " = ?",
				new String[] { ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE },

				// Show primary email addresses first. Note that there won't be
				// a primary email address if the user hasn't specified one.
				ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
		List<String> emails = new ArrayList<String>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			emails.add(cursor.getString(ProfileQuery.ADDRESS));
			cursor.moveToNext();
		}

		//addEmailsToAutoComplete(emails);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> cursorLoader) {

	}*/

	/*private interface ProfileQuery {
		String[] PROJECTION = { ContactsContract.CommonDataKinds.Email.ADDRESS,
				ContactsContract.CommonDataKinds.Email.IS_PRIMARY, };

		int ADDRESS = 0;
		int IS_PRIMARY = 1;
	}*/

	/**
	 * Use an AsyncTask to fetch the user's email addresses on a background
	 * thread, and update the email text field with results on the main UI
	 * thread.
	 */
	/*class SetupEmailAutoCompleteTask extends
			AsyncTask<Void, Void, List<String>> {

		@Override
		protected List<String> doInBackground(Void... voids) {
			ArrayList<String> emailAddressCollection = new ArrayList<String>();

			// Get all emails from the user's contacts and copy them to a list.
			ContentResolver cr = getContentResolver();
			Cursor emailCur = cr.query(
					ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
					null, null, null);
			while (emailCur.moveToNext()) {
				String email = emailCur
						.getString(emailCur
								.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
				emailAddressCollection.add(email);
			}
			emailCur.close();

			return emailAddressCollection;
		}

		@Override
		protected void onPostExecute(List<String> emailAddressCollection) {
			addEmailsToAutoComplete(emailAddressCollection);
		}
	}*/

	/*private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
		// Create adapter to tell the AutoCompleteTextView what to show in its
		// dropdown list.
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				LoginActivity.this,
				android.R.layout.simple_dropdown_item_1line,
				emailAddressCollection);

		mEmailView.setAdapter(adapter);
	}*/

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {

			try {
				// Simulate network access.
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				return false;
			}
			
			ag = ds.ingresar(spUser.getSelectedItem().toString(), mPasswordView.getText().toString());
			if(ag != null) {
				status = true;
			}
			
			/*if(password.equals("1"))
				status = true;
			else
				status = false;*/

			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				if(status) {
					bundle = new Bundle();
					bundle.putString("us", spUser.getSelectedItem().toString());
					//bundle.putInt("id", ids[spUser.getSelectedItemPosition()]);
					bundle.putString("clave", agentes.get(spUser.getSelectedItemPosition()));
					bundle.putInt("id", idAgente.get(spUser.getSelectedItemPosition()));
					bundle.putString("user", spUser.getSelectedItem().toString());
					startActivity(new Intent(getApplicationContext(), Menu.class).putExtras(bundle));
					onDestroy();
				}
				else {
					mPasswordView.setError(getString(R.string.error_incorrect_password));
					mPasswordView.requestFocus();
				}
			} else {
				mPasswordView.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
		switch (parent.getId()) {
		case R.id.spUser:
			//System.out.println("spinner");
			clave = agentes.get(position);
			System.out.println(clave);
			break;

		default:
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.finish();
	}
	
}
