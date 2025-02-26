/*
 * Created on 28-ago-06
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ceiba.biblioteca.utilitarios.utilitarios;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.ByteArrayOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.StringTokenizer;

/**
 * @author eescobar
 *
 * Objeto Encriptacion que nos permite utilizar varias funciones 
 * para encriptar y desencriptar datos.
 * 
 */
public class Encriptacion
{
	
	public static void main(String arg[])
	{
		String cifrado = Encriptar("komtal19", "01-02-03-04-05-06-07-08-09");
		System.out.println(cifrado);
		
		//String descifrado = Desencriptar("48-189-171-53-138-99-28-211-137-61-113-112-42-36-193-227", "01-02-48-44-55-88-99-44-77");
		//System.out.println(descifrado);
	}


	/**
	 * Funcion Encriptar:
	 * Es la funcion que se encarga de recibir la cadena a encriptar y la llave magica para alimentar el 
	 * algoritmo de encriptacion. Devuelve la cadena ya encriptada.
	 * 
	 * @patrLlaveMagica cadena de valores numericos separados por guiones '-', con un minimo de 8 pares de digitos,
	 * Ej. 00-00-00-00-00-00-00
	 * 
	 * @return String Resultado
	 */
	public static String Encriptar(String p_strCadena, String p_strLlaveMagica)
	{

		SecretKey CLlaveSecreta = obtieneLlave(p_strLlaveMagica);
		
		Cipher cifrador = null;
		byte[] cadenaByte = null;
		byte[] cadenaCifradaByte = null;
		
		try
		{
			cifrador = Cipher.getInstance("DES/ECB/PKCS5Padding");
		} catch (NoSuchAlgorithmException e3)
		{
			e3.printStackTrace();
		} catch (NoSuchPaddingException e3)
		{
			e3.printStackTrace();
		}
		
		try
		{
			cifrador.init(Cipher.ENCRYPT_MODE,CLlaveSecreta);
		} catch (InvalidKeyException e4)
		{
			e4.printStackTrace();
		}
		
		cadenaByte = p_strCadena.getBytes();
		try
		{
			cadenaCifradaByte = cifrador.doFinal(cadenaByte);
		} catch (IllegalStateException e5)
		{

			e5.printStackTrace();
		} catch (IllegalBlockSizeException e5)
		{
			e5.printStackTrace();
		} catch (BadPaddingException e5)
		{
			e5.printStackTrace();
		}
		
		return  ObtieneCadena(cadenaCifradaByte);
		
	}
	
	/**
	 * Funcion Desencriptar:
	 * Es la funcion que se encarga de recibir la cadena a desencriptar y la llave magica para alimentar el
	 * algoritmo de desencriptacion. Devuelve la cadena ya desencriptada. tomar en cuenta que la llave magica debe ser la 
	 * misma que la utilizada en la encriptacion.
	 * 

	 * @return String
	 */
	public static String Desencriptar(String p_strCadena, String p_strLlaveMagica)
	{

		SecretKey CLlaveSecreta = obtieneLlave(p_strLlaveMagica);
		
		Cipher cifrador = null;
		byte[] cadenaByte = null;
		byte[] cadenaDescifradaByte = null;
		
		try {
			cifrador = Cipher.getInstance("DES/ECB/PKCS5Padding");
		} catch (NoSuchAlgorithmException e3) {
			e3.printStackTrace();
		} catch (NoSuchPaddingException e3) {
			e3.printStackTrace();
		}
		
		try {
			cifrador.init(Cipher.DECRYPT_MODE,CLlaveSecreta);
		} catch (InvalidKeyException e4) {
			e4.printStackTrace();
		}
		
		cadenaByte = obtieneBytes(p_strCadena);
		try {
			cadenaDescifradaByte = cifrador.doFinal(cadenaByte);
		} catch (IllegalStateException e5) {
			e5.printStackTrace();
		} catch (IllegalBlockSizeException e5) {
			e5.printStackTrace();
		} catch (BadPaddingException e5) {
			e5.printStackTrace();
		}

		return new String(cadenaDescifradaByte);			
	}
	
	/**
	 * Funcion obtieneCadena:
	 * Esta se encarga de recibir un arreglo de bytes y lo pasa a un formato de cadena.
	 * 

	 * @return String
	 */
	private static String ObtieneCadena( byte[] p_bbytes )
	{
	  StringBuffer bufferCadena = new StringBuffer();
	  for( int i=0; i<p_bbytes.length; i++ )
	  {
		byte b = p_bbytes[ i ];
		bufferCadena.append( ( int )( 0x00FF & b ) );
		if( i+1 <p_bbytes.length )
		{
			bufferCadena.append( "-" );
		}
	  }
	  return bufferCadena.toString();
	}
	
	/**
	 * Funcion obtieneBytes:
	 * Esta se encarga de recibir una cadena y lo pasa a un formato de arreglo de bytes
	 * 

	 * @return byte[](arreglo de bytes)
	 */
	private static byte[] obtieneBytes( String p_strcadena )
	{
	  ByteArrayOutputStream bos = new ByteArrayOutputStream();
	  StringTokenizer st = new StringTokenizer( p_strcadena, "-", false );
	  while( st.hasMoreTokens() )
	  {
		int i = Integer.parseInt( st.nextToken() );
		bos.write( ( byte )i );
	  }
	  return bos.toByteArray();
	}
	
	/**
	 * Funcion obtieneLlave:
	 * Esta se encarga de recibir en formato de cadena la llave magica y transformarla para devolver
	 * una instancia del objeto SecretKey que alimenta el algoritmo.
	 * 

	 * @return SecretKey
	 */
	private static SecretKey obtieneLlave(String p_strLlaveMagica )
	{
		DESKeySpec dks = null;
		SecretKeyFactory fabricaLlaveSecreta = null;
		SecretKey llaveSecreta = null;
		
		byte[] llaveMagicaByte = obtieneBytes(p_strLlaveMagica);

		try {
			dks = new DESKeySpec(llaveMagicaByte);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		
		try {
			fabricaLlaveSecreta = SecretKeyFactory.getInstance("DES");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		
		try {
			llaveSecreta = fabricaLlaveSecreta.generateSecret(dks);
		} catch (InvalidKeySpecException e2) {
			e2.printStackTrace();
		}
				
		return llaveSecreta;
	}
}
