package pt.ajax;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.security.cert.Certificate;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.signatures.IExternalSignatureContainer;
import com.itextpdf.signatures.PdfSigner;

import pt.core.ama.cmd.soap.metodo.ValidateOtp;
import pt.core.ama.cmd.soap.resposta.entity.EntityValidateOtp;
import pt.core.ama.cmd.util.CallAMA;
import pt.core.ama.cmd.util.Criptografia;
import pt.core.ama.pdf.MyExternalSignatureContainer;
import pt.sessao.entity.DadosSessao;

/**
 * Servlet implementation class UploadPDF
 */
@WebServlet("/CodigoSMS")
@MultipartConfig
public class CodigoSMS extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CodigoSMS() {
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

	@SuppressWarnings("static-access")
	private void setAcao(HttpServletRequest request, HttpServletResponse response){
		HttpSession sessao = request.getSession(true);
		
		DadosSessao ds = (DadosSessao) sessao.getAttribute("assinaPDF");
		String codigoSMS = request.getParameter("codigoSMS");
		
		String json = null;
		
		//obter o Hash da AMA		
		ValidateOtp validateOtp = new ValidateOtp(ds.getProcessoId(), codigoSMS);
		EntityValidateOtp validateOtpResp = validateOtp.callEndPoint();
		int code = validateOtpResp.getStatus().getCode();
		if(code != 200) {
			System.out.println("Resposta AMA com status: " + code);	
			json="{\"status\":-11}";
        	new ResponseJSON().setRespostaJSON(json, response);
        	return;
		}

		String tmp = validateOtpResp.getAssinatura();
		byte[] hashSigned = null;

		if(new Criptografia().isBase64(tmp)) hashSigned = new Criptografia().getDecodeBase64Str(tmp);
		else hashSigned = tmp.getBytes(StandardCharsets.UTF_8);
		

		try {
			BouncyCastleProvider providerBC = new BouncyCastleProvider();
	        Security.addProvider(providerBC);
	
	        //obter (via Serviço Web) todos os certificados (3 ao todo)
	        Certificate[] chain = new CallAMA().getCertificates(ds.getClienteId());
	        if(null == chain) {
	        	json="{\"status\":-10}";
	        	new ResponseJSON().setRespostaJSON(json, response);
	        	return;
	        }
			
			String dest = "c:/tmp/ama/" + System.currentTimeMillis() + ".pdf";
			PdfReader reader = new PdfReader(ds.getTmpPDF());
	        try(FileOutputStream os = new FileOutputStream(dest)) {
	            PdfSigner signer = new PdfSigner(reader, os, new StampingProperties());

	            IExternalSignatureContainer external = new MyExternalSignatureContainer(hashSigned, chain, PdfName.Adobe_PPKLite, PdfName.Adbe_pkcs7_detached);

	            // Signs a PDF where space was already reserved. The field must cover the whole document.
	            signer.signDeferred(signer.getDocument(), "sig", os, external);
	        }
	        reader.close();
	        reader = null;

	        //eliminar o ficheiro temporário
	        File f = new File(ds.getTmpPDF());
	        if(f.exists()) f.delete();
	        json="{\"status\":1}";
		}catch(Exception e) {
			System.out.println("Erro: " + e);
			json="{\"status\":1}";
		}
		new ResponseJSON().setRespostaJSON(json, response);
	}
}