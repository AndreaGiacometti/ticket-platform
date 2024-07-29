package it.giacometti.ticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import it.giacometti.ticket.model.Nota;
import it.giacometti.ticket.repository.NotaRepository;
import jakarta.validation.Valid;

import java.util.List;

@Controller
@RequestMapping
public class NotaController {

	@Autowired
	private NotaRepository notaRepository;

// VISUALIZZA

	@GetMapping("/note/ticket/{ticketId}")
	public String show(@PathVariable int ticketId, Model model) {

		List<Nota> note = notaRepository.findByTicketId(ticketId);

		model.addAttribute("note", note);
		model.addAttribute("ticketId", ticketId);

		return "note/list";
	}

// CREA

	@GetMapping("/note/create/{ticketId}")
	public String create(@PathVariable int ticketId, Model model) {

		model.addAttribute("nota", new Nota());

		return "note/create";
	}

	@PostMapping("/note/create")
	public String store(@Valid @ModelAttribute("nota") Nota createNota, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			return "note/create";
		}

		notaRepository.save(createNota);

		int ticketId = createNota.getTicket().getId();
		return "redirect:/note/ticket/" + ticketId;
	}

// MODIFICA

	@GetMapping("/note/edit/{id}")
	public String editNotaForm(@PathVariable int id, Model model) {
		model.addAttribute("nota", notaRepository.findById(id).get());
		return "note/edit";
	}

	@PostMapping("/note/edit")
	public String editNota(@Valid @ModelAttribute("nota") Nota editNota, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			
			return "note/edit";
		}

		notaRepository.save(editNota);

		int ticketId = editNota.getTicket().getId();
		return "redirect:/note/ticket/" + ticketId;
	}

// ELIMINA

	@PostMapping("/note/delete/{id}")
	public String deleteNota(@Valid @PathVariable int id) {

		Nota nota = notaRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("ID" + id + "non trovato"));
		
		notaRepository.deleteById(id);
		
		int ticketId = nota.getTicket().getId();
		
		return "redirect:/note/ticket/" + ticketId;
	}
}