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
		  
		    // Recupera l'utente autenticato
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			DatabaseUserDetails userDetails = (DatabaseUserDetails) authentication.getPrincipal();
			
			model.addAttribute("nome", userDetails.getNome());
	        model.addAttribute("cognome", userDetails.getCognome());
	        model.addAttribute("email", userDetails.getEmail());
	        model.addAttribute("statoPersonale", userDetails.getStatoPersonale());
			
			// Ottieni l'email dell'utente autenticato
	        String email = authentication.getName();

	        // Trova l'utente tramite l'email
	        User user = userRepository.findByEmail(email)
	                                  .orElseThrow(() -> new RuntimeException("User not found"));

	        // Recupera i ticket associati all'utente
	        List<Ticket> tickets = ticketRepository.findByUserId(user.getId());

	        // Aggiungi la lista al modello
	        model.addAttribute("authorities", authentication.getAuthorities());
	        model.addAttribute("tickets", tickets);

	        return "operatore/dashboard"; 
	  }
	  
	  
	    @PostMapping("/updateStatus")
	    public String updateStatus(@RequestParam("statoPersonale") String statoPersonale, Model model) {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String email = authentication.getName();

	        User user = userRepository.findByEmail(email)
	                                  .orElseThrow(() -> new RuntimeException("User not found"));

	        user.setStatoPersonale(statoPersonale);
	        userRepository.save(user);

	        // Dopo l'aggiornamento, ricarica la dashboard con i dati aggiornati
	        model.addAttribute("nome", user.getNome());
	        model.addAttribute("cognome", user.getCognome());
	        model.addAttribute("email", user.getEmail());
	        model.addAttribute("statoPersonale", user.getStatoPersonale());

	        List<Ticket> tickets = ticketRepository.findByUserId(user.getId());
	        model.addAttribute("tickets", tickets);

	        return "operatore/dashboard";
	    }

}