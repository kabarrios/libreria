package com.ceiba.biblioteca.utilitarios.utilitarios.mq;



import com.ceiba.biblioteca.utilitarios.utilitarios.BeanHelper;

import java.io.Serializable;

public class MQConfigBean implements Serializable {

	private static final long serialVersionUID = 7259224651955248046L;
	
	private String queueManagerName;
	private String queueManagerHost;
	private Integer queueManagerPort;
	private String queueManagerChannel;
	private Integer listenTimeOut;
	private String queueManagerReqQueue;
	private String prefixLogger;
	private String pathLogger;
	private String baseLogger;
	private String transactionLogger;
	private String errorTransactionLogger;
	private String invokerQueueConfiguration;
	private String invokerPathBackup;
	
	public MQConfigBean() {
		super();
	}

	public String getQueueManagerName() {
		return queueManagerName;
	}

	public void setQueueManagerName(String queueManagerName) {
		this.queueManagerName = queueManagerName;
	}

	public String getQueueManagerHost() {
		return queueManagerHost;
	}

	public void setQueueManagerHost(String queueManagerHost) {
		this.queueManagerHost = queueManagerHost;
	}

	public Integer getQueueManagerPort() {
		return queueManagerPort;
	}

	public void setQueueManagerPort(Integer queueManagerPort) {
		this.queueManagerPort = queueManagerPort;
	}

	public String getQueueManagerChannel() {
		return queueManagerChannel;
	}

	public void setQueueManagerChannel(String queueManagerChannel) {
		this.queueManagerChannel = queueManagerChannel;
	}

	public Integer getListenTimeOut() {
		return listenTimeOut;
	}

	public void setListenTimeOut(Integer listenTimeOut) {
		this.listenTimeOut = listenTimeOut;
	}

	public String getQueueManagerReqQueue() {
		return queueManagerReqQueue;
	}

	public void setQueueManagerReqQueue(String queueManagerReqQueue) {
		this.queueManagerReqQueue = queueManagerReqQueue;
	}

	public String getPrefixLogger() {
		return prefixLogger;
	}

	public void setPrefixLogger(String prefixLogger) {
		this.prefixLogger = prefixLogger;
	}

	public String getPathLogger() {
		return pathLogger;
	}

	public void setPathLogger(String pathLogger) {
		this.pathLogger = pathLogger;
	}

	public String getBaseLogger() {
		return baseLogger;
	}

	public void setBaseLogger(String baseLogger) {
		this.baseLogger = baseLogger;
	}

	public String getInvokerQueueConfiguration() {
		return invokerQueueConfiguration;
	}

	public void setInvokerQueueConfiguration(String invokerQueueConfiguration) {
		this.invokerQueueConfiguration = invokerQueueConfiguration;
	}

	public String getInvokerPathBackup() {
		return invokerPathBackup;
	}

	public void setInvokerPathBackup(String invokerPathBackup) {
		this.invokerPathBackup = invokerPathBackup;
	}

	public String getTransactionLogger() {
		return transactionLogger;
	}

	public void setTransactionLogger(String transactionLogger) {
		this.transactionLogger = transactionLogger;
	}

	public String getErrorTransactionLogger() {
		return errorTransactionLogger;
	}

	public void setErrorTransactionLogger(String errorTransactionLogger) {
		this.errorTransactionLogger = errorTransactionLogger;
	}

	//@Override
	public String toString(){
		return BeanHelper.bean2String(this);
	}

}
