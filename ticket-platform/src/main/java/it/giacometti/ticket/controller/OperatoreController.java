package it.giacometti.ticket.controller;

import it.giacometti.ticket.model.Role;
import it.giacometti.ticket.model.Ticket;
import it.giacometti.ticket.model.User;
import it.giacometti.ticket.repository.RoleRepository;
import it.giacometti.ticket.repository.TicketRepository;
import it.giacometti.ticket.repository.UserRepository;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Controller
@RequestMapping("/operatore")
public class OperatoreController {

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;

//VISUALIZZA

	@GetMapping("/dashboard")
	public String showDashboard(Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Utente non trovato"));

		model.addAttribute("user", user);

		List<Ticket> tickets = ticketRepository.findByUserId(user.getId());

		model.addAttribute("authorities", authentication.getAuthorities());
		model.addAttribute("tickets", tickets);

		return "operatore/dashboard";
	}

// CREA
	
	@GetMapping("/create")
    public String create(Model model) {
		
		List<Role> roles = roleRepository.findAll();
		
        model.addAttribute("user", new User());
        model.addAttribute("roles", roles);
        
        return "admin/createUser";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
    	
        if (bindingResult.hasErrors()) {
        	
            List<Role> roles = roleRepository.findAll();
            model.addAttribute("roles", roles);
            
            return "admin/createUser";
        }

        int operatoreRoleId = 2;
        Role role = roleRepository.findById(operatoreRoleId)
                                  .orElseThrow(() -> new RuntimeException("Ruolo 'Operatore' non trovato"));

        user.setRoles(Set.of(role));  
        
		/*
		 * String emailWithDomain = user.getEmail() + "@ticketfacile.it";
		 * user.setEmail(emailWithDomain);
		 * 
		 * String password = (user.getCognome() + "." + user.getNome()).toLowerCase();
		 * user.setPassword(password);
		 */
        
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
      
        String passwordWithPrefix = "{bcrypt}" + encryptedPassword;

        user.setPassword(passwordWithPrefix);

        userRepository.save(user);
        return "redirect:/admin/dashboard";
    }

//MODIFICA

	@GetMapping("/edit")
	public String edit(Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = authentication.getName();

		User user = userRepository.findByEmail(currentUsername)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user: " + currentUsername));

		model.addAttribute("user", user);
		return "operatore/editOperatore";
	}

	
    @PostMapping("/updateStatus")
    public String editUser(@Valid @ModelAttribute("user") User editUser, BindingResult bindingResult, Model model) {
    	
    	int operatoreRoleId = 2;
        Role role = roleRepository.findById(operatoreRoleId)
                                  .orElseThrow(() -> new RuntimeException("Ruolo 'Operatore' non trovato"));

        editUser.setRoles(Set.of(role));  
      
        List<Ticket> userTickets = ticketRepository.findByUserId(editUser.getId());

        boolean allTicketsCompleted = true; 
        
        for (Ticket ticket : userTickets) {
            if (!"completato".equals(ticket.getStato())) {
                allTicketsCompleted = false;
                break;
            }
        }


        if (!allTicketsCompleted && "non attivo".equals(editUser.getStatoPersonale())) {
            bindingResult.rejectValue("statoPersonale", "error.user", "Non puoi cambiare lo stato dell'utente in 'non attivo' perché non tutti i ticket sono completati.");
        }

        if (bindingResult.hasErrors()) {
            return "operatore/editOperatore";
        }

        userRepository.save(editUser);
        return "redirect:/operatore/dashboard";
    }
}
	  

