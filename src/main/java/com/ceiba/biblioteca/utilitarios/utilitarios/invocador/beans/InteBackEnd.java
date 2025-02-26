package com.ceiba.biblioteca.utilitarios.utilitarios.invocador.beans;

import java.io.Serializable;

public class InteBackEnd implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private DataSource datasource;
	private FileBackEnd file;
	private com.siman.Integracion.utilitarios.invocador.beans.StoreProcedure storeProcedure;
	private com.siman.Integracion.utilitarios.invocador.beans.PGMAS400 pgmAS400;
	private String script;
	
	public InteBackEnd(){
	}

	public FileBackEnd getFile() {
		return file;
	}

	public void setFile(FileBackEnd file) {
		this.file = file;
	}

	public com.siman.Integracion.utilitarios.invocador.beans.PGMAS400 getPgmAS400() {
		return pgmAS400;
	}

	public void setPgmAS400(com.siman.Integracion.utilitarios.invocador.beans.PGMAS400 pgmAS400) {
		this.pgmAS400 = pgmAS400;
	}

	public com.siman.Integracion.utilitarios.invocador.beans.StoreProcedure getStoreProcedure() {
		return storeProcedure;
	}

	public void setStoreProcedure(com.siman.Integracion.utilitarios.invocador.beans.StoreProcedure storeProcedure) {
		this.storeProcedure = storeProcedure;
	}

	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}
	
}
