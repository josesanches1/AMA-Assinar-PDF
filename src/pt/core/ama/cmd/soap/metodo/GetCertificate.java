package pt.core.ama.cmd.soap.metodo;

import java.security.cert.X509Certificate;

import pt.core.ama.cmd.conexao.SendSoap;
import pt.core.ama.cmd.soap.resposta.GetCertificationResposta;
import pt.core.ama.cmd.util.Config;
import pt.core.ama.cmd.util.Criptografia;

public class GetCertificate {

	
	private final String SOAP =
			"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ama=\"http://Ama.Authentication.Service/\">" +
		 	"<soapenv:Header/>" +
		 	"<soapenv:Body>" +
		 	"<ama:GetCertificate>" +
		 	"<ama:applicationId>%s</ama:applicationId>" +
		 	"<ama:userId>%s</ama:userId>" +
		 	"</ama:GetCertificate>" +
		 	"</soapenv:Body>" +
		 	"</soapenv:Envelope>";
	
	//Metodo no endpoint
	private final String SOAPAction = "http://Ama.Authentication.Service/SCMDService/GetCertificate"; 
	
	private String clientId = null;
	private boolean hasErroCertificado = false;
	
	/**
	 * Contrutor
	 * 
	 * @param applicationId
	 * @param clientId
	 */
	public GetCertificate(String clientId) {
		Criptografia criptografia= new Criptografia();
		this.clientId = criptografia.getCifraAMA(clientId);
		criptografia = null;
		hasErroCertificado = null == this.clientId;
	}
	
	/**
	 * Obtém a string soap formatada para enviar ao serviço web
	 * 
	 * @return
	 */
	private String getSOAP() {
		if(hasErroCertificado) return null;

		return 
				String.format(
						SOAP, 
							Config.getApplicationId(),
							clientId);
		
	}
	
	/**
	 * Efetua uma conexão ao EndPoint, submete o request (SOAP).
	 * Devolve a resposta dos certificados do utilizador. 
	 */
	public X509Certificate[] callEndPoint() {
		if(hasErroCertificado) return null;
		String soap = getSOAP();
		if(null ==soap) return null;
		
		//efetua a chamada ao servico web
		String resposta = new SendSoap().setConnection(soap, SOAPAction);
		if(null == resposta) return null;
		try {
			//Nota: atenção pode existir mais do que um certificado.
			String certificado = new GetCertificationResposta().parseSOAP(resposta);
			
			String[] tmp = certificado.split("-----END CERTIFICATE-----");
			X509Certificate[] certs = new X509Certificate[tmp.length];
			for (int i = 0; i < tmp.length; i++) {
				String _tmp = tmp[i].replaceFirst("-----BEGIN CERTIFICATE-----", "");
				certs[i] = new Criptografia().getCertificado(_tmp);
			}
			return certs;
		}catch(NullPointerException e) {}
		return null;
	}
}