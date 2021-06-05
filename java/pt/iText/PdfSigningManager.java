package pt.iText;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.signatures.CrlClientOnline;
import com.itextpdf.signatures.OcspClientBouncyCastle;
import com.itextpdf.signatures.PdfSignatureAppearance;
import com.itextpdf.signatures.PdfSigner;
import com.itextpdf.signatures.TSAClientBouncyCastle;


public class PdfSigningManager {
	
	String _signatureFieldname = "Signature1";

	X509Certificate[] _userCertificateChain = null;
	TSAClientBouncyCastle _tsaClient = null;
	CrlClientOnline _crlClients = null;
	OcspClientBouncyCastle _ocspClient = null;
	
	public PdfSigningManager(	X509Certificate[] chain, 
								CrlClientOnline crlClients, 
								OcspClientBouncyCastle ocspClient,
								TSAClientBouncyCastle tsaClient) {
		
		_userCertificateChain = chain;
        _ocspClient = ocspClient;
        _crlClients = crlClients;
        _tsaClient = tsaClient;
	}

	public HashesForSigning CreateTemporaryPdfForSigning(SigningInformation signingInformation) {
		
		try (OutputStream ops = new FileOutputStream(signingInformation.getTemporarypdf());){
			PdfReader reader = new PdfReader(signingInformation.getPdftobesigned());
	        StampingProperties prop = new StampingProperties();
	
			PdfSigner pdfSigner = new PdfSigner(reader, ops, prop);
	
			pdfSigner.setFieldName(_signatureFieldname);
	
	
	        PdfSignatureAppearance  appearance = pdfSigner.getSignatureAppearance();
	
	        appearance.setPageRect(new Rectangle(10,750,150,50))
	                  .setPageNumber(signingInformation.getPageNumber())
	                  .setLayer2FontSize(6f)
	                  .setReason(signingInformation.getReason())
	                  .setLocation(signingInformation.getLocation())
	                  .setLayer2Text(getBuildVisibleInformation(signingInformation.getReason(), signingInformation.getLocation()))
	                  .setCertificate(_userCertificateChain[0]);
	
//	        if (signingInformation.getLogo()!= null) {
//	            appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC_AND_DESCRIPTION)
//	                      .setSignatureGraphic(signingInformation.getLogo());
//	        }
	
	        Collection<byte[]> crlBytesList = getCrlByteList();
	        Collection<byte[]> ocspBytesList = getOcspBytesList();
	        
	        
	        PrefareForAmaSigningContainer container = new PrefareForAmaSigningContainer( _userCertificateChain, crlBytesList, ocspBytesList);
	        
	        pdfSigner.signExternalContainer(container, EstimateContainerSize(crlBytesList)); // add size for timestamp in signature
	        
	        
	
	        return new HashesForSigning(container.getHashToBeSignedByAma(), container.getNakedHash());
		} catch (IOException | GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
	
	public void SignIntermediatePdf(SignatureInformation signatureInformation) {
		try (OutputStream writer = new FileOutputStream(signatureInformation.getPathToSignedPdf());) {
			PdfReader pdfReader = new PdfReader(signatureInformation.getPathToIntermediaryPdf());
	        PdfDocument document = new PdfDocument(pdfReader);

	
	        Collection<byte[]> crlBytesList = getCrlByteList();//new CertCRL().getRCRLs(_userCertificateChain);
	        Collection<byte[]> ocspBytesList = getOcspBytesList();//new GetOcsp().getOcspBytesList(_userCertificateChain, _ocspClient);
	
	        var container = new InjectAmaSignatureContainer(signatureInformation.getSignature(),
	                                                                _userCertificateChain,
	                                                                signatureInformation.getNakedHashFromIntermediaryPdf(),
	                                                                crlBytesList,
	                                                                ocspBytesList,
	                                                                _tsaClient);
	        PdfSigner.signDeferred(document, _signatureFieldname, writer, container);
		} catch (IOException | GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	private String getBuildVisibleInformation(String reason, String location ) {
		StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
        	.append("Assinado por: autor\n" )
        	.append("BI: xxxxxxxx\n")
        	.append("Date: data\n");
        if(!location.isBlank())  stringBuilder.append("Local: " + location + "\n");

        if (!reason.isBlank()) stringBuilder.append("Motivo: " + reason);
        
        return stringBuilder.toString();
    }
	
	 private int EstimateContainerSize(Collection<byte[]> crlBytesList) {
         int estimatedSize = 8192 + //initial base container size
                           ( _ocspClient != null ? 4192 : 0 ) +
                           ( _tsaClient != null  ? 4600 : 0 );
         if(crlBytesList != null) {
        	 for (byte[] bs : crlBytesList) {
        		 estimatedSize+= bs.length + 10;
			}
         }

         return estimatedSize;
     }
	 
	 private Collection<byte[]> getCrlByteList(){
		 if(_crlClients == null) return null;
		 Collection<byte[]> coll = null;
		 for(int i=0;i<_userCertificateChain.length;i++) {
			 Collection<byte[]> tmp = GetCrlClientBytesList(_userCertificateChain[i]);
			 if(null != tmp ) {
				 coll = new ArrayList<byte[]>();
				 coll.addAll(tmp);
			 }
		 }
		 return coll;
	 }
	 
	 private Collection<byte[]> GetCrlClientBytesList(X509Certificate certificate) {
         if(_crlClients == null) return null;
         return _crlClients.getEncoded(certificate, null);
     }
            
	 private Collection<byte[]> getOcspBytesList() {
         if(_userCertificateChain.length <= 1 ||
            _ocspClient == null) {
             return null;
         }
         
         Collection<byte[]> list = new ArrayList<byte[]>();
         for(var i = 0; i < _userCertificateChain.length - 1; i++) {
             byte[] encoded = _ocspClient.getEncoded(_userCertificateChain[i], _userCertificateChain[i + 1], null);
             if(encoded != null) {
                 list.add(encoded);
             }
         }

         return list;
     }
}
