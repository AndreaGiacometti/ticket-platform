package it.giacometti.ticket.controller;

import it.giacometti.ticket.model.Ticket;
import it.giacometti.ticket.model.User;
import it.giacometti.ticket.repository.TicketRepository;
import it.giacometti.ticket.repository.UserRepository;
import it.giacometti.ticket.security.DatabaseUserDetails;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/operatore")
public class OperatoreController {

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/dashboard")
	public String showDashboard(Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		model.addAttribute("user", user);

		// Recupera i ticket associati all'utente
		List<Ticket> tickets = ticketRepository.findByUserId(user.getId());

		// Aggiungi la lista al modello
		model.addAttribute("authorities", authentication.getAuthorities());
		model.addAttribute("tickets", tickets);

		return "operatore/dashboard";
	}

	@PostMapping("/updateStatus")
	public String updateStatus(@RequestParam("nome") String nome, @RequestParam("cognome") String cognome,
			@RequestParam("email") String email, @RequestParam("statoPersonale") String statoPersonale, Model model) {
		// Ottieni le informazioni sull'utente autenticato
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentEmail = authentication.getName();

		// Trova l'utente nel database usando l'email attuale
		User user = userRepository.findByEmail(currentEmail).orElseThrow(() -> new RuntimeException("User not found"));

		// Aggiorna i dettagli dell'utente con i nuovi valori
		user.setNome(nome);
		user.setCognome(cognome);
		user.setEmail(email);
		user.setStatoPersonale(statoPersonale);

		// Salva le modifiche nel database
		userRepository.save(user);

		// Aggiorna il modello con i nuovi dettagli dell'utente
		model.addAttribute("nome", user.getNome());
		model.addAttribute("cognome", user.getCognome());
		model.addAttribute("email", user.getEmail());
		model.addAttribute("statoPersonale", user.getStatoPersonale());

		// Recupera e aggiunge i ticket dell'utente al modello
		List<Ticket> tickets = ticketRepository.findByUserId(user.getId());
		model.addAttribute("tickets", tickets);

		// Ritorna la vista della dashboard dell'operatore
		return "redirect:/login";
	}

	@GetMapping("/edit")
	public String editUserForm(Model model) {
		// Ottieni l'autenticazione corrente
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = authentication.getName();

		// Trova l'utente nel repository basato sul nome utente (o email, o altro
		// identificatore univoco)
		User user = userRepository.findByEmail(currentUsername)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user: " + currentUsername));

		// Aggiungi l'utente al modello per il rendering della vista
		model.addAttribute("user", user);
		return "operatore/editOperatore";
	}

	// Aggiorna l'utente
	@PostMapping("/edit")
	public String updateUser(@ModelAttribute User user) {
		User existingUser = userRepository.findById(user.getId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + user.getId()));

		existingUser.setNome(user.getNome());
		existingUser.setCognome(user.getCognome());
		existingUser.setEmail(user.getEmail() + "@ticketfacile.it");
		existingUser.setStatoPersonale(user.getStatoPersonale());

		userRepository.save(existingUser);
		return "redirect:/operatore/dashboard"; // Supponendo che ci sia una lista degli utenti
	}

}