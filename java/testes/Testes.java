package testes;

import java.net.MalformedURLException;
import java.security.cert.X509Certificate;
import java.util.Scanner;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.util.UrlUtil;
import com.itextpdf.signatures.CrlClientOnline;
import com.itextpdf.signatures.OcspClientBouncyCastle;
import com.itextpdf.signatures.TSAClientBouncyCastle;

import pt.core.ws.soap.ama.metodo.GetCertificate;
import pt.core.ws.soap.ama.metodo.SCMDSign;
import pt.core.ws.soap.ama.metodo.ValidateOtp;
import pt.core.ws.soap.ama.resposta.entity.EntitySign;
import pt.core.ws.soap.ama.resposta.entity.EntityValidateOtp;
import pt.core.ws.util.Criptografia;
import pt.iText.HashesForSigning;
import pt.iText.PdfSigningManager;
import pt.iText.SignatureInformation;
import pt.iText.SigningInformation;

public class Testes {
	
	/*Necessário configurar as variaveis da classe: Config.java
	 * 
	 *Dependências:
	 	bcpkix-jdk15on-1.68.jar
		bcprov-jdk15on-1.68.jar
		commons-codec-1.15.jar
		commons-io-2.6.jar
		forms-7.1.15.jar
		io-7.1.15.jar
		kernel-7.1.15.jar
		layout-7.1.15.jar
		pdfa-7.1.15.jar
		sign-7.1.15.jar
		slf4j-api-1.7.30.jar
	 */
	

	private static final String pdfToBeSigned = "C:\\tmp\\ama\\PDF1.pdf";
	private static final String temporaryPdf = "C:\\tmp\\ama\\PDF1_empty.pdf";
	private static final String finalPdf = "C:\\tmp\\ama\\PDF1_assinado.pdf";
	
	public static void main(String[] args) throws MalformedURLException {
		/****************************************
		 * LER cliente formato: +351 9600000000 *
		 ****************************************/
		String clientId = null;
		Scanner sc= new Scanner(System.in); //System.in is a standard input stream.
		System.out.print("Nº de telemovel: ");
		clientId = sc.nextLine();
System.out.println("[aceite]");
		X509Certificate[] chain = null;

		//1ª chamada -> GetCertificate, obter os certificados do utilizador
		GetCertificate getCert = new GetCertificate(clientId);
		chain = getCert.callEndPoint();
		if(null == chain) {
			System.out.println("[WS GetCertificate] - obter certificados. Cliente enexistente: " + clientId);
			return;
		}

		TSAClientBouncyCastle tsaClient = new TSAClientBouncyCastle("https://freetsa.org/tsr");
		CrlClientOnline crlClients = new CrlClientOnline(chain);
		OcspClientBouncyCastle ocspClient = new OcspClientBouncyCastle(null);

		PdfSigningManager pdfSigner = new PdfSigningManager(chain,
											                crlClients,
											                ocspClient,
											                tsaClient);
		
		String  pathToLogo = "c:\\tmp\\ama\\logo.jpg";
		ImageData logo = ImageDataFactory.createJpeg(UrlUtil.toURL(pathToLogo));
		
		HashesForSigning hashInformation = pdfSigner.CreateTemporaryPdfForSigning(
																		new SigningInformation(
																				pdfToBeSigned,
																				temporaryPdf,
																				"porque sim",
																				"lx",
																				logo));
		//2ª chamada -> SCMDSign
		String hash4ama = new Criptografia().getBase64(hashInformation.getHashForSigning());
		
		/******************
		 * LER PIN *
		 ******************/
		String pin = null;
		sc= new Scanner(System.in); 
		System.out.print("PIN: ");
		pin = sc.nextLine();
System.out.println("[aceite]");
		SCMDSign sign = new SCMDSign("PDF1.pdf", hash4ama, pin, clientId);
		EntitySign signResp = sign.callEndPoint();
		if(null == signResp ) {
			System.out.println("[WS SCMDSign] - obter hash assinada: ");
			return;
		}
		
		String processoId = signResp.getProcessId();
		
		/******************
		 * LER CODIGO SMS *
		 ******************/
		sc= new Scanner(System.in); //System.in is a standard input stream.
		System.out.print("Codigo SMS: ");
		String codigoSMS = sc.nextLine(); //reads string.
System.out.println("[aceite]");
		
		ValidateOtp validateOtp = new ValidateOtp(processoId, codigoSMS);
		EntityValidateOtp validateOtpResp = validateOtp.callEndPoint();
		System.out.println("[ValidateOtpResposta] status: " + validateOtpResp.getStatus().getCode());

		byte[] assinadoAMA = validateOtpResp.getAssinatura().getBytes();
		
		SignatureInformation signaInfo = new SignatureInformation(
				temporaryPdf,
                finalPdf,
                assinadoAMA,
                hashInformation.getNakedHash(),
                null);


		pdfSigner.SignIntermediatePdf(signaInfo);
		
		System.out.println("***** FIM *****");
	}

}
