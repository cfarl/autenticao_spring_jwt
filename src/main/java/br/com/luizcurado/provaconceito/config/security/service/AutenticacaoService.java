package br.com.luizcurado.provaconceito.config.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.luizcurado.provaconceito.config.security.model.Usuario;
import br.com.luizcurado.provaconceito.config.security.repository.UsuarioRepository;

//---------------------------------------------------------------------------------------------------------------------
/** Service usado na autenticacao do usuário. Contém um método que retorna o usuário com sua senha (UserDetails). */
//---------------------------------------------------------------------------------------------------------------------
@Service
public class AutenticacaoService implements UserDetailsService {
	
	@Autowired
	private UsuarioRepository repository;

	//-----------------------------------------------------------------------------------------
	/** Retorna o usuário com sua senha, em um objeto do Spring Security (UserDetails) */
	//-----------------------------------------------------------------------------------------
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> usuario = repository.findByEmail(username);
		if (usuario.isPresent()) {
			return usuario.get();
		}
		
		throw new UsernameNotFoundException("Dados inválidos!");
	}

}
