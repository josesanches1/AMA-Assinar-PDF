package pt.iText;

import com.itextpdf.signatures.ITSAClient;

public class SignatureInformation {

	private String PathToIntermediaryPdf = null;
	private String pathToSignedPdf = null;
	private byte[] Signature = null;
	private byte[] NakedHashFromIntermediaryPdf = null;
	private ITSAClient tsaClient = null;
	
	public SignatureInformation(String PathToIntermediaryPdf,
             String pathToSignedPdf,
             byte[] Signature,
             byte[] NakedHashFromIntermediaryPdf,
             ITSAClient tsaClient) {
		
		this.PathToIntermediaryPdf = PathToIntermediaryPdf;
		this.pathToSignedPdf = pathToSignedPdf;
		this.Signature = Signature;
		this.NakedHashFromIntermediaryPdf = NakedHashFromIntermediaryPdf;
		this.tsaClient = tsaClient;
	}

	public String getPathToIntermediaryPdf() {
		return PathToIntermediaryPdf;
	}

	public String getPathToSignedPdf() {
		return pathToSignedPdf;
	}

	public byte[] getSignature() {
		return Signature;
	}

	public byte[] getNakedHashFromIntermediaryPdf() {
		return NakedHashFromIntermediaryPdf;
	}

	public ITSAClient getTsaClient() {
		return tsaClient;
	}
}