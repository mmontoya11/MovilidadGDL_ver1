package com.perspective.movilidad.gs;

public class Infracciones {
	
	private int idCInfracciones;
	private String cveInfracciones,descripcion,vigencia,capturo,fecha,articulo;
	private double importeInfraccion;

	public Infracciones() {
		this.idCInfracciones = 0;
		this.cveInfracciones = "";
		this.descripcion = "";
		this.vigencia = "";
		this.capturo = "";
		this.importeInfraccion = 0d;
		this.articulo = "";
	}
	
	public Infracciones(int idCInfracciones,String cveInfracciones,String descripcion,String vigencia,String capturo,String fecha,double importeInfraccion, String articulo) {
		this.idCInfracciones = idCInfracciones;
		this.cveInfracciones = cveInfracciones;
		this.descripcion = descripcion;
		this.vigencia = vigencia;
		this.capturo = capturo;
		this.importeInfraccion = importeInfraccion;
		this.articulo = articulo;
	}
	
	public Infracciones(String cveInfracciones,String descripcion,String vigencia,String capturo,String fecha,double importeInfraccion, String articulo) {
		this.cveInfracciones = cveInfracciones;
		this.descripcion = descripcion;
		this.vigencia = vigencia;
		this.capturo = capturo;
		this.importeInfraccion = importeInfraccion;
		this.articulo = articulo;
	}
	
	public int getIdCInfracciones() {
		return idCInfracciones;
	}
	public void setIdCInfracciones(int idCInfracciones) {
		this.idCInfracciones = idCInfracciones;
	}
	public String getCveInfracciones() {
		return cveInfracciones;
	}
	public void setCveInfracciones(String cveInfracciones) {
		this.cveInfracciones = cveInfracciones;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getVigencia() {
		return vigencia;
	}
	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
	}
	public String getCapturo() {
		return capturo;
	}
	public void setCapturo(String capturo) {
		this.capturo = capturo;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public double getImporteInfraccion() {
		return importeInfraccion;
	}
	public void setImporteInfraccion(double importeInfraccion) {
		this.importeInfraccion = importeInfraccion;
	}

	public String getArticulo() {
		return articulo;
	}

	public void setArticulo(String articulo) {
		this.articulo = articulo;
	}
}
