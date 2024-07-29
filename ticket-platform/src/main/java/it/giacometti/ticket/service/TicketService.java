package it.giacometti.ticket.service;

import java.util.List;

import it.giacometti.ticket.model.Ticket;

public interface TicketService {
	
	List<Ticket> getTicketsByCategoriaId(int categoriaId);
	
	List<Ticket> findTicketsByStato(String stato);
	
	public List<Ticket> getAllTickets();
	
    }
 