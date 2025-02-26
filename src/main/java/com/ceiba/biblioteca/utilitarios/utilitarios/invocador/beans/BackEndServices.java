package com.ceiba.biblioteca.utilitarios.utilitarios.invocador.beans;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

/**
 * Clase que contiene todos los servicios mas las constantes que para
 * ellos se definieron
 * @author echicas
 * Creada 20080721
 */
public class BackEndServices implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/** contien un mapa de servicios */
	private Map servicios;
	

	public Map getServices() {
		return this.servicios;
	}

	public void setServices(Map servicios) {
		this.servicios = servicios;
	}
	
	public String toString(){
		if(this.servicios!=null){
			String result = new String();
			for (Iterator entry = this.servicios.values().iterator(); entry.hasNext();) result += "[" + entry.next() + "] ";
			return result;
		}
		else return "null";
	}
	
	
	
}