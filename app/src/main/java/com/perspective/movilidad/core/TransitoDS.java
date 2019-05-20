package com.perspective.movilidad.core;

import java.util.ArrayList;
import java.util.List;

import com.perspective.movilidad.gs.Agentes;
import com.perspective.movilidad.gs.Colores;
import com.perspective.movilidad.gs.Estados;
import com.perspective.movilidad.gs.Fotografia;
import com.perspective.movilidad.gs.Infraccion;
import com.perspective.movilidad.gs.Infracciones;
import com.perspective.movilidad.gs.Marcas;
import com.perspective.movilidad.gs.Placas;
import com.perspective.movilidad.gs.Tabletas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import android.util.Log;

public class TransitoDS {
	
	public  SQLiteDatabase database;
	private GestionBD bdHelper;
	private ContentValues cv = null;
	private Agentes agente;
	
	public TransitoDS(Context context) {
		bdHelper = new GestionBD(context);
	}
	
	public void open() throws SQLiteException {
		database = bdHelper.openDataBase();
	}
	
	public void close() {
		if (bdHelper != null) {
			database.close();
			bdHelper.close();
		}
	}
	
	public Agentes ingresar(String user,String pass) {
		try {
			String sql = "SELECT * FROM c_agentes WHERE trim(cve_agente) = '" + user.trim() + "' ";
			System.out.println(sql);
			Cursor cursor = database.rawQuery(sql,null);
			System.out.println(cursor.getCount());
			System.out.println(cursor.getColumnIndex("password"));
			if (database!=null) {
				if(cursor.moveToFirst()) {
					do {
						System.out.println("*" + cursor.getString(7).trim() + "*");
						System.out.println("*" + pass.trim() + "*");
						System.out.println(cursor.getString(7).trim().equalsIgnoreCase(pass));
						if(cursor.getString(1).trim().equalsIgnoreCase(user.trim()) & cursor.getString(7).trim().equalsIgnoreCase(pass)) {
							agente = cursorToAgentes(cursor);
						}
						else {
							agente = null;
						}
					}while (cursor.moveToNext());
				}
				cursor.close();
			}
		}catch (Exception e) {
			Log.e("Error", e.getMessage());
		}
		System.out.println(agente == null);
		return agente;
		
	}
	
	public long crearAgente(String cve_agente,String paterno_agente,String materno_agente,String nombre_agente,String capturo,String fecha) {
		
		cv = new ContentValues();
		
		cv.put("cve_agente", cve_agente);
		cv.put("paterno_agente",paterno_agente);
		cv.put("materno_agente", materno_agente);
		cv.put("nombre_agente", nombre_agente);
		cv.put("capturo", capturo);
		cv.put("fecha", fecha);
		
		return database.insert("c_agentes", null, cv);
		
	}
	
	public long crearCInfraciones(String cve_infraccion,String descripcion,double importe_infraccion,String vigencia,String capturo,String fecha, String estacionometros) {
		
		cv = new ContentValues();
		
		cv.put("cve_infraccion", cve_infraccion);
		cv.put("descripcion",descripcion);
		cv.put("importe_infraccion", importe_infraccion);
		cv.put("vigencia", vigencia);
		cv.put("capturo", capturo);
		cv.put("fecha", fecha);
		cv.put("estacionometros", estacionometros);
		
		return database.insert("c_infracciones", null, cv);
		
	}
	
