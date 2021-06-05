package pt.iText;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.Collection;

import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.signatures.BouncyCastleDigest;
import com.itextpdf.signatures.DigestAlgorithms;
import com.itextpdf.signatures.ExternalBlankSignatureContainer;
import com.itextpdf.signatures.PdfPKCS7;
import com.itextpdf.signatures.PdfSigner;

import pt.core.ws.util.ArrayByte;

public class PrefareForAmaSigningContainer extends ExternalBlankSignatureContainer{
	
	//private final byte[] _sha256SigPrefix = {0x30, 0x31, 0x30, 0x0d, 0x06, 0x09, 0x60, (byte)0x86, 0x48, 0x01, 0x65, 0x03, 0x04, 0x02, 0x01, 0x05, 0x00, 0x04, 0x20};
	private final char[] _sha256SigPrefix = {0x30, 0x31, 0x30, 0x0d, 0x06, 0x09, 0x60, 0x86, 0x48, 0x01, 0x65, 0x03, 0x04, 0x02, 0x01, 0x05, 0x00, 0x04, 0x20};
	
	private final X509Certificate[] _certificates;
	private final Collection<byte[]> _crlBytesCollection;
	private final Collection<byte[]> _ocspBytes;


	public PrefareForAmaSigningContainer(X509Certificate[] certificates, Collection<byte[]> crlBytesCollection, Collection<byte[]> ocspBytes){
		super(PdfName.Adobe_PPKLite, PdfName.Adbe_pkcs7_detached);
		_certificates = certificates;
		_crlBytesCollection = crlBytesCollection;
		_ocspBytes = ocspBytes;
	}

	private byte[] HashToBeSignedByAma = null;//new byte[0];
	
	public final byte[] getHashToBeSignedByAma(){
		return HashToBeSignedByAma;
	}

	private void setHashToBeSignedByAma(byte[] value){
		HashToBeSignedByAma = value;
	}

	/** 
	 Original naked hash of the document (used for injecting the signature when it's retrieved from AMA)
	*/
	private byte[] NakedHash = null;

	public final byte[] getNakedHash(){
		return NakedHash;
	}
	private void setNakedHash(byte[] value){
		NakedHash = value;
	}

	@Override
	public byte[] sign(InputStream data){
		try {
			// crea pdf pkcs7 for signing the document
			BouncyCastleDigest digest = new BouncyCastleDigest();
			PdfPKCS7 sgn = new PdfPKCS7(null, _certificates, DigestAlgorithms.SHA256, null, digest, false);
			
			// get hash for document bytes
			setNakedHash(DigestAlgorithms.digest(data, digest.getMessageDigest(DigestAlgorithms.SHA256)));
	
			// get attributes
			var docBytes = sgn.getAuthenticatedAttributeBytes(	getNakedHash(), 
																PdfSigner.CryptoStandard.CMS, 
																_ocspBytes, 
																_crlBytesCollection);
			// hash it again
			try(InputStream myInputStream = new ByteArrayInputStream(docBytes);){ 
				byte[] docBytesHash = DigestAlgorithms.digest(myInputStream, digest.getMessageDigest(DigestAlgorithms.SHA256));
	   
	   
				//prepend sha256 prefix
				byte[] totalHash = new ArrayByte().juntar(new String(_sha256SigPrefix).getBytes(), docBytesHash);
				setHashToBeSignedByAma(totalHash);
	
				//devolve array vazio
				return new byte[0];
			} 
		}catch (IOException | GeneralSecurityException ioe) {
            throw new RuntimeException(ioe);
        }
	}
}