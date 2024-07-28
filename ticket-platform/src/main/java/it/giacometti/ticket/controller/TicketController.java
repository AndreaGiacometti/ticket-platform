package it.giacometti.ticket.controller;

import java.util.Arrays;
import java.util.List;


import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.giacometti.ticket.model.Categoria;
import it.giacometti.ticket.model.User;
import it.giacometti.ticket.model.Ticket;
import it.giacometti.ticket.repository.CategoriaRepository;
import it.giacometti.ticket.repository.UserRepository;
import it.giacometti.ticket.repository.TicketRepository;

@Controller
@RequestMapping("/ticket")
public class TicketController {

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private UserRepository operatoreRepository;

// VISUALIZZA
	
	@GetMapping("/{id}")
	public String show(@PathVariable int id, Model model) {
		Ticket ticket = ticketRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid ticket Id:" + id));
		model.addAttribute("ticket", ticket);
		return "ticket/viewTicket";
	}

// CREA
	
	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("ticket", new Ticket());
		
		// Aggiungi la lista degli operatori al modello
		List<User> operatori = operatoreRepository.findAll();
		model.addAttribute("operatori", operatori);

		// Aggiungi la lista delle categorie al modello
		List<Categoria> categorie = categoriaRepository.findAll();
		model.addAttribute("categorie", categorie);

		return "ticket/createTicket";
	}

	@PostMapping("/create")
	public String store(@ModelAttribute Ticket ticket, Model model) {
		
	    // Recupera l'operatore associato al ticket
	    User operatore = operatoreRepository.findById(ticket.getUser().getId())
	            .orElseThrow(() -> new IllegalArgumentException("Operatore non trovato"));

	    // Verifica se l'operatore è attivo
	    if (!operatore.getStatoPersonale().equalsIgnoreCase("attivo")) {
	        model.addAttribute("errorMessage", "L'operatore selezionato non è attivo e non può essere assegnato a questo ticket.");
	        return create(model); // Torna alla vista del form con il messaggio di errore
	    }

	    // Se l'operatore è attivo, salva il ticket
	    ticketRepository.save(ticket);
	    return "admin/dashboard";
	}

// MODIFICA
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable int id, Model model) {
		
		model.addAttribute("ticket", ticketRepository.findById(id).get());
		
		List<String> statuses = Arrays.asList("da fare", "in corso", "completato");
		model.addAttribute("statuses", statuses);
		
		List<Categoria> categorie = categoriaRepository.findAll();
		model.addAttribute("categorie", categorie);
		
		return "ticket/editTicket";
	}

	@PostMapping("/edit")
	public String store(@ModelAttribute Ticket ticket) {
	 	
		Ticket existingTicket = ticketRepository.findById(ticket.getId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid ticket Id:" + ticket.getId()));
		
		existingTicket.setStato(ticket.getStato());
		existingTicket.setCategoria(ticket.getCategoria());
		
		ticketRepository.save(existingTicket);
		
		return "/admin/dashboard";
	}

// ELIMINA
	
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable int id) {
		ticketRepository.deleteById(id);
		return "redirect:admin/dashboard";
	}

// SEARCHBAR
	
	@GetMapping("/search")
	public String searchTickets(@RequestParam("keyword") String keyword, Model model) {
		List<Ticket> tickets;
		if (keyword != null && !keyword.isEmpty()) {
			tickets = ticketRepository.findByTitoloContainingOrDescrizioneContaining(keyword, keyword);
		} else {
			tickets = ticketRepository.findAll();
		}
		model.addAttribute("tickets", tickets);
		return "admin/dashboard";
	}
}
