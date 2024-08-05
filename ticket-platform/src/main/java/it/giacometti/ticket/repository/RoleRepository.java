package it.giacometti.ticket.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import it.giacometti.ticket.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}

