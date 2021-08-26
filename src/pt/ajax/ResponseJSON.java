package pt.ajax;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class ResponseJSON {

	/**
	 * Área central para todas as respostas JSON.
	 * 
	 * @param json
	 * @param response
	 */
	public void setRespostaJSON(String json, HttpServletResponse response){
//System.out.println("JSON : " + json);
		try{
			//json=Util.getValor(json);
			response.setContentType("application/json; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			
			response.getWriter().write(json);
		}catch(IOException e){
			System.err.println("ERRO: " + e);
		}
	}
}
