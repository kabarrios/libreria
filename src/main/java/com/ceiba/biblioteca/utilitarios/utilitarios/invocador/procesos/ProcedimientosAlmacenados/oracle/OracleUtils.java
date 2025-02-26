package com.ceiba.biblioteca.utilitarios.utilitarios.invocador.procesos.ProcedimientosAlmacenados.oracle;




import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;

public class OracleUtils implements Serializable {

	private static final String DBMS_LOB_CREATETEMPORARY_SP = "BEGIN DBMS_LOB.CREATETEMPORARY(?, TRUE, DBMS_LOB.SESSION); END;";
	public static final String DRIVER_CLASS_NAME = "oracle.jdbc.driver.OracleDriver";
	private static final long serialVersionUID = 1518464889504357473L;
	

//	public static CLOB string2Clob(String datos, Connection connection) throws Exception{
//		CLOB tempClob = CLOB.empty_lob();
//		CallableStatement upAge = null;
//		try{
//			upAge = connection.prepareCall(DBMS_LOB_CREATETEMPORARY_SP);
//			upAge.registerOutParameter(1, java.sql.Types.CLOB);
//			upAge.execute();
//			tempClob = (CLOB)upAge.getClob(1);
//			tempClob.putString(1, datos);
//		}
//		catch (Throwable e){
//			throw new Exception(e);
//		}
//		finally{
//			if(upAge!=null) upAge.close();
//		}
//		return tempClob;
//	}
	
	public static String clob2String(Clob clob) throws IOException, SQLException {
		if(clob==null){
			return "";
		}
		return IOUtils.toString(new BufferedReader(clob.getCharacterStream()));
	}
	
}
