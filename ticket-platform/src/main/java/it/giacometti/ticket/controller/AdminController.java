package it.giacometti.ticket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.giacometti.ticket.model.Ticket;
import it.giacometti.ticket.repository.TicketRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private TicketRepository ticketRepository;

	@GetMapping("/dashboard")
	public String searchTickets(@RequestParam(value="keyword", required = false) String keyword, Model model) {
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
