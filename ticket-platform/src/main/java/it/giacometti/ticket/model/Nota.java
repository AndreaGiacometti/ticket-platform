package it.giacometti.ticket.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Nota {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotBlank(message = "Il campo autore non può essere vuoto")
	private String autore;

	private LocalDate dataCreazione;

	private LocalDate dataModifica;
	
	@NotBlank(message = "Il testo non può essere vuoto")
	@Size(max = 500, message = "Il testo non può superare i 500 caratteri")
	private String testo;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "ticket_id")
	private Ticket ticket;

	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name = "operatore_id")
	private User user;

	@PrePersist
	protected void onCreate() {
		if (dataCreazione == null) {
			dataCreazione = LocalDate.now();
		}
	}

	@PreUpdate
	protected void onUpdate() {
		dataModifica = LocalDate.now();
	}

	// Getters e Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAutore() {
		return autore;
	}

	public void setAutore(String autore) {
		this.autore = autore;
	}

    public LocalDate getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDate dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public LocalDate getDataModifica() {
        return dataModifica;
    }

    public void setDataModifica(LocalDate dataModifica) {
        this.dataModifica = dataModifica;
    }

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}