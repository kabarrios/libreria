package com.ceiba.biblioteca.utilitarios.utilitarios;


import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.*;

public class FileHelper implements Serializable {
	

	private static final long serialVersionUID = -4316756136918891380L;
	private static final Log log = LogFactory.getLog(FileHelper.class);

	public static void crearArchivo(String ruta) {		
		crearArchivo(ruta, true);
	}
	
	public static void crearArchivo(String ruta, boolean sobreEscribir) {
		try{
			File file = new File(ruta);
			if( !file.exists() && sobreEscribir ){
				log.debug("Creando archivo: "+ruta);
				setReadWriteProperties(file);
				file.createNewFile();
			}
			/*else{
				log.debug("El archivo "+ruta+" ya existe, no se creara");
			}*/
		}
		catch(IOException e){
			log.error("Error al crear el archivo " + ruta, e);
			throw new RuntimeException("Error al crear el archivo " + ruta, e);
		}
	}
	

	public static void setReadWriteProperties(File file) throws IOException {
		//file.setWritable(true);
		//file.setReadable(true);
	}
	
	/**
	 * @deprecated
	 * @param file
	 */
	public static void setReadWriteProperties(String file){
		try{
			Runtime.getRuntime().exec("chmod 777 " + file);
		}
		catch(Throwable e){
			log.error("No se pudo cambiar permisos al archivo " + file);
		}
	}
	
	public static void escribirArchivo(String ruta, String texto) {
		escribirArchivo(ruta, texto, false);
	}
	
	public static void escribirArchivo(String ruta, String texto, boolean sobreEscribir) {
		try{
			FileWriter writer = new FileWriter(ruta,!sobreEscribir);
			writer.write(texto);
			writer.close();
		}
		catch(IOException e){
			log.error("Error al escribir el archivo " + ruta, e);
			throw new RuntimeException("Error al escribir el archivo " + ruta, e);
		}
	}
	
	public static void escribirArchivo(String ruta, String texto, boolean sobreEscribir, boolean preBackup) {
		try{
			if(preBackup){
				boolean resultado = new File(ruta).renameTo(new File(ruta + "." +  DateHelper.getDateLogger(DateHelper.LOG_NAME_DATE_FORMAT)));
				if(!resultado){
					log.error("No se pudo realizar el backup");
				}
			}
			FileWriter writer = new FileWriter(ruta,!sobreEscribir);
			writer.write(texto);
			writer.close();
		}
		catch(IOException e){
			log.error("Error al escribir el archivo " + ruta, e);
			throw new RuntimeException("Error al escribir el archivo " + ruta, e);
		}
	}
	
	public static String leerArchivo(String ruta) {
		StringBuffer  fileContent = new StringBuffer();
		BufferedReader reader = null;
		try{
			File file = new File(ruta);
			if( file.exists() ){
				reader = new BufferedReader(new FileReader(ruta));
				fileContent.append(IOUtils.toString(reader));
			}
			else{
				throw new FileNotFoundException("El archivo "+ruta+" no existe");
			}
		}
		catch(IOException e){
			log.error("Error al leer el archivo " + ruta, e);
			throw new RuntimeException("Error al leer el archivo " + ruta, e);
		}
		finally{
			IOUtils.closeQuietly(reader);
		}
		return fileContent.toString();
	}
	
	public static void guardarArchivoBase64(String ruta, String datos){
    	try {
    		byte[] imgBytes = new Base64().decode(datos.getBytes());
    		FileOutputStream osf = new FileOutputStream(new File(ruta));  
    		osf.write(imgBytes);  
    		osf.flush();  
    	} 
    	catch (Throwable e) {
    		log.error("Error al escribir el archivo " + ruta, e);
    		throw new RuntimeException("Error al escribir el archivo " + ruta, e);
		}
	}
	
	public static String[] getFilesNameByExtension(String dir, String extension) {
		return new File(dir).list(getEndsWithFilenameFilter(extension));
	}
	
	public static File[] getFilesByExtension(String dir, String extension) {
		return new File(dir).listFiles(getEndsWithFilenameFilter(extension));
	}
	
	public static FilenameFilter getEndsWithFilenameFilter(final String extension){
		return new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.endsWith(extension);
		    }
		};
	}
	
	public static byte[] getBytesFromFile(String fileName) throws IOException {
		return getBytesFromFile(new File(fileName));
	}
	
	public static byte[] getBytesFromFile(File file) throws IOException {
	    InputStream is = null;
	    try{
	    	is = new FileInputStream(file);
		    // Get the size of the file
		    long length = file.length();
		    // You cannot create an array using a long type.
		    // It needs to be an int type.
		    // Before converting to an int type, check
		    // to ensure that file is not larger than Integer.MAX_VALUE.
		    if (length > Integer.MAX_VALUE) {
		        // File is too large
		    	throw new RuntimeException("File is too large");
		    }
	
		    // Create the byte array to hold the data
		    byte[] bytes = new byte[(int)length];
	
		    // Read in the bytes
		    int offset = 0;
		    int numRead = 0;
		    while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
		        offset += numRead;
		    }
	
		    // Ensure all the bytes have been read in
		    if (offset < bytes.length) {
		        throw new IOException("Could not completely read file "+file.getName());
		    }
	    	return bytes;
	    }
	    finally{
	    	// Close the input stream and return bytes
	    	if(is!=null) is.close();
	    }
	}

}
