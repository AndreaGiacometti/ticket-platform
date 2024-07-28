package it.giacometti.ticket.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
import jakarta.validation.Valid;
import it.giacometti.ticket.repository.TicketRepository;

@Controller
@RequestMapping
public class TicketController {

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private UserRepository userRepository;

// VISUALIZZA

	@GetMapping("/ticket/{id}")
	public String show(@PathVariable int id, Model model) {
		Ticket ticket = ticketRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid ticket Id:" + id));
		model.addAttribute("ticket", ticket);
		return "ticket/viewTicket";
	}

// CREA

	@GetMapping("/ticket/create")
	public String create(Model model) {
		model.addAttribute("ticket", new Ticket());

		// Aggiungi la lista degli operatori al modello
		List<User> operatori = userRepository.findAll();
		model.addAttribute("operatori", operatori);

		// Aggiungi la lista delle categorie al modello
		List<Categoria> categorie = categoriaRepository.findAll();
		model.addAttribute("categorie", categorie);

		return "ticket/createTicket";
	}

	@PostMapping("/ticket/create")
	public String store(@Valid @ModelAttribute("ticket") Ticket formTicket, 
	                    BindingResult bindingResult, 
	                    Model model) {
	    // Verifica se ci sono errori di validazione del ticket
	    if (bindingResult.hasErrors()) {
	        return "ticket/create"; // Ritorna alla vista del form con gli errori di validazione
	    }
	    
	    User user = userRepository.findById(formTicket.getUser().getId())
	            .orElse(null);

	    if (user == null || !"attivo".equalsIgnoreCase(user.getStatoPersonale())) {
	        model.addAttribute("errorMessage", "L'operatore selezionato non è attivo.");
	        System.out.println("Errore: L'operatore selezionato non è attivo.");
	        return "ticket/create"; // Ritorna alla vista del form con il messaggio di errore
	    }

	    // Se tutto è valido, salva il ticket
	    ticketRepository.save(formTicket);

	    // Redirect alla dashboard o altra pagina
	    return "redirect:/admin/dashboard";
	}

				
// MODIFICA

	@GetMapping("/ticket/edit/{id}")
	public String edit(@PathVariable int id, Model model) {

		model.addAttribute("ticket", ticketRepository.findById(id).get());

		List<String> statuses = Arrays.asList("da fare", "in corso", "completato");
		model.addAttribute("statuses", statuses);

		List<Categoria> categorie = categoriaRepository.findAll();
		model.addAttribute("categorie", categorie);

		return "ticket/editTicket";
	}

	@PostMapping("/ticket/edit")
	public String store(@ModelAttribute Ticket ticket) {

		Ticket existingTicket = ticketRepository.findById(ticket.getId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid ticket Id:" + ticket.getId()));

		existingTicket.setStato(ticket.getStato());
		existingTicket.setCategoria(ticket.getCategoria());

		ticketRepository.save(existingTicket);
		
		//Redirect alla dashboard in base al ruolo dell'utente loggato
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		boolean isAdmin = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ADMIN"));
		boolean isOperatore = authentication.getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals("OPERATORE"));

		if (isAdmin) {
			return "redirect:/admin/dashboard";
		} else if (isOperatore) {
			return "redirect:/operatore/dashboard";
		}

		return "redirect:/login";
	}

// ELIMINA

	@PostMapping("/ticket/delete/{id}")
	public String delete(@PathVariable int id) {
		ticketRepository.deleteById(id);
		return "redirect:/admin/dashboard";
	}

// SEARCHBAR

	@GetMapping("/ticket/search")
	public String searchTickets(@RequestParam("keyword") String keyword, Model model) {
	    List<Ticket> tickets;
	    if (keyword != null && !keyword.isEmpty()) {
	        tickets = ticketRepository.findByTitoloContainingOrDescrizioneContaining(keyword, keyword);
	    } else {
	        tickets = ticketRepository.findAll();
	    }
	    model.addAttribute("tickets", tickets);
	    return "ticket/searchResults"; // Nome del template per visualizzare i risultati
	}
}