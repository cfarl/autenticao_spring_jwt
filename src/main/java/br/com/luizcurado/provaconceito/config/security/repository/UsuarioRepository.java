package br.com.luizcurado.provaconceito.config.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.luizcurado.provaconceito.config.security.model.Usuario;

//---------------------------------------------------------------------------
/** Repositório usado para consulta de usuários que serão autenticados */
//---------------------------------------------------------------------------
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	//--------------------------------------
	/** Recupera usuário por email */
	//--------------------------------------
	Optional<Usuario> findByEmail(String email);

}
