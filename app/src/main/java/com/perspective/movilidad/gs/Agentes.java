package com.perspective.movilidad.gs;

public class Agentes {
	
	private int idCAgentes;
	private String cveAgente,paternoAgente,maternoAgente,nombreAgente,capturo,fecha,password,folioMinimo,folioMaximo;

	public Agentes() {
		this.idCAgentes = 0;
		this.cveAgente = "";
		this.paternoAgente = "";
		this.maternoAgente = "";
		this.nombreAgente = "";
		this.capturo = "";
		this.fecha = "";
		this.password = "";
	}
	
	public Agentes(String folioMinimo, String folioMaximo) {
		this.folioMinimo = folioMinimo;
		this.folioMaximo = folioMaximo;
	}
	
	public Agentes(int idCAgentes,String cveAgente,String paternoAgente,String maternoAgente,String nombreAgente,String capturo, String fecha, String password) {
		this.idCAgentes = idCAgentes;
		this.cveAgente = cveAgente;
		this.paternoAgente = paternoAgente;
		this.maternoAgente = maternoAgente;
		this.nombreAgente = nombreAgente;
		this.capturo = capturo;
		this.fecha = fecha;
		this.password = password;
	}
	
	public Agentes(String cveAgente,String paternoAgente,String maternoAgente,String nombreAgente,String capturo, String fecha, String password) {
		this.cveAgente = cveAgente;
		this.paternoAgente = paternoAgente;
		this.maternoAgente = maternoAgente;
		this.nombreAgente = nombreAgente;
		this.capturo = capturo;
		this.fecha = fecha;
		this.password = password;
	}
	
	public int getIdCAgentes() {
		return idCAgentes;
	}
	public void setIdCAgentes(int idCAgentes) {
		this.idCAgentes = idCAgentes;
	}
	public String getCveAgente() {
		return cveAgente;
	}
	public void setCveAgente(String cveAgente) {
		this.cveAgente = cveAgente;
	}
	public String getPaternoAgente() {
		return paternoAgente;
	}
	public void setPaternoAgente(String paternoAgente) {
		this.paternoAgente = paternoAgente;
	}
	public String getMaternoAgente() {
		return maternoAgente;
	}
	public void setMaternoAgente(String maternoAgente) {
		this.maternoAgente = maternoAgente;
	}
	public String getNombreAgente() {
		return nombreAgente;
	}
	public void setNombreAgente(String nombreAgente) {
		this.nombreAgente = nombreAgente;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFolioMinimo() {
		return folioMinimo;
	}

	public void setFolioMinimo(String folioMinimo) {
		this.folioMinimo = folioMinimo;
	}

	public String getFolioMaximo() {
		return folioMaximo;
	}

	public void setFolioMaximo(String folioMaximo) {
		this.folioMaximo = folioMaximo;
	}
	
	
	

}
