package com.ceiba.biblioteca.utilitarios.utilitarios;

import java.io.Serializable;

public class ConstantesConector implements Serializable {
	
	private static final long serialVersionUID = 7127994334199287619L;
	
	public static final String PLANTILLA_MENSAJES_PROPERTIES = "recursos/PlantillaMensajes.properties";
	public static final String PLANTILLA_LOG_PROPERTIES = "recursos/PlantillaLog.properties";
	public static final String RESOURCE_NAME = "recursos/MensajesConector";
	
	public static final String MSG_ERROR_INIT_CONTEXTO_MQ = "error.contexto";
	public static final String MSG_ERROR_INIT_PLANTILLAS = "error.plantillas";
	public static final String MSG_ERROR_NO_IMPLEMENTADO = "operacion.no.implementada";
	public static final String MSG_QUEUEMANAGER_CONECTADO = "queueManager.conectado";
	public static final String MSG_QUEUEMANAGER_DESCONECTADO = "queueManager.desconectado";
	
	public static final String CODIGO_SERVICIO_INACTIVO = "-20001";
	public static final String CODIGO_EXCEPCION_NO_CONTROLADA = "-20002";
	public static final String CODIGO_SERVICIO_ENVENENADO = "-20003";
	public static final String CODIGO_SERVICIO_REINTENTO_VENCIDO = "-20018";
	public static final String CODIGO_SERVICIO_REINTENTO = "-20019";
	public static final String CODIGO_SERVICIO_ERROR_DATOS = "-20020";
	public static final String CODIGO_SERVICIO_EXITO = "000000000";
	
	public static final String ARCHIVO_PERSISTENCIA = "Persistencia.properties";
	public static final String ARCHIVO_LOG = "Servicios.properties";
	public static final String ARCHIVO_CONFIGURACION_CLIENTE = "ConectorClienteMQ.properties";
	public static final String REGEX_PROPERTIES = "ServiciosRegex.properties";
	public static final String INVALID_CHAR_REGEX = "invalid.char";
	
	public static final String ALARM_Q = "ALARM.Q";
	public static final String DEFINICION_COLA_REQ = ".REQ";
	public static final String DEFINICION_COLA_RESP = ".RESP";
	
	public static final String DATOS_TAG_NAME = "Datos";
	public static final String RETCODEDESCRIPTION_TAG_NAME = "retcodedescription";
	public static final String RETCODE_TAG_NAME = "retcode";
	public static final String HEADER_TAG_NAME = "Header";
	public static final String SERVICIO_TAG_NAME = "servicio";
	public static final String PAIS_TAG_NAME = "pais";
	public static final String SERVICE_VARS_TAG_NAME = "ServiceVars";
	
	public static final int MQ_PUT_EXPIRY_FACTOR = 1200;
	public static final int MQ_GET_EXPIRY_FACTOR = 1000;
	
	public static final int NO_WAIT_TIME = 0;

}
