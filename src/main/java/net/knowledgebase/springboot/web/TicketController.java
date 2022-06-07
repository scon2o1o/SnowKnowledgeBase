package net.knowledgebase.springboot.web;

import net.knowledgebase.springboot.model.Client;
import net.knowledgebase.springboot.model.Ticket;
import net.knowledgebase.springboot.repository.ClientRepository;
import net.knowledgebase.springboot.service.SettingsService;
import net.knowledgebase.springboot.service.SmtpService;
import net.knowledgebase.springboot.service.TicketService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.List;

@Controller
public class TicketController {
    private TicketService ticketService;
    private SettingsService settingsService;
    private SmtpService smtpService;
    private ClientRepository clientRepository;
    public final String SUPPORT_EMAIL = "shaneconcannon@gmail.com";

    public TicketController(TicketService ticketService, SettingsService settingsService, SmtpService smtpService, ClientRepository clientRepository) {
        super();
        this.ticketService = ticketService;
        this.settingsService = settingsService;
        this.smtpService = smtpService;
        this.clientRepository = clientRepository;
    }

    @GetMapping("/tickets/new")
    public String createTicketForm(Model model, Model settingsModel) {
        Ticket ticket = new Ticket();
        model.addAttribute("ticket", ticket);
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "create_ticket";
    }

    @PostMapping("/tickets")
    public String saveTicket(@ModelAttribute("ticket") Ticket ticket) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            Client client = clientRepository.findByEmail(currentPrincipalName);
            addTicketToDatabase(ticket, client);
            sendEmail(ticket, client);
            return "redirect:/tickets/new?success";
        } catch (Exception e) {
            return "redirect:/tickets/new?fail";
        }
    }

    private void addTicketToDatabase(Ticket ticket, Client client) {
        ticket.setContact(client.getFirstName() + " " + client.getLastName());
        ticket.setCompany(client.getCompany());
        ticket.setEmail(client.getEmail());
        ticket.setPhone(client.getPhone());
        ticket.setMobile(client.getMobile());
        ticket.setDateAdded(new Date());
        ticketService.saveTicket(ticket);
    }

    private void sendEmail(Ticket ticket, Client client) {
        String content = "Hi Support, a new support ticket has been created through the Client Portal"
                + "\n\nCreated by: " + client.getFirstName() + " " + client.getLastName()
                + "\nEmail: " + client.getEmail()
                + "\nCompany: " + client.getCompany()
                + "\nPhone: " + client.getPhone()
                + "\nMobile: " + client.getMobile()
                + "\n\n" + ticket.getContent();
        smtpService.sendEmail(SUPPORT_EMAIL, "New Support Ticket Created: " + ticket.getSubject(), content);
    }
}
