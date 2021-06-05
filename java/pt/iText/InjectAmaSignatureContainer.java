package pt.iText;

import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.Collection;

import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.signatures.BouncyCastleDigest;
import com.itextpdf.signatures.DigestAlgorithms;
import com.itextpdf.signatures.IExternalSignatureContainer;
import com.itextpdf.signatures.ITSAClient;
import com.itextpdf.signatures.PdfPKCS7;
import com.itextpdf.signatures.PdfSigner;

public class InjectAmaSignatureContainer implements IExternalSignatureContainer {
	
    private X509Certificate[] _certificates;
    private Collection<byte[]> _crlBytesCollection;
    private byte[] _documentHash;
    private Collection<byte[]> _ocspBytes;
    private byte[] _signature;
    private ITSAClient _tsaClient;


    public InjectAmaSignatureContainer(byte[] signature,
                                       X509Certificate[] certificates,
                                       byte[] documentHash,
                                       Collection<byte[]> crlBytesCollection,
                                       Collection<byte[]> ocspBytes,
                                       ITSAClient tsaClient) {
        _signature = signature;
        _certificates = certificates;
        _documentHash = documentHash;
        _crlBytesCollection = crlBytesCollection;
        _ocspBytes = ocspBytes;
        _tsaClient = tsaClient;
    }

    @Override
	public byte[] sign(InputStream arg0) throws GeneralSecurityException {
        BouncyCastleDigest digest = new BouncyCastleDigest();
		PdfPKCS7 sgn = new PdfPKCS7(null, _certificates, DigestAlgorithms.SHA256, null, digest, false);

        sgn.setExternalDigest(_signature,
                              null,
                              "RSA");

        byte[] encodedSig = sgn.getEncodedPKCS7(_documentHash,
                                             PdfSigner.CryptoStandard.CMS,
                                             _tsaClient,
                                             _ocspBytes,
                                             _crlBytesCollection);

        return encodedSig;
    }

    public void ModifySigningDictionary(PdfDictionary signDic) {
    }

	@Override
	public void modifySigningDictionary(PdfDictionary arg0) {
		// TODO Auto-generated method stub
		
	}

}
