package com.ceiba.biblioteca.utilitarios.utilitarios.mq;



import com.ceiba.biblioteca.utilitarios.utilitarios.BeanHelper;

import java.io.Serializable;


public class TemplateMensageBean implements Serializable {
	
	private static final long serialVersionUID = -854152043989634380L;
	private String alarma;
	private String requerimientoCliente;
	private String respuestaCliente;
	private String requerimientoServer;
	private String respuestaServer;
	private String respuestaBackEnd;
	
	public TemplateMensageBean(){
		super();
	}
	
	public String getAlarma() {
		return alarma;
	}
	public void setAlarma(String alarma) {
		this.alarma = alarma;
	}
	public String getRequerimientoCliente() {
		return requerimientoCliente;
	}
	public void setRequerimientoCliente(String requerimientoCliente) {
		this.requerimientoCliente = requerimientoCliente;
	}
	public String getRespuestaCliente() {
		return respuestaCliente;
	}
	public void setRespuestaCliente(String respuestaCliente) {
		this.respuestaCliente = respuestaCliente;
	}
	public String getRespuestaServer() {
		return respuestaServer;
	}
	public void setRespuestaServer(String respuestaServer) {
		this.respuestaServer = respuestaServer;
	}
	
	public String getRequerimientoServer() {
		return requerimientoServer;
	}

	public void setRequerimientoServer(String requerimientoServer) {
		this.requerimientoServer = requerimientoServer;
	}

	public String getRespuestaBackEnd() {
		return respuestaBackEnd;
	}

	public void setRespuestaBackEnd(String respuestaBackEnd) {
		this.respuestaBackEnd = respuestaBackEnd;
	}

	public String toString(){
		return BeanHelper.bean2String(this);
	}
	
}
