package com.perspective.movilidad.gs;

public class Estados {
	
	private int idEstado;
	private String estados,capturo,fecha;
	
	public Estados() {
		this.idEstado = 0;
		this.estados = "";
		this.capturo = "";
		this.fecha = "";
	}
	
	public Estados(int idEstado,String estados, String capturo, String fecha) {
		this.idEstado = idEstado;
		this.estados = estados;
		this.capturo = capturo;
		this.fecha = fecha;
	}
	
	public Estados(int idEstado,String estados) {
		this.idEstado = idEstado;
		this.estados = estados;
	}
	
	public Estados(String estados) {
		this.estados = estados;
	}

	public int getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(int idEstado) {
		this.idEstado = idEstado;
	}

	public String getEstados() {
		return estados;
	}

	public void setEstados(String estados) {
		this.estados = estados;
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

}
