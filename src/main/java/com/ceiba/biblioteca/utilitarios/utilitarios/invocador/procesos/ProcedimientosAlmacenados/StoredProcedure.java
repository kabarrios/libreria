package com.ceiba.biblioteca.utilitarios.utilitarios.invocador.procesos.ProcedimientosAlmacenados;



import com.ceiba.biblioteca.utilitarios.utilitarios.BasicDataSourceHelper;
import com.ceiba.biblioteca.utilitarios.utilitarios.ConstantesConector;
import com.ceiba.biblioteca.utilitarios.utilitarios.Encriptacion;
import com.ceiba.biblioteca.utilitarios.utilitarios.ThrowableHelper;
import com.ceiba.biblioteca.utilitarios.utilitarios.invocador.procesos.BackendIntegracion;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.Serializable;
import java.sql.Connection;

public abstract class StoredProcedure extends BackendIntegracion implements Serializable {

	private static final long serialVersionUID = 7107720892722173337L;
	protected BasicDataSource dataSource;
	protected String storeProcedureName;
	
	public String getStoreProcedureName() {
		return this.storeProcedureName;
	}

	public void setStoreProcedureName(String storeProcedureName) {
		this.storeProcedureName = storeProcedureName;
	}

	public BasicDataSource getDataSource() {
		return this.dataSource;
	}

	public void setDataSource(BasicDataSource dataSource) {
		this.dataSource = dataSource;
	}

	protected abstract String executeStoredProcedure(String datos, Connection connection);
	
	protected abstract String executeStoredProcedure(String servicio, String datos, Connection connection);
	
	protected String getRespuestaConRollbackDataBaseTransaction(Throwable exception, Connection connection) {
		try{
			BasicDataSourceHelper.rollbackConnection(connection);
		}
		catch (Exception e) {
			return construirRespuesta(ConstantesConector.CODIGO_SERVICIO_ERROR_DATOS,"Error al hacer rollback sobre la transaccion\n" + ThrowableHelper.stackTrace2String(e) + "\n" + ThrowableHelper.stackTrace2String(exception), DATOS_VACIO);
		}
		return construirRespuesta(ConstantesConector.CODIGO_SERVICIO_ERROR_DATOS,"Error al invocar el procedimiento almacenado\n" + ThrowableHelper.stackTrace2String(exception), DATOS_VACIO);
	}
	
	protected String getPassword(String password, String magicKey){
		return Encriptacion.Desencriptar(password,magicKey);
	}
	
	protected void buildStoreProcedureName(String name){
		this.storeProcedureName = "begin " + name + "; end;";
	}

}
