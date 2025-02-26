package com.ceiba.biblioteca.utilitarios.utilitarios.mq.jms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.jms.*;
import java.io.Serializable;
import java.net.URL;

public abstract class JMSQueueManagerContext implements Serializable {
	
	private static Log log = LogFactory.getLog(JMSQueueManagerContext.class);
	private static final long serialVersionUID = -8818140861262321953L;
	protected QueueConnectionFactory mqConnFactory = null;
	protected QueueConnection connection = null;
	protected QueueSession session = null;
	
	public abstract void initConnection(String queueManagerName, String channel, String host, int port) throws JMSException;
	
	public abstract void initConnection(String queueManagerName, URL ccdt) throws JMSException;
	
	public abstract void initConnection(String queueManagerName, URL ccdt, int clientReconnectTimeout) throws JMSException;
	
	public abstract void initConnection(String jndiResource) throws JMSException;
	
	public QueueConnectionFactory getConnFactory() {
		return mqConnFactory;
	}

	public void setConnFactory(QueueConnectionFactory mqConnFactory) {
		this.mqConnFactory = mqConnFactory;
	}

	public QueueConnection getConnection() {
		try{
			if(this.connection==null){
				this.connection = (QueueConnection) mqConnFactory.createQueueConnection();
			}
		}
		catch (JMSException e) {
			throwLinkedException(e);
		}
		return connection;
	}

	public void setConnection(QueueConnection connection) {
		this.connection = connection;
	}

	public QueueSession getSession(boolean transacted) {
		try{
			if(this.session==null){
				this.session = (QueueSession) getConnection().createQueueSession(transacted, Session.SESSION_TRANSACTED);
			}
		}
		catch (JMSException e) {
			//throw new RuntimeException(e);
			throwLinkedException(e);
		}
		return session;
	}
	
	public QueueSession getSession() {
		return getSession(false);
	}

	public void setSession(QueueSession session) {
		this.session = session;
	}
	
	public void closeConnection(){
		try{
			if(this.session!=null){
				this.session.close();
			}
			if(this.connection!=null){
				this.connection.close();
			}
			log.info("Se ha cerrado la conexi�n al queue manager");
		}
		catch (JMSException e) {
			//throw new RuntimeException("Error al cerrar el objeto Session o Connection", e);
			throwLinkedException(e);
		}
	}
	
	public void throwLinkedException(JMSException e){
		if(e.getLinkedException()!=null){
			throw new RuntimeException(e.getLinkedException());
		}
		throw new RuntimeException(e);
	}

}
