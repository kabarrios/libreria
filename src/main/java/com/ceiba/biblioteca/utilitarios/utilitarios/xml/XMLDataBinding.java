package com.ceiba.biblioteca.utilitarios.utilitarios.xml;


import org.hibernate.cfg.annotations.reflection.XMLContext;
import org.xml.sax.InputSource;

import java.io.*;
import java.rmi.MarshalException;
import java.util.Iterator;
import java.util.List;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;


public class XMLDataBinding implements Serializable {

	public static final String DECLARACION_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	private static final long serialVersionUID = 2477776803008589292L;
    private boolean ignoreExtraElements;
    private boolean ignoreExtraAttributes;
    private boolean whiteSpacePreserve;
    private XMLContext xmlContext;
    private List listaMapping;
	private boolean validate;
    
    public XMLDataBinding(){
        this.ignoreExtraElements = false;
        this.ignoreExtraAttributes = false;
        this.whiteSpacePreserve = true;
        this.validate = true;
    }
        
    public Object xml2Object(String xml, Class clazz) throws MarshalException, ValidationException{
    	validateXMLContext();
    	Unmarshaller unmarshaller = new Unmarshaller();
        unmarshaller.setWhitespacePreserve(this.whiteSpacePreserve);
        unmarshaller.setIgnoreExtraElements(this.ignoreExtraElements);
        unmarshaller.setIgnoreExtraAttributes(this.ignoreExtraAttributes);
        unmarshaller.setValidation(this.validate);
        unmarshaller.setClass(clazz);
		return null;
       // return unmarshaller.unmarshal(new StringReader(xml));
    }
    
    public static Object simpleXML2Object(String xml, Class clazz) throws MarshalException, ValidationException{
		return null;
		//return Unmarshaller.unmarshal(clazz, new StringReader(xml));
    }
    
    public String object2XML(Object object) throws IOException, MarshalException, ValidationException{
    	validateXMLContext();
    	StringWriter writer = new StringWriter();
//        Marshaller marshaller = this.xmlContext.createMarshaller();
//        marshaller.setWriter(writer);
//        marshaller.marshal(object);
		return null;
       // return writer.getBuffer().toString();
    }
    
    public static String simpleObject2XML(Object object) throws MarshalException, ValidationException{
    	StringWriter writer = new StringWriter();
    //	Marshaller.marshal(object, writer);
    	return writer.getBuffer().toString();
    }
    
    private void validateXMLContext(){
    	if(this.xmlContext == null){
    		throw new IllegalStateException("El contexto xml de castor debe ser inicializado"); 
    	}
    }
    
    public static String removeXMLDeclaration(String xml){
    	if(xml.startsWith(DECLARACION_XML)){
    		return xml.substring(DECLARACION_XML.length() + 1);
    	}
    	return xml;
    }
    
	public boolean isIgnoreExtraAttributes() {
		return ignoreExtraAttributes;
	}

	public void setIgnoreExtraAttributes(boolean ignoreExtraAttributes) {
		this.ignoreExtraAttributes = ignoreExtraAttributes;
	}

	public boolean isIgnoreExtraElements() {
		return ignoreExtraElements;
	}

	public void setIgnoreExtraElements(boolean ignoreExtraElements) {
		this.ignoreExtraElements = ignoreExtraElements;
	}

	public boolean isWhiteSpacePreserve() {
		return whiteSpacePreserve;
	}

	public void setWhiteSpacePreserve(boolean whiteSpacePreserve) {
		this.whiteSpacePreserve = whiteSpacePreserve;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}
	
	public XMLContext getXmlContext() {
		return xmlContext;
	}

	public void setXmlContext(XMLContext xmlContext) {
		this.xmlContext = xmlContext;
	}
	
	public void setMappingsToXmlContext(List mappings){
		Mapping mapping;
//		try{
//			for (Iterator iter = mappings.iterator(); iter.hasNext();) {
//				mapping = this.xmlContext.createMapping();
//				mapping.loadMapping(new InputSource((String)iter.next()));
//				this.xmlContext.addMapping(mapping);
//			}
//		}
//		catch (MappingException e) {
//			throw new RuntimeException("Error al cargar un mapping", e);
//		}
	}
	
	public void setMappingsToXmlContext(String urlMappings){
		Mapping mapping;
//		try{
//		//	mapping = this.xmlContext.createMapping();
//			mapping.loadMapping(new InputSource(urlMappings));
//			this.xmlContext.addMapping(mapping);
//		}
//		catch (MappingException e) {
//			throw new RuntimeException("Error al cargar un mapping", e);
//		}
	}
	
	public void setMappingsToXmlContext(InputStream mapping){
		Mapping mappingTemp;
//		try{
//			mappingTemp = this.xmlContext.createMapping();
//			mappingTemp.loadMapping(new InputSource(mapping));
//			this.xmlContext.addMapping(mappingTemp);
//		}
//		catch (MappingException e) {
//			throw new RuntimeException("Error al cargar un mapping", e);
//		}
	}
	
	

	public List getListaMapping() {
		return listaMapping;
	}

	public void setListaMapping(List listaMapping) {
		this.listaMapping = listaMapping;
	}
   
}