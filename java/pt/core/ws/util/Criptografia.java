package pt.core.ws.util;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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

import org.apache.tomcat.util.codec.binary.Base64;

public class Criptografia {
	
	/**Codifica uma String em Base64
	 * 
	 * @param str
	 * @return
	 */
	public String getBase64(String str) {
		return new String(Base64.encodeBase64(str.getBytes(StandardCharsets.UTF_8)));
	}
	
	/**Codifica uma String em Base64
	 * 
	 * @param str
	 * @return
	 */
	public String getBase64(byte[] b) {
		return new String(Base64.encodeBase64(b));
	}

	public X509Certificate getCertificado(String texto) {
		try {
			byte[] encodedCert = texto.getBytes("UTF-8");
		    byte[] decodedCert = Base64.decodeBase64(encodedCert);
		    CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
		    InputStream in = new ByteArrayInputStream(decodedCert);
		    return (X509Certificate) certFactory.generateCertificate(in);
		} catch (CertificateException | UnsupportedEncodingException e) {
			System.out.println("[getCertificado] ler certificado: " + e);
		}
		return null;
	}
	
	
	
	
	/**
	 * Com basa do certificado em ficheiro (*.cer) cifra a String
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
	        return new String(Base64.encodeBase64(criptado));
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
