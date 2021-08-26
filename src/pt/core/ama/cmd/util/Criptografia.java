package pt.core.ama.cmd.util;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

public class Criptografia {
	
	/**
	 * Codifica uma String em Base64
	 * 
	 * @param str
	 * @return
	 */
	public String getEncodeBase64(String str) {
		return getEncodeBase64Str(str.getBytes(StandardCharsets.UTF_8));
		//return new String(Base64.getEncoder().encode(str.getBytes(StandardCharsets.UTF_8)));
	}
	/**
	 * Codifica um array de bytes em Base64
	 * 
	 * @param str
	 * @return
	 */
	public byte[] getEncodeBase64(byte[] b) {
		return Base64.encodeBase64(b);
	}
	/**
	 * Codifica um array de bytes em Base64 de devolve uma string
	 * 
	 * @param str
	 * @return
	 */
	public String getEncodeBase64Str(byte[] b) {
		return new String(getEncodeBase64(b));
	}
	
	public String getEncodeBase64Str(String str) {
		return new String(getEncodeBase64(str.getBytes(StandardCharsets.UTF_8)));
	}
	
	
	/**
	 * Descodifica uma array de bytes que se encontra em Base64
	 * 
	 * @param b
	 * @return
	 */
	public byte[] getDecodeBase64(byte[] b) {
		return Base64.decodeBase64(b);
	}
	
	public byte[] getDecodeBase64Str(String str) {
		return Base64.decodeBase64(str.getBytes(StandardCharsets.UTF_8));
	}
	
	public boolean isBase64(byte[] b) {
		return Base64.isBase64(b);
	}
	
	public boolean isBase64(String str) {
		return Base64.isBase64(str.getBytes(StandardCharsets.UTF_8));
	}
	

	public X509Certificate getCertificado(String texto) {
		try {
			byte[] encodedCert = texto.getBytes(StandardCharsets.UTF_8);
		    byte[] decodedCert = getDecodeBase64(encodedCert);
		    CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
		    InputStream in = new ByteArrayInputStream(decodedCert);
		    return (X509Certificate) certFactory.generateCertificate(in);
		} catch (CertificateException e) {
			System.out.println("[getCertificado] ler certificado: " + e);
		}
		return null;
	}
	
	/**
	 * Com base no certificado em ficheiro (*.cer) cifra a String
	 * 
	 * @param str
	 * @return
	 */
	public String getCifraAMA(String str) {
		try {
	        PublicKey pk = getChavePublicaFicheiro();
	        if(null == pk) return null;
	        Cipher cipher = Cipher.getInstance("RSA"); //"RSA/ECB/PKCS1Padding", "RSA"
	        cipher.init(Cipher.ENCRYPT_MODE, pk);
	        byte[] criptado = cipher.doFinal(str.getBytes());
	        return getEncodeBase64Str(criptado);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Obtem a chave publica do certificado.
	 * A origem do certificado é um ficheiro do tipo .cer
	 * 
	 * @param ficheiro
	 * @return
	 */
	private PublicKey getChavePublicaFicheiro() {
		try {
			FileInputStream fileInputStream = new FileInputStream(Config.getCertificadoAMA());
	
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509Certificate certificate = (X509Certificate)cf.generateCertificate(fileInputStream);
			return  certificate.getPublicKey();
		} catch (FileNotFoundException | CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
}
