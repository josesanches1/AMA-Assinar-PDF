package pt.core.ws.soap.ama.resposta.entity;

public class EntityHashStructure {

	private String id = null;			//Indentificador do documento
	private String assinatura = null;	//Assinatura do documento
	private String nome = null;			//Nome do documento
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAssinatura() {
		return assinatura;
	}
	public void setAssinatura(String assinatura) {
		this.assinatura = assinatura;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}