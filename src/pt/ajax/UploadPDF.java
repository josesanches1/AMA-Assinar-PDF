package pt.ajax;

import java.io.IOException;
import java.io.InputStream;
import java.security.Security;
import java.security.cert.Certificate;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import pt.core.ama.cmd.util.CallAMA;
import pt.core.ama.pdf.AssinaturaVazia;
import pt.sessao.entity.DadosSessao;

/**
 * Servlet implementation class UploadPDF
 */
@WebServlet("/UploadPDF")
@MultipartConfig
public class UploadPDF extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadPDF() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		setAcao(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		setAcao(request,response);
	}

	private void setAcao(HttpServletRequest request, HttpServletResponse response){
		HttpSession sessao = request.getSession(true);
		
		DadosSessao ds = new DadosSessao();
		//Obter os dados
		//facultativos
		ds.setMotivo(request.getParameter("motivo"));
		ds.setLocal(request.getParameter("local"));
		String tmp = request.getParameter("posX");
		ds.setPosX(null==tmp?-1:Float.parseFloat(tmp));
		tmp = request.getParameter("posY");
		ds.setPosY(null==tmp?-1:Float.parseFloat(tmp));
		tmp = request.getParameter("pagina");
		ds.setPagina(null==tmp?-1:Integer.parseInt(tmp));
		tmp = request.getParameter("pagH");
		ds.setPagH(null==tmp?-1:Float.parseFloat(tmp));
		tmp = request.getParameter("pagW");
		ds.setPagW(null==tmp?-1:Float.parseFloat(tmp));
		//Obrigatórios
		ds.setClienteId(request.getParameter("telemovel"));
		ds.setPIN(request.getParameter("pin"));
		String json = null;
		try {
			Part part = request.getPart("filePDF");
			InputStream origemPDF = part.getInputStream();
			String nomePDF = part.getSubmittedFileName();
			
			BouncyCastleProvider providerBC = new BouncyCastleProvider();
	        Security.addProvider(providerBC);
	
	        //obter (via Serviço Web) todos os certificados (3 ao todo)
	        Certificate[] chain = new CallAMA().getCertificates(ds.getClienteId());
	        if(null == chain) {
	        	json="{\"status\":-10}";
	        	new ResponseJSON().setRespostaJSON(json, response);
	        	return;
	        }
	        //ler o HASH para ser assinado (via Serviço Web) e cria um ficheiro PDF temporário com a informação.
	        String tmpPDF = System.getProperty("java.io.tmpdir") + System.currentTimeMillis()  + ".pdf";
	        ds.setTmpPDF(tmpPDF);
	        //A partir do PDF original, lê o HASH para ser assinado,
	      	//Cria um PDF temporário (com uma assinatura vazia).
	      	//Nota: "sig" é o nome do campo que identifica a assinatura no PDF
	        byte[] hash4Sign = new AssinaturaVazia().emptySignature(tmpPDF, "sig", chain, origemPDF, ds);
	        if(null == hash4Sign) {
	        	json="{\"status\":-11}";
	        	new ResponseJSON().setRespostaJSON(json, response);
	        	return;
	        }
	        //Concatena "sha256SigPrefix" com o HASH obtido do PDF (o HASH do PDF deve ter 32 bytes, com o valor "sha256SigPrefix" fica com 51 bytes)
	        //Envia o HASH (51 bytes) para a AMA
	        //Após a invocação deste metodo (Serviço Web) aguarda-se sms da AMA com codigo
	        String processoId = new CallAMA().getProcessId(hash4Sign, nomePDF, ds.getClienteId(), ds.getPIN());
	        if(null == processoId) {
	        	json="{\"status\":-12}";
	        	new ResponseJSON().setRespostaJSON(json, response);
	        	return;
	        }
	        ds.setProcessoId(processoId);
	        //Guarda a informação em sessão e devolve o controlo ao browser
	        sessao.setAttribute("assinaPDF", ds);
	        json="{\"status\":1}";
	        
		}catch(Exception e) {
			System.out.println("Erro: " + e);
			json="{\"status\":-1}";
		}
		new ResponseJSON().setRespostaJSON(json, response);
	}
}