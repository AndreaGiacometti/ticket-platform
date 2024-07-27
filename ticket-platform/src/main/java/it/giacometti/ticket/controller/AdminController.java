package it.giacometti.ticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.giacometti.ticket.repository.TicketRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private TicketRepository ticketRepository;

	@GetMapping("/dashboard")
	public String showDashboard(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// Ottieni le autorità dell'utente
		model.addAttribute("authorities", authentication.getAuthorities());
		// Aggiungi tutti i ticket al modello
		model.addAttribute("tickets", ticketRepository.findAll());
		return "admin/dashboard"; // Nome della tua vista Thymeleaf
	}
}
