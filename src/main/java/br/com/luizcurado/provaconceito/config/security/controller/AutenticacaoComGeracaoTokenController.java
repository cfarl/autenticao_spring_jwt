package br.com.luizcurado.provaconceito.config.security.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.luizcurado.provaconceito.config.security.dto.TokenDto;
import br.com.luizcurado.provaconceito.config.security.service.TokenService;

//------------------------------------------------------------------------------------------
/**
 * Faz a autenticacao do usuário, usando login e senha, e retorna um token JWT.
 * Ao chamar a url http://localhost:8080/auth com POST, informando no body:
 * {
 *	 "email" : "lrcurado@email.com",
 *	 "senha" : "123456"
 * }
 * Será retornado o seguinte JSON:
 * {
 *	 "token": "eyJhbGciOiJIUzI1NiJ9....",
 * 	 "tipo": "Bearer"
 * } 
 */
//------------------------------------------------------------------------------------------
@RestController
@RequestMapping("/auth")
public class AutenticacaoComGeracaoTokenController {
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private TokenService tokenService;

	@PostMapping
	public ResponseEntity<TokenDto> autenticar(@RequestBody @Valid DadosLoginDTO dadosLoginDTO) {
		// Recupera usuário e senha, na forma de um DTO do Spring Security
		UsernamePasswordAuthenticationToken dadosLoginSpringSecurity = new UsernamePasswordAuthenticationToken(dadosLoginDTO.getEmail(), dadosLoginDTO.getSenha());
		
		try {
			// Faz a autenticacao do usuário, chamando o AutenticacaoService, que implementa UserDetailsService
			Authentication authentication = authManager.authenticate(dadosLoginSpringSecurity);
			
			// Feita a autenticacao, gera um token para o usuário
			String token = tokenService.gerarToken(authentication);
			return ResponseEntity.ok(new TokenDto(token, "Bearer"));
			
		} catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
}
