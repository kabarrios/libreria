package com.ceiba.biblioteca.utilitarios.utilitarios.mq.jms;

import javax.jms.JMSException;
import javax.jms.QueueConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.Serializable;
import java.net.URL;

public class JMSQueueManagerContextjndi extends JMSQueueManagerContext implements Serializable {

	private static final long serialVersionUID = -8326441501804337344L;
	
	public void initConnection(String jndiResource) throws JMSException {
		try{
			Context initialContext = new InitialContext();
			mqConnFactory = (QueueConnectionFactory) initialContext.lookup(jndiResource);
		}
		catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public void initConnection(String queueManagerName, String channel, String host, int port) throws JMSException {
		throw new UnsupportedOperationException("Operaci�n no implementada en esta operacion");
	}

	public void initConnection(String queueManagerName, URL ccdt) throws JMSException {
		throw new UnsupportedOperationException("Operaci�n no implementada en esta operacion");
	}

	public void initConnection(String queueManagerName, URL ccdt, int clientReconnectTimeout) throws JMSException {
		throw new UnsupportedOperationException("Operaci�n no implementada en esta operacion");
	}

}
