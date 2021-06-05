package pt.iText;

import com.itextpdf.io.image.ImageData;

public class SigningInformation {

	private String pdftobesigned;
	private String temporarypdf;
	private int PageNumber = 1; 
	private String reason = "";
	private String Location = ""; 
	private ImageData Logo = null;
	
	public SigningInformation(String pdftobesigned, String temporarypdf, String reason, String Location, ImageData logo2) {
		this.pdftobesigned = pdftobesigned;
		this.temporarypdf = temporarypdf;
		this.reason = reason;
		this.Location = Location;
		Logo = logo2;
	}

	public String getPdftobesigned() {
		return pdftobesigned;
	}

	public String getTemporarypdf() {
		return temporarypdf;
	}

	public int getPageNumber() {
		return PageNumber;
	}

	public String getReason() {
		return reason;
	}

	public String getLocation() {
		return Location;
	}

	public ImageData getLogo() {
		return Logo;
	}
	
	
}
