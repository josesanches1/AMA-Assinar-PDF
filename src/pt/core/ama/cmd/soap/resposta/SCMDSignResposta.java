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

import pt.core.ama.cmd.soap.resposta.entity.EntitySign;

public class SCMDSignResposta {

	/**
	 * Efetua o parse do SOAP recebido.
	 * 
	 * @param soap
	 * @return
	 */
	public EntitySign parseSOAP(String soap) {
		try {
			DocumentBuilderFactory dbf =DocumentBuilderFactory.newInstance();
		    DocumentBuilder db = dbf.newDocumentBuilder();
		    InputSource is = new InputSource(new StringReader(soap));

		    Document doc = db.parse(is);
		    String tmp = getValor("a:Code", doc);
		    if(null == tmp) {
		    	System.out.println("[SCMDSignResposta] impossivel obter a tag (Code). SOAP: " + soap);
		    	return null;
		    }
		    int httpCode = Integer.parseInt(tmp);
		    EntitySign resp = new EntitySign();
		    resp.setCode(httpCode);
		    //processId
		    resp.setProcessId(getValor("a:ProcessId", doc));
		    //Message
		    tmp = getValor("a:Message", doc);
		    if(null == tmp) {
		    	System.out.println("[SCMDSignResposta] impossivel obter a tag (a:Message). SOAP: " + soap);
		    	return null;
		    }
		    resp.setMessage(tmp);
		    //Field (aceita-se null)
		    resp.setField(getValor("a:Field", doc));
		    //FieldValue (aceita-se null)
		    resp.setField(getValor("a:FieldValue", doc));
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
	    String val = node.getTextContent();
	    if(val.isEmpty()) return null;
	    return val;
	}

}
