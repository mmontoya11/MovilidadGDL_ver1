package com.perspective.movilidad.gs;

import android.util.Log;

public class Justificar {
	
	public static String Conversion(String txtOriginal) {
				
		//String txtEspeciales = txtOriginal.replace('�', (char)160).replace('�', '�').replace('�', (char)161).replace('�', (char)162).replace('�', (char)163).replace('�', (char)165).replace('�', (char)164).replace('�', '�').replace('�', '�').replace('�', '�').replace('�', '�').replace('�', (char)129);
		String txtEspeciales = txtOriginal.replace('á', (char)160).replace('é', '‚').replace('í', (char)161).replace('ó', (char)162).replace('ú', (char)163).replace('Ñ', (char)165).replace('ñ', (char)164).replace('ä', '„').replace('ë', '‰').replace('ï', '‹').replace('ö', '”').replace('ü', (char)129);
		
		
		return txtEspeciales;
	}
	
	public static String[] justifocarTexto(String txt, String font) {
		
		String[] arrayCadenaO = txt.split(" ");	
		String [] arrayCadenaC = new String[1];
		String Auxiliar = "";
		int i = 0;
		//for (int x=0; x<arrayCadenaO.length; x++) 
		int x = 0;	
		int car = 0;
		if(font.equalsIgnoreCase("a")) {
			car = 48;
		}else {
			car = 64;
		}
	
		while(x < arrayCadenaO.length) { 
			if (x > 0 && i > 0){
				String[] temp_arrayCadenaC = new String[i + 1];
				System.arraycopy(arrayCadenaC, 0, temp_arrayCadenaC, 0, arrayCadenaC.length);
				arrayCadenaC =  temp_arrayCadenaC;
			}
			try {
				
				if  (((Auxiliar + " " +  arrayCadenaO[x]).length() <= car)) {  //&&  arrayCadenaO.[x] != " "
					Auxiliar =   Auxiliar  +  arrayCadenaO[x] + " ";
					x++;			
					//System.out.println(" x = " + x + "  ArrayL " + arrayCadenaO.length + " Auxiliar  " + Auxiliar.length());
					if ((x == arrayCadenaO.length) &&( Auxiliar.length() > 0)) 
						arrayCadenaC[i] =   Auxiliar.substring(0, Auxiliar.length()-1);
				}
				else  {
				
					arrayCadenaC[i] = Auxiliar.substring(0, Auxiliar.length()-1); 
					i++;
					
					//System.out.println(Auxiliar);
				    Auxiliar = "";
				   // x--;
				}
			}catch (StringIndexOutOfBoundsException e) {
				Log.e("exception", e.getMessage());
			}
			
		}
		
		String lineas = "";
		int longitud;
		for (int yy=0; yy<arrayCadenaC.length; yy++) {
			lineas = "";
			longitud = arrayCadenaC[yy].length() + 2;
			while(longitud <= car) {
					lineas = "  ";
					arrayCadenaC[yy] = arrayCadenaC[yy] + lineas;
					longitud = arrayCadenaC[yy].length() + 2;
			}
		}

		return arrayCadenaC;
	}
	
	public static String[] justifocarTexto1(String txt,int caracter) {
		
		String[] arrayCadenaO = txt.split(" ");	
		String [] arrayCadenaC = new String[1];
		String Auxiliar = "";
		int i = 0;
		//for (int x=0; x<arrayCadenaO.length; x++) 
		int x = 0;	
	
		while(x < arrayCadenaO.length) { 
			if (x > 0 && i > 0){
				String[] temp_arrayCadenaC = new String[i + 1];
				System.arraycopy(arrayCadenaC, 0, temp_arrayCadenaC, 0, arrayCadenaC.length);
				arrayCadenaC =  temp_arrayCadenaC;
			}
			try {
		if  (((Auxiliar + " " +  arrayCadenaO[x]).length() <= caracter)   ) {  //&&  arrayCadenaO.[x] != " "
				Auxiliar =   Auxiliar  +  arrayCadenaO[x] + " ";
				x++;			
				//System.out.println(" x = " + x + "  ArrayL " + arrayCadenaO.length + " Auxiliar  " + Auxiliar.length());
				if ((x == arrayCadenaO.length) &&( Auxiliar.length() > 0)) 
					arrayCadenaC[i] =   Auxiliar.substring(0, Auxiliar.length()-1);
			}
			else  {
			
				arrayCadenaC[i] = Auxiliar.substring(0, Auxiliar.length()-1); 
				i++;
				
				//System.out.println(Auxiliar);
			    Auxiliar = "";
			   // x--;
			}
			}catch (StringIndexOutOfBoundsException e) {
				Log.e("exception", e.getMessage());
			}
			
		}
		
		String lineas = "";
		int longitud;
		for (int yy=0; yy<arrayCadenaC.length; yy++) {
			lineas = "";
			longitud = arrayCadenaC[yy].length() + 2;
			while(longitud <= caracter) {
					lineas = "  ";
					arrayCadenaC[yy] = arrayCadenaC[yy] + lineas;
					longitud = arrayCadenaC[yy].length() + 2;
			}
		}

		return arrayCadenaC;
	}
	
