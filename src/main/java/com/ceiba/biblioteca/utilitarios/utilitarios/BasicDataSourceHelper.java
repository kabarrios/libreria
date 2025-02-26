package com.ceiba.biblioteca.utilitarios.utilitarios;


import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BasicDataSourceHelper implements Serializable {
	
	private static final long serialVersionUID = -4353793828393467943L;
	private static Log log = LogFactory.getLog(BasicDataSourceHelper.class);
	public static final String ORACLE_DRIVER_CLASS_NAME = "oracle.jdbc.driver.OracleDriver";
	public static final String MICROSOFT_SQL_SERVER_2000_DRIVER_CLASS_NAME = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
	public static final String POSTGRESQL_DRIVER_CLASS_NAME = "org.postgresql.Driver";
	public static final String MYSQL_DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
	
	public static BasicDataSource getBasicDataSource(String url, String driverClassName, String username, String password, boolean defaultAutoCommit){
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setUrl(url);
		dataSource.setDefaultAutoCommit(defaultAutoCommit);
		return dataSource;
	}
	
	public static Connection getConnection(BasicDataSource dataSource) throws SQLException{
		if(dataSource == null){
			log.fatal("Favor verifique si ha realizado la inicializacion de su DataSource");
			throw new IllegalArgumentException("Favor verifique si ha realizado la inicializacion de su DataSource [ DataSource = null ]");
		}
		return dataSource.getConnection();
	}
	
	//@Deprecated
	public static void closeConnection(Connection connection){
		try{
			if(connection!=null && !connection.isClosed()){
				connection.close();
			}
		}
		catch (SQLException e) {
			log.error("No se pudo cerrar la conexion", e);
			throw new RuntimeException("No se pudo cerrar la conexion", e);
		}
	}
	
	//@Deprecated
	public static void closeConnectionQuietly(Connection connection){
		try{
			closeConnection(connection);
		}
		catch (Exception e) {
			log.error("No se pudo cerrar la conexion", e);
		}
	}
	
	public static void commitConnection(Connection connection){
		try{
			if(connection!=null && !connection.isClosed() && !connection.getAutoCommit()){
				connection.commit();
				DbUtils.printWarnings(connection);
			}
		}
		catch (SQLException e) {
			log.error("No se pudo hacer commit sobre la transaccion", e);
			throw new RuntimeException("No se pudo hacer commit sobre la transaccion",e);
		}
	}
	
	public static void rollbackConnection(Connection connection){
		try{
			DbUtils.rollback(connection);
			DbUtils.printWarnings(connection);
		}
		catch (SQLException e) {
			log.error("No se pudo hacer rollback", e);
			throw new RuntimeException("No se pudo hacer rollback",e);
		}
	}
	
	public static Connection getSimpleSQLConnection(String url, String driverClassName, String username, String password, boolean defaultAutoCommit) throws SQLException{
		Connection connection = null;
		if(DbUtils.loadDriver(driverClassName)){
			connection = DriverManager.getConnection(url,username,password);
			connection.setAutoCommit(defaultAutoCommit);
			DbUtils.printWarnings(connection);
		}
		else{
			throw new RuntimeException("No se pudo cargar el driverClassName " + driverClassName);
		}
		return connection;
	}
	
	//@Deprecated
	public static void closeCallableStatementQuietly(CallableStatement cs){
		try{
			closeCallableStatement(cs);
		}
		catch (Exception e) {
			log.error("No se pudo cerrar el CallableStatement", e);
		}
	}
	
	//@Deprecated
	public static void closeCallableStatement(CallableStatement cs){
		try{
			if(cs!=null){
				cs.close();
			}
		}
		catch (Exception e) {
			log.error("No se pudo cerrar el CallableStatement", e);
			throw new RuntimeException("No se pudo cerrar el CallableStatement",e);
		}
	}
	

}
