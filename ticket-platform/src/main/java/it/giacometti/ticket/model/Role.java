package it.giacometti.ticket.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Role {

	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	@Id
	private int id;
		
	@NotBlank
    @Size (min = 3)
	private String nome;
	
	
	
	public String getName() {
		return nome;
	}
		
	public void setName(String nome) {
			this.nome = nome;
		}
		
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
			this.id = id;
		

	}
}
