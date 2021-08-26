package pt.core.ama.cmd.soap.metodo;

import pt.core.ama.cmd.conexao.SendSoap;
import pt.core.ama.cmd.soap.resposta.SCMDSignResposta;
import pt.core.ama.cmd.soap.resposta.entity.EntitySign;
import pt.core.ama.cmd.util.Config;
import pt.core.ama.cmd.util.Criptografia;

public class SCMDSign  {

	
	private final String SOAP =
			"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ama=\"http://Ama.Authentication.Service/\" xmlns:ama1=\"http://schemas.datacontract.org/2004/07/Ama.Structures.CCMovelSignature\">" +
			"<soapenv:Header/>" +
			"<soapenv:Body>" +
			"<ama:SCMDSign>" +
			"<ama:request>" +
			"<ama1:ApplicationId>%s</ama1:ApplicationId>" +	//1º application Id
			"<ama1:DocName>%s</ama1:DocName>" +				//2º nome Documento
			"<ama1:Hash>%s</ama1:Hash>" +					//3º hash documento
			"<ama1:Pin>%s</ama1:Pin>" +						//4º pin
			"<ama1:UserId>%s</ama1:UserId>" +				//5º client Id
			"</ama:request>" +
			"</ama:SCMDSign>" +
			"</soapenv:Body>" +
			"</soapenv:Envelope>";
	
	//Metodo no endpoint
	private final String SOAPAction = "http://Ama.Authentication.Service/SCMDService/SCMDSign"; 
	
	private String nomeDocumento = null;
	private String Base64Hash = null;
	private String pin = null;
	private String clientId = null;
	
	/**
	 * Contrutor
	 * 
	 * @param applicationId
	 * @param clientId
	 */
	public SCMDSign(String nomeDocumento, String Base64Hash, String pin, String clientId) {
		Criptografia criptografia= new Criptografia();
		this.clientId = criptografia.getCifraAMA(clientId);
		this.pin = criptografia.getCifraAMA(pin);
		this.nomeDocumento = nomeDocumento;
		this.Base64Hash = Base64Hash;
		criptografia = null;
	}
	
	/**
	 * Obtém a string soap formatada para enviar ao serviço web
	 * 
	 * @return
	 */
	private String getSOAP() {
		if(null == clientId || null == pin) return null;
		return 
				String.format(
						SOAP, 
							Config.getApplicationId(),
							nomeDocumento,
							Base64Hash,
							pin,
							clientId);
	}
	
	/**
	 * Efetua uma conexão ao EndPoint e submete request (SOAP).
	 * Se houve uma resposta (envelope) efetua o parse da informação, para obtenção da resposta. 
	 */
	public EntitySign callEndPoint() {
		if(null == clientId || null == pin) return null;
		String resposta = new SendSoap().setConnection(getSOAP(), SOAPAction);
		if(null != resposta) {
			//Nota: atenção pode existir mais do que um certificado.
			return new SCMDSignResposta().parseSOAP(resposta);
		}
		return null;
	}
}