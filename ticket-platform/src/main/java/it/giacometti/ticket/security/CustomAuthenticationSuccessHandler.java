package it.giacometti.ticket.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Ottieni il primo ruolo dell'utente autenticato
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        // Verifica il ruolo e reindirizza di conseguenza
        if ("ADMIN".equals(role)) {
            response.sendRedirect("/admin/dashboard");
        } else if ("OPERATORE".equals(role)) {
            response.sendRedirect("/operatore/dashboard");
        } else {
            response.sendRedirect("/"); // Redirect di default per altri ruoli
        }
    }
}

