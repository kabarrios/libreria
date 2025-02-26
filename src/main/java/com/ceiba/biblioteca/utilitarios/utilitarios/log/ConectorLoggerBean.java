package com.ceiba.biblioteca.utilitarios.utilitarios.log;



import com.ceiba.biblioteca.utilitarios.utilitarios.BeanHelper;

import java.io.Serializable;

public class ConectorLoggerBean implements Serializable {
	
	private static final long serialVersionUID = -513404257840930984L;
	private String logClientReqSFTemplate;
	private String logClientReqRRTemplate;
	private String logClientRespRRTemplate;
	private String logServerReqSFTemplate;
	private String logServerReqRRTemplate;
	private String logServerRespRRTemplate;
	private String logMQErrorTemplante;
	private String logBackeneErrorTemplate;
	private String logFileTemplate;

	public ConectorLoggerBean() {
		super();
	}

	public String getLogClientReqSFTemplate() {
		return logClientReqSFTemplate;
	}

	public void setLogClientReqSFTemplate(String logClientReqSFTemplate) {
		this.logClientReqSFTemplate = logClientReqSFTemplate;
	}

	public String getLogClientReqRRTemplate() {
		return logClientReqRRTemplate;
	}

	public void setLogClientReqRRTemplate(String logClientReqRRTemplate) {
		this.logClientReqRRTemplate = logClientReqRRTemplate;
	}

	public String getLogClientRespRRTemplate() {
		return logClientRespRRTemplate;
	}

	public void setLogClientRespRRTemplate(String logClientRespRRTemplate) {
		this.logClientRespRRTemplate = logClientRespRRTemplate;
	}

	public String getLogServerReqSFTemplate() {
		return logServerReqSFTemplate;
	}

	public void setLogServerReqSFTemplate(String logServerReqSFTemplate) {
		this.logServerReqSFTemplate = logServerReqSFTemplate;
	}

	public String getLogServerReqRRTemplate() {
		return logServerReqRRTemplate;
	}

	public void setLogServerReqRRTemplate(String logServerReqRRTemplate) {
		this.logServerReqRRTemplate = logServerReqRRTemplate;
	}

	public String getLogServerRespRRTemplate() {
		return logServerRespRRTemplate;
	}

	public void setLogServerRespRRTemplate(String logServerRespRRTemplate) {
		this.logServerRespRRTemplate = logServerRespRRTemplate;
	}
		
	public String getLogFileTemplate() {
		return logFileTemplate;
	}
	public void setLogFileTemplate(String logFileTemplate) {
		this.logFileTemplate = logFileTemplate;
	}
	
	public String toString(){
		return BeanHelper.bean2String(this);
	}

	/**
	 * @return the logBackeneErrorTemplate
	 */
	public String getLogBackeneErrorTemplate() {
		return logBackeneErrorTemplate;
	}

	/**
	 * @param logBackeneErrorTemplate the logBackeneErrorTemplate to set
	 */
	public void setLogBackeneErrorTemplate(String logBackeneErrorTemplate) {
		this.logBackeneErrorTemplate = logBackeneErrorTemplate;
	}

	/**
	 * @return the logMQErrorTemplante
	 */
	public String getLogMQErrorTemplante() {
		return logMQErrorTemplante;
	}

	/**
	 * @param logMQErrorTemplante the logMQErrorTemplante to set
	 */
	public void setLogMQErrorTemplante(String logMQErrorTemplante) {
		this.logMQErrorTemplante = logMQErrorTemplante;
	}

	
}
