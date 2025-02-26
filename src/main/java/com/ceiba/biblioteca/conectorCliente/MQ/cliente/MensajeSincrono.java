package com.ceiba.biblioteca.conectorCliente.MQ.cliente;

import java.io.Serializable;

import com.ceiba.biblioteca.conectorCliente.MQ.contexto.ParametrosMensajeCliente;
import com.ceiba.biblioteca.utilitarios.utilitarios.ConstantesConector;
import com.ceiba.biblioteca.utilitarios.utilitarios.mq.ParametrosConfiguracionMQ;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;


/**
 * Clase que implementa el envio de mensajes S�ncronos en modalidad cliente a trav�s de MQI.
 * @version 2.0
 * @author echicas
 */
public class MensajeSincrono extends EnviarMensajeMQI implements Serializable {
	
	private static Log log = LogFactory.getLog(MensajeSincrono.class);

	private static final long serialVersionUID = 1417598786574318815L;
	
	private MQMessage mqMessageReq = null;
	
	
	public MensajeSincrono(MQQueueManager mqQueueManager, ParametrosConfiguracionMQ parametros, ParametrosMensajeCliente headerCliente, String datosCliente) {
		super(mqQueueManager, parametros, headerCliente, datosCliente);
	}

	public int enviarMensaje(int aleatorio, int priority) throws Exception, MQException {
		try{
			messagePut(getQueueNameReq(), getQueueNameResp(), aleatorio, priority);
			messageGet(getQueueNameResp());
		}
		catch (MQException e) {
			//log.fatal("Error MQ al enviar/recibir el mensaje para el servicio " + parametrosCliente.getCodigoServicio() +  ", favor verifique con su administrador de aplicaciones",e);
			throw e;
		}
		catch (Exception e) {
			//log.fatal("Error al enviar el mensaje, favor verifique con su administrador de aplicaciones",e);
			throw e;
		}
		return 0;
	}

	protected void messageGet(String queueRespName) throws Exception, MQException {
		MQQueue mqQueueResp = null;
		try{
			//iniciamos la opciones para el get del mensaje
			MQGetMessageOptions gmo = new MQGetMessageOptions();
			gmo.options = openOptionsResp;
			gmo.waitInterval = this.parametrosCliente.getTiempoExpiracion() * ConstantesConector.MQ_GET_EXPIRY_FACTOR;
			
			//Build the message
			MQMessage inMsg = new MQMessage();
			inMsg.correlationId = mqMessageReq.messageId;
			// Get the message from the queue
			mqQueueResp = mqQueueManager.accessQueue(queueRespName, openOptionsResp, this.configuracionMQ.getQueueManager(), null, null);
			log.info("Esperando respuesta para " + this.parametrosCliente.getCodigoServicio());
			mqQueueResp.get(inMsg, gmo);
			log.info("Se obtuvo respuesta para " + this.parametrosCliente.getCodigoServicio());
			this.datosGCSSIEnvelopeResp = inMsg.readStringOfByteLength(inMsg.getMessageLength());
			this.datosRespuesta = obtenerDatosRespuesta(this.datosGCSSIEnvelopeResp);
			
		}
		finally{
			if(mqQueueResp!=null){
				mqQueueResp.close();
			}
		}
	}

	protected void messagePut(String queueReqName, String replyToQueueName, int aleatorio, int priority) throws Exception, MQException {
		//creamos el mensaje basico
		mqMessageReq = new MQMessage();
		mqMessageReq.format = CMQC.MQFMT_STRING;
		mqMessageReq.applicationIdData = this.parametrosCliente.getCodigoServicio() + "-" + this.parametrosCliente.getNumeroTransaccion();
		mqMessageReq.clearMessage();
		mqMessageReq.priority = priority;
		
		//iniciamos la opciones para el put del mensaje
		MQPutMessageOptions pmo = new MQPutMessageOptions();
		pmo.options = CMQC.MQPMO_SET_IDENTITY_CONTEXT;

		//manejamos los grupos de mensajes
		if(this.parametrosCliente.getGrupoMensaje()!=null && !this.parametrosCliente.getGrupoMensaje().equals("")){
			pmo.options += CMQC.MQPMO_SYNCPOINT;
			messageGroupSettings(aleatorio, mqMessageReq);
		}
		
		// verificamos la persistencia del mensaje
		if(this.parametrosCliente.isMensajePersistente()){
			mqMessageReq.persistence = CMQC.MQPER_PERSISTENT;
		}
		else{
			mqMessageReq.persistence = CMQC.MQPER_NOT_PERSISTENT;
		}
		
		//seteamos el tiempo de expiracion
		mqMessageReq.expiry = this.parametrosCliente.getTiempoExpiracion() * ConstantesConector.MQ_PUT_EXPIRY_FACTOR;
		
		//direccionamos la respuesta
		mqMessageReq.replyToQueueName = replyToQueueName;
		mqMessageReq.replyToQueueManagerName = this.configuracionMQ.getQueueManager();
		
		// modificado por Ernesto Chicas 20081125
		// se setea el tipo de mensaje a request pues el mensaje es R&R
		mqMessageReq.messageType = CMQC.MQMT_REQUEST;
		
		mqMessageReq.writeString(this.datosGCSSIEnvelopeReq);
		
		/*
		// nos conectamos a la cola
		mqQueue = this.mqQueueManager.accessQueue(queueReqName, openOptionsReq, null, null, null);
		//colocamos el mensaje
		mqQueue.put(mqMessage, pmo);
		*/
		mqQueueManager.put(queueReqName, mqMessageReq, pmo);
		
		//verificamos si era un grupo de mensajes y hacemos commit
		if (this.parametrosCliente.getGrupoMensaje().equals(this.parametrosCliente.getTotalMensajes())){
			mqQueueManager.commit();
		}
		log.info("Mensaje enviado a " + this.parametrosCliente.getCodigoServicio());
	}

}
