package com.ceiba.biblioteca.utilitarios.utilitarios.mq;



import com.ceiba.biblioteca.utilitarios.utilitarios.BeanHelper;

import java.io.Serializable;

public class ParametrosConfiguracionMQ implements Serializable {

	private static final long serialVersionUID = 6083746274380674263L;
	
	private String queueManager;
	private String host;
	private String channel;
	private int puertoServidor;
	private String rutaLog;
	private String extensionLog;
	private String usuario;
	private String clave;
	private String aplicacion;
	private String definicionRequerimiento;
	private String definicionRespuesta;
	private String transaccionLogger;
	private String errorTransaccionLogger;
	private String version;
	private int listenTime;
	private String queueReq;
	private String alarma;
	private int backout;
	private String prefijoLog;
	private String invocadorConfiguracion;
	private String invocadorQueue;
	private int instancias;
	private String ccdtPath;
	private int clientReconnectTimeout;
	private boolean bindingMode;


	public ParametrosConfiguracionMQ() {
		super();
	}


	/**
	 * @return the aplicacion
	 */
	public String getAplicacion() {
		return aplicacion;
	}


	/**
	 * @param aplicacion the aplicacion to set
	 */
	public void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
	}


	/**
	 * @return the channel
	 */
	public String getChannel() {
		return channel;
	}


	public String getCcdtPath() {
		return ccdtPath;
	}


	public void setCcdtPath(String ccdtPath) {
		this.ccdtPath = ccdtPath;
	}


	/**
	 * @param channel the channel to set
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}


	/**
	 * @return the clave
	 */
	public String getClave() {
		return clave;
	}


	/**
	 * @param clave the clave to set
	 */
	public void setClave(String clave) {
		this.clave = clave;
	}


	/**
	 * @return the extensionLog
	 */
	public String getExtensionLog() {
		return extensionLog;
	}


	/**
	 * @param extensionLog the extensionLog to set
	 */
	public void setExtensionLog(String extensionLog) {
		this.extensionLog = extensionLog;
	}


	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}


	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}


	/**
	 * @return the puertoServidor
	 */
	public int getPuertoServidor() {
		return puertoServidor;
	}


	/**
	 * @param puertoServidor the puertoServidor to set
	 */
	public void setPuertoServidor(int puertoServidor) {
		this.puertoServidor = puertoServidor;
	}


	/**
	 * @return the queueManager
	 */
	public String getQueueManager() {
		return queueManager;
	}


	/**
	 * @param queueManager the queueManager to set
	 */
	public void setQueueManager(String queueManager) {
		this.queueManager = queueManager;
	}


	/**
	 * @return the rutaLog
	 */
	public String getRutaLog() {
		return rutaLog;
	}


	/**
	 * @param rutaLog the rutaLog to set
	 */
	public void setRutaLog(String rutaLog) {
		this.rutaLog = rutaLog;
	}


	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}


	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}


	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}


	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}


	/**
	 * @return the definicionRequerimiento
	 */
	public String getDefinicionRequerimiento() {
		return definicionRequerimiento;
	}


	/**
	 * @param definicionRequerimiento the definicionRequerimiento to set
	 */
	public void setDefinicionRequerimiento(String definicionRequerimiento) {
		this.definicionRequerimiento = definicionRequerimiento;
	}


	/**
	 * @return the definicionRespuesta
	 */
	public String getDefinicionRespuesta() {
		return definicionRespuesta;
	}


	/**
	 * @param definicionRespuesta the definicionRespuesta to set
	 */
	public void setDefinicionRespuesta(String definicionRespuesta) {
		this.definicionRespuesta = definicionRespuesta;
	}


	/**
	 * @return the errorTransaccionLogger
	 */
	public String getErrorTransaccionLogger() {
		return errorTransaccionLogger;
	}


	/**
	 * @param errorTransaccionLogger the errorTransaccionLogger to set
	 */
	public void setErrorTransaccionLogger(String errorTransaccionLogger) {
		this.errorTransaccionLogger = errorTransaccionLogger;
	}


	/**
	 * @return the transaccionLogger
	 */
	public String getTransaccionLogger() {
		return transaccionLogger;
	}


	/**
	 * @param transaccionLogger the transaccionLogger to set
	 */
	public void setTransaccionLogger(String transaccionLogger) {
		this.transaccionLogger = transaccionLogger;
	}


	/**
	 * @return the alarma
	 */
	public String getAlarma() {
		return alarma;
	}


	/**
	 * @param alarma the alarma to set
	 */
	public void setAlarma(String alarma) {
		this.alarma = alarma;
	}


	/**
	 * @return the backout
	 */
	public int getBackout() {
		return backout;
	}


	/**
	 * @param backout the backout to set
	 */
	public void setBackout(int backout) {
		this.backout = backout;
	}


	/**
	 * @return the invocadorConfiguracion
	 */
	public String getInvocadorConfiguracion() {
		return invocadorConfiguracion;
	}


	/**
	 * @param invocadorConfiguracion the invocadorConfiguracion to set
	 */
	public void setInvocadorConfiguracion(String invocadorConfiguracion) {
		this.invocadorConfiguracion = invocadorConfiguracion;
	}


	/**
	 * @return the invocadorQueue
	 */
	public String getInvocadorQueue() {
		return invocadorQueue;
	}


	/**
	 * @param invocadorQueue the invocadorQueue to set
	 */
	public void setInvocadorQueue(String invocadorQueue) {
		this.invocadorQueue = invocadorQueue;
	}


	/**
	 * @return the listenTime
	 */
	public int getListenTime() {
		return listenTime;
	}


	/**
	 * @param listenTime the listenTime to set
	 */
	public void setListenTime(int listenTime) {
		this.listenTime = listenTime;
	}


	/**
	 * @return the prefijoLog
	 */
	public String getPrefijoLog() {
		return prefijoLog;
	}


	/**
	 * @param prefijoLog the prefijoLog to set
	 */
	public void setPrefijoLog(String prefijoLog) {
		this.prefijoLog = prefijoLog;
	}


	/**
	 * @return the queueReq
	 */
	public String getQueueReq() {
		return queueReq;
	}


	/**
	 * @param queueReq the queueReq to set
	 */
	public void setQueueReq(String queueReq) {
		this.queueReq = queueReq;
	}
	
	
	public String toString(){
		return BeanHelper.bean2String(this);
	}


	public int getInstancias() {
		return instancias;
	}


	public void setInstancias(int instancias) {
		this.instancias = instancias;
	}


	public int getClientReconnectTimeout() {
		return clientReconnectTimeout;
	}


	public void setClientReconnectTimeout(int clientReconnectTimeout) {
		this.clientReconnectTimeout = clientReconnectTimeout;
	}


	/**
	 * @param bindingMode the bindingMode to set
	 */
	public void setBindingMode(boolean bindingMode) {
		this.bindingMode = bindingMode;
	}


	/**
	 * @return the bindingMode
	 */
	public boolean isBindingMode() {
		return bindingMode;
	}
	

	public boolean getBindingMode() {
		return bindingMode;
	}
	
}
