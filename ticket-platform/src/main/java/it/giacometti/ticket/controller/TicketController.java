package it.giacometti.ticket.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

	// Dettaglio del Ticket
	@GetMapping("/{id}")
	public String viewTicket(@PathVariable int id, Model model) {
		Ticket ticket = ticketRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid ticket Id:" + id));
		model.addAttribute("ticket", ticket);
		return "ticket/viewTicket";
	}

	// Creazione di un nuovo Ticket
	@GetMapping("/create")
	public String createTicketForm(Model model) {
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
	public String createTicket(@ModelAttribute Ticket ticket) {
		ticketRepository.save(ticket);
		return "redirect:/admin/dashboard";
	}

	// Modifica del Ticket
	@GetMapping("/edit/{id}")
	public String editTicketForm(@PathVariable int id, Model model) {
		Ticket ticket = ticketRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid ticket Id:" + id));
		model.addAttribute("ticket", ticket);
		List<String> statuses = Arrays.asList("da fare", "in corso", "completato");
		model.addAttribute("statuses", statuses);
		List<Categoria> categories = categoriaRepository.findAll();
		model.addAttribute("categories", categories);
		return "ticket/editTicket";
	}

	@PostMapping("/edit")
	public String updateTicket(@ModelAttribute Ticket ticket) {
		ticketRepository.save(ticket);
		return "redirect:/admin/dashboard";
	}

	// Eliminazione del Ticket
	@PostMapping("/delete/{id}")
	public String deleteTicket(@PathVariable int id) {
		ticketRepository.deleteById(id);
		return "redirect:/admin/dashboard";
	}
}
