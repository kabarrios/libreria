package com.ceiba.biblioteca.utilitarios.utilitarios.invocador.procesos.ProcedimientosAlmacenados.oracle;


import com.ceiba.biblioteca.utilitarios.utilitarios.BasicDataSourceHelper;
import com.ceiba.biblioteca.utilitarios.utilitarios.ConstantesConector;
import com.ceiba.biblioteca.utilitarios.utilitarios.ThrowableHelper;
import com.ceiba.biblioteca.utilitarios.utilitarios.invocador.beans.InteBackEnd;
import com.ceiba.biblioteca.utilitarios.utilitarios.invocador.procesos.ProcedimientosAlmacenados.StoredProcedure;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class OracleStoredProcedure extends StoredProcedure implements Serializable {
	
	private static Log log = LogFactory.getLog(OracleStoredProcedure.class);
	private static final long serialVersionUID = 1687904355627399017L;

	protected String executeStoredProcedure(String datos, Connection connection) {
		CallableStatement cs = null;
		String respuesta = new String();
		try{
			cs = connection.prepareCall(storeProcedureName);
			cs.registerOutParameter(2, java.sql.Types.CLOB);
			//cs.setClob(1, OracleUtils.string2Clob(datos, connection));
			cs.execute();
			respuesta = OracleUtils.clob2String(cs.getClob(2));
			if(!connection.getAutoCommit()){
				connection.commit();
			}
		}
		catch (Throwable e) {
			log.fatal("Error en la ejecuci�n del procedimiento almacenado en oracle",e);
			respuesta = getRespuestaConRollbackDataBaseTransaction(e, connection);
		}
		finally{
			if(cs!=null){
				try{
					BasicDataSourceHelper.closeCallableStatement(cs);
				}
				catch (Throwable e) {
					log.error("No se pudo cerrar el CallableStatement en la ejecucion de " + getStoreProcedureName());
				}
			}
		}
		return respuesta;
	}

	protected String executeStoredProcedure(String servicio, String datos, Connection connection) {
		throw new UnsupportedOperationException("Operaci�n no implementada");
	}

	public String ejecutarServicio(InteBackEnd backend, String datos, String headerData) {
		
		buildStoreProcedureName(backend.getStoreProcedure().getNameProcedure());
		
		String respuesta = new String();
		
		Connection con = null;
		
		String user = backend.getDatasource().getUsername();
		String password = getPassword(backend.getDatasource().getPassword(), backend.getDatasource().getMagickey());
		String url = backend.getDatasource().getUrl();
		
		log.debug("Se ejecutara el procedimiento almacenado con el siguiente bloque sql: " + storeProcedureName);
		try{
			con = BasicDataSourceHelper.getSimpleSQLConnection(url, OracleUtils.DRIVER_CLASS_NAME, user, password, false);
			respuesta = executeStoredProcedure(datos,con);
		}
		catch(SQLException e){
			log.fatal("Error al iniciar la conexion a la base de datos ", e);
			respuesta = construirRespuesta(ConstantesConector.CODIGO_SERVICIO_REINTENTO, ThrowableHelper.stackTrace2String(e), DATOS_VACIO);
		} 
		catch (Throwable e) {
			log.fatal("Error al intentar ejecutar el procedimiento almacenado " + getStoreProcedureName(),e);
			respuesta = construirRespuesta(ConstantesConector.CODIGO_SERVICIO_ERROR_DATOS, ThrowableHelper.stackTrace2String(e), DATOS_VACIO);
		}
		finally{
			if(con!=null){
				try{
					BasicDataSourceHelper.closeConnection(con);
				}
				catch (Throwable e) {
					log.error("no se pudo cerrar una conexi�n a la base de datos " + url);
				}
			}
		}
		
		return respuesta;
		
	}


}
