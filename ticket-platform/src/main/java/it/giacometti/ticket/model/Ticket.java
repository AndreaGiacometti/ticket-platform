package it.giacometti.ticket.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Il campo non può essere vuoto")
    private String titolo;

    @Column(nullable = false, length = 500)
    @NotBlank(message = "Il campo non può essere vuoto")
    private String descrizione;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "Il campo non può essere vuoto")
    private String stato = "da fare";

    private LocalDate dataCreazione;

    private LocalDate dataModifica;
    
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

    @ManyToOne
    @JsonBackReference
    @NotNull(message = "Il campo non può essere vuoto")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JsonBackReference
    @NotNull(message = "Il campo non può essere vuoto")
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @JsonManagedReference
    @OneToMany(mappedBy = "ticket")
    private List<Nota> note;

	// Getters and Setters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public List<Nota> getNote() {
		return note;
	}

	public void setNote(List<Nota> note) {
		this.note = note;
	}

}