	//serie_infracciones ,observaciones ,id_c_infracciones , marca , tipo ,color ,procedencia ,domicilio_conductor ,colonia_conductor ,ciudad_conductor ,licencia_conductor ,tipo_licencia ,lugar_infraccion ,nombre_propietario ,domicilio_propietario ,colonia_propietario ,ciudad_propietario ,ciudad_propietario ,articulos ,observacion_tablet
	public long crearInfraciones(String peterno_infraccion,String materno_infraccion,String nombres_infraccion,String fecha_multa,String cve_infraccion,String hora_infraccion, String cve_agente,String serie_infracciones,String observaciones,int id_c_infracciones,String marca ,String tipo,String color,String procedencia,String domicilio_conductor,String colonia_conductor,String ciudad_conductor,String licencia_conductor,String tipo_licencia,String lugar_infraccion,String nombre_propietario,String domicilio_propietario,String colonia_propietario,String ciudad_propietario,String articulos,String observacion_tablet, double latitud, double longitud, String placa, int id_placa, String folio, String linea, String modelo, String estatus, String vigencia, String cve_infraccion1, String cve_infraccion2, String cve_infraccion3, String cve_infraccion4,String cve_infraccion5,String entre_calle,String zona,String estat) {
		
		cv = new ContentValues();
		
		cv.put("peterno_infraccion", peterno_infraccion);
		cv.put("materno_infraccion",materno_infraccion);
		cv.put("nombres_infraccion", nombres_infraccion);
		cv.put("fecha_multa", fecha_multa);
		cv.put("cve_infraccion", cve_infraccion);
		cv.put("hora_infraccion", hora_infraccion);
		cv.put("cve_agente", cve_agente);
		cv.put("serie_infracciones",serie_infracciones);
		cv.put("observaciones", observaciones);
		cv.put("id_c_infracciones", id_c_infracciones);
		cv.put("marca", marca);
		cv.put("tipo", tipo);
		cv.put("color", color);
		cv.put("cve_agente", cve_agente);
		cv.put("procedencia",procedencia);
		cv.put("domicilio_conductor", domicilio_conductor);
		cv.put("colonia_conductor", colonia_conductor);
		cv.put("ciudad_conductor", ciudad_conductor);
		cv.put("licencia_conductor", licencia_conductor);
		cv.put("cve_infraccion", cve_infraccion);
		cv.put("tipo_licencia",tipo_licencia);
		cv.put("lugar_infraccion", lugar_infraccion);
		cv.put("nombre_propietario", nombre_propietario);
		cv.put("domicilio_propietario", domicilio_propietario);
		cv.put("colonia_propietario", colonia_propietario);
		cv.put("ciudad_propietario", ciudad_propietario);
		cv.put("ciudad_propietario", ciudad_propietario);
		cv.put("articulos", articulos);
		cv.put("observacion_tablet", observacion_tablet);
		cv.put("latitud", latitud);
		cv.put("longitud", longitud);
		cv.put("placa", placa);
		cv.put("idPlaca", id_placa);
		cv.put("folio", folio);
		cv.put("linea", linea);
		cv.put("modelo", modelo);
		cv.put("estatus", estatus);
		cv.put("vigencia", vigencia);
		cv.put("cve_infraccion1", cve_infraccion1);
		cv.put("cve_infraccion2", cve_infraccion2);
		cv.put("cve_infraccion3", cve_infraccion3);
		cv.put("cve_infraccion4", cve_infraccion4);
		cv.put("cve_infraccion5", cve_infraccion5);
		cv.put("entre_calle", entre_calle);
		cv.put("zona", zona);
		cv.put("estat", estat);
		
		return database.insert("infracciones", null, cv);
		
	}
	
	
	
	public boolean crearFotografia(Fotografia foto) {
		cv = new ContentValues();
		
		cv.put("numero_acta", foto.getNumero_acta());
		cv.put("archivo", foto.getArchivo());
		cv.put("estatus", String.valueOf(foto.getEstatus()));
		
		return database.insert("Fotografia", null, cv) > 0;
	}
	
	public boolean crearPlaca(Placas placa) {
		
		cv = new ContentValues();
		
		cv.put("placa", placa.getPlaca());
		cv.put("propie_placas", placa.getPropiePlaca());
		cv.put("direccion_placas", placa.getDireccion());
		cv.put("colonia_placas", placa.getColonia());
		cv.put("serie", placa.getSerie());
		cv.put("marca", placa.getMarca());
		cv.put("tipo", placa.getTipo());
		cv.put("linea", placa.getLinea());
		cv.put("color", placa.getColor());
		cv.put("axo_modelo", placa.getAxo());
		
		return database.insert("placas", null, cv) > 0;
		
	}
	
	public List<Agentes> getAllAgentes(String condicion) {
		if (TextUtils.isEmpty(condicion))
			condicion = "1=1";
		List<Agentes> agente = new ArrayList<Agentes>();
		String sql = "select * from c_agentes where " + condicion + " order by trim(cve_agente) asc";
		System.out.println(sql);
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				Agentes agentes = cursorToAgentes(cursor);
				agente.add(agentes);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return agente;
	}
	
