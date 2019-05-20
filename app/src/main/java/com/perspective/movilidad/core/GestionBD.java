package com.perspective.movilidad.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class GestionBD extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "transito.db";
	private static final int DATABASE_VERSION = 1;
	public SQLiteDatabase mDB;
	//private Context context;
	
	private String tablaInfracciones = "CREATE TABLE infracciones(id_infracciones integer PRIMARY KEY AUTOINCREMENT,peterno_infraccion text,materno_infraccion text,nombres_infraccion text,fecha_multa text,cve_infraccion text,hora_infraccion text,cve_agente text,serie_infracciones text,observaciones text,id_c_infracciones integer, marca text, tipo text,color text,procedencia text,domicilio_conductor text,colonia_conductor text,ciudad_conductor text,licencia_conductor text,tipo_licencia text,lugar_infraccion text,nombre_propietario text,domicilio_propietario text,colonia_propietario text,ciudad_propietario text,articulos text,observacion_tablet text,latitud real,longitud real,placa TEXT, idPlaca INTEGER, folio TEXT,linea TEXT,modelo Text,estatus Text,vigencia Text,cve_infraccion1 text,cve_infraccion2 text,cve_infraccion3 text,cve_infraccion4 text,cve_infraccion5 text)";
	private String tablaAgentes = "CREATE TABLE c_agentes(id_c_agentes integer PRIMARY KEY AUTOINCREMENT,cve_agente text,paterno_agente text,materno_agente text,nombres_agente text,capturo text,fecha numeric,password text)";
	private String tablaCInfracciones = "CREATE TABLE c_infracciones(id_c_infracciones integer PRIMARY KEY AUTOINCREMENT,cve_infraccion text,descripcion text,importe_infraccion real,vigencia text,capturo text,fecha numeric,articulos text)";
	private String tablaPlacas = "CREATE TABLE placas(id_placa integer,placa text,propie_placas text,direccion_placas real,colonia_placas text,serie text,marca text,tipo text,linea text,axo_modelo integer)";
	//private String tablaPlacas = "CREATE TABLE placas(id_placa integer,placa text,paternoPlaca TEXT, maternoPlaca TEXT, nombrePlaca TEXT,propie_placas text,direccion_placas real,colonia_placas text,serie text,marca text,tipo text,linea text,axo_modelo integer)";
	
	public GestionBD(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	public GestionBD(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(tablaInfracciones);
			db.execSQL(tablaAgentes);
			db.execSQL(tablaCInfracciones);
			db.execSQL(tablaPlacas);
			
			crearUsuario(db);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	public void crearUsuario(SQLiteDatabase db) {
		
		ContentValues cv = new ContentValues();
		
		cv.put("cve_agente", "Administrador");
		cv.put("paterno_agente", "del");
		cv.put("materno_agente", "Sistema");
		cv.put("nombres_agente", "Administrador");
		cv.put("capturo", "pgm");
		cv.put("fecha", "20/07/2015");
		cv.put("password", "1");
		
		db.insert("c_agentes", null, cv);
		
		cv.put("cve_agente", "Sistemas");
		cv.put("paterno_agente", "del");
		cv.put("materno_agente", "Sistema");
		cv.put("nombres_agente", "Sistemas");
		cv.put("capturo", "pgm");
		cv.put("fecha", "20/01/2016");
		cv.put("password", "1");
		
		db.insert("c_agentes", null, cv);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	@Override
	public synchronized void close() {
		if (mDB != null) {
			mDB.close();
		}
	}
	
	public SQLiteDatabase openDataBase() throws SQLException {
		
		mDB = this.getReadableDatabase();
        // Open the database
        /*String myPath = DB_PATH + DATABASE_NAME;
        mDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);*/
        return mDB;
    }

}