	public static String[] justifocarTexto(String txt,int l) {
		
		String[] arrayCadenaO = txt.split(" ");	
		String [] arrayCadenaC = new String[1];
		String Auxiliar = "";
		int i = 0;
		//for (int x=0; x<arrayCadenaO.length; x++) 
		int x = 0;
		int n = 80 + (80 - l) ;
	
		while(x < arrayCadenaO.length) { 
			if (x > 0 && i > 0){
				String[] temp_arrayCadenaC = new String[i + 1];
				System.arraycopy(arrayCadenaC, 0, temp_arrayCadenaC, 0, arrayCadenaC.length);
				arrayCadenaC =  temp_arrayCadenaC;
			}
			try {
		if  (((Auxiliar + " " +  arrayCadenaO[x]).length() <= n)   ) {  //&&  arrayCadenaO.[x] != " "
				Auxiliar =   Auxiliar  +  arrayCadenaO[x] + " ";
				x++;			
				//System.out.println(" x = " + x + "  ArrayL " + arrayCadenaO.length + " Auxiliar  " + Auxiliar.length());
				if ((x == arrayCadenaO.length) &&( Auxiliar.length() > 0)) arrayCadenaC[i] =   Auxiliar.substring(0, Auxiliar.length()-1);
			}
			else  {
			
				arrayCadenaC[i] = Auxiliar.substring(0, Auxiliar.length()-1); 
				i++;
				
				//System.out.println(Auxiliar);
			    Auxiliar = "";
			   // x--;
			}
			}catch (StringIndexOutOfBoundsException e) {
				Log.e("exception", e.getMessage());
			}
			
		}
		
		String lineas = "";
		int longitud;
		for (int yy=0; yy<arrayCadenaC.length; yy++) {
			lineas = "";
			longitud = arrayCadenaC[yy].length() + 2;
			while(longitud <= n) {
					lineas = " -";
					arrayCadenaC[yy] = arrayCadenaC[yy] + lineas;
					longitud = arrayCadenaC[yy].length() + 2;
			}
		}

		return arrayCadenaC;
	}
	
	public static String PX(int x, String Texto) {
		int Fila =  x;
		String X1 = "";
	    for (int i = 1; i <=Fila; i++) {
	    	X1 = X1 + " ";
		}		
	    
	    return  X1 + Texto.replace("Ã‘", "Â¥");
	
	}
	
	
	public static String PY( int y) {
		int Col  =  y; 
		String Y1 = "";
	    for (int i = 1; i <=Col; i++) {
			Y1 = Y1 + "\n";
		}	
	
		return Y1 ;
	
	}
	
	public static String mes(int m) {
		String me;
		switch (m) {
		case 1:
			me = "enero";
			break;
		case 2:
			me = "febrero";
			break;
		case 3:
			me = "marzo";
			break;
		case 4:
			me = "abril";
			break;
		case 5:
			me = "mayo";
			break;
		case 6:
			me = "junio";
			break;
		case 7:
			me = "julio";
			break;
		case 8:
			me = "agosto";
			break;
		case 9:
			me = "septiembre";
			break;
		case 10:
			me = "octubre";
			break;
		case 11:
			me = "noviembre";
			break;
		case 12:
			me = "diciembre";
			break;
			
		default:
			me = "";
			break;
		}
		return me;
	}
	
	public static String llenarLinea() {
		String txt = "";
		for(int i = 0; i < 40; i ++) {
			txt += " -";
		}
		return txt;
	}

}
