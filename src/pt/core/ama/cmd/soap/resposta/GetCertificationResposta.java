package pt.core.ama.cmd.soap.resposta;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class GetCertificationResposta {

	/*
		<s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/">
	   		<s:Body>
	      		<GetCertificateResponse xmlns="http://Ama.Authentication.Service/">
	         		<GetCertificateResult> (CERTIFICADO) </GetCertificateResult>
	      		</GetCertificateResponse>
	   		</s:Body>
		</s:Envelope>
	 */
	
	
	/**
	 * Efetua o parse do SOAP recebido.
	 * 
	 * @param soap
	 * @return
	 */
	public String parseSOAP(String soap) {
		try {
			DocumentBuilderFactory dbf =DocumentBuilderFactory.newInstance();
		    DocumentBuilder db = dbf.newDocumentBuilder();
		    InputSource is = new InputSource(new StringReader(soap));

		    Document doc = db.parse(is);
		    NodeList nodeList = doc.getElementsByTagName("GetCertificateResult");
		    if(null == nodeList) return null;

		    Node node = nodeList.item(0);
		    String certificado = node.getTextContent();
		    if(certificado.isEmpty()) return null;
		    
		   return certificado; 
		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
//	public static void main(String[] args) {
//		String soap =
//			"<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">"
//			+ "   <s:Body>"
//			+ "      <GetCertificateResponse xmlns=\"http://Ama.Authentication.Service/\">"
////			+ "         <GetCertificateResult a:nil=\"true\" xmlns:a=\"http://www.w3.org/2001/XMLSchema-instance\"/>"
//			+ "          <GetCertificateResult>     </GetCertificateResult>"
//			+ "      </GetCertificateResponse>"
//			+ "   </s:Body>"
//			+ "</s:Envelope>";
//		//Nota 
//		String certificado = new GetCertificationResponse().parseSOAP(soap);
//	}
}
