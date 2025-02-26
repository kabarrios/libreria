package com.ceiba.biblioteca.utilitarios.utilitarios;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.*;


public class Messages implements Serializable {

	private static final long serialVersionUID = -2614731969279676121L;
	/**
	 * The Jakarta Commons Logging instance used for all logging.
	 */
	private static final Log log = LogFactory.getLog(Messages.class);
	
	/**
	 * The resource bundle holds all the messages.
	 */
	private static ResourceBundle messages;
	/**
	 * Once a format has been created once, it is cached here.
	 */
	private static Hashtable formats;
	
	private static Locale defaultLocale;
	
	 static{
	    setDefaultLocale();
	 }

	/**
	 * Hide default constructor of utility class.
	 */
	private Messages() {
		setDefaultLocale();
	}

	/**
	 * Set the default locale to use for loading messages. Calling this method
	 * will reload all the messages based on the new locale name.
	 */
	public static void setDefaultLocale() {
		setLocale(Locale.getDefault());
	}
	
	public static  void setDefaultLocale(String language, String country) {
		setLocale(new Locale(language, country));
	}
	
	public static void setDefaultLocale(String language){
		setLocale(new Locale(language, new String()));
	}
	
	public static Locale getDefaultLocale(){
		return defaultLocale;
	}

	/**
	 * Set the locale to use for loading messages. Calling this method will
	 * reload all the messages based on the new locale name.
	 * 
	 * @param locale
	 *            the locale for which a resource bundle is desired.
	 */
	public static void setLocale(Locale locale) {
		try {
			messages = ResourceBundle.getBundle(ConstantesConector.RESOURCE_NAME, locale);
		} 
		catch (Exception e) {
			messages = new EmptyResourceBundle(null);
			log.error("Failed to locate messages resource " + ConstantesConector.RESOURCE_NAME);
		}
		formats = new Hashtable();
	}

	/**
	 * Format the named message using any number of arguments 
	 * and return the full message text.
	 * 
	 * @param message 
	 * 			The message name
	 * @param args 
	 * 			Argument list
	 * @return The full message text
	 */
	public static String format(String message, Object[] args) {
		try {
			MessageFormat mf = (MessageFormat)formats.get(message);
			if (mf == null) {
				String msg;
				try {
					msg = messages.getString(message);
				} 
				catch (MissingResourceException e) {
					log.error(e);
					//throw new RuntimeException(e);
					return new String();
				}
				mf = new MessageFormat(msg);
				formats.put(message, mf);
			}
			return mf.format(args);
		} 
		catch (Exception e) {
			log.error("An internal error occured while processing message " + message,e);
			throw new RuntimeException("An internal error occured while processing message " + message);
			//return message;
		}
	}
	
	public static String message(String message, Locale locale){
		ResourceBundle bundle = ResourceBundle.getBundle(ConstantesConector.RESOURCE_NAME, locale);
		return bundle.getString(message);
	}

	/**
	 * Return the text of the named message without formatting.
	 * @param message The message name
	 * @return The full message text
	 */
	public static String message(String message) {
		try {
			return messages.getString(message);
		} 
		catch (MissingResourceException e) {
			log.error(e);
			throw new RuntimeException(e);
			//return message;
		}
	}

	/**
	 * A empty resource bundle.
	 */
	//@SuppressWarnings("unchecked")
	private static class EmptyResourceBundle extends ResourceBundle implements Enumeration {

		public EmptyResourceBundle(Messages messages) {
		}

		/**
		 * @see java.util.ResourceBundle.getKeys()
		 */
		public Enumeration getKeys() {
			return this;
		}

		/**
		 * @see java.util.ResourceBundle.handleGetObject(java.lang.String)
		 */
		protected Object handleGetObject(String name) {
			return "[Missing message " + name + "]";
		}

		/**
		 * @see java.util.Enumeration.hasMoreElements()
		 */
		public boolean hasMoreElements() {
			return false;
		}

		/**
		 * @see java.util.Enumeration.nextElement()
		 */
		public Object nextElement() {
			return null;
		}

	}
}