package it.giacometti.ticket.security;

import java.io.IOException;
import java.security.AuthProvider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfiguration {

	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http.authorizeHttpRequests()
	        .requestMatchers("/operatore/**").hasAnyAuthority("OPERATORE", "ADMIN")
	        .requestMatchers("/admin/**").hasAuthority("ADMIN")
	        .requestMatchers("/**").permitAll()
	        .and()
	        .formLogin()
	        .loginPage("/login")
	        .successHandler(new CustomAuthenticationSuccessHandler()) // Usa il custom success handler
	        .and()
	        .logout()
	        .logoutUrl("/logout")
	        .logoutSuccessUrl("/login?logout");

	    return http.build();
	}
	
	@Bean
	UserDetailsService userDetailsService() {
		return new DatabaseUserDetailsService();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}	
	
}