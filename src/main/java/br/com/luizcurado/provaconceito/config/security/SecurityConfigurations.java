package br.com.luizcurado.provaconceito.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.luizcurado.provaconceito.config.security.repository.UsuarioRepository;
import br.com.luizcurado.provaconceito.config.security.service.AutenticacaoService;
import br.com.luizcurado.provaconceito.config.security.service.TokenService;

@EnableWebSecurity
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

	@Autowired
	private AutenticacaoService autenticacaoService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	//----------------------------------------------------------------------------------------------------
	/** Configuracoes de autenticacao. Define o service responsável por recuperar o usuário que está 
	 *  sendo autenticado (UserDetailsService), e também o algoritmo usado para codificar sua senha.
	 */
	//----------------------------------------------------------------------------------------------------
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder()) ;
	}
	
	//--------------------------------------------------------------
	/** Configuracoes de autorizacao */
	//--------------------------------------------------------------
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		// Permite o acesso aos endpoints de consulta
		.antMatchers(HttpMethod.GET, "/produtos").permitAll()
		.antMatchers(HttpMethod.GET, "/produtos/**").permitAll()
		.antMatchers(HttpMethod.POST, "/auth").permitAll()
		.antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
		.antMatchers(HttpMethod.GET, "/swagger-ui.html").permitAll()
		
		// Restringe o acesso aos outros endpoints a usuários autenticados
		.anyRequest().authenticated()
		.and().csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		
		// Adiciona o filtro que autentica via token, para ele processar o request antes do UsernamePasswordAuthenticationFilter
		.and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class);
	}
	
	
	//-------------------------------------------------------------------------
	/** Configuracoes de recursos estaticos(js, css, imagens, etc.) */
	//-------------------------------------------------------------------------
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**");
	}
	
}







