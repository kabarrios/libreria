package com.ceiba.biblioteca.utilitarios.utilitarios.contingencias;

import com.ceiba.biblioteca.utilitarios.utilitarios.TemplateMessageHelper;
import com.ceiba.biblioteca.utilitarios.utilitarios.mq.MQBasicHelper;
import com.ibm.mq.MQQueueManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;

public class Alarma implements Serializable {
	
	private static Log log = LogFactory.getLog(Alarma.class);
	private static final long serialVersionUID = -5536483759601716459L;

	public static boolean enviarAColaAlarma(MQQueueManager queueManager, String nombreColaAlarma, String header, String codigoError, String descripcionError, String datosRequerimiento, String datosRespuesta){
		String xmlAlarma = null;
		try{
			xmlAlarma = TemplateMessageHelper.getXmlAlarma(header, codigoError, descripcionError, datosRequerimiento, datosRespuesta);
			MQBasicHelper.simpleSendMessage2Queue(queueManager, nombreColaAlarma, xmlAlarma, true, MQBasicHelper.getBasicMQPutMessageOptions());
			return true;
		}
		catch (Exception e) {
			log.error("No se pudo colocar el mensaje en la cola de alarma " + nombreColaAlarma, e);
		}
		return false;
	}
	
	public static boolean enviarAColaAlarma(MQQueueManager queueManager, String nombreColaAlarma, String header, String codigoError, String descripcionError, String datosRequerimiento){
		return enviarAColaAlarma(queueManager, nombreColaAlarma, header, codigoError, descripcionError, datosRequerimiento, "");
	}
	
}
