package pt.core.ama.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;

import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.signatures.BouncyCastleDigest;
import com.itextpdf.signatures.DigestAlgorithms;
import com.itextpdf.signatures.IExternalSignatureContainer;
import com.itextpdf.signatures.OcspClientBouncyCastle;
import com.itextpdf.signatures.PdfPKCS7;
import com.itextpdf.signatures.PdfSigner;

public class MyExternalBlankSignatureContainer implements IExternalSignatureContainer {

	/* Signature dictionary. Filter and SubFilter.  */
    private PdfDictionary sigDic;
    private byte[] hash4Sign = null;
    private Certificate[] chain = null;

    public MyExternalBlankSignatureContainer(Certificate[] _chain, PdfName filter, PdfName subFilter) {
        sigDic = new PdfDictionary();
        sigDic.put(PdfName.Filter, filter);
        sigDic.put(PdfName.SubFilter, subFilter);
        chain = _chain;
    }
    
    public byte[] getHash4Sign() {
    	return hash4Sign;
    }

    @Override
    public byte[] sign(InputStream data) throws GeneralSecurityException {
        try {
            String hashAlgorithm = DigestAlgorithms.SHA256;//"SHA-256";
            BouncyCastleDigest digest = new BouncyCastleDigest();
            MessageDigest md = digest.getMessageDigest(hashAlgorithm);

            byte[]   hash = DigestAlgorithms.digest(data, md);
            PdfPKCS7 sgn  = new PdfPKCS7(null, chain, hashAlgorithm, null, digest, false);

            OcspClientBouncyCastle ocspClient = new OcspClientBouncyCastle(null);
            Collection<byte[]> ocsp = new ArrayList<byte[]>();
     	   	for(int i = 0; i < chain.length - 1; i++) {
     	   		byte[] encoded = ocspClient.getEncoded((X509Certificate)chain[i], (X509Certificate)chain[i + 1], null);
     	   		if(encoded != null) ocsp.add(encoded);
     	   	}

            byte[] attributeBytes = sgn.getAuthenticatedAttributeBytes(hash, PdfSigner.CryptoStandard.CADES, ocsp, null);

             //create sha256 message digest
            hash4Sign = MessageDigest.getInstance(hashAlgorithm).digest(attributeBytes);

            return new byte[0];
        } catch (IOException | GeneralSecurityException de) {
            de.printStackTrace();
            throw new GeneralSecurityException(de);
        }
    }

    @Override
    public void modifySigningDictionary(PdfDictionary signDic) {
        signDic.putAll(sigDic);
    }
}
