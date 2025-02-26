package com.ceiba.biblioteca.utilitarios.utilitarios.invocador.procesos.archivos;

import com.ceiba.biblioteca.utilitarios.utilitarios.ConstantesConector;
import com.ceiba.biblioteca.utilitarios.utilitarios.FileHelper;
import com.ceiba.biblioteca.utilitarios.utilitarios.ThrowableHelper;
import com.ceiba.biblioteca.utilitarios.utilitarios.invocador.beans.InteBackEnd;
import com.ceiba.biblioteca.utilitarios.utilitarios.invocador.procesos.BackendIntegracion;

import com.siman.Integracion.utilitarios.xml.SimpleXMLHelper;

import java.io.Serializable;

public class StoreFileBackEnd extends BackendIntegracion implements Serializable{
	
	private static final long serialVersionUID = 5039162108211542492L;
	private static final String NAME_FILE_TAG = "FileName";
	
	/**
	 * M�todo que guarda un archivo en el servidor donde es ejecutado
	 * @param backend
	 * @param data
	 * @param headerData
	 * @return String con la respuesta esperada por los servicios de integraci�n
	 */
	public String ejecutarServicio(InteBackEnd backend, String data, String headerData) {
		String file = backend.getFile().getDest() + SimpleXMLHelper.getValueTag(NAME_FILE_TAG, headerData);
		try{
			FileHelper.escribirArchivo(file, data);
		}
		catch(Exception e){
			return construirRespuesta(ConstantesConector.CODIGO_SERVICIO_ERROR_DATOS, "Error, el archivo " + file + " no pudo ser almacenado.- " + ThrowableHelper.stackTrace2String(e), new String());
		}
		return construirRespuesta(ConstantesConector.CODIGO_SERVICIO_EXITO, "Operaci�n de almacenado exitosa para el archivo " + file, new String());

	}

}
