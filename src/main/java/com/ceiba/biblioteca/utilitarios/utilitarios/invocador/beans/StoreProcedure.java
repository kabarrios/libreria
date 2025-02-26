package com.siman.Integracion.utilitarios.invocador.beans;

import java.io.Serializable;

public class StoreProcedure implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String nameProcedure;
	private String driverClassName;
	
	public StoreProcedure(){
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getNameProcedure() {
		return nameProcedure;
	}

	public void setNameProcedure(String nameProcedure) {
		this.nameProcedure = nameProcedure;
	}	
	
}
