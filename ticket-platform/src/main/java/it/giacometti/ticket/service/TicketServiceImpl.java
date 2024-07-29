package it.giacometti.ticket.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.giacometti.ticket.model.Ticket;
import it.giacometti.ticket.repository.TicketRepository;

@Service
public class TicketServiceImpl implements TicketService {
	
	@Autowired
	TicketRepository ticketRepository;

	@Override
	public List<Ticket> getAllTickets() {
		return ticketRepository.findAll();
	}

	@Override
	public List<Ticket> getTicketsByCategoriaId(int categoriaId) {
		return ticketRepository.findByCategoriaId(categoriaId);
	}

	@Override
	public List<Ticket> getTicketsByStato(String stato) {
		return ticketRepository.findByStato(stato);
	}
	
	


}
