package pt.core.ama.cmd.util;

import java.security.cert.X509Certificate;

import pt.core.ama.cmd.soap.metodo.GetCertificate;
import pt.core.ama.cmd.soap.metodo.SCMDSign;
import pt.core.ama.cmd.soap.resposta.entity.EntitySign;

public class CallAMA {

	/**
	 * Obtem os certificados da AMA associado ao telemovel (utilizador)
	 * 
	 * @param clientId
	 * @return
	 */
	public X509Certificate[] getCertificates(String clientId) {
		GetCertificate getCert = new GetCertificate(clientId);
		return getCert.callEndPoint();
	}
	
	/**
	 * Concatena o prefixo fornecido pela AMA com o Hash do PDF (original).
	 * Envia a informação para a AMA via Serviço Web, aguarda-se SMS.
	 * 
	 * @param hashForSigned
	 * @param clientId
	 * @param pin
	 * @return
	 */
	public String getProcessId(byte[] hashForSigned, String nomeOriginalPDF, String clientId, String pin) {
		if(new Criptografia().isBase64(hashForSigned)) {
			System.out.println("HASH para assinar em Base 64!");
			hashForSigned = new Criptografia().getDecodeBase64(hashForSigned);
		}
	
		//2ª chamada -> SCMDSign
		final byte[] _sha256SigPrefix = {0x30, 0x31, 0x30, 0x0d, 0x06, 0x09, 0x60, (byte)0x86, 0x48, 0x01, 0x65, 0x03, 0x04, 0x02, 0x01, 0x05, 0x00, 0x04, 0x20};
		//prepend sha256 prefix to hash for send signed
		byte[] hashToBeSignedByAma = new byte[_sha256SigPrefix.length + hashForSigned.length];
		System.arraycopy(_sha256SigPrefix, 0, hashToBeSignedByAma, 0, _sha256SigPrefix.length );
		System.arraycopy(hashForSigned, 0, hashToBeSignedByAma, _sha256SigPrefix.length, hashForSigned.length );
		
		String hash4ama = new Criptografia().getEncodeBase64Str(hashToBeSignedByAma);
//System.out.println("Tamanho HASH: " + hashForSigned.length + ", Tamanho pre: " + _sha256SigPrefix.length +", Tamnho HASH com pre: " + hashToBeSignedByAma.length);


		SCMDSign sign = new SCMDSign(nomeOriginalPDF, hash4ama, pin, clientId);
		EntitySign signResp = sign.callEndPoint();
		if(null == signResp ) {
			System.out.println("[WS SCMDSign] - obter hash assinada: ");
			return null;
		}

		String processoId = signResp.getProcessId();
		return processoId;
	}
}
