package com.ceiba.biblioteca.utilitarios.utilitarios.invocador.procesos;



import com.ceiba.biblioteca.utilitarios.utilitarios.TemplateMessageHelper;

import java.io.Serializable;

public abstract class BackendSupportBase implements Serializable {
	
	private static final long serialVersionUID = -1651301000774972191L;

	public abstract String ejecutarServicio(String data);
	
	protected String construirRespuesta(String retcode, String description, String datos){
		return TemplateMessageHelper.getXmlRespuestaBackend(retcode, description , datos);
	}

}
