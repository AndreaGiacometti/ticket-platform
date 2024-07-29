package it.giacometti.ticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import it.giacometti.ticket.model.Categoria;
import it.giacometti.ticket.model.Nota;
import it.giacometti.ticket.model.Ticket;
import it.giacometti.ticket.model.User;
import it.giacometti.ticket.repository.NotaRepository;
import jakarta.validation.Valid;

import java.util.List;

@Controller
@RequestMapping("/note")
public class NotaController {

	@Autowired
	private NotaRepository notaRepository;

// VISUALIZZA
	
	@GetMapping("/ticket/{ticketId}")
	public String show(@PathVariable int ticketId, Model model) {
		List<Nota> note = notaRepository.findByTicketId(ticketId);
		model.addAttribute("note", note);
		model.addAttribute("ticketId", ticketId);
		return "note/list"; // Nome della vista che mostra la lista delle note
	}

// CREA

	@GetMapping("/create/{ticketId}") public String create(@PathVariable int
	 ticketId, Model model) { model.addAttribute("nota", new Nota());
	 return "note/create";
	 }
	 	
	@PostMapping("/create")
	public String store(@Valid @ModelAttribute("nota") Nota createNota, BindingResult bindingResult, Model model) {
		
		if (bindingResult.hasErrors()) {
			return "note/create";
		}
		
		notaRepository.save(createNota);

		int ticketId = createNota.getTicket().getId();
		return "redirect:/note/ticket/" + ticketId;
	}

// MODIFICA

	@GetMapping("/edit/{id}")
	public String editNotaForm(@PathVariable int id, Model model) {
		model.addAttribute("nota", notaRepository.findById(id).get());
		return "note/edit";
	}

	/*
	 * @PostMapping("/edit") public String updateNota(@ModelAttribute Nota nota) {
	 * 
	 * Nota existingNota = notaRepository.findById(nota.getId()) .orElseThrow(() ->
	 * new IllegalArgumentException("Invalid nota Id:" + nota.getId()));
	 * 
	 * existingNota.setAutore(nota.getAutore());
	 * existingNota.setTesto(nota.getTesto());
	 * 
	 * notaRepository.save(existingNota);
	 * 
	 * return "redirect:/note/ticket/" + existingNota.getTicket().getId(); //
	 * Ritorna alla lista delle note del ticket }
	 */
	
	@PostMapping("/edit")
	public String editNota(@ModelAttribute("nota") Nota editNota, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {

			return "ticket/editTicket";
		}

		notaRepository.save(editNota);
		
		int ticketId = editNota.getTicket().getId();
		return "redirect:/note/ticket/" + ticketId;
	}
// ELIMINA

	@PostMapping("/delete/{id}")
	public String deleteNota(@PathVariable int id) {
		Nota nota = notaRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid nota Id:" + id));
		int ticketId = nota.getTicket().getId(); // Ottieni l'ID del ticket associato
		notaRepository.delete(nota);
		return "redirect:/note/ticket/" + ticketId; // Ritorna alla lista delle note del ticket
	}
}