package it.giacometti.ticket.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import it.giacometti.ticket.model.User;
import it.giacometti.ticket.model.Role;

public class DatabaseUserDetails implements UserDetails {

	private final int id;
	private final String nome;
    private final String cognome;
    private final String email;
	private final String username;
	private final String password;
	private final String statoPersonale;
	
	private final Set<GrantedAuthority> authorities;
	
	public DatabaseUserDetails (User user) {
		this.id = user.getId();
		this.nome = user.getNome();
        this.cognome = user.getCognome();
        this.email = user.getEmail();
		this.username = user.getEmail();
		this.password = user.getPassword();
		this.statoPersonale = user.getStatoPersonale();
		this.authorities = new HashSet<>();
		for(Role ruolo : user.getRoles()){
			this.authorities.add(new SimpleGrantedAuthority(ruolo.getName()));
		}
		
	}
	
	  @Bean
	  CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
	        return new CustomAuthenticationSuccessHandler();
	    }
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	public int getId() {
		return this.id;
	}
	
	@Override
	public String getPassword() {
		return this.password;
	}

	@Override 
	public String getUsername() { 
		return this.username;
	}


	public String getNome() {
		return nome;
	}
	
	public String getCognome() {
		return cognome;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getStatoPersonale() {
		return statoPersonale;
	}

}
