package it.giacometti.ticket.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.giacometti.ticket.model.Ticket;
import it.giacometti.ticket.service.TicketService;

@RestController
@RequestMapping("/api")
public class TicketApi {

	@Autowired
	private TicketService ticketService;

	@GetMapping("/tickets")
	public ResponseEntity<List<Ticket>> getAllTickets() {

		List<Ticket> tickets = ticketService.getAllTickets();

		if (tickets.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(tickets);
	}

	@GetMapping("/categoria/{categoriaId}")
	public ResponseEntity<List<Ticket>> get(@PathVariable("categoriaId") int categoriaId) {

		List<Ticket> tickets = ticketService.getTicketsByCategoriaId(categoriaId);

		if (!tickets.isEmpty()) {
			return new ResponseEntity<List<Ticket>>(tickets, HttpStatus.OK);
		}
		return new ResponseEntity<List<Ticket>>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/stato/{stato}")
	public ResponseEntity<List<Ticket>> get(@PathVariable("stato") String stato) {

		List<Ticket> tickets = ticketService.getTicketsByStato(stato);

		if (!tickets.isEmpty()) {
			return new ResponseEntity<List<Ticket>>(tickets, HttpStatus.OK);
		}
		return new ResponseEntity<List<Ticket>>(HttpStatus.NOT_FOUND);
	}
}