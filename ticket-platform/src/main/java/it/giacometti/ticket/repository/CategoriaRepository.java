package it.giacometti.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import it.giacometti.ticket.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer>{
}