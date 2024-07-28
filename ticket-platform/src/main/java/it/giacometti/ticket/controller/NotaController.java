package it.giacometti.ticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import it.giacometti.ticket.model.Nota;
import it.giacometti.ticket.model.Ticket;
import it.giacometti.ticket.model.User;
import it.giacometti.ticket.repository.NotaRepository;
import it.giacometti.ticket.repository.TicketRepository;
import it.giacometti.ticket.repository.UserRepository;
import it.giacometti.ticket.security.DatabaseUserDetails;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/note")
public class NotaController {

    @Autowired
    private NotaRepository notaRepository;

    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private UserRepository userRepository;

    // Mostra la lista delle note per un ticket specifico
    @GetMapping("/ticket/{ticketId}")
    public String showNotesByTicket(@PathVariable int ticketId, Model model) {
        List<Nota> note = notaRepository.findByTicketId(ticketId);
        model.addAttribute("note", note);
        model.addAttribute("ticketId", ticketId);
        return "note/list"; // Nome della vista che mostra la lista delle note
    }

    // Mostra il form per creare una nuova nota
    @GetMapping("/create/{ticketId}")
    public String createNotaForm(@PathVariable int ticketId, Model model) {
        model.addAttribute("nota", new Nota());
        model.addAttribute("ticketId", ticketId);
      
        return "note/create"; // Nome della vista per creare una nuova nota
    }
    
    @PostMapping("/create")
    public String createNota(@RequestParam int ticketId, @ModelAttribute Nota nota) {
        // Recupera il ticket dal repository usando l'ID del ticket
        Ticket ticket = ticketRepository.findById(ticketId).orElse(null);
        if (ticket != null) {
            // Imposta il ticket per la nota
            nota.setTicket(ticket);
            // Imposta la data di creazione della nota alla data corrente
            nota.setDataCreazione(LocalDate.now());

            // Ottiene l'autenticazione dell'utente attuale dal contesto di sicurezza
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof UserDetails) {
                    // Se il principal è un'istanza di UserDetails, ottiene i dettagli dell'utente
                    DatabaseUserDetails userDetails = (DatabaseUserDetails) principal;
                    // Ottiene il Nome dell'utente autenticato
                    String autore = userDetails.getNome() + " " + userDetails.getCognome();
                    // Ottiene l'id dell'utente autenticato
                    int id_operatore = userDetails.getId();
                   
                    nota.setAutore(autore);
                 // Recupera l'oggetto User dal repository usando l'ID
                    User user = userRepository.findById(id_operatore).orElse(null);
                    if (user != null) {
                        nota.setUser(user);
                    }
                }
            }

            // Salva la nota nel repository
            notaRepository.save(nota);
        }
        // Reindirizza alla lista delle note del ticket specificato
        return "redirect:/note/ticket/" + ticketId;
    }
 // Mostra il form per modificare una nota esistente
    @GetMapping("/edit/{id}")
    public String editNotaForm(@PathVariable int id, Model model) {
        Nota nota = notaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid nota Id:" + id));
        model.addAttribute("nota", nota);
        return "note/edit"; // Nome della vista per modificare una nota
    }

    // Aggiorna una nota esistente
    @PostMapping("/edit")
    public String updateNota(@ModelAttribute Nota nota) {
        Nota existingNota = notaRepository.findById(nota.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid nota Id:" + nota.getId()));
        existingNota.setAutore(nota.getAutore()); // Aggiorna i campi modificabili
        existingNota.setTesto(nota.getTesto());
        // Non modificare dataCreazione
        notaRepository.save(existingNota);
        return "redirect:/note/ticket/" + existingNota.getTicket().getId(); // Ritorna alla lista delle note del ticket
    }
 // Elimina una nota specifica
    @PostMapping("/delete/{id}")
    public String deleteNota(@PathVariable int id) {
        Nota nota = notaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid nota Id:" + id));
        int ticketId = nota.getTicket().getId(); // Ottieni l'ID del ticket associato
        notaRepository.delete(nota);
        return "redirect:/note/ticket/" + ticketId; // Ritorna alla lista delle note del ticket
    }
}