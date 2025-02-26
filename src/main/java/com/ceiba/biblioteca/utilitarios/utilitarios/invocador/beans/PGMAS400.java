package com.siman.Integracion.utilitarios.invocador.beans;

import java.io.Serializable;

public class PGMAS400 implements Serializable {

	private static final long serialVersionUID = 1L;
	private String namePGM;
	private String database;
	private Parameter parameter;
	
	public PGMAS400(){
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getNamePGM() {
		return namePGM;
	}

	public void setNamePGM(String namePGM) {
		this.namePGM = namePGM;
	}

	public Parameter getParameter() {
		return parameter;
	}

	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}
}
