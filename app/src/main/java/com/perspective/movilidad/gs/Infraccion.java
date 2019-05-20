package com.perspective.movilidad.gs;

public class Infraccion {
	
	private String paterno_infraccion,materno_infraccion,nombres_infraccion,fecha_multa,cve_infraccion,hora_infraccion,cve_agente,serie_infracciones,observaciones,marca,tipo,color,procedencia,domicilio_conductor,colonia_conductor,ciudad_conductor,licencia_conductor,tipo_lic,lugar_infaccion,nombre_propietario,domicilio_propietario,colonia_propietario,ciudad_propietario,articulos,observaciones_tablet,linea,vigencia,cve_infraccion1,cve_infraccion2,cve_infraccion3,cve_infraccion4,cve_infraccion5,lat,lon,placa,folio,modelo,entre_calle,zona,stat;
	private int id_infracciones,c_id_infracciones;
	private double latitud, longitud;
	
	public Infraccion() {
		this.id_infracciones = 0;
		this.paterno_infraccion = "";
		this.materno_infraccion = "";
		this.nombres_infraccion = "";
		this.fecha_multa = "";
		this.cve_infraccion = "";
		this.hora_infraccion = "";
		this.cve_agente = "";
		this.serie_infracciones = "";
		this.observaciones = "";
		this.marca = "";
		this.tipo = "";
		this.color = "";
		this.procedencia = "";
		this.domicilio_conductor = "";
		this.colonia_conductor = "";
		this.ciudad_conductor = "";
		this.licencia_conductor = "";
		this.tipo_lic = "";
		this.lugar_infaccion = "";
		this.nombre_propietario = "";
		this.domicilio_propietario = "";
		this.colonia_propietario = "";
		this.ciudad_propietario = "";
		this.articulos = "";
		this.observaciones_tablet = "";
		this.c_id_infracciones = 0;
		this.latitud  = 0d;
		this.longitud = 0d;
		this.linea = "";
		this.vigencia = "";
		this.cve_infraccion1 = "";
		this.cve_infraccion2 = "";
		this.cve_infraccion3 = "";
		this.cve_infraccion4 = "";
		this.cve_infraccion5 = "";
		this.lat = "";
		this.lon = "";
		this.placa = "";
		this.folio = "";
		this.modelo = "";
		this.entre_calle = "";
		this.zona = "";
		this.stat = "";
	}
	
	public Infraccion(int id_infracciones,String paterno_infraccion,String materno_infraccion,String nombres_infraccion,String fecha_multa,String cve_infraccion,String hora_infraccion,String cve_agente,String serie_infracciones,String observaciones,String marca,String tipo,String color,String procedencia,String domicilio_conductor,String colonia_conductor,String ciudad_conductor,String licencia_conductor,String tipo_lic,String lugar_infaccion,String nombre_propietario,String domicilio_propietario,String colonia_propietario,String ciudad_propietario,String articulos,String observaciones_tablet,int c_id_infracciones,double latitud, double longitud,String linea,String vigencia,String cve_infraccion1,String cve_infraccion2, String cve_infraccion3, String cve_infraccion4, String cve_infraccion5,String placa,String folio,String modelo,String entre_calle,String zona,String stat) {
		this.id_infracciones = id_infracciones;
		this.paterno_infraccion = paterno_infraccion;
		this.materno_infraccion = materno_infraccion;
		this.nombres_infraccion  = nombres_infraccion;
		this.fecha_multa = fecha_multa;
		this.cve_infraccion = cve_infraccion;
		this.hora_infraccion = hora_infraccion;
		this.cve_agente = cve_agente;
		this.serie_infracciones = serie_infracciones;
		this.observaciones = observaciones;
		this.marca = marca;
		this.tipo = tipo;
		this.color = color;
		this.procedencia = procedencia;
		this.domicilio_conductor = domicilio_conductor;
		this.colonia_conductor = colonia_conductor;
		this.ciudad_conductor = ciudad_conductor;
		this.licencia_conductor = licencia_conductor;
		this.tipo_lic = tipo_lic;
		this.lugar_infaccion = lugar_infaccion;
		this.nombre_propietario = nombres_infraccion;
		this.domicilio_propietario = domicilio_propietario;
		this.colonia_propietario = colonia_propietario;
		this.ciudad_propietario = ciudad_propietario;
		this.articulos = articulos;
		this.observaciones_tablet = observaciones;
		this.c_id_infracciones = c_id_infracciones;
		this.latitud  = latitud;
		this.longitud = longitud;
		this.linea = linea;
		this.vigencia = vigencia;
		this.cve_infraccion1 = cve_infraccion1;
		this.cve_infraccion2 = cve_infraccion2;
		this.cve_infraccion3 = cve_infraccion3;
		this.cve_infraccion4 = cve_infraccion4;
		this.cve_infraccion5 = cve_infraccion5;
		this.placa = placa;
		this.folio = folio;
		this.modelo = modelo;
		this.entre_calle = entre_calle;
		this.zona = zona;
		this.stat = stat;
	}
	
