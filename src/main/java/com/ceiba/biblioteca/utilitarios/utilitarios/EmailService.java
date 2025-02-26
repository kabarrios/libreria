package com.ceiba.biblioteca.utilitarios.utilitarios;


import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.Serializable;
import java.util.Date;
import java.util.Properties;

public class EmailService implements Serializable {
	
	private static final long serialVersionUID = 4484941192021227811L;
	public static final String MAIL_SMTP_HOST_KEY = "mail.smtp.host";
	public static final String MAIL_SMTP_STARTTLS_ENABLE_KEY = "mail.smtp.starttls.enable";
	public static final String MAIL_SMTP_PORT_KEY ="mail.smtp.port";
	public static final String MAIL_SMTP_USER = "mail.smtp.user";
	public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
	public static final String X_PRIORITY = "X-Priority";
	public static boolean SESSION_DEBUG = false;
	
	private Properties emailProperties;
	private String from;
	private String replyTo;
	private String[] to;
	private String[] cc;
	private String[] bcc;
	private String priority;
	
	private Session session;
	
	public EmailService(){
		super();
	}
	
	public EmailService(String smtpHost, String smtpPort){
		this();
		initEmailProperties();
		this.emailProperties.put(MAIL_SMTP_HOST_KEY, smtpHost);
		this.emailProperties.put(MAIL_SMTP_PORT_KEY, smtpPort);
	}
	
	public EmailService(String smtpHost, String smtpPort, String smtpUser){
		this(smtpHost, smtpPort);
		this.emailProperties.put(MAIL_SMTP_USER, smtpUser);
	}
	
	public void sendEmail(String subject, String body, String mimeType){
		assertNotNullProperties(emailProperties);
		try{
			Message message = getMessage(getSession());
			message.setSubject(subject);
			message.setContent(body, mimeType);
			Transport.send(message);
		}
		catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	public void sendEmailWithAttachment(String subject, String body, String bodyMimeType, String attachmentFileName, byte[] attachmentContent, String attachmentMimiType){
		assertNotNullProperties(emailProperties);
		Multipart mimeMultipart;
		try{		
			mimeMultipart = new MimeMultipart();
			//add text part
			mimeMultipart.addBodyPart(getBodyPart(body, bodyMimeType));
			//add attachment part
			mimeMultipart.addBodyPart(getAttachementPart(attachmentFileName, attachmentContent, attachmentMimiType));

			Message message = getMessage(getSession());
			message.setSubject(subject);
			message.setContent(mimeMultipart);
			
			Transport.send(message);

		}
		catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public Session getSession() {
		if(this.session==null){
			this.session = Session.getDefaultInstance(emailProperties, null);
			this.session.setDebug(SESSION_DEBUG);
		}
		return this.session;
	}
	
	public Message getMessage(Session session) throws MessagingException{
		//TODO agregar la validacion de las direcciones de correo
		Message message = new MimeMessage(session);
		if(this.priority != null && !this.priority.equals("")){
			message.addHeader(X_PRIORITY, this.priority);
		}
		message.setFrom(new InternetAddress(from));
		message.setRecipients(Message.RecipientType.TO, getAddress(this.to));
		if(cc!=null && cc.length>0){
			message.setRecipients(Message.RecipientType.CC, getAddress(this.cc));
		}
		if(bcc!=null && bcc.length>0){
			message.setRecipients(Message.RecipientType.BCC, getAddress(this.bcc));
		}
		if(replyTo!=null && !replyTo.equals("")){
			message.setReplyTo(getAddress(new String[]{replyTo}));
		}
		message.setSentDate(new Date());
		return message;
	}
	
	public Address[] getAddress(String[] address) throws AddressException {
		StringBuffer buffer = new StringBuffer();
		for(int i=0;i<address.length;++i){
			buffer.append(address[i]);
			buffer.append(",");
		}
		return InternetAddress.parse(buffer.toString(), false);
	}
	
	public BodyPart getBodyPart(String body, String mimeType) throws MessagingException {
		BodyPart text = new MimeBodyPart();
		text.setText(body);
		text.setContent(body, mimeType);
		return text;
	}
	
	public BodyPart getAttachementPart(String fileName, byte[] content, String mimeType) throws MessagingException{
		BodyPart mimeAttachPart = new MimeBodyPart();
		mimeAttachPart.setDataHandler(new DataHandler(new ByteArrayDataSource(content,mimeType)));
		mimeAttachPart.setFileName(fileName);
		return mimeAttachPart;
	}

	public Properties getEmailProperties() {
		return emailProperties;
	}

	public void setEmailProperties(Properties emailProperties) {
		this.emailProperties = emailProperties;
	}
	
	public void setSmtpHost(String smtpHost){
		initEmailProperties();
		emailProperties.put(MAIL_SMTP_HOST_KEY, smtpHost);
	}
	
	public void setSmtpPort(String smtpPort){
		initEmailProperties();
		emailProperties.put(MAIL_SMTP_PORT_KEY, smtpPort);
	}
	
	public void setSmtpStarttlsEnableKey(boolean smtpStarttlsEnableKey){
		initEmailProperties();
		emailProperties.put(MAIL_SMTP_STARTTLS_ENABLE_KEY, new Boolean(smtpStarttlsEnableKey));
	}
	
	public void setSmtpUser(String smtpUser){
		initEmailProperties();
		emailProperties.put(MAIL_SMTP_USER, smtpUser);
	}
	
	public void setSmtpAuth(String smtpAuth){
		initEmailProperties();
		emailProperties.put(MAIL_SMTP_AUTH, smtpAuth);
	}
	
	public void initEmailProperties(){
		if(emailProperties==null){
			emailProperties = new Properties();
		}
	}
	
	private void assertNotNullProperties(Properties emailProperties){
		if(emailProperties==null || emailProperties.isEmpty()){
			throw new IllegalArgumentException("Mail configuration can not be null or empty");
		}
	}

	public String[] getBcc() {
		return bcc;
	}

	public void setBcc(String[] bcc) {
		this.bcc = bcc;
	}
	
	public void setBcc(String bcc){
		this.bcc = new String[]{bcc};
	}

	public String[] getCc() {
		return cc;
	}

	public void setCc(String[] cc) {
		this.cc = cc;
	}
	
	public void setCc(String cc) {
		this.cc = new String[]{cc};
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	public String[] getTo() {
		return to;
	}

	public void setTo(String[] to) {
		this.to = to;
	}
	
	public void setTo(String to) {
		this.to = new String[]{to};
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

}
