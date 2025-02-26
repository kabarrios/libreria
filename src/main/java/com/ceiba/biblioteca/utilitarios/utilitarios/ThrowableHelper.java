package com.ceiba.biblioteca.utilitarios.utilitarios;


import org.apache.commons.lang3.StringEscapeUtils;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

public class ThrowableHelper implements Serializable{

	private static final long serialVersionUID = -5083433007684405739L;

	public static String getDisplayStackTraceInformation(Throwable exception) {
		if (exception == null) return new String();
		StringBuffer buffer = new StringBuffer();
		String className;
		String packageName;
		String simpleClassName;
		StackTraceElement[] stackElements = exception.getStackTrace();
		for (int lcv = 0; lcv < stackElements.length; lcv++) {
			buffer.append("Filename: " + stackElements[lcv].getFileName() + " ");
			buffer.append("Line number: " + stackElements[lcv].getLineNumber() + " ");
			className = stackElements[lcv].getClassName();
			packageName = extractPackageName(className);
			simpleClassName = extractSimpleClassName(className);
			buffer.append("Package name: " + (packageName.equals("") ? " default package " : packageName) + " ");
			buffer.append("Full class name: " + className + " ");
			buffer.append("Simple class name: " + simpleClassName + " ");
			buffer.append("Unmunged class name: "  + unmungeSimpleClassName(simpleClassName) + " ");
			buffer.append("Direct class name: " + extractDirectClassName(simpleClassName) + " ");
			buffer.append("Method name: " + stackElements[lcv].getMethodName() + " ");
			buffer.append("Native method?: " + stackElements[lcv].isNativeMethod() + " ");
			buffer.append("toString(): " + stackElements[lcv].toString() + " ");
		}
		return buffer != null ? buffer.toString() : new String();
	}
	
	public static String extractPackageName (String fullClassName){
		if ((fullClassName==null) || (fullClassName.equals(""))){
			return "";
		}
		int lastDot = fullClassName.lastIndexOf ('.');
		if (0 >= lastDot){
			return "";
		}
		return fullClassName.substring (0, lastDot);
     }
	
	public static String extractSimpleClassName (String fullClassName) {
		if ((fullClassName==null) || (fullClassName.equals(""))){
			return "";
		}
		int lastDot = fullClassName.lastIndexOf ('.');
		if (0 > lastDot){
			return fullClassName;
		}
		return fullClassName.substring (++lastDot);
     }
	
	public static String extractDirectClassName (String simpleClassName){
		if ((null == simpleClassName) || (simpleClassName.equals(""))){
			return "";
		}
		int lastSign = simpleClassName.lastIndexOf ('$');
		if (lastSign >= 0){
			return simpleClassName;
		}
		return simpleClassName.substring (++lastSign);
    }
	
	public static String unmungeSimpleClassName (String simpleClassName){
		if ((null == simpleClassName) || (simpleClassName.equals(""))){
			return "";
		}
		return simpleClassName.replace ('$', '.');
	}
	
	public static String stackTrace2String(Throwable e){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return StringEscapeUtils.escapeXml(sw.toString());
	}
	
}
