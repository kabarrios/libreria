package com.ceiba.biblioteca.utilitarios.utilitarios;



import org.apache.tomcat.util.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class StringHelper implements Serializable {
	
	private static final long serialVersionUID = -291990132133814479L;
	public static final String UTF_8 = "UTF-8";
	public static final String CR = "\\n";
    public static final String LF = "\\r";
    public static final String SPACE = "\\s+";
    public static final String PIPE = "|";

	public static InputStream string2InputStream(String text){
		try {
			return new ByteArrayInputStream(text.getBytes(UTF_8));
		} 
		catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static InputStream String2Stream(String text, String encoding){
		try {
			return new ByteArrayInputStream(text.getBytes(encoding));
		} 
		catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * Metodo que recibe una cadena de caracteres en Base64 y devuelve un <code>InputStream</code>.
	 * @param data cadena de caracteres en Base64
	 * @return <code>InputStream</code>
	 */
	public static InputStream base64String2InputStream(String data){
		try {
			byte[] imgBytes = new Base64().decode(data.getBytes());
			return new ByteArrayInputStream(imgBytes);
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
    
    public static String removeCharacter(String source, String regex) {
    	String regexAux = regex;
    	if(!regexAux.startsWith("(")) regexAux = "(" + regexAux;
    	if(!regexAux.endsWith(")")) regexAux = regexAux + ")";
    	String resp = source.replaceAll(regexAux, ""); 
    	return resp;
    }
    
    public static String removeCRLF(String source) {
    	return source.replaceAll("("+CR+PIPE+LF+")", "");
    }
    

}
