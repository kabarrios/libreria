package com.ceiba.biblioteca.utilitarios.utilitarios.invocador;

import com.ceiba.biblioteca.utilitarios.utilitarios.ConstantesConector;
import com.ceiba.biblioteca.utilitarios.utilitarios.TemplateMessageHelper;
import com.ceiba.biblioteca.utilitarios.utilitarios.ThrowableHelper;
import com.ceiba.biblioteca.utilitarios.utilitarios.invocador.beans.BackEndServices;
import com.ceiba.biblioteca.utilitarios.utilitarios.invocador.beans.InteBackEnd;
import com.ceiba.biblioteca.utilitarios.utilitarios.invocador.beans.Service;
import com.ceiba.biblioteca.utilitarios.utilitarios.xml.XMLDataBinding;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.security.InvalidParameterException;

public class InvocadorBackend implements Serializable{
	
	private static final Log log = LogFactory.getLog(InvocadorBackend.class);
	private static final long serialVersionUID = 7071315743112097351L;
	private static XMLDataBinding xmlbind;
	private BackEndServices backEnd;
	private static final String MAPPING_SERVICES = "mapeos/ServiciosMapping.xml";
	
	static{
		xmlbind = new XMLDataBinding();
		try{
		//	xmlbind.setXmlContext(new XMLContext());
			xmlbind.setMappingsToXmlContext(InvocadorBackend.class.getClassLoader().getResourceAsStream(MAPPING_SERVICES));
		}
		catch (Exception e) {
			log.fatal("Error al iniciar la configuraci�n del invocador",e);
			throw new RuntimeException(e);
		}
	}
	
	private InvocadorBackend(){
	}
	
	public InvocadorBackend(BackEndServices servicios){
		this.backEnd = servicios;
	}
	
	public InvocadorBackend(String xmlServicios){
		try{
			this.backEnd = (BackEndServices)xmlbind.xml2Object(xmlServicios, BackEndServices.class);
		}
		catch (Exception e) {
			log.fatal("Error al iniciar la configuraci�n del invocador", e);
			throw new RuntimeException("Error al iniciar la configuraci�n del invocador", e);
		}
	}

	public String execute(String codigoServicio, String datos){
		Service servicio;
		try{
			if(this.backEnd.getServices().containsKey(codigoServicio)){
				servicio = (Service)this.backEnd.getServices().get(codigoServicio);
				if(servicio.isEnabled()){
					log.info("Invocando "+servicio.getClazz()+"."+servicio.getMethod());
					return (String) MethodUtils.invokeExactMethod(Class.forName(servicio.getClazz()).newInstance(), servicio.getMethod(), datos);
				}
				else{
					log.info("El servicio " + servicio.getCodigoServicio() + " no esta activo");
					return TemplateMessageHelper.getXmlRespuestaBackend(ConstantesConector.CODIGO_SERVICIO_INACTIVO, "El servicio " + servicio.getCodigoServicio() + " no esta activo", datos);
				}
			}
			else{
				return TemplateMessageHelper.getXmlRespuestaBackend(ConstantesConector.CODIGO_SERVICIO_INACTIVO, "El servicio no se encuentra en la configuracion", datos);
			}
		}
		catch (Throwable e) {
			log.fatal("Error al iniciar la invocacion del servicio, no se puede continuar", e);
			return TemplateMessageHelper.getXmlRespuestaBackend(ConstantesConector.CODIGO_EXCEPCION_NO_CONTROLADA, "Error al iniciar la invocacion del servicio, no se puede continuar.- " + ThrowableHelper.stackTrace2String(e), new String());
		}
	}
	
	public String execute(String codigoServicio, String datos, String xmlHeader){
		Service servicio;
		try{
			if(this.backEnd.getServices().containsKey(codigoServicio)){
				servicio = (Service)this.backEnd.getServices().get(codigoServicio);
				if(servicio.isEnabled()){
					log.info("Invocando "+servicio.getClazz()+"."+servicio.getMethod());
					InteBackEnd backend = servicio.getBackend()==null?new InteBackEnd():servicio.getBackend();
					return (String)MethodUtils.invokeExactMethod(Class.forName(servicio.getClazz()).newInstance(), servicio.getMethod(), new Object[]{backend, datos, xmlHeader});
				}
				else{
					log.info("El servicio " + servicio.getCodigoServicio() + " no esta activo");
					return TemplateMessageHelper.getXmlRespuestaBackend(ConstantesConector.CODIGO_SERVICIO_INACTIVO, "El servicio " + servicio.getCodigoServicio() + " no esta activo", datos);
				}
			}
			else{
				return TemplateMessageHelper.getXmlRespuestaBackend(ConstantesConector.CODIGO_SERVICIO_INACTIVO, "El servicio no se encuentra en la configuracion", datos);
			}
		}
		catch (Throwable e) {
			log.fatal("Error al iniciar la invocacion del servicio, no se puede continuar", e);
			return TemplateMessageHelper.getXmlRespuestaBackend(ConstantesConector.CODIGO_EXCEPCION_NO_CONTROLADA, "Error al iniciar la invocacion del servicio, no se puede continuar.- " + ThrowableHelper.stackTrace2String(e), new String());
		}
	}
	
	public Service getServicio(String codigoServicio){
		return (Service)this.backEnd.getServices().get(codigoServicio);
	}


	/**
	 * @return the backEnd
	 */
	public BackEndServices getBackEnd() {
		return backEnd;
	}


	/**
	 * @param backEnd the backEnd to set
	 */
	public void setBackEnd(BackEndServices backEnd) {
		if(backEnd!=null){
			this.backEnd = backEnd;
		}
		else{
			throw new InvalidParameterException("La configuraci�n no puede ser null");
		}
	}

}
