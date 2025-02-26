package com.ceiba.biblioteca.conectorCliente.MQ.cliente;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;

import com.ceiba.biblioteca.conectorCliente.MQ.contexto.ParametrosMensajeCliente;
import com.ceiba.biblioteca.utilitarios.utilitarios.ConstantesConector;
import com.ceiba.biblioteca.utilitarios.utilitarios.mq.ParametrosConfiguracionMQ;
import com.ceiba.biblioteca.utilitarios.utilitarios.mq.jms.JMSQueueManagerContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibm.jms.JMSTextMessage;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.jms.MQQueue;
import com.ibm.msg.client.jms.JmsConstants;


/**
 * Clase que implementa el envio de mensajes S�ncronos en modalidad cliente a trav�s de JMS.
 * @version 2.0
 * @author echicas
 */

public class MensajeSincronoJMS extends EnviarMensajeJMS implements Serializable {
	
	private static Log log = LogFactory.getLog(MensajeSincronoJMS.class);

	private static final long serialVersionUID = -3182006660947796860L;
	
	private String selector;

	public MensajeSincronoJMS(JMSQueueManagerContext jmsContext, ParametrosConfiguracionMQ parametros, ParametrosMensajeCliente headerCliente, String datosCliente) throws JMSException {
		super(jmsContext, parametros, headerCliente, datosCliente);
	}

	public int enviarMensajeJMS(int aleatorio, int priority) throws JMSException {
		try{
			messagePutJMS(getQueueNameReq(), getQueueNameResp(), aleatorio,priority);
			messageGetJMS(getQueueNameResp());
		}
		catch (JMSException e) {
			throw e;
		}
		return 0;
	}

	protected void messageGetJMS(String queueRespName) throws JMSException {
		QueueReceiver queueReceiver = null;
		try{
			MQQueue inQueue = (MQQueue) this.jmsContext.getSession().createQueue(queueRespName);
			String selector = this.selector==null?this.parametrosCliente.getSelectorJms():this.selector;
			log.info("Selector a utilizar: " + selector);
			queueReceiver = this.jmsContext.getSession().createReceiver(inQueue, selector);
			log.info("Esperando respuesta para " + this.parametrosCliente.getCodigoServicio());
			JMSTextMessage messageIn = (JMSTextMessage)queueReceiver.receive(this.parametrosCliente.getTiempoExpiracion() * ConstantesConector.MQ_GET_EXPIRY_FACTOR);
			if(messageIn!=null){
				log.info("Se obtuvo respuesta para " + this.parametrosCliente.getCodigoServicio());
				this.datosGCSSIEnvelopeResp = messageIn.getText();
				this.datosRespuesta = obtenerDatosRespuesta(this.datosGCSSIEnvelopeResp);
			}
			else{
				String error = "No se obtuvo respuesta durante " + this.parametrosCliente.getTiempoExpiracion() + " segundos para el servicio " + this.parametrosCliente.getCodigoServicio(); 
				log.error(error);
				throw new JMSException(error + "\n", "2033");
			}
		}
		finally{
			if(queueReceiver!=null){
				try{
					queueReceiver.close();
				}
				catch (JMSException e) {
					log.error("No se pudo cerrar el objeto QueueSender en la transacci�n para el servicio " + this.parametrosCliente.getCodigoServicio(), e.getLinkedException());
				}
			}
		}
	}

	protected void messagePutJMS(String queueReqName, String replyToQueueName, int aleatorio, int priority) throws JMSException {
		QueueSender sender = null;
		try{
			//creamos el acceso a la cola
			MQQueue queue = (MQQueue) this.jmsContext.getSession().createQueue(queueReqName);
			queue.setBooleanProperty(MQConstants.WMQ_MQMD_WRITE_ENABLED, true);
			
			//verificamos la persistencia del mensaje
			if(this.parametrosCliente.isMensajePersistente()){
				queue.setPersistence(JmsConstants.DELIVERY_PERSISTENT);
			}
			else{
				queue.setPersistence(JmsConstants.DELIVERY_NOT_PERSISTENT);
			}
			
			//establecemos prioridad y otros valores por defecto para la interaccion con la cola
			queue.setPriority(priority);
			setQueueDefaults(queue);
			queue.setExpiry(this.parametrosCliente.getTiempoExpiracion() * ConstantesConector.MQ_PUT_EXPIRY_FACTOR);
			
			//creamos el mensaje
			JMSTextMessage message = (JMSTextMessage) this.jmsContext.getSession().createTextMessage();
		
			//establecemos el tipo de mensaje MQ
			message.setIntProperty(JmsConstants.JMS_IBM_MQMD_MSGTYPE, MQConstants.MQMT_REQUEST);
			
			//a�adimos las propiedades de usuario
			addProperties2JMSMessage(message);
			
			//se a�aden las propiedades de grupo
			messageGroupSettings(aleatorio, message);
			
			message.setText(this.datosGCSSIEnvelopeReq);
			
			message.setJMSReplyTo(new MQQueue(replyToQueueName));
			
			//abrimos el canal para el sender
			sender = (QueueSender) this.jmsContext.getSession().createSender(queue);
			
			//se asume que connection.start(); ya fue ejecutado y se cerrar� por parte del usuario
			//se envia el mensaje
			sender.send(message);
			
			if(this.parametrosCliente.getSelectorJms()==null || this.parametrosCliente.getSelectorJms().equals("")){
				this.selector = "JMSCorrelationID = '" + message.getJMSMessageID() + "'";
			}
			
			log.info("Mensaje enviado a " + this.parametrosCliente.getCodigoServicio());
		}
		finally{
			if(sender!=null){
				try{
					sender.close();
				}
				catch (JMSException e) {
					log.error("No se pudo cerrar el objeto QueueSender en la transacci�n para el servicio " + this.parametrosCliente.getCodigoServicio(), e.getLinkedException());
				}
			}
		}
		
	}
	
	

}
