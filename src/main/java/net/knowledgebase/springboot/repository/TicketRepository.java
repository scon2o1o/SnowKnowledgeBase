package net.knowledgebase.springboot.repository;

import net.knowledgebase.springboot.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
