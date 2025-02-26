package com.ceiba.biblioteca.utilitarios.utilitarios.invocador.procesos.archivos;

import com.ceiba.biblioteca.utilitarios.utilitarios.ConstantesConector;
import com.ceiba.biblioteca.utilitarios.utilitarios.FileHelper;
import com.ceiba.biblioteca.utilitarios.utilitarios.ThrowableHelper;
import com.ceiba.biblioteca.utilitarios.utilitarios.invocador.beans.InteBackEnd;
import com.ceiba.biblioteca.utilitarios.utilitarios.invocador.procesos.BackendIntegracion;
import com.siman.Integracion.utilitarios.xml.SimpleXMLHelper;

import java.io.Serializable;

public class StoreFileBackEndTokenBase64  extends BackendIntegracion implements Serializable{

	private static final long serialVersionUID = -2198876279228793701L;
	private static final String NAME_FILE_TAG = "token";
	
	/**
	 * Alamacena un archivo en la ruta especificada en la configuraci�n y con el nombre enviado
	 * en el tag token del header del requerimiento, el string debe estar en base64 para poder ser
	 * decodificado correctamente
	 * @param backend Configuraci�n del backend
	 * @param data Datos a ser almacenados
	 * @param headerData Header que debe contener la informaci�n complementaria
	 * @return Respuesta con la estructura esperada por el conector servidor v2
	 */
	public String ejecutarServicio(InteBackEnd backend, String data, String headerData){
		String file = backend.getFile().getDest() + SimpleXMLHelper.getValueTag(NAME_FILE_TAG, headerData);
    	try {
    		FileHelper.guardarArchivoBase64(file, data);
    	}
    	catch (Throwable e) {
    		return construirRespuesta(ConstantesConector.CODIGO_SERVICIO_ERROR_DATOS, "Error, el archivo " + file + " no pudo ser almacenado.- " + ThrowableHelper.stackTrace2String(e), new String());
		}
    	return construirRespuesta(ConstantesConector.CODIGO_SERVICIO_EXITO, "Operaci�n de almacenado exitosa para el archivo " + file, new String());
	}

}
