package com.ceiba.biblioteca.utilitarios.utilitarios.invocador.beans;

import java.io.Serializable;

public class DataSource implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String url;
	private String username;
	private String password;
	private String magickey;
	
	public DataSource(){
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getMagickey() {
		return magickey;
	}

	public void setMagickey(String magickey) {
		this.magickey = magickey;
	}
}
