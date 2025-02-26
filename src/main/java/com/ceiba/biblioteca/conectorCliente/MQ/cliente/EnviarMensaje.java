package com.ceiba.biblioteca.conectorCliente.MQ.cliente;

import java.io.Serializable;

import javax.jms.Message;

import com.ceiba.biblioteca.conectorCliente.MQ.contexto.ParametrosMensajeCliente;
import com.ceiba.biblioteca.utilitarios.utilitarios.TemplateMessageHelper;
import com.ceiba.biblioteca.utilitarios.utilitarios.mq.ParametrosConfiguracionMQ;
import com.ibm.mq.MQMessage;

/**
 * Clase base para el envio de mensajes, proporciona las definiciones para interactuar con Websphere MQ en modo Cliente.
 * @version 1.0
 * @author echicas
 */
public abstract class EnviarMensaje implements Serializable {
	
	private static final long serialVersionUID = 8377576849506705707L;
	/** Almacena la configuracion para el conector cliente */
	protected ParametrosConfiguracionMQ configuracionMQ;
	/** Almacena los datos que seran colocados en el encabezado del la envoltura xml de integracion */
	protected ParametrosMensajeCliente parametrosCliente;
	/** Almacena el requerimiento del usuario en formato xml con la envoltura GCS_SI */
	protected String datosGCSSIEnvelopeReq;
	/** Almacena la respuesta al requerimiento del usuario (mensaje s�ncrono) en formato xml con la envoltura GCS_SI */
	protected String datosGCSSIEnvelopeResp;
	/** Almacena la secci�n de Datos de la respuesta al requerimiento del usuario (mensaje s�ncrono) */
	protected String datosRespuesta;
	/** Almacena la secci�n de Datos del requerimiento del usuario */
	protected String datosCliente;
	
	/**
	 * Obtiene la parte correspondiente a "Datos" de la aplicaci�n cliente
	 * @return the datosRespuesta
	 */
	public String getDatosRespuesta() {
		return datosRespuesta;
	}

	/**
	 * Establece la parte correspondiente a "Datos" de la aplicaci�n cliente
	 * @param datosRespuesta the datosRespuesta to set
	 */
	protected void setDatosRespuesta(String datosRespuesta) {
		this.datosRespuesta = datosRespuesta;
	}
	
	/**
	 * Construye y retorna el xml con la estructura GCS_SI de todos los parametros entregados por la aplicaci�n cliente
	 * @return #{@link java.lang.String} con la estructura GCS_SI
	 */
	public String getXMLDatosGCSSIEnvelope(){
		if(datosCliente!=null || !datosCliente.equals("")){
			this.datosGCSSIEnvelopeReq = TemplateMessageHelper.getXmlRequerimientoCliente(
					this.parametrosCliente.getCodigoServicio(),
					this.configuracionMQ.getUsuario(),
					this.configuracionMQ.getClave(),
					this.parametrosCliente.getNombreAplicacionOrigen(),
					this.parametrosCliente.getPaisOrigen(),
					this.parametrosCliente.getCompania(),
					this.parametrosCliente.getCadena(),
					this.parametrosCliente.getSucursal(),
					this.parametrosCliente.getNumeroTransaccion(),
					this.parametrosCliente.getHostOrigen(),
					this.parametrosCliente.getToker(),
					this.configuracionMQ.getVersion(),
					this.datosCliente);
		}else{
			throw new RuntimeException("Datos de negocio vacios, no se puede continuar!!!");
		}
		return this.datosGCSSIEnvelopeReq;
	}
	
	/**
	 * Retorna el nombre de la cola donde se deben colocar los requerimientos.
	 * La estructura estandar es <codigo_servicio> + '.REQ' 
	 * @return #{@link java.lang.String} con el nombre de la cola de requerimientos
	 */
	protected String getQueueNameReq(){
		return this.parametrosCliente.getCodigoServicio() + this.configuracionMQ.getDefinicionRequerimiento();
	}

	/**
	 * Retorna el nombre de la cola donde se extraen las respuestas.
	 * La estructura estandar es <codigo_servicio> + '.RESP' 
	 * @return #{@link java.lang.String} con el nombre de la cola de respuestas
	 */
	protected String getQueueNameResp(){
		return this.parametrosCliente.getCodigoServicio() + this.configuracionMQ.getDefinicionRespuesta();
	}

	/**
	 * Retorna la respuesta a la transacci�n s�ncrona con la estructura GCS_SI
	 * @return the datosGCSSIEnvelopeResp #{@link java.lang.String} con el xml de respuesta con estructura GCS_SI
	 */
	public String getDatosGCSSIEnvelopeResp() {
		return datosGCSSIEnvelopeResp;
	}
	
	/**
	 * Valida que no se envien parametros null al constructor pues todos son necesarios para el correcto funcionamineto
	 */
	protected abstract void assertConstructor();
	
	/**
	 * Extrae la secci�n Datos de la respuesta con estructura GCS_SI
	 * @param xml Respuesta con estructura GCS_SI
	 * @return #{@link java.lang.String} con la secci�n de Datos que corresponden al cliente
	 */
	public String obtenerDatosRespuesta(String xml){
		if(xml==null || xml.trim().equals("")){
			return new String();
		}
		String strNodeText = null;
		String strNodeStart = "<Datos>";
		String strNodeEnd = "</Datos>";
		int start = xml.indexOf(strNodeStart);
		int end = xml.lastIndexOf(strNodeEnd, xml.length());
		
		if (start >= 0 && end > 0) {
				strNodeText = xml.substring(start + strNodeStart.length(), end);
		}
		return strNodeText;
	}
	
	/**
	 * Establece las propiedades para que un mensaje sea parte de un grupo de mensajes en MQ
	 * @param aleatorio N�mero base para establecer el grupo
	 * @param message Mensaje MQ ({@link Message} � {@link MQMessage})
	 */
	public abstract void messageGroupSettings(int aleatorio, Object message);

}
