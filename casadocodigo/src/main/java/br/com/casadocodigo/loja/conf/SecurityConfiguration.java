package br.com.casadocodigo.loja.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.casadocodigo.loja.daos.UsuarioDAO;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UsuarioDAO usuarioDao;	

	@Override 		// definindo as permissões de acesso de cada URL. A order das definições influencia. 
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeRequests()
					.antMatchers("/produtos/novo").hasRole("ADMIN")			// internamente ele ja espera que tenha ROLE_ como prefixo, ou seja, no banco tem que estar ROLE_ADMIN
					.antMatchers("/carrinho").permitAll()
					.antMatchers(HttpMethod.POST, "/produtos").hasRole("ADMIN")
					.antMatchers(HttpMethod.GET, "/produtos").hasRole("ADMIN")
					.antMatchers("/produtos/**").permitAll()
					.antMatchers("/").permitAll()
					.anyRequest().authenticated()
					.and().formLogin();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
		authManagerBuilder.userDetailsService(usuarioDao).passwordEncoder(new BCryptPasswordEncoder());		// definindo o encriptador da senha como sendo o default do spring
	}
}