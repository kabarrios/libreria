//package com.ceiba.biblioteca.conectorCliente.MQ.principal;
//
//import java.io.Serializable;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
///**
// * Permite el envio a Websphere MQ a trav�s de consola
// * @version 2.0
// * @author echicas
// */
//@SpringBootApplication
//public class ConectorClienteWrapper implements Serializable {
//
//	private static final long serialVersionUID = 786978448421119154L;
//	private static Log log = LogFactory.getLog(ConectorClienteWrapper.class);
//
//	private ConectorClienteWrapper(){
//	}
//
//	/**
//	 * Main para permitir el acceso a envio de mensajes a Websphere MQ por medio de consola.
//	 * @param args Debe contener los mismos parametros que si se utilizar� desde una clase java
//	 * <p>codigoServicio C�digo de serivico a ser utilizado</p>
//	 * <p>paisOrigen Pa�s desde donde se genera la transacci�n</p>
//	 * <p>compania Compa�ia para la que se genera la transacci�n</p>
//	 * <p>cadena Cadena para la que se genera la transacci�n</p>
//	 * <p>sucursal Sucursal para la que se genera la transacci�n</p>
//	 * <p>numeroTransaccion N�mero de transacci�n (campo solo descriptivo)</p>
//	 * <p>hostOrigen IP/Nombre del servidor desde el que se genera la transacci�n</p>
//	 * <p>token Token (campo solo descriptivo)</p>
//	 * <p>datosNegocio Datos de negocio</p>
//	 * <p>tiempoExpiracion Tiempo de espera por la respuesta a la petici�n en segundos</p>
//	 * <p>nombreAplicacion Nombre del modulo/aplicaci�n que genera la transacci�n</p>
//	 */
//	public static void main(String args[]){
//		try{
//			//sincrono
//			if(new Integer(args[9]).intValue()>0){
//				String respuesta = ConectorCliente.sendSynchronousMessageToESB(args[0], args[1], args[2], args[3], args[4],args[5],args[6], args[7], args[8],new Integer(args[9]).intValue(), args[10]);
//				log.info("Respuesta: " + respuesta);
//			}
//			//asincrono
//			else{
//				ConectorCliente.sendAsynchronousMessageToESB(args[0], args[1], args[2], args[3], args[4],args[5],args[6], args[7], args[8], args[10]);
//				log.info("Requerimiento enviado con exito");
//			}
//		}
//		catch (Exception e) {
//			log.fatal("Error al procesar el requerimiento",e);
//		}
//	}
//
//}