	public Infraccion(int id_infracciones,String paterno_infraccion,String materno_infraccion,String nombres_infraccion,String fecha_multa,String cve_infraccion,String hora_infraccion,String cve_agente,String serie_infracciones,String observaciones,String marca,String tipo,String color,String procedencia,String domicilio_conductor,String colonia_conductor,String ciudad_conductor,String licencia_conductor,String tipo_lic,String lugar_infaccion,String nombre_propietario,String domicilio_propietario,String colonia_propietario,String ciudad_propietario,String articulos,String observaciones_tablet,int c_id_infracciones,String latitud, String longitud,String linea,String vigencia,String cve_infraccion1,String cve_infraccion2, String cve_infraccion3, String cve_infraccion4, String cve_infraccion5,String placa,String folio,String modelo,String entre_calle,String zona,String stat) {
		this.id_infracciones = id_infracciones;
		this.paterno_infraccion = paterno_infraccion;
		this.materno_infraccion = materno_infraccion;
		this.nombres_infraccion  = nombres_infraccion;
		this.fecha_multa = fecha_multa;
		this.cve_infraccion = cve_infraccion;
		this.hora_infraccion = hora_infraccion;
		this.cve_agente = cve_agente;
		this.serie_infracciones = serie_infracciones;
		this.observaciones = observaciones;
		this.marca = marca;
		this.tipo = tipo;
		this.color = color;
		this.procedencia = procedencia;
		this.domicilio_conductor = domicilio_conductor;
		this.colonia_conductor = colonia_conductor;
		this.ciudad_conductor = ciudad_conductor;
		this.licencia_conductor = licencia_conductor;
		this.tipo_lic = tipo_lic;
		this.lugar_infaccion = lugar_infaccion;
		this.nombre_propietario = nombres_infraccion;
		this.domicilio_propietario = domicilio_propietario;
		this.colonia_propietario = colonia_propietario;
		this.ciudad_propietario = ciudad_propietario;
		this.articulos = articulos;
		this.observaciones_tablet = observaciones;
		this.c_id_infracciones = c_id_infracciones;
		this.lat  = latitud;
		this.lon = longitud;
		this.linea = linea;
		this.vigencia = vigencia;
		this.cve_infraccion1 = cve_infraccion1;
		this.cve_infraccion2 = cve_infraccion2;
		this.cve_infraccion3 = cve_infraccion3;
		this.cve_infraccion4 = cve_infraccion4;
		this.cve_infraccion5 = cve_infraccion5;
		this.placa = placa;
		this.folio = folio;
		this.modelo = modelo;
		this.entre_calle = entre_calle;
		this.zona = zona;
		this.stat = stat;
	}

	public String getPaterno_infraccion() {
		return paterno_infraccion;
	}

	public void setPaterno_infraccion(String paterno_infraccion) {
		this.paterno_infraccion = paterno_infraccion;
	}

	public String getMaterno_infraccion() {
		return materno_infraccion;
	}

	public void setMaterno_infraccion(String materno_infraccion) {
		this.materno_infraccion = materno_infraccion;
	}

	public String getNombres_infraccion() {
		return nombres_infraccion;
	}

	public void setNombres_infraccion(String nombres_infraccion) {
		this.nombres_infraccion = nombres_infraccion;
	}

	public String getFecha_multa() {
		return fecha_multa;
	}

	public void setFecha_multa(String fecha_multa) {
		this.fecha_multa = fecha_multa;
	}

	public String getCve_infraccion() {
		return cve_infraccion;
	}

	public void setCve_infraccion(String cve_infraccion) {
		this.cve_infraccion = cve_infraccion;
	}

	public String getHora_infraccion() {
		return hora_infraccion;
	}

	public void setHora_infraccion(String hora_infraccion) {
		this.hora_infraccion = hora_infraccion;
	}

	public String getCve_agente() {
		return cve_agente;
	}

