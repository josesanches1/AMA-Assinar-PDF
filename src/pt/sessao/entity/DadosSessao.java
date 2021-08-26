package pt.sessao.entity;

public class DadosSessao {
	
	//info recebidos
	private int pagina = -1;
	private float posX = -1;
	private float posY = -1;
	private float pagW = -1;
	private float pagH = -1;
	
	private String motivo = null;
	private String local = null;
	private String clienteId = null;
	private String PIN = null;
	
	//info criado durante o processo
	private String processoId = null;
	private String tmpPDF = null;
	
	public float getPosX() {
		return posX;
	}
	public void setPosX(float posX) {
		this.posX = posX;
	}
	public float getPosY() {
		return posY;
	}
	public void setPosY(float posY) {
		this.posY = posY;
	}
	public float getPagW() {
		return pagW;
	}
	public void setPagW(float pagW) {
		this.pagW = pagW;
	}
	public float getPagH() {
		return pagH;
	}
	public void setPagH(float pagH) {
		this.pagH = pagH;
	}
	public int getPagina() {
		return pagina;
	}
	public void setPagina(int pagina) {
		this.pagina = pagina;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
	public String getClienteId() {
		return clienteId;
	}
	public void setClienteId(String clienteId) {
		this.clienteId = clienteId;
	}
	public String getPIN() {
		return PIN;
	}
	public void setPIN(String pIN) {
		PIN = pIN;
	}
	public String getProcessoId() {
		return processoId;
	}
	public void setProcessoId(String processoId) {
		this.processoId = processoId;
	}
	public String getTmpPDF() {
		return tmpPDF;
	}
	public void setTmpPDF(String tmpPDF) {
		this.tmpPDF = tmpPDF;
	}
}
