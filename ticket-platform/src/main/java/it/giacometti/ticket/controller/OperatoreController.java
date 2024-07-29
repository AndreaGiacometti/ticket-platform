package it.giacometti.ticket.controller;

import it.giacometti.ticket.model.Categoria;
import it.giacometti.ticket.model.Ticket;
import it.giacometti.ticket.model.User;
import it.giacometti.ticket.repository.TicketRepository;
import it.giacometti.ticket.repository.UserRepository;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zaxxer.hikari.metrics.micrometer.MicrometerMetricsTracker;

@Controller
@RequestMapping("/operatore")
public class OperatoreController {

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private UserRepository userRepository;

//VISUALIZZA

	@GetMapping("/dashboard")
	public String showDashboard(Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Utente non trovato"));

		model.addAttribute("user", user);

		List<Ticket> tickets = ticketRepository.findByUserId(user.getId());

		model.addAttribute("authorities", authentication.getAuthorities());
		model.addAttribute("tickets", tickets);

		return "operatore/dashboard";
	}

//MODIFICA

	@GetMapping("/edit")
	public String editUserForm(Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = authentication.getName();

		User user = userRepository.findByEmail(currentUsername)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user: " + currentUsername));

		model.addAttribute("user", user);
		return "operatore/editOperatore";
	}

	
    @PostMapping("/updateStatus")
    public String store(@Valid @ModelAttribute("user") User editUser, BindingResult bindingResult, Model model) {
      
        List<Ticket> userTickets = ticketRepository.findByUserId(editUser.getId());

        boolean allTicketsCompleted = true; 
        
        for (Ticket ticket : userTickets) {
            if (!"completato".equals(ticket.getStato())) {
                allTicketsCompleted = false;
                break; // Esci dal ciclo se trovi un ticket non completato
            }
        }


        if (!allTicketsCompleted && "non attivo".equals(editUser.getStatoPersonale())) {
            bindingResult.rejectValue("statoPersonale", "error.user", "Non puoi cambiare lo stato dell'utente in 'non attivo' perché non tutti i ticket sono completati.");
        }

        if (bindingResult.hasErrors()) {
            return "operatore/editOperatore";
        }

        userRepository.save(editUser);
        return "redirect:/operatore/dashboard";
    }
}
	  

	 

		/*
		 * @PostMapping("/updateStatus") public String
		 * updateStatus(@RequestParam("nome") String nome, @RequestParam("cognome")
		 * String cognome,
		 * 
		 * @RequestParam("email") String email, @RequestParam("statoPersonale") String
		 * statoPersonale, Model model) {
		 * 
		 * // Ottieni le informazioni sull'utente autenticato Authentication
		 * authentication = SecurityContextHolder.getContext().getAuthentication();
		 * String currentEmail = authentication.getName();
		 * 
		 * // Trova l'utente nel database usando l'email attuale User user =
		 * userRepository.findByEmail(currentEmail).orElseThrow(() -> new
		 * RuntimeException("User not found"));
		 * 
		 * // Aggiorna i dettagli dell'utente con i nuovi valori user.setNome(nome);
		 * user.setCognome(cognome); user.setEmail(email);
		 * user.setStatoPersonale(statoPersonale);
		 * 
		 * // Salva le modifiche nel database userRepository.save(user);
		 * 
		 * // Aggiorna il modello con i nuovi dettagli dell'utente
		 * model.addAttribute("nome", user.getNome()); model.addAttribute("cognome",
		 * user.getCognome()); model.addAttribute("email", user.getEmail());
		 * model.addAttribute("statoPersonale", user.getStatoPersonale());
		 * 
		 * // Recupera e aggiunge i ticket dell'utente al modello List<Ticket> tickets =
		 * ticketRepository.findByUserId(user.getId()); model.addAttribute("tickets",
		 * tickets);
		 * 
		 * // Ritorna la vista della dashboard dell'operatore
		 * 
		 * return "redirect:/login"; }
		 */