	public void setCve_agente(String cve_agente) {
		this.cve_agente = cve_agente;
	}

	public String getSerie_infracciones() {
		return serie_infracciones;
	}

	public void setSerie_infracciones(String serie_infracciones) {
		this.serie_infracciones = serie_infracciones;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getProcedencia() {
		return procedencia;
	}

	public void setProcedencia(String procedencia) {
		this.procedencia = procedencia;
	}

	public String getDomicilio_conductor() {
		return domicilio_conductor;
	}

	public void setDomicilio_conductor(String domicilio_conductor) {
		this.domicilio_conductor = domicilio_conductor;
	}

	public String getColonia_conductor() {
		return colonia_conductor;
	}

	public void setColonia_conductor(String colonia_conductor) {
		this.colonia_conductor = colonia_conductor;
	}

	public String getCiudad_conductor() {
		return ciudad_conductor;
	}

	public void setCiudad_conductor(String ciudad_conductor) {
		this.ciudad_conductor = ciudad_conductor;
	}

	public String getLicencia_conductor() {
		return licencia_conductor;
	}

	public void setLicencia_conductor(String licencia_conductor) {
		this.licencia_conductor = licencia_conductor;
	}

	public String getTipo_lic() {
		return tipo_lic;
	}

	public void setTipo_lic(String tipo_lic) {
		this.tipo_lic = tipo_lic;
	}

	public String getLugar_infaccion() {
		return lugar_infaccion;
	}

	public void setLugar_infaccion(String lugar_infaccion) {
		this.lugar_infaccion = lugar_infaccion;
	}

	public String getNombre_propietario() {
		return nombre_propietario;
	}

	public void setNombre_propietario(String nombre_propietario) {
		this.nombre_propietario = nombre_propietario;
	}

	public String getDomicilio_propietario() {
		return domicilio_propietario;
	}

	public void setDomicilio_propietario(String domicilio_propietario) {
		this.domicilio_propietario = domicilio_propietario;
	}

	public String getColonia_propietario() {
		return colonia_propietario;
	}

	public void setColonia_propietario(String colonia_propietario) {
		this.colonia_propietario = colonia_propietario;
	}

	public String getCiudad_propietario() {
		return ciudad_propietario;
	}

	public void setCiudad_propietario(String ciudad_propietario) {
		this.ciudad_propietario = ciudad_propietario;
	}

	public String getArticulos() {
		return articulos;
	}

	public void setArticulos(String articulos) {
		this.articulos = articulos;
	}

	public String getObservaciones_tablet() {
		return observaciones_tablet;
	}

	public void setObservaciones_tablet(String observaciones_tablet) {
		this.observaciones_tablet = observaciones_tablet;
	}

	public int getC_id_infracciones() {
		return c_id_infracciones;
	}

	public void setC_id_infracciones(int c_id_infracciones) {
		this.c_id_infracciones = c_id_infracciones;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public String getLinea() {
		return linea;
	}

	public void setLinea(String linea) {
		this.linea = linea;
	}

	public String getVigencia() {
		return vigencia;
	}

	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
	}

	public String getCve_infraccion1() {
		return cve_infraccion1;
	}

	public void setCve_infraccion1(String cve_infraccion1) {
		this.cve_infraccion1 = cve_infraccion1;
	}

	public String getCve_infraccion2() {
		return cve_infraccion2;
	}

	public void setCve_infraccion2(String cve_infraccion2) {
		this.cve_infraccion2 = cve_infraccion2;
	}

	public String getCve_infraccion3() {
		return cve_infraccion3;
	}

	public void setCve_infraccion3(String cve_infraccion3) {
		this.cve_infraccion3 = cve_infraccion3;
	}

	public String getCve_infraccion4() {
		return cve_infraccion4;
	}

	public void setCve_infraccion4(String cve_infraccion4) {
		this.cve_infraccion4 = cve_infraccion4;
	}

	public String getCve_infraccion5() {
		return cve_infraccion5;
	}

	public void setCve_infraccion5(String cve_infraccion5) {
		this.cve_infraccion5 = cve_infraccion5;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public int getId_infracciones() {
		return id_infracciones;
	}

	public void setId_infracciones(int id_infracciones) {
		this.id_infracciones = id_infracciones;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getEntre_calle() {
		return entre_calle;
	}

	public void setEntre_calle(String entre_calle) {
		this.entre_calle = entre_calle;
	}

	public String getZona() {
		return zona;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}
	
}