package com.ceiba.biblioteca.utilitarios.utilitarios.invocador.procesos;

import com.ceiba.biblioteca.utilitarios.utilitarios.TemplateMessageHelper;
import com.ceiba.biblioteca.utilitarios.utilitarios.invocador.beans.InteBackEnd;


import java.io.Serializable;

public abstract class BackendIntegracion implements Serializable{

	private static final long serialVersionUID = 6236270562144778029L;
	protected static final String DATOS_VACIO = "";
	
	public abstract String ejecutarServicio(InteBackEnd backend, String data, String headerData);
	
	protected String construirRespuesta(String retcode, String retcodeDescription, String datos){
		return TemplateMessageHelper.getXmlRespuestaBackend(retcode, retcodeDescription , datos);
	}

}
