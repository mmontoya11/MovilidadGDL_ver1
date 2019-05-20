package com.perspective.movilidad.gs;

public class Tabletas {
	
	private int idCTab;
	private String tableta, usuarioTableta, capturo, fecha;
	
	public Tabletas() {
		this.idCTab = 0;
		this.tableta = "";
		this.usuarioTableta = "";
		this.capturo = "";
		this.fecha = "";
	}
	
	public Tabletas(int idCTab, String tableta, String usuarioTableta, String capturo, String fecha) {
		this.idCTab = idCTab;
		this.tableta = tableta;
		this.usuarioTableta = usuarioTableta;
		this.capturo = capturo;
		this.fecha = fecha;
	}
	
	public Tabletas(String tableta, String usuarioTableta, String capturo, String fecha) {
		this.tableta = tableta;
		this.usuarioTableta = usuarioTableta;
		this.capturo = capturo;
		this.fecha = fecha;
	}
	
	public int getIdCTab() {
		return idCTab;
	}
	public void setIdCTab(int idCTab) {
		this.idCTab = idCTab;
	}
	public String getTableta() {
		return tableta;
	}
	public void setTableta(String tableta) {
		this.tableta = tableta;
	}
	public String getUsuarioTableta() {
		return usuarioTableta;
	}
	public void setUsuarioTableta(String usuarioTableta) {
		this.usuarioTableta = usuarioTableta;
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
