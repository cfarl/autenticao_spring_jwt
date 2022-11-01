package br.com.luizcurado.provaconceito.config.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

//-------------------------------------------------------------------
/** Entidade que guarda os dados de um perfil de usuário */
//-------------------------------------------------------------------
@Entity
public class Perfil implements GrantedAuthority {
	
	private static final long serialVersionUID = 1L;

	//------------------------------------------------------
	// Atributos da classe
	//------------------------------------------------------
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nome;

	//-----------------------------------------------------------------
	/** Recupera o nome do perfil, de acordo com o Spring Security */
	//-----------------------------------------------------------------
	@Override
	public String getAuthority() {
		return nome;
	}
	
	//------------------------------------------------
	// Métodos get/set
	//------------------------------------------------
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
