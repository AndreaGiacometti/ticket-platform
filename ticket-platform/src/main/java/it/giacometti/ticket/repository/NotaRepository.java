package it.giacometti.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import it.giacometti.ticket.model.Nota;

public interface NotaRepository extends JpaRepository<Nota, Integer>{
	List<Nota> findByTicketId(int ticketId);
}
