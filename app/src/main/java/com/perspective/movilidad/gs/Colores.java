package com.perspective.movilidad.gs;

public class Colores {
	
	private int idColores;
	private String color,capturo,fecha;
	
	public Colores() {
		this.idColores = 0;
		this.color = "";
		this.capturo = "";
		this.fecha = "";
	}
	
	public Colores(int idColores,String color, String capturo, String fecha) {
		this.idColores = idColores;
		this.color = color;
		this.capturo = capturo;
		this.fecha = fecha;
	}
	
	public Colores(int idColores,String color) {
		this.idColores = idColores;
		this.color = color;
	}
	
	public Colores(String color) {
		this.color = color;
	}

	public int getIdColores() {
		return idColores;
	}

	public void setIdColores(int idColores) {
		this.idColores = idColores;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
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
