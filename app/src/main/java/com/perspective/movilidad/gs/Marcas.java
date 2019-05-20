package com.perspective.movilidad.gs;

public class Marcas {
	
	private int idMarca;
	private String marca,capturo,fecha;
	
	public Marcas() {
		this.idMarca = 0;
		this.marca = "";
		this.capturo = "";
		this.fecha = "";
	}
	
	public Marcas(int idMarca,String marca,String capturo, String fecha) {
		this.idMarca = idMarca;
		this.marca = marca;
		this.capturo = capturo;
		this.fecha = fecha;
	}
	
	public Marcas(int idMarca,String marca) {
		this.idMarca = idMarca;
		this.marca = marca;
	}
	
	public Marcas(String marca) {
		this.marca = marca;
	}

	public int getIdMarca() {
		return idMarca;
	}

	public void setIdMarca(int idMarca) {
		this.idMarca = idMarca;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
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
