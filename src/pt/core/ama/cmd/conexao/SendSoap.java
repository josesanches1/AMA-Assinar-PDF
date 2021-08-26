package pt.core.ama.cmd.conexao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import pt.core.ama.cmd.util.Config;

public class SendSoap {

	/**
	 * Invoca o EndPoint com o soap correspondente
	 * 
	 * 
	 * @param SOAP
	 * @param SOAPAction
	 * @return
	 */
	public String setConnection(String SOAP, String SOAPAction){
		if(null == SOAP) return null;
		try {
			URL oURL = new URL(Config.getAndPointAMA());
			HttpURLConnection con = (HttpURLConnection) oURL.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Authorization", Config.getAuthBasic());
			con.setRequestProperty("Content-type", "text/xml; charset=utf-8");
			con.setRequestProperty("SOAPAction", SOAPAction);
			con.setDoOutput(true);

//System.out.println(SOAP);
		   
			OutputStream reqStream = con.getOutputStream();
			reqStream.write(SOAP.getBytes());
		   
			int responseCode = con.getResponseCode();
//System.out.println("HTTP CODE: " + responseCode);		
			if (responseCode == HttpURLConnection.HTTP_OK) { //success
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
	   				response.append(inputLine);
				}
			   	in.close();
//System.out.println("Resposta [OK]: " + response);
				return response.toString();
			}
	   		//codigo HTTP diferente de 200
	   		BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
	        StringBuffer result = new StringBuffer();
	        String line = "";
	        while ((line = in.readLine()) != null) {
	           result.append(line);
	        }
//System.out.println("Resposta [ERRO]: " + result);
	        in.close();
	        return result.toString();
		}catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}	
}