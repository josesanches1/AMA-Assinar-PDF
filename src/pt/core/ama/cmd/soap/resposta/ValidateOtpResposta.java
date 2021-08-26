package pt.core.ama.cmd.soap.resposta;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pt.core.ama.cmd.soap.resposta.entity.EntitySign;
import pt.core.ama.cmd.soap.resposta.entity.EntityValidateOtp;

public class ValidateOtpResposta {

	
	/**
	 * Efetua o parse do SOAP recebido.
	 * 
	 * @param soap
	 * @return
	 */
	public EntityValidateOtp parseSOAP(String soap) {
		try {
			DocumentBuilderFactory dbf =DocumentBuilderFactory.newInstance();
		    DocumentBuilder db = dbf.newDocumentBuilder();
		    InputSource is = new InputSource(new StringReader(soap));

		    Document doc = db.parse(is);
		    if(null == doc) {
		    	System.out.println("[ValidateOtpResposta] erro em parse. SOAP: " + soap);
		    	return null;
		    }

		    EntityValidateOtp resp = new EntityValidateOtp();
		    
		    //obter o nó do status
		    EntitySign status = getStatus(doc);
		    if(null == status) {
		    	System.out.println("[ValidateOtpResposta] erro em parse (obter status). SOAP: " + soap);
		    	return null;
		    }
		    resp.setStatus(status);
		    
		    String assinatura = getValor("a:Signature", doc);
		    resp.setAssinatura(assinatura);

		   return resp; 
		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private String getValor(String tag, Document doc) {
		NodeList nodeList = doc.getElementsByTagName(tag);
	    if(null == nodeList) return null;

	    Node node = nodeList.item(0);
	    if(null == node) return null;
	    String val = node.getTextContent();
	    if(val.isEmpty()) return null;
	    return val;
	}
	
	private EntitySign getStatus(Document doc) {
		try {
			NodeList nl = doc.getElementsByTagName("a:Status");
		    if(null == nl) return null;
		    if (nl.getLength() == 0 || !nl.item(0).hasChildNodes()) return null;
		    nl = nl.item(0).getChildNodes();
		    EntitySign resp = new EntitySign();
		    for (int i = 0; i < nl.getLength(); i++) {
		        if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
		        	Element el = (Element) nl.item(i);
		        	switch(el.getNodeName()) {
		        		case "a:Code": resp.setCode(Integer.parseInt(el.getTextContent()));break;
		        		case "a:Field": resp.setField(el.getTextContent());break;
		        		case "a:FieldValue": resp.setFieldValue(el.getTextContent());break;
		        		case "a:Message": resp.setMessage(el.getTextContent());break;
		        		case "a:ProcessId": resp.setProcessId(el.getTextContent());break;
		        	}
		        }
		    }
		    return resp;
		}catch(Exception e) {
			System.out.println("[ValidateOtpResposta - getStatus] Erro: " + e);
		}
		return null;
	}
}
