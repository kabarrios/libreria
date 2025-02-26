package com.ceiba.biblioteca.utilitarios.utilitarios;



import org.apache.tomcat.util.codec.binary.Base64;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Esta clase definide metodos varios para ser utilizados como utilidades para diferentes objetivos,
 * entre los cuales se tienen:
 * <ul>
 * 	<li>Comprimir un achivo a partir de un <code>InputStream</code></li>
 * 	<li>Descomprimir un archivo a partir de un <code>InputStream</code>, lo guarda en la ubicacion especificada</li>
 * 	<li>Descomprimir un archivo a partir de un <code>InputStream</code>, devuelve un String en codificacion Base64</li>
 * </ul>
 * @author consultor
 *
 */

public class UtilitiesHelper implements Serializable{

	/**
	 * Atributo que identifica esta clase cuando es serializada
	 */
	private static final long serialVersionUID = -4692712778574147783L;
	/**
	 * Metodo que comprime informacion que viene en un <code>InputStream</code>, lo guarda temporalmente, con el
	 * nombre especificado para el archivo zip, y dentro de este guarda el archivo con el nombre especificado.
	 * @param data flujo de datos
	 * @param fileName nombre que tendra el archivo dentro del zip
	 * @param zipFileName nombre del zip
	 * @throws IOException
	 */
	public static void compressIntoZip(InputStream data, String fileName, String zipFileName) throws IOException{
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		byte[] buffer = new byte[1024];
		try {
			fos = new FileOutputStream(zipFileName);
			zos = new ZipOutputStream(fos);
			zos.setLevel(9);
			zos.setMethod(ZipOutputStream.DEFLATED);
			ZipEntry ze = new ZipEntry(fileName);
			zos.putNextEntry(ze);
			int len;
			while((len=data.read(buffer)) != -1){
				zos.write(buffer, 0, len);
			}
			zos.closeEntry();
		}
		finally{
			try {zos.close();}
			catch (Throwable e) {/*throw new RuntimeException(e);*/}
		}
	}
	
	/**
	 * Metodo que descomprime un archivo representado por un InputStream, guarda el archivo descomprimido en la ubicacion
	 * que se especifica.
	 * @param path ubicacion a nivel de directorios en donde se guarda el archivo descomprimido
	 * @param data flujo de datos a descomprimir
	 * @throws IOException
	 */
	public static void uncompressFromZip(String path, InputStream data) throws IOException{
		
		FileOutputStream fos = null;
		ZipInputStream zis = null;
		byte[] buffer = new byte[1024];
		try {
			zis = new ZipInputStream(data);
			//ZipEntry ze = zis.getNextEntry();
			if(zis.getNextEntry() != null){
				fos = new FileOutputStream(path);
				int len;
				while ((len=zis.read(buffer)) != -1){
					fos.write(buffer, 0, len);
				}
				fos.flush();
				fos.close();
				zis.closeEntry();
			}
		}
		finally{
			try {zis.close();}
			catch (Throwable e) {/*throw new RuntimeException(e);*/}
		}
	}
	/**
	 * Metodo que descomprime un archivo representado por un InputStream, y devuelve un flujo en Base64 representado en una 
	 * cadena de caracteres.
	 * @param data flujo de datos a descomprimir
	 * @return Cadena de caracteres en Base64
	 * @throws IOException
	 */
	public static String uncompressFromZip(InputStream data) throws IOException{
		
		ByteArrayOutputStream out = null;
		ZipInputStream zis = null;
		byte[] buffer = new byte[1024];
		String stream = null;
		try {
			zis = new ZipInputStream(data);
			//ZipEntry ze = zis.getNextEntry();
			if(zis.getNextEntry() != null){
				out = new ByteArrayOutputStream();
				int len;
				while ((len=zis.read(buffer)) != -1){
					out.write(buffer, 0, len);
				}
				byte[] content = out.toByteArray(); 
				stream = new String(Base64.encodeBase64(content));
				out.close();
				zis.closeEntry();
			}
			return stream;
		}
		finally{
			try {zis.close();}
			catch (Throwable e) {/*throw new RuntimeException(e);*/}
		}
		
	}
	
}
