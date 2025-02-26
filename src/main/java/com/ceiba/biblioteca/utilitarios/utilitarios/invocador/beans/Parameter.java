package com.siman.Integracion.utilitarios.invocador.beans;

import java.io.Serializable;

public class Parameter implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String type;
	private String data;
	private String length;

	public Parameter(){
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
