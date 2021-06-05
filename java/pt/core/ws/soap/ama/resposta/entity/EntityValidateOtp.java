package pt.core.ws.soap.ama.resposta.entity;

import java.util.List;

public class EntityValidateOtp {

	private EntitySign status = null;
	private String assinatura = null;
	private String certiticado = null;
	private List<EntityHashStructure>  hashStructure = null;
	
	public EntitySign getStatus() {
		return status;
	}
	public void setStatus(EntitySign status) {
		this.status = status;
	}
	public String getAssinatura() {
		return assinatura;
	}
	public void setAssinatura(String assinatura) {
		this.assinatura = assinatura;
	}
	public String getCertiticado() {
		return certiticado;
	}
	public void setCertiticado(String certiticado) {
		this.certiticado = certiticado;
	}
	public List<EntityHashStructure> getHashStructure() {
		return hashStructure;
	}
	public void setHashStructure(List<EntityHashStructure> hashStructure) {
		this.hashStructure = hashStructure;
	}
	
}
