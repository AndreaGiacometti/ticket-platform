package it.giacometti.ticket.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.giacometti.ticket.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findByEmail(String email);
}