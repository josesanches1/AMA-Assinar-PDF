package pt.iText;

public class HashesForSigning {
	
	

	byte[] HashForSigning = null; 
	byte[] NakedHash = null;
	
	public HashesForSigning(byte[] HashForSigning, byte[] NakedHash) {
		this.HashForSigning = HashForSigning;
		this.NakedHash = NakedHash;
	}

	public byte[] getHashForSigning() {
		return HashForSigning;
	}
	public void setHashForSigning(byte[] hashForSigning) {
		HashForSigning = hashForSigning;
	}
	public byte[] getNakedHash() {
		return NakedHash;
	}
	public void setNakedHash(byte[] nakedHash) {
		NakedHash = nakedHash;
	}
	
	
}
