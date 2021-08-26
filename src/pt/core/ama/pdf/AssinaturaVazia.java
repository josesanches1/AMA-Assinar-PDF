package pt.core.ama.pdf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.util.StreamUtil;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.signatures.CertificateInfo;
import com.itextpdf.signatures.PdfSignatureAppearance;
import com.itextpdf.signatures.PdfSigner;

import pt.sessao.entity.DadosSessao;

public class AssinaturaVazia {

	
	public byte[] emptySignature(String dest, String fieldname, Certificate[] chain, InputStream origemPDF, DadosSessao ds) throws IOException, GeneralSecurityException {
        PdfReader reader = new PdfReader(origemPDF);
        PdfSigner signer = new PdfSigner(reader, new FileOutputStream(dest), new StampingProperties().useAppendMode());
        if(ds.getPagina()>0) {
        	//https://github.com/mkl-public/testarea-itext7/blob/master/src/test/java/mkl/testarea/itext7/signature/CreateSpecialSignatureAppearance.java
        	PdfSignatureAppearance appearance = signer
        			.getSignatureAppearance()
        			.setReason(ds.getMotivo())
        			.setLocation(ds.getLocal())
                    .setReuseAppearance(false);
        	
        	//Rectangle rect = new Rectangle(ds.getPosX(), ds.getPosY(), 200, 80);
        	Rectangle pagesize = signer.getDocument().getPage(ds.getPagina()).getPageSize();

        	float newPosY = pagesize.getTop() - (ds.getPosY() * pagesize.getTop() / ds.getPagH()) - 52;
        	float newPosX = ds.getPosX() * pagesize.getWidth() / ds.getPagW();
        	
        	Rectangle rect = new Rectangle(newPosX, newPosY, 200, 52);
            appearance.setPageRect(rect).setPageNumber(ds.getPagina());
            signer.setFieldName(fieldname);

        	// create the appearance yourself using code borrowed from iText's default for NAME_AND_DESCRIPTION
            PdfFormXObject layer2 = appearance.getLayer2();
            PdfCanvas canvas = new PdfCanvas(layer2, signer.getDocument());

            PdfFont font = PdfFontFactory.createFont();

            String name = null;
            CertificateInfo.X500Name x500name = CertificateInfo.getSubjectFields((X509Certificate)chain[0]);
            if (x500name != null) {
                name = x500name.getField("CN");
                if (name == null)
                    name = x500name.getField("E");
            }
            if (name == null)
                name = "";

            Rectangle dataRect = new Rectangle(52, 0, rect.getWidth() - 52, rect.getHeight());
            Rectangle logo = new Rectangle(0, 0, 50, 50);

        	// using different, customized font sizes 
            try (@SuppressWarnings("deprecation")
			Canvas layoutCanvas = new Canvas(canvas, signer.getDocument(), logo);) {
            	try (InputStream imageResource = getClass().getResourceAsStream("/recursos/logo.png")) {
	      	        ImageData imageData = ImageDataFactory.create(StreamUtil.inputStreamToArray(imageResource));
	      	        Image img = new Image(imageData); 
	      	        layoutCanvas.add(img);
            	}
            }

            try (@SuppressWarnings("deprecation")
			Canvas layoutCanvas = new Canvas(canvas, signer.getDocument(), dataRect);) {
                Paragraph paragraph = new Paragraph().setFont(font).setMargin(0).setMultipliedLeading(0.9f);
                paragraph.add(new Text("Assinado digitalmente por: ").setFontSize(8));
                paragraph.add(new Text(name + '\n').setFontSize(9));
                paragraph.add(new Text("Data: " + new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z").format(signer.getSignDate().getTime()) + '\n').setFontSize(6));
                paragraph.add(new Text("Rezão: " + appearance.getReason() + '\n').setFontSize(8));
                paragraph.add(new Text("Local: " + appearance.getLocation()).setFontSize(8));
                layoutCanvas.add(paragraph);
            }
          
          
          
//          try (InputStream imageResource = getClass().getResourceAsStream("/recursos/logo.png")) {
//  	        ImageData imageData = ImageDataFactory.create(StreamUtil.inputStreamToArray(imageResource));
//  	        imageData.setHeight(60);
//  	        imageData.setWidth(60);
        	
        	
        	
        	
        }
        
        
        

//        appearance
//        		.setLayer2FontSize(6)
////                .setPageRect(new Rectangle(36, 748, 250, 50))
//        		.setPageRect(new Rectangle(ds.getPosX(), ds.getPosY(), 250, 50))
//                .setPageNumber(ds.getPagina())
//                .setCertificate(chain[0])
//                .setSignatureCreator("DGEG");
//        signer.setFieldName(fieldname);
	        
	    /* ExternalBlankSignatureContainer constructor will create the PdfDictionary for the signature
         * information and will insert the /Filter and /SubFilter values into this dictionary.
         * It will leave just a blank placeholder for the signature that is to be inserted later.
         */
        MyExternalBlankSignatureContainer external = new MyExternalBlankSignatureContainer(chain, PdfName.Adobe_PPKLite, PdfName.Adbe_pkcs7_detached);

       
        // Sign the document using an external container.
        // 8192 is the size of the empty signature placeholder.
        signer.signExternalContainer(external, 8192);
        byte[] hash4Sign = external.getHash4Sign();
        return hash4Sign;
    }
}
