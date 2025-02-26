package com.ceiba.biblioteca.conectorCliente.MQ.contexto;

import com.ceiba.biblioteca.utilitarios.utilitarios.ConstantesConector;

import java.io.Serializable;


/**
 * Plantilla que contiene los datos proporcionados por el cliente para el envio de mensajes a Webpshere MQ.
 * @version 1.0
 * @author echicas
 */
public class ParametrosMensajeCliente implements Serializable {

	private static final long serialVersionUID = 1279802910881756376L;

	private String codigoServicio;
	private String paisOrigen;
	private String compania;
	private String cadena;
	private String sucursal;
	private String numeroTransaccion;
	private String hostOrigen;
	private String toker;
	private String grupoMensaje;
	private String totalMensajes;
	private int tiempoExpiracion = ConstantesConector.NO_WAIT_TIME;
	private boolean mensajePersistente = false;
	private String nombreAplicacionOrigen;
	private boolean escribirLog = false;
	private int prioridad;
	private	String selectorJms;
	
	public ParametrosMensajeCliente(){
		super();
	}
	
	/**
	 * @return the cadena
	 */
	public String getCadena() {
		return cadena;
	}
	/**
	 * @param cadena the cadena to set
	 */
	public void setCadena(String cadena) {
		this.cadena = cadena;
	}
	/**
	 * @return the codigoServicio
	 */
	public String getCodigoServicio() {
		return codigoServicio;
	}
	/**
	 * @param codigoServicio the codigoServicio to set
	 */
	public void setCodigoServicio(String codigoServicio) {
		this.codigoServicio = codigoServicio;
	}
	/**
	 * @return the compania
	 */
	public String getCompania() {
		return compania;
	}
	/**
	 * @param compania the compania to set
	 */
	public void setCompania(String compania) {
		this.compania = compania;
	}

	/**
	 * @return the grupoMensaje
	 */
	public String getGrupoMensaje() {
		return grupoMensaje;
	}
	/**
	 * @param grupoMensaje the grupoMensaje to set
	 */
	public void setGrupoMensaje(String grupoMensaje) {
		this.grupoMensaje = grupoMensaje;
	}
	/**
	 * @return the hostOrigen
	 */
	public String getHostOrigen() {
		return hostOrigen;
	}
	/**
	 * @param hostOrigen the hostOrigen to set
	 */
	public void setHostOrigen(String hostOrigen) {
		this.hostOrigen = hostOrigen;
	}
	/**
	 * @return the mensajePersistente
	 */
	public boolean isMensajePersistente() {
		return mensajePersistente;
	}
	/**
	 * @param mensajePersistente the mensajePersistente to set
	 */
	public void setMensajePersistente(boolean mensajePersistente) {
		this.mensajePersistente = mensajePersistente;
	}
	/**
	 * @return the nombreAplicacionOrigen
	 */
	public String getNombreAplicacionOrigen() {
		return nombreAplicacionOrigen;
	}
	/**
	 * @param nombreAplicacionOrigen the nombreAplicacionOrigen to set
	 */
	public void setNombreAplicacionOrigen(String nombreAplicacionOrigen) {
		this.nombreAplicacionOrigen = nombreAplicacionOrigen;
	}
	/**
	 * @return the numeroTransaccion
	 */
	public String getNumeroTransaccion() {
		return numeroTransaccion;
	}
	/**
	 * @param numeroTransaccion the numeroTransaccion to set
	 */
	public void setNumeroTransaccion(String numeroTransaccion) {
		this.numeroTransaccion = numeroTransaccion;
	}
	/**
	 * @return the paisOrigen
	 */
	public String getPaisOrigen() {
		return paisOrigen;
	}
	/**
	 * @param paisOrigen the paisOrigen to set
	 */
	public void setPaisOrigen(String paisOrigen) {
		this.paisOrigen = paisOrigen;
	}
	/**
	 * @return the sucursal
	 */
	public String getSucursal() {
		return sucursal;
	}
	/**
	 * @param sucursal the sucursal to set
	 */
	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}
	/**
	 * @return the tiempoExpiracion
	 */
	public int getTiempoExpiracion() {
		return tiempoExpiracion;
	}
	/**
	 * @param tiempoExpiracion the tiempoExpiracion to set
	 */
	public void setTiempoExpiracion(int tiempoExpiracion) {
		this.tiempoExpiracion = tiempoExpiracion;
	}
	/**
	 * @return the toker
	 */
	public String getToker() {
		return toker;
	}
	/**
	 * @param toker the toker to set
	 */
	public void setToker(String toker) {
		this.toker = toker;
	}
	/**
	 * @return the totalMensajes
	 */
	public String getTotalMensajes() {
		return totalMensajes;
	}
	/**
	 * @param totalMensajes the totalMensajes to set
	 */
	public void setTotalMensajes(String totalMensajes) {
		this.totalMensajes = totalMensajes;
	}

	/**
	 * @return the escribirLog
	 */
	public boolean isEscribirLog() {
		return escribirLog;
	}

	/**
	 * @param escribirLog the escribirLog to set
	 */
	public void setEscribirLog(boolean escribirLog) {
		this.escribirLog = escribirLog;
	}

	public int getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(int prioridad) {
		this.prioridad = prioridad;
	}

	public String getSelectorJms() {
		return selectorJms;
	}

	public void setSelectorJms(String selectorJms) {
		this.selectorJms = selectorJms;
	}
	

}
