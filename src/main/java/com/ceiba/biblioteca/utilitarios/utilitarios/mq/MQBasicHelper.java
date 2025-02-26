package com.ceiba.biblioteca.utilitarios.utilitarios.mq;

import com.ibm.mq.*;
import com.ibm.mq.constants.MQConstants;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import static com.ceiba.biblioteca.utilitarios.utilitarios.mq.MQReasonCodeResolver.resolve;

public class MQBasicHelper implements Serializable {

	private static final long serialVersionUID = -1819846654943368810L;
	
	/**
	 * 
	 * @param mqQueueManager
	 * @param queueName
	 * @param data
	 * @param persistence
	 * @param putMessageOptions
	 * @throws IOException
	 * @throws MQException
	 */
	public static void simpleSendMessage2Queue(MQQueueManager mqQueueManager, String queueName, String data, boolean persistence, MQPutMessageOptions putMessageOptions) throws IOException, MQException{
		MQMessage message = getBasicMQMessage();
		message.writeString(data);
		if(persistence){
			message.persistence = MQConstants.MQPER_PERSISTENT;
		}
		else{
			message.persistence = MQConstants.MQPER_NOT_PERSISTENT;
		}
		mqQueueManager.put(queueName, message, putMessageOptions);
		mqQueueManager.commit();
	}
	
	public static void simpleSendMessage2Queue(String queueManagerName, String channel, String host, int port, String queueName, String data, boolean persistence, MQPutMessageOptions putMessageOptions) throws IOException, MQException{
		MQQueueManager qmgr = getConnection(queueManagerName, channel, host, port);
		simpleSendMessage2Queue(qmgr, queueName, data, persistence, putMessageOptions);
	}
	
	public static MQQueueManager getConnection(String queueManagerName, String channel, String host, int port) throws MQException{
		MQEnvironment.hostname = host;
		MQEnvironment.port = port;
		MQEnvironment.channel = channel;
		MQEnvironment.properties.put(MQConstants.TRANSPORT_PROPERTY, MQConstants.TRANSPORT_MQSERIES);
		
		try {
			//token = MQEnvironment.addConnectionPoolToken();
			return new MQQueueManager(queueManagerName);
		} catch (MQException e) {
			throw e;
		}
	}
	
	/**
	 * Crea una instancia de MQMessage con valores por defecto
	 * @return instancia de MQMessage para ser manipulada
	 */
	public static MQMessage getBasicMQMessage(){
		MQMessage message = new MQMessage();
		message.messageType = MQConstants.MQMT_DATAGRAM;
		message.format = MQConstants.MQFMT_STRING;
		message.expiry = MQConstants.MQEI_UNLIMITED;
		message.messageId = MQConstants.MQMI_NONE;
		message.persistence = MQConstants.MQPER_PERSISTENT;
		return message;
	}
	
	public static MQPutMessageOptions getBasicMQPutMessageOptions(){
		MQPutMessageOptions pmo = new MQPutMessageOptions();
		pmo.options = MQConstants.MQPMO_SET_IDENTITY_CONTEXT;
		return pmo;
	}
	
	public static boolean isDatagramMessage(MQMessage message){
		if(message.messageType == MQConstants.MQMT_DATAGRAM){
			return true;
		}
		return false;
	}
	
	public static boolean isRequestMessage(MQMessage message){
		if(message.messageType == MQConstants.MQMT_REQUEST){
			return true;
		}
		return false;
	}
	
	public static String getIdAsString(byte[] id){
		StringBuffer resultado = new StringBuffer();
		byte temp;
		for(int i=0, l=id.length; i<l; i++){
			temp = id[i];
			resultado.append(Integer.toString((temp & 0xff) + 0x100, 16).substring(1));
		}
		return resultado.toString();
	}
	
	public static String getMessageTypeAsString(MQMessage message){
		return Integer.toString(message.messageType);
	}
	
	public static String lookupReasonCodeWrapper(MQException exception){
		return resolve(exception);
	}

	public static Object[] getMQExceptionInfo(MQException exception){
		ArrayList exceptionInfo = new ArrayList();
		exceptionInfo.add(new Integer(exception.reasonCode));
		exceptionInfo.add(lookupReasonCodeWrapper(exception));
		exceptionInfo.add(new Integer(exception.completionCode));
		exceptionInfo.add(exception.getMessage());
		return exceptionInfo.toArray();
	}
	
	public static void closeQManager(MQQueueManager queueManager, MQPoolToken token) throws MQException {
		try {
			if(queueManager!=null){
				queueManager.disconnect();
				queueManager.close();
			}
			if(token!=null){
				MQEnvironment.removeConnectionPoolToken(token);
			}
		} catch (MQException e) {
			throw e;
		}
	}
	
	public static void closeQManager(MQQueueManager queueManager) throws MQException  {
		closeQManager(queueManager,null);
	}
	
	public static void closeQueue(MQQueue queue) throws MQException{
		if(queue!=null){
			queue.close();
		}
	}
	
	public static String getMQMessageFromQueueManager(MQQueueManager queueManager, String queueName, int timeOut) throws IOException, MQException{
		String configuracion = null;
	    MQQueue queue = null;
	    MQGetMessageOptions gmo = new MQGetMessageOptions();
	    int qOptions = MQConstants.MQOO_INPUT_AS_Q_DEF | MQConstants.MQOO_OUTPUT | MQConstants.MQOO_INQUIRE;
	    MQMessage message;
	    try {
	    	queue = queueManager.accessQueue(queueName, qOptions);
	    	gmo.options = MQConstants.MQGMO_WAIT | MQConstants.MQGMO_CONVERT | MQConstants.MQGMO_FAIL_IF_QUIESCING;
	    	gmo.waitInterval = timeOut;
	    	message = new MQMessage();
	    	queue.get(message, gmo);
	    	configuracion = message.readLine();
	    }
	    finally {
	    	closeQueue(queue);
	    }
	    return configuracion;
	}

}
