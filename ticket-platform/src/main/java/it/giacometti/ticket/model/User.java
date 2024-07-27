package it.giacometti.ticket.model;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Email
	private String email;

	@NotBlank
	@Size(min = 3)
	private String password;

	@NotBlank
	@Size(min = 3)
	private String nome;

	@NotBlank
	@Size(min = 2)
	private String cognome;

	@NotNull
	private String statoPersonale;

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Role> roles;

	// Getters and Setters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getStatoPersonale() {
		return statoPersonale;
	}

	public void setStatoPersonale(String statoPersonale) {
		this.statoPersonale = statoPersonale;
	}

	public Set<Role> getRoles() {
		return roles;

	}
}
