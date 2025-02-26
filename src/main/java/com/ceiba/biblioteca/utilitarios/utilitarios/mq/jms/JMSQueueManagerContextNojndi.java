package com.ceiba.biblioteca.utilitarios.utilitarios.mq.jms;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

import javax.jms.JMSException;
import java.net.URL;

public class JMSQueueManagerContextNojndi extends JMSQueueManagerContext {

	private static final long serialVersionUID = 5789249884213834799L;

	public void initConnection(String queueManagerName, String channel, String host, int port) throws JMSException {
		MQQueueConnectionFactory temp = new MQQueueConnectionFactory();
		temp.setTransportType(WMQConstants.WMQ_CM_CLIENT);
		temp.setHostName(host);
		temp.setQueueManager(queueManagerName);
		temp.setPort(port);
		temp.setChannel(channel);
		this.mqConnFactory = temp;
	}

	public void initConnection(String jndiResource) throws JMSException {
		throw new UnsupportedOperationException("Operaci�n no implementada en esta operacion");
	}

	public void initConnection(String queueManagerName, URL ccdt) throws JMSException {
		MQQueueConnectionFactory factory = new MQQueueConnectionFactory();
		factory.setTransportType(WMQConstants.WMQ_CM_BINDINGS_THEN_CLIENT);
		factory.setClientReconnectOptions(WMQConstants.WMQ_CLIENT_RECONNECT);
		factory.setCCDTURL(ccdt);
		factory.setQueueManager(queueManagerName);
		this.mqConnFactory = factory;
	}
	
	public void initConnection(String queueManagerName, URL ccdt, int clientReconnectTimeout) throws JMSException {
		MQQueueConnectionFactory factory = new MQQueueConnectionFactory();
		factory.setTransportType(WMQConstants.WMQ_CM_BINDINGS_THEN_CLIENT);
		factory.setClientReconnectOptions(WMQConstants.WMQ_CLIENT_RECONNECT);
		factory.setCCDTURL(ccdt);
		factory.setQueueManager(queueManagerName);
		factory.setClientReconnectTimeout(clientReconnectTimeout);
		this.mqConnFactory = factory;
	}
	
}
