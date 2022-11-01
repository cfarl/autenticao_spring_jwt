package br.com.luizcurado.provaconceito.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//-------------------------------------------------------------
/** Entidade persistente que representa um produto */
//-------------------------------------------------------------
@Entity
@Table(name = "Produto")
public class Produto {
	//--------------------------------------------------------
	// Atributos da classe
	//--------------------------------------------------------
	@Id 
	@Column(name="id") 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="nome_produto")
	private String nome;
	
	//--------------------------------------------------------
	// Métodos get/set
	//--------------------------------------------------------
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
