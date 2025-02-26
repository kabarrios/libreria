package com.ceiba.biblioteca.utilitarios.utilitarios.mq;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQPoolToken;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.MQConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;

public class QueueManagerContext implements Serializable {

	private static Log log = LogFactory.getLog(QueueManagerContext.class);
	private static final long serialVersionUID = 7113871924229884916L;
	
	protected MQQueueManager queueManager = null;
	protected MQPoolToken token = null;
	
	public QueueManagerContext(){
		super();
	}

	/**
	 * Inicia la conexion a un Queue Manager
	 * @param queueManagerName Nombre del queue manager
	 * @param channel nombre del canal servidor
	 * @param host direccion ip o nombre del servidor donde se encuentra el queue manager
	 * @param port puerto de escucha del queue manager
	 * @throws MQException
	 */
	public void initConnection(String queueManagerName, String channel, String host, int port) throws MQException{
		MQEnvironment.hostname = host;
		MQEnvironment.port = port;
		MQEnvironment.channel = channel;
		MQEnvironment.properties.put(MQConstants.TRANSPORT_PROPERTY, MQConstants.TRANSPORT_MQSERIES);
		
		try {
			token = MQEnvironment.addConnectionPoolToken();
			queueManager = new MQQueueManager(queueManagerName);
			log.info("Se ha establecido la conexi�n con " + queueManagerName);
		} catch (MQException e) {
			//log.fatal("Error al conectarse al queue manager " + queueManagerName,e);
			throw e;
		}
	}
	
	/**
	 * Inicia la conexion a un Queue Manager
	 * @param queueManagerName Nombre del queue manager
	 * @param channel nombre del canal servidor
	 * @param host direccion ip o nombre del servidor donde se encuentra el queue manager
	 * @param port puerto de escucha del queue manager
	 * @param bindingMode true para conexi�n en modo binding, false para conexi�n en modo client
	 * @throws MQException
	 */
	public void initConnection(String queueManagerName, String channel, String host, int port, boolean bindingMode) throws MQException{
		if(bindingMode){
			MQEnvironment.hostname = null;
			log.info("La conexi�n a MQ ser� en modo BINDING");
		}
		else{
			MQEnvironment.hostname = host;
			log.info("La conexi�n a MQ ser� en modo CLIENT");
		}
		MQEnvironment.port = port;
		MQEnvironment.channel = channel;
		MQEnvironment.properties.put(MQConstants.TRANSPORT_PROPERTY, MQConstants.TRANSPORT_MQSERIES);
		
		try {
			token = MQEnvironment.addConnectionPoolToken();
			queueManager = new MQQueueManager(queueManagerName);
			log.info("Se ha establecido la conexi�n con " + queueManagerName);
		} catch (MQException e) {
			log.fatal("Error al conectarse al queue manager " + queueManagerName,e);
			throw e;
		}
	}
	
	/**
	 * Cerrar conexi�n con un queue manager
	 */
	public void closeQManager() {
		try {
			MQBasicHelper.closeQManager(queueManager, token);
			//MQBasicHelper.closeQManager(queueManager);
			log.info("Se ha cerrado la conexi�n a " + queueManager.getName());
		} catch (MQException e) {
			log.fatal("Error al cerrar el contexto de MQ",e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Cerrar conexi�n a un queue manager sin hacer des...orden
	 */
	public void closeQManagerQuietly() {
		try {
			closeQManager();
			log.info("Se ha cerrado la conexi�n a " + queueManager.getName());
		} catch (Exception e) {
			log.error("Error al cerrar el contexto de MQ",e);
		}
	}

	/**
	 * @return the queueManager
	 */
	public MQQueueManager getQueueManager() {
		return queueManager;
	}

}