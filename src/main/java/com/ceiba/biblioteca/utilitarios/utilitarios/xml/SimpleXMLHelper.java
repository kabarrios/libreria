package com.siman.Integracion.utilitarios.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;

public class SimpleXMLHelper implements Serializable {	

	private static final long serialVersionUID = -4099974683209465657L;
	private static final Log log = LogFactory.getLog(SimpleXMLHelper.class);
   
	public static String getValueTag(String nombreNodo, String xml){
		String strNodeText = new String();
		String strNodeStart = "<" + nombreNodo + ">";
		String strNodeEnd = "</" + nombreNodo + ">";
		try{
			int start = xml.indexOf(strNodeStart);
			int end = xml.indexOf(strNodeEnd, start);
			if (start >= 0 && end > 0){
			   strNodeText = xml.substring(start + strNodeStart.length(), end);
			}
		} 
		catch (Exception e) {
			log.error("Error al extraer los datos dentro del tag " + nombreNodo,e);
			//throw new RuntimeException("Error al extraer los datos dentro del tag " + p_strNode,e);
			return "";
		}
		return strNodeText;
	}
    
	/**
	 * 
	 * @param codigo
	 * @param descripcion
	 * @return
	 * @deprecated
	 */
	public static String construyeRespuesta(String codigo, String descripcion){
		return "<GCS_SI><retcode>" + codigo + "</retcode><retcodedescription>" + descripcion + "</retcodedescription><Datos/></GCS_SI>";
	}
	
	/**
	 * 
	 * @param codigo
	 * @param descripcion
	 * @param datos
	 * @return
	 * @deprecated
	 */
	public static String construyeRespuesta(String codigo, String descripcion, String datos){
		return "<GCS_SI><retcode>" + codigo + "</retcode><retcodedescription>" + descripcion + "</retcodedescription><Datos>" + datos + "</Datos></GCS_SI>";
	}
	
}