package br.com.luizcurado.provaconceito.config.security.controller;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

//----------------------------------------------------------------------------------
/** DTO que guarda o email e senha de um usuário que está sendo autenticado */
//----------------------------------------------------------------------------------
public class DadosLoginDTO {

	//----------------------------------------
	// Atributos do DTO
	//----------------------------------------
	private String email;
	private String senha;

	//-----------------------------------------------------------------
	/** Converte o DTO para um objeto usado pelo Spring Security */
	//-----------------------------------------------------------------
	public UsernamePasswordAuthenticationToken converter() {
		return new UsernamePasswordAuthenticationToken(email, senha);
	}	
	
	//----------------------------------------
	// Métodos get/set
	//----------------------------------------	
	public String getEmail() {
		return email;
	}

	public String getSenha() {
		return senha;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
