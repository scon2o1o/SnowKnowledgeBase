package net.knowledgebase.springboot.service;

import net.knowledgebase.springboot.model.Ticket;
import net.knowledgebase.springboot.repository.TicketRepository;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl implements TicketService{

    private TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {
        super();
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket saveTicket (Ticket ticket){
        return ticketRepository.save(ticket);
    }
}
