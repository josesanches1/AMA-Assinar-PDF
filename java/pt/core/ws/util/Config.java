package pt.core.ws.util;

public class Config {

	private static final String CERT_AMA = "C:/tmp/ama/certnew.cer";
	private static final String ENDPOINT_AMA = "https://preprod.cmd.autenticacao.gov.pt/Ama.Authentication.Frontend/SCMDService.svc";
	private static final String APPID = "xxx-xxx-xx..."; //fornecidas pela AMA
	private static final String USER = "...."; //fornecidas pela AMA
	private static final String PWD = "....";//fornecidas pela AMA
	
	private static String applicationId = null;
	private static String user = null;
	private static String pwd = null;
	private static String basic = null;
	
	
	public static String getApplicationId() {
		if(null==applicationId) applicationId = new Criptografia().getBase64(APPID);
		return applicationId;
	}
	
	private static String getUser() {
		if(null==user) user = USER;
		return user;
	}
	
	private static String getPwd() {
		if(null==pwd) pwd = PWD;
		return pwd;
	}
	
	public static String getAuthBasic() {
		if(null==basic) basic = "Basic " + new Criptografia().getBase64(getUser() + ":" + getPwd());
		return basic;
	}
	
	public static String getCertificadoAMA() {
		return CERT_AMA;
	}
	
	public static String getAndPointAMA() {
		return ENDPOINT_AMA;
	}
	

}