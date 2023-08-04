package br.com.senai.core.domain;

public class Endereco {
	
	private String cidade;
	private String longradouro;
	private String bairro;
	private String complemento;
	
	public Endereco(String cidade, String longradouro, String bairro, String complemento) {
		this.cidade = cidade;
		this.longradouro = longradouro;
		this.bairro = bairro;
		this.complemento = complemento;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getLongradouro() {
		return longradouro;
	}

	public void setLongradouro(String longradouro) {
		this.longradouro = longradouro;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	

}
