package com.ceiba.biblioteca.conectorCliente.MQ.cliente;

import java.io.Serializable;

import com.ceiba.biblioteca.conectorCliente.MQ.contexto.ParametrosMensajeCliente;
import com.ceiba.biblioteca.utilitarios.utilitarios.mq.ParametrosConfiguracionMQ;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;


/**
 * Clase que implementa el envio de mensajes As�ncronos en modalidad cliente a trav�s de MQI.
 * @version 2.0
 * @author echicas
 */
public class MensajeAsincrono extends EnviarMensajeMQI implements Serializable {

	private static final long serialVersionUID = -7065634597707494244L;
	
	private static Log log = LogFactory.getLog(MensajeAsincrono.class);
	
	/**
	 * Inicializa las configuraciones necesarias para el envio de mensajes mediante MQI a Websphere MQ.
	 * @param mqQueueManager Instancia con la conexi�n a Websphere MQ abierta.
	 * @param parametros Configuraci�n del Conector Cliente
	 * @param headerCliente Parametros proporcionados por el usuario para interactuar con Websphere MQ
	 * @param datosCliente �rea de datos del cliente
	 */
	public MensajeAsincrono(MQQueueManager mqQueueManager, ParametrosConfiguracionMQ parametros, ParametrosMensajeCliente headerCliente, String datosCliente) {
		super(mqQueueManager, parametros, headerCliente, datosCliente);
	}
	
	public int enviarMensaje(int aleatorio, int priority) throws Exception, MQException {
		try{
			messagePut(getQueueNameReq(), null, aleatorio, priority);
		}
		catch (MQException e) {
			//log.fatal("Error MQ al enviar el mensaje, favor verifique con su administrador de aplicaciones",e);
			throw e;
		}
		catch (Exception e) {
			//log.fatal("Error al enviar el mensaje, favor verifique con su administrador de aplicaciones",e);
			throw e;
		}
		return 0;
	}

	protected void messageGet(String queueRespName) throws Exception, MQException {
		throw new UnsupportedOperationException("Operaci�n no permitida en envios asincronos");
	}

	protected void messagePut(String queueReqName, String replyToQueueName, int aleatorio, int priority) throws Exception, MQException {
		//creamos el mensaje basico
		MQMessage mqMessage = new MQMessage();
		mqMessage.clearMessage();
		mqMessage.messageId = CMQC.MQMI_NONE;
		mqMessage.format = CMQC.MQFMT_STRING;
		mqMessage.applicationIdData = this.parametrosCliente.getCodigoServicio() + "-" + this.parametrosCliente.getNumeroTransaccion();
		mqMessage.priority = priority;
		
		//iniciamos la opciones para el put del mensaje
		MQPutMessageOptions pmo = new MQPutMessageOptions();
		pmo.options = CMQC.MQOO_SET_IDENTITY_CONTEXT;
		
		//manejamos los grupos de mensajes
		if(this.parametrosCliente.getGrupoMensaje()!=null && !this.parametrosCliente.getGrupoMensaje().equals("")){
			pmo.options += CMQC.MQPMO_SYNCPOINT;
			messageGroupSettings(aleatorio, mqMessage);
		}
		
		//verificamos la persistencia del mensaje
		if(this.parametrosCliente.isMensajePersistente()){
			mqMessage.persistence = CMQC.MQPER_PERSISTENT;
		}
		else{
			mqMessage.persistence = CMQC.MQPER_NOT_PERSISTENT;
		}
		
		//seteamos la expiracion del mensaje
		mqMessage.expiry = CMQC.MQEI_UNLIMITED;
		
		// modificado por Ernesto Chicas 20081125
		// se setea el tipo de mensaje a datagrama pues el mensaje es S&F
		mqMessage.messageType = CMQC.MQMT_DATAGRAM;
		
		mqMessage.writeString(this.datosGCSSIEnvelopeReq);

		/*
		// nos conectamos a la cola
		mqQueue = mqQueueManager.accessQueue(queueReqName, openOptionsReq);
		//colocamos el mensaje
		mqQueue.put(mqMessage, pmo);
		*/
		mqQueueManager.put(queueReqName, mqMessage, pmo);
		
		log.info("Mensaje enviado a " + this.parametrosCliente.getCodigoServicio());
		
		//verificamos si era un grupo de mensajes y hacemos commit
		if (this.parametrosCliente.getGrupoMensaje().equals(this.parametrosCliente.getTotalMensajes())){
			mqQueueManager.commit();
		}
		
	}

}
