package com.ceiba.biblioteca.utilitarios.utilitarios.contingencias;

import com.ceiba.biblioteca.utilitarios.utilitarios.TemplateMessageHelper;
import com.ibm.jms.JMSTextMessage;
import com.ibm.mq.jms.MQQueue;
import com.ibm.msg.client.wmq.WMQConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.jms.*;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class AlarmaJMS implements Serializable {
	
	private static final long serialVersionUID = 2320113266689684172L;
	private static Log log = LogFactory.getLog(AlarmaJMS.class);

	public static boolean enviarAColaAlarmaMensajeJMS(QueueConnectionFactory mqConnFactory, String nombreColaAlarma, String header, String codigoError, String descripcionError, String datosRequerimiento, String datosRespuesta, Map jmsProperties){
		QueueConnection connection = null;
		QueueSession session = null;
		QueueSender sender = null;
		String xmlAlarma = TemplateMessageHelper.getXmlAlarma(header, codigoError, descripcionError, datosRequerimiento, datosRespuesta);
		try{
			connection = (QueueConnection) mqConnFactory.createQueueConnection();
			session = (QueueSession) connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			
			MQQueue queue = (MQQueue) session.createQueue(nombreColaAlarma);
			queue.setPersistence(WMQConstants.DELIVERY_PERSISTENT);
			queue.setPriority(5);
			queue.setExpiry(WMQConstants.WMQ_EXP_UNLIMITED);
			queue.setFailIfQuiesce(WMQConstants.WMQ_FIQ_YES);
			queue.setEncoding(WMQConstants.WMQ_ENCODING_NATIVE);
			
			sender = (QueueSender) session.createSender(queue);
			
			
			JMSTextMessage message = (JMSTextMessage) session.createTextMessage(xmlAlarma);
			message.setJMSExpiration(WMQConstants.WMQ_EXP_UNLIMITED);
			
			if(jmsProperties!=null && !jmsProperties.isEmpty()){
				for(Iterator entries = jmsProperties.entrySet().iterator(); entries.hasNext();){
					Entry entry = (Entry) entries.next();
					message.setObjectProperty((String)entry.getKey(), entry.getValue());
				}
			}
						
			connection.start();

			sender.send(message);
			
			return true;
		}
		catch (JMSException e) {
			log.error("No se pudo colocar el mensaje en la cola de alarma " + nombreColaAlarma, e);
		}
		finally{
			try{
				if(sender!=null){
					sender.close();
				}
				if(session!=null){
					session.close();
				}
				if(connection!=null){
					connection.close();
				}
			}
			catch (JMSException e) {
				log.error("Error al cerrar la conexi�n con jms", e);
			}
		}
		return false;
	}
	
	public static boolean enviarAColaAlarmaMensajeJMS(QueueConnectionFactory mqConnFactory, String nombreColaAlarma, String header, String codigoError, String descripcionError, String datosRequerimiento, Map jmsProperties){
		return enviarAColaAlarmaMensajeJMS(mqConnFactory, nombreColaAlarma, header, codigoError, descripcionError, datosRequerimiento, "", jmsProperties);
	}
	
}
