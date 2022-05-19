package net.knowledgebase.springboot.web;

import net.bytebuddy.utility.RandomString;
import net.knowledgebase.springboot.exception.InternalServerErrorException;
import net.knowledgebase.springboot.exception.ResourceNotFoundException;
import net.knowledgebase.springboot.model.Audit;
import net.knowledgebase.springboot.model.Client;
import net.knowledgebase.springboot.model.User;
import net.knowledgebase.springboot.repository.ClientRepository;
import net.knowledgebase.springboot.repository.UserRepository;
import net.knowledgebase.springboot.service.AuditService;
import net.knowledgebase.springboot.service.UserService;
import net.knowledgebase.springboot.web.dto.UserRegistrationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientRestController {

    private ClientRepository clientRepository;
    private UserService userService;
    private UserRepository userRepository;
    private AuditService auditService;

    public ClientRestController(ClientRepository clientRepository, UserService userService,
                                UserRepository userRepository, AuditService auditService) {
        this.clientRepository = clientRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.auditService = auditService;
    }

    @PostMapping
    public Client createClient(@RequestBody Client client, HttpServletRequest request) {
        if (client.getCompany().equals("") || client.getCompany().equals(" ")) {
            throw new InternalServerErrorException("No company assigned to this client");
        }
        if (client.isAccount()) {
            User user = userRepository.findByEmail(client.getEmail());
            if (user == null) {
                UserRegistrationDto registrationDto = new UserRegistrationDto(client.getFirstName(),
                        client.getLastName(), client.getEmail(), null, "Client", RandomString.make(30));
                userService.save(registrationDto);
            }
        }
        Audit audit = new Audit("Client '" + client.getFirstName() + " " + client.getLastName() + "' added", "Action via API");
        auditService.saveAudit(audit);
        return clientRepository.save(client);
    }

    @PostMapping("/multiple")
    public String createMultipleClients(@RequestBody List<Client> clients, HttpServletRequest request) {
        for (Client client : clients) {
            clientRepository.save(client);
            Audit audit = new Audit("Client '" + client.getFirstName() + " " + client.getLastName() + "' added", "Action via API");
            if (client.isAccount()) {
                User user = userRepository.findByEmail(client.getEmail());
                if (user == null) {
                    UserRegistrationDto registrationDto = new UserRegistrationDto(client.getFirstName(),
                            client.getLastName(), client.getEmail(), null, "Client", RandomString.make(30));
                    userService.save(registrationDto);
                }
            }
        }
        return "Success";
    }

    @PutMapping("{id}")
    public ResponseEntity<Client> updateClient(@PathVariable String id, @RequestBody Client clientDetails, HttpServletRequest request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No client found with id: " + id));
        client.setLastName(clientDetails.getLastName());
        if (!clientDetails.getLastName().equals(client.getLastName())) {
            Audit audit = new Audit("Client " + client.getId() + " updated. Name updated from '" + client.getLastName() + "' to '" + clientDetails.getLastName() + "'");
            auditService.saveAudit(audit);
        }
        client.setFirstName(clientDetails.getFirstName());
        if (!clientDetails.getFirstName().equals(client.getFirstName())) {
            Audit audit = new Audit("Client " + client.getId() + " updated. Name updated from '" + client.getFirstName() + "' to '" + clientDetails.getFirstName() + "'");
            auditService.saveAudit(audit);
        }
        client.setAccount(clientDetails.isAccount());
        if (clientDetails.isAccount() != client.isAccount()) {
            Audit audit = new Audit("Client " + client.getId() + " updated. Name updated from '" + client.isAccount() + "' to '" + clientDetails.isAccount() + "'");
            auditService.saveAudit(audit);
        }
        client.setEmail(clientDetails.getEmail());
        if (!clientDetails.getEmail().equals(client.getEmail())) {
            Audit audit = new Audit("Client " + client.getId() + " updated. Name updated from '" + client.getEmail() + "' to '" + clientDetails.getEmail() + "'");
            auditService.saveAudit(audit);
        }
        client.setCompany(clientDetails.getCompany());
        if (!clientDetails.getCompany().equals(client.getCompany())) {
            Audit audit = new Audit("Client " + client.getId() + " updated. Name updated from '" + client.getCompany() + "' to '" + clientDetails.getCompany() + "'");
            auditService.saveAudit(audit);
        }
        client.setMobile(clientDetails.getMobile());
        if (!clientDetails.getMobile().equals(client.getMobile())) {
            Audit audit = new Audit("Client " + client.getId() + " updated. Name updated from '" + client.getMobile() + "' to '" + clientDetails.getMobile() + "'");
            auditService.saveAudit(audit);
        }
        client.setPhone(clientDetails.getPhone());
        if (!clientDetails.getPhone().equals(client.getPhone())) {
            Audit audit = new Audit("Client " + client.getId() + " updated. Name updated from '" + client.getPhone() + "' to '" + clientDetails.getPhone() + "'");
            auditService.saveAudit(audit);
        }
        clientRepository.save(client);
        if (client.isAccount()) {
            User user = userRepository.findByEmail(client.getEmail());
            if (user == null) {
                UserRegistrationDto registrationDto = new UserRegistrationDto(client.getFirstName(),
                        client.getLastName(), client.getEmail(), null, "Client", RandomString.make(30));
                userService.save(registrationDto);
            }
        }
        return ResponseEntity.ok(client);
    }
}
