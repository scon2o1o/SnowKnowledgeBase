package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