	public int getAllAgentes1(String condicion) {
		if (TextUtils.isEmpty(condicion))
			condicion = "1=1";
		int boleta = 0;
		String sql = "select MAX(folio) as folio from infracciones where " + condicion;
		System.out.println(sql);
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				boleta = cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return boleta;
	}
	
	public String getAllAgentes2(String condicion) {
		if (TextUtils.isEmpty(condicion))
			condicion = "1=1";
		String  fecha = "";
		String sql = "select fecha_multa from infracciones where " + condicion;
		System.out.println(sql);
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				fecha = cursor.getString(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return fecha;
	}
	
	public List<Infracciones> getAllInfracciones(String condicion) {
		if (TextUtils.isEmpty(condicion))
			condicion = "1=1";
		List<Infracciones> infra = new ArrayList<Infracciones>();
		String sql = "select * from c_infracciones where " + condicion +  " order by trim(cve_infraccion) asc";
		System.out.println(sql);
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				Infracciones infracciones = cursorToInfracciones(cursor);
				infra.add(infracciones);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return infra;
	}
	
	public int updateInfraccion(String condicion) {
		if (TextUtils.isEmpty(condicion))
			condicion = "1=1";
		String sql = "update infracciones set estatus='N' where " + condicion;
		System.out.println(sql);
		//System.out.println(condicion);
		
		
		
		ContentValues cv = new ContentValues();
		
		cv.put("estatus", "N");
		
		return database.update("infracciones", cv, condicion, null);

	}
	
	public List<String> getAllInfraccionesColumn(String condicion) {
		if (TextUtils.isEmpty(condicion))
			condicion = "1=1";
		List<String> infra = new ArrayList<String>();
		String sql = "select * from c_infracciones where " + condicion +  " order by trim(cve_infraccion) asc";
		System.out.println(sql);
		Cursor cursor = database.rawQuery(sql, null);
		
		for (int i = 0; i < cursor.getColumnCount(); i++) {
			infra.add(cursor.getColumnName(i));
		}
		cursor.close();
		return infra;
	}
	
	public void getAllInfracciones1(String condicion) {
		if (TextUtils.isEmpty(condicion))
			condicion = "1=1";
		String sql = "select * from infracciones where " + condicion;
		System.out.println(sql);
		Cursor cursor = database.rawQuery(sql, null);
		
		for (int i = 0; i < cursor.getColumnCount(); i++) {
			System.out.println(cursor.getColumnName(i) + " " + i);
		}
		
		/*if (cursor.moveToFirst()) {
			do {
				
			} while (cursor.moveToNext());
		}*/
		cursor.close();
	}
	
	public List<Infraccion> getAllInfraccion(String condicion) {
		if (TextUtils.isEmpty(condicion))
			condicion = "1=1";
		List<Infraccion> infraccion = new ArrayList<Infraccion>();
		String sql = "select * from infracciones where " + condicion;
		System.out.println(sql);
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				Infraccion infracciones = cursorToInfraccion(cursor);
				infraccion.add(infracciones);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return infraccion;
	}
	
	public List<Placas> getAllPlacas(String condicion) {
		if (TextUtils.isEmpty(condicion))
			condicion = "1=1";
		List<Placas> placa = new ArrayList<Placas>();
		String sql = "select * from placas where " + condicion;
		System.out.println(sql);
		Cursor cursor = database.rawQuery(sql, null);
		System.err.println(cursor.getCount());
		if (cursor.moveToFirst()) {
			do {
				Placas placas = cursorToPlacas(cursor);
				placa.add(placas);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return placa;
	}
	
	
	public int getCountPlacas(String condicion) {
		
		if (TextUtils.isEmpty(condicion))
			condicion = "1=1";
		String sql = "select * from placas where " + condicion;
		System.out.println(sql);
		Cursor cursor = database.rawQuery(sql, null);
		System.err.println(cursor.getCount());
		
		cursor.close();
		return cursor.getCount();
		
	}
	
	public List<Tabletas> getAllTabletas(String condicion) {
		if (TextUtils.isEmpty(condicion))
			condicion = "1=1";
		List<Tabletas> tableta = new ArrayList<Tabletas>();
		String sql = "select * from c_tabletas where " + condicion;
		System.out.println(sql);
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				Tabletas tabletas = cursorToTabletas(cursor);
				tableta.add(tabletas);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return tableta;
	}
	
	public List<Agentes> getAllFolio(String condicion) {
		if (TextUtils.isEmpty(condicion))
			condicion = "1=1";
		List<Agentes> agente = new ArrayList<Agentes>();
		String sql = "select folio_minimo,folio_maximo from c_agentes where " + condicion;
		System.out.println(sql);
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				Agentes agentes = cursorToAgenteFolio(cursor);
				agente.add(agentes);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return agente;
	}
	
	public int validarCampo(String tabla , String campo){
		
		String sql = "SELECT * FROM " + tabla;
		System.err.println(sql);
		Cursor cursor = database.rawQuery(sql, null);
		try {
			if(database != null){
				//if (c.moveToFirst()) {
				if(cursor.getColumnIndex(campo) > 0)
					return cursor.getColumnIndex(campo);
				//}
				
			}
		} catch (SQLiteException e) {
			Log.e("SQLite", e.getMessage());
		}
		finally {
			cursor.close();
		}
		return 0;
		
	}
	
	public List<Infraccion> getUltimoinfracciones(String condicion) {
		if(TextUtils.isEmpty(condicion))
			condicion = "1=1";
		List<Infraccion> infracciones = new ArrayList<Infraccion>();
		String sql = "select * from infracciones where " + condicion + " ORDER BY id_infracciones DESC LIMIT 1";
		//String sql = "select * from infracciones where " + condicion + " ORDER BY id_infracciones DESC LIMIT 1";
		System.out.println(sql);
		Cursor cursor = database.rawQuery(sql, null);
		if(cursor.moveToFirst()) {
			do {
				Infraccion infraccion = cursorToInfraccion(cursor);
				infracciones.add(infraccion);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return infracciones;
	}
	
	public List<Fotografia> getAllFotografia(String condicion) {
		if (TextUtils.isEmpty(condicion))
			condicion = "1=1";
		List<Fotografia> foto = new ArrayList<Fotografia>();
		String sql = "select * from Fotografia where " + condicion + " order by id_fotografia desc";
		System.out.println(sql);
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				Fotografia fotos = cursorToFotografia(cursor);
				foto.add(fotos);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return foto;
	}
	
	public List<String> getAllFotografia(String condicion,int pruebas) {
		if (TextUtils.isEmpty(condicion))
			condicion = "1=1";
		List<String> foto = new ArrayList<String>();
		
		String sql = "select numero_acta,fecha_multa from Fotografia a" +
				" join infracciones b on a.numero_acta = b.folio where " + condicion;
		System.out.println(sql);
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				foto.add("numero " + cursor.getString(0) + " fecha " + cursor.getString(1));
			} while (cursor.moveToNext());
		}
		cursor.close();
		return foto;
	}
	
	public List<Marcas> getAllMarca(String condicion) {
		if(TextUtils.isEmpty(condicion))
			condicion = "1=1";
		List<Marcas> marca = new ArrayList<Marcas>();
		String sql = "select * from c_marcas where " + condicion;
		System.out.println(sql);
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				Marcas marcas = cursorToMarcas(cursor);
				marca.add(marcas);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return marca;
	}
	
	public List<Estados> getAllEstado(String condicion) {
		if(TextUtils.isEmpty(condicion))
			condicion = "1=1";
		List<Estados> estado = new ArrayList<Estados>();
		String sql = "select * from c_estados where " + condicion;
		System.out.println(sql);
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				Estados estados = cursorToEstados(cursor);
				estado.add(estados);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return estado;
	}
	
	public List<Colores> getAllColor(String condicion) {
		if(TextUtils.isEmpty(condicion))
			condicion = "1=1";
		List<Colores> color = new ArrayList<Colores>();
		String sql = "select * from c_colores where " + condicion;
		System.out.println(sql);
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				Colores colores = cursorToColores(cursor);
				color.add(colores);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return color;
	}
	
	public Colores cursorToColores(Cursor cursor)  {
		return new Colores(cursor.getInt(0), cursor.getString(1));
	}
	
	public Estados cursorToEstados(Cursor cursor) {
		return new Estados(cursor.getInt(0), cursor.getString(1));
	}
	
	public Marcas cursorToMarcas(Cursor cursor) {
		return new Marcas(cursor.getInt(0), cursor.getString(1));
	}
	
	public Fotografia cursorToFotografia(Cursor cursor) {
		return new Fotografia(cursor.getInt(0), cursor.getString(2), cursor.getString(3), cursor.getString(5).charAt(0));
	}
	
	public Placas cursorToPlacas(Cursor cursor) {
		return new Placas(cursor.getInt(0),cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getInt(9), cursor.getString(cursor.getColumnIndex("paterno_placas")), cursor.getString(cursor.getColumnIndex("materno_placas")),cursor.getString(cursor.getColumnIndex("nombres_placas")), cursor.getString(cursor.getColumnIndex("color")));
	}
	
	public Agentes cursorToAgentes(Cursor cursor) {
		//System.err.println(cursor.getString(2) + " " +cursor.getString(3) + cursor.getString(cursor.getColumnIndex("nombres_agente")));
		if(cursor.getColumnIndex("nombres_agente")>=0)
			return new Agentes(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(cursor.getColumnIndex("nombres_agente")), cursor.getString(5), cursor.getString(6), cursor.getString(7));
		else
			return new Agentes(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
	}
	public Agentes cursorToAgenteFolio(Cursor cursor) {
		return new Agentes(cursor.getString(0), cursor.getString(1));
	}
	
	public Infracciones cursorToInfracciones(Cursor cursor) {
		//return new Infracciones(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getDouble(3), cursor.getString(8), cursor.getInt(10), cursor.getDouble(9));
		return new Infracciones(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getDouble(3), cursor.getString(7));
	}
	
	public Infraccion cursorToInfraccion(Cursor cursor) {	
		// paterno_infraccion, materno_infraccion, nombres_infraccion, fecha_multa, cve_infraccion, hora_infraccion, cve_agente, serie_infracciones, observaciones, marca, tipo, color, procedencia, domicilio_conductor, colonia_conductor, ciudad_conductor, licencia_conductor, tipo_lic, lugar_infaccion, nombre_propietario, domicilio_propietario, colonia_propietario, ciudad_propietario, articulos, observaciones_tablet, c_id_infracciones
		//peterno_infraccion 1,materno_infraccion 2,nombres_infraccion 3,fecha_multa 4,cve_infraccion 5,hora_infraccion 6,cve_agente 7,serie_infracciones 8,observaciones 9,id_c_infracciones 10, marca 11, tipo 12,color 13,procedencia 14,domicilio_conductor 15,colonia_conductor 16,ciudad_conductor 17,licencia_conductor 18,tipo_licencia 19,lugar_infraccion 20,nombre_propietario 21,domicilio_propietario 22,colonia_propietario 23,ciudad_propietario 24,articulos 25,observacion_tablet 26 
		return new Infraccion(cursor.getInt(0),cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(11), cursor.getString(12), cursor.getString(13), cursor.getString(14), cursor.getString(15), cursor.getString(16), cursor.getString(17), cursor.getString(18), cursor.getString(19), cursor.getString(20), cursor.getString(21), cursor.getString(22), cursor.getString(23), cursor.getString(24), cursor.getString(25), cursor.getString(26), cursor.getInt(10), cursor.getDouble(cursor.getColumnIndex("latitud")), cursor.getDouble(cursor.getColumnIndex("longitud")), cursor.getString(cursor.getColumnIndex("linea")), cursor.getString(cursor.getColumnIndex("vigencia")), cursor.getString(cursor.getColumnIndex("cve_infraccion1")),cursor.getString(cursor.getColumnIndex("cve_infraccion2")),cursor.getString(cursor.getColumnIndex("cve_infraccion3")),cursor.getString(cursor.getColumnIndex("cve_infraccion4")),cursor.getString(cursor.getColumnIndex("cve_infraccion5")),cursor.getString(cursor.getColumnIndex("placa")),cursor.getString(cursor.getColumnIndex("folio")),cursor.getString(cursor.getColumnIndex("modelo")),cursor.getString(cursor.getColumnIndex("entre_calle")),cursor.getString(cursor.getColumnIndex("zona")),cursor.getString(cursor.getColumnIndex("estat")));
	}
	
	public Tabletas cursorToTabletas(Cursor cursor) {	
		return new Tabletas(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
	}

}
