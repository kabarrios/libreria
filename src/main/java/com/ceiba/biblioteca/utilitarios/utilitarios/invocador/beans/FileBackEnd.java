package com.ceiba.biblioteca.utilitarios.utilitarios.invocador.beans;

import java.io.Serializable;

public class FileBackEnd implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String dest;
	private String data;
	
	public FileBackEnd(){
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

}
