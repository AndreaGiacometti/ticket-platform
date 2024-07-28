package it.giacometti.ticket.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.giacometti.ticket.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Integer>{
	  List<Ticket> findByUserId(int userId);
	  List<Ticket> findByTitoloContainingOrDescrizioneContaining(String titolo, String descrizione);
}