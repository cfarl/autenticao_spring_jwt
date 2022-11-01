package br.com.luizcurado.provaconceito.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.luizcurado.provaconceito.config.security.model.Usuario;
import br.com.luizcurado.provaconceito.config.security.repository.UsuarioRepository;
import br.com.luizcurado.provaconceito.config.security.service.TokenService;

//--------------------------------------------------------
/** Filtro que faz a autenticação via token */
//--------------------------------------------------------
public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {
	
	private TokenService tokenService;
	private UsuarioRepository repository;

	public AutenticacaoViaTokenFilter(TokenService tokenService, UsuarioRepository repository) {
		this.tokenService = tokenService;
		this.repository = repository;
	}

	//------------------------------------------------------------------------------------------------
	/** Faz a autenticacao do usuário caso o token seja encontrado no header da requisição HTTP */
	//------------------------------------------------------------------------------------------------
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// Recupera o token "Authorization" do header da requisição 
		String token = recuperaToken(request);
		
		// Verifica se o token é válido
		boolean valido = tokenService.isTokenValido(token);
		
		// Se o token for válido, informa o Spring Security que a autenticação já foi feita
		if (valido) {
			autenticaUsuarioParaSpringSecurity(token);
		}
		
		// Chama o próximo filtro da sequência
		filterChain.doFilter(request, response);
	}

	//---------------------------------------------------------------------
	/** Informa o Spring Security que a autenticação já foi feita */
	//---------------------------------------------------------------------
	private void autenticaUsuarioParaSpringSecurity(String token) {
		// Recupera o id do usuário do token
		Long idUsuario = tokenService.getIdUsuario(token);
		
		// Recupera o usuário que tem o id informado
		Usuario usuario = repository.findById(idUsuario).get();
		
		// Passa o usuário autenticado para o Spring Security
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	//-------------------------------------------------------------------
	/** Recupera o token "Authorization" do header da requisição  */
	//-------------------------------------------------------------------
	private String recuperaToken(HttpServletRequest request) {
		// Recupera o valor do header "Authorization"
		String token = request.getHeader("Authorization");
		
		// Recupera do valor do header o token que aparece após o texto "Bearer " 
		if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		}
		
		return token.substring(7, token.length());
	}

}
