package br.com.luizcurado.provaconceito.config.security.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.luizcurado.provaconceito.config.security.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

//----------------------------------------------------------------------------
/** Service usado para geração e verificação de tokens para usuários */
//----------------------------------------------------------------------------
@Service
public class TokenService {
	
	@Value("${forum.jwt.expiration}")
	private String expiration;
	
	@Value("${forum.jwt.secret}")
	private String secret;

	//--------------------------------------------------------------
	/** Gera um token para o usuário que foi autenticado */
	//--------------------------------------------------------------
	public String gerarToken(Authentication authentication) {
		Usuario usuarioAutenticado = (Usuario) authentication.getPrincipal();
		Date hoje = new Date();
		Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));
		
		return Jwts.builder()
				.setIssuer("API do Fórum da Alura")
				.setSubject(usuarioAutenticado.getId().toString())
				.setIssuedAt(hoje)
				.setExpiration(dataExpiracao)
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}
	
	//--------------------------------------------------------------
	/** Verifica se o token é válido */
	//--------------------------------------------------------------
	public boolean isTokenValido(String token) {
		try {
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	//--------------------------------------------------------------
	/** Recupera do token o id do usuário */
	//--------------------------------------------------------------	
	public Long getIdUsuario(String token) {
		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		return Long.parseLong(claims.getSubject());
	}

}
