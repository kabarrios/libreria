package com.ceiba.biblioteca.utilitarios.utilitarios.invocador.beans;



import com.ceiba.biblioteca.utilitarios.utilitarios.BeanHelper;

import java.io.Serializable;

/**
 * Clase que contiene lo necesario para la invocaci�n
 * de un servicio
 * @author echicas
 * Creada 20080721
 */
public class Service implements Serializable {
	
	private static final long serialVersionUID = 3783994858124955166L;
	private String codigoServicio;
	/** Full Qualify Name de la clase a ser invocada */
	private String clazz;
	/** Nombre del metodo que se invocara */
	private String method;
	/** Descripci�n del servicio */
	private String description;
	/** Nombre de la aplicaci�n due�a del servicio */
	private String userApplicationId;
	/** Identifica si el backend esta habilitado */
	private boolean enabled;
	/** Identifica si el backend debe hacer journal */
	private boolean jounal;
	/** identifica si un backend pertenece a terceros */
	private boolean thirdParty;
	/** Queue Manager sobre el que el backend esta operando */
	private String queueManager;
	/** Contiene la configuraci�n adici�nal de backend que son de itegraci�n */
	private InteBackEnd backend;
	/** Determina si el servicio sera invocado como request&reply */
	private boolean threaded;

	public Service(){
	}
		
	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isJounal() {
		return jounal;
	}

	public void setJounal(boolean jounal) {
		this.jounal = jounal;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUserApplicationId() {
		return userApplicationId;
	}


	public void setUserApplicationId(String userApplicationId) {
		this.userApplicationId = userApplicationId;
	}

	public String getQueueManager() {
		return queueManager;
	}

	public void setQueueManager(String queueManager) {
		this.queueManager = queueManager;
	}
	
	public boolean isThirdParty() {
		return thirdParty;
	}

	public void setThirdParty(boolean thirdParty) {
		this.thirdParty = thirdParty;
	}

	public InteBackEnd getBackend() {
		return backend;
	}

	public void setBackend(InteBackEnd backend) {
		this.backend = backend;
	}
	
	
	public boolean isThreaded() {
		return threaded;
	}

	public void setThreaded(boolean threaded) {
		this.threaded = threaded;
	}
	
	/**
	 * M�todo toString de la clase
	 */
	public String toString(){
		return BeanHelper.bean2String(this);
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

}
