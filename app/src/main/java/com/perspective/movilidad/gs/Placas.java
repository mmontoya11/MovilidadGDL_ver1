package com.perspective.movilidad.gs;

public class Placas {
	
	private int idPlaca,axo;
	private String placa,paternoPalca,maternoPlaca,nombrePlaca,propiePlaca,direccion,colonia,serie,marca,tipo,linea,color;
	
	public Placas() {
		super();
		this.idPlaca = 0;
		this.placa = "";
		this.paternoPalca = "";
		this.maternoPlaca = "";
		this.nombrePlaca = "";
		this.propiePlaca = "";
		this.direccion = "";
		this.colonia = "";
		this.serie = "";
		this.marca = "";
		this.tipo = "";
		this.linea = "";
		this.axo = 0;
		this.paternoPalca = "";
		this.maternoPlaca = "";
		this.nombrePlaca = "";
		this.color = "";
	}
	
	public Placas(String placa, String propiePlaca, String direccion, String colonia, String serie, String marca, String tipo, String linea, int axo, String color) {
		super();
		this.placa = placa;
		this.propiePlaca = propiePlaca;
		this.direccion = direccion;
		this.colonia = colonia;
		this.serie = serie;
		this.marca = marca;
		this.tipo = tipo;
		this.linea = linea;
		this.axo = axo;
		this.color = color;
	}
	
	public Placas(int idPlaca, String placa, String propiePlaca, String direccion, String colonia, String serie, String marca, String tipo, String linea, int axo, String paternoPalca, String maternoPlaca, String nombrePlaca, String color) {
		super();
		this.idPlaca = idPlaca;
		this.axo = axo;
		this.placa = placa;
		this.propiePlaca = propiePlaca;
		this.direccion = direccion;
		this.colonia = colonia;
		this.serie = serie;
		this.marca = marca;
		this.tipo = tipo;
		this.linea = linea;
		this.axo = axo;
		this.paternoPalca = paternoPalca;
		this.maternoPlaca = maternoPlaca;
		this.nombrePlaca = nombrePlaca;
		this.color = color;
	}

	public Placas(String placa, String propiePlaca, String direccion,String colonia, String serie, String marca, String tipo, String linea, int axo, String paternoPalca, String maternoPlaca, String nombrePlaca, String color) {
		super();
		this.placa = placa;
		this.propiePlaca = propiePlaca;
		this.direccion = direccion;
		this.colonia = colonia;
		this.serie = serie;
		this.marca = marca;
		this.tipo = tipo;
		this.linea = linea;
		this.axo = axo;
		this.paternoPalca = paternoPalca;
		this.maternoPlaca = maternoPlaca;
		this.nombrePlaca = nombrePlaca;
		this.color = color;
	}

	public int getIdPlaca() {
		return idPlaca;
	}

	public void setIdPlaca(int idPlaca) {
		this.idPlaca = idPlaca;
	}

	public int getAxo() {
		return axo;
	}

	public void setAxo(int axo) {
		this.axo = axo;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getPropiePlaca() {
		return propiePlaca;
	}

	public void setPropiePlaca(String propiePlaca) {
		this.propiePlaca = propiePlaca;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getColonia() {
		return colonia;
	}

	public void setColonia(String colonia) {
		this.colonia = colonia;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
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

	public String getLinea() {
		return linea;
	}

	public void setLinea(String linea) {
		this.linea = linea;
	}

	public String getPaternoPalca() {
		return paternoPalca;
	}

	public void setPaternoPalca(String paternoPalca) {
		this.paternoPalca = paternoPalca;
	}

	public String getMaternoPlaca() {
		return maternoPlaca;
	}

	public void setMaternoPlaca(String maternoPlaca) {
		this.maternoPlaca = maternoPlaca;
	}

	public String getNombrePlaca() {
		return nombrePlaca;
	}

	public void setNombrePlaca(String nombrePlaca) {
		this.nombrePlaca = nombrePlaca;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
}
