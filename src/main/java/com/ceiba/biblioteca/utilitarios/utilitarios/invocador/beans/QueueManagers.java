package com.siman.Integracion.utilitarios.invocador.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * JavaBean contenedor de los queue managers a ser actualizados con datos para el invocador 
 * @author Ernesto Chicas
 *
 */
public class QueueManagers implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private ArrayList listQMgrs;
	
	public ArrayList getListQMgrs() {
		return listQMgrs;
	}
	
	public void setListQMgrs(ArrayList listQMgrs) {
		this.listQMgrs = listQMgrs;
	}
	
}
