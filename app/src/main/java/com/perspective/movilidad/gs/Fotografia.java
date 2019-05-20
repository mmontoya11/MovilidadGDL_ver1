package com.perspective.movilidad.gs;

public class Fotografia {
	//id_fotografia INTEGER PRIMARY KEY AUTOINCREMENT, id_levantamiento int,numero_acta text,archivo text,descripcion text,estatus text
	private int idFotofrafia,idInfraccion;
	private String numero_acta,archivo;
	private char estatus;
	
	
	public Fotografia() {
		this.idFotofrafia = 0;
		this.idInfraccion = 0;
		this.numero_acta = "";
		this.archivo = "";
		this.estatus = ' ';
	}
	
	public Fotografia(int idFotofrafia,int idInfraccion,String numero_acta,String archivo,char enviado) {
		this.idFotofrafia = idFotofrafia;
		this.idInfraccion = idInfraccion;
		this.numero_acta = numero_acta;
		this.archivo = archivo;
		this.estatus = enviado;
	}
	
	public Fotografia(int idFotofrafia,String numero_acta,String archivo,char enviado) {
		this.idFotofrafia = idFotofrafia;
		this.numero_acta = numero_acta;
		this.archivo = archivo;
		this.estatus = enviado;
	}
	
	public Fotografia(String numero_acta,String archivo,char enviado) {
		this.numero_acta = numero_acta;
		this.archivo = archivo;
		this.estatus = enviado;
	}
	
	public int getIdFotofrafia() {
		return idFotofrafia;
	}
	public void setIdFotofrafia(int idFotofrafia) {
		this.idFotofrafia = idFotofrafia;
	}
	public int getIdInfraccion() {
		return idInfraccion;
	}
	public void setIdInfraccion(int idInfraccion) {
		this.idInfraccion = idInfraccion;
	}
	public String getNumero_acta() {
		return numero_acta;
	}
	public void setNumero_acta(String numero_acta) {
		this.numero_acta = numero_acta;
	}
	public String getArchivo() {
		return archivo;
	}
	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}
	public char getEstatus() {
		return estatus;
	}
	public void setEstatus(char enviado) {
		this.estatus = enviado;
	}

}
