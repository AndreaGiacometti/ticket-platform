package it.giacometti.ticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

		model.addAttribute("tickets", ticketRepository.findAll());
		return "admin/dashboard";
	}
}
