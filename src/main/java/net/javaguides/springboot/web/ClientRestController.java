package net.javaguides.springboot.web;

import net.javaguides.springboot.exception.InternalServerErrorException;
import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Audit;
import net.javaguides.springboot.model.Client;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.repository.ClientRepository;
import net.javaguides.springboot.repository.UserRepository;
import net.javaguides.springboot.service.AuditService;
import net.javaguides.springboot.service.UserService;
import net.javaguides.springboot.web.dto.UserRegistrationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public Client createClient(@RequestBody Client client) {
        if (client.getCompany().equals("") || client.getCompany().equals(" ")) {
            throw new InternalServerErrorException("No company assigned to this client");
        }
        if (client.isAccount() == true) {
            User user = userRepository.findByEmail(client.getEmail());
            if (user == null) {
                UserRegistrationDto registrationDto = new UserRegistrationDto(client.getFirstName(),
                        client.getLastName(), client.getEmail(), client.getEmail(), "Client");
                userService.save(registrationDto);
            }
        }
        Audit audit = new Audit("Client '" + client.getFirstName() + " " + client.getLastName() + "' added", "Action via API");
        return clientRepository.save(client);
    }

    @PostMapping("/multiple")
    public String createMultipleClients(@RequestBody List<Client> clients) {
        for (Client client : clients) {
            clientRepository.save(client);
            Audit audit = new Audit("Client '" + client.getFirstName() + " " + client.getLastName() + "' added", "Action via API");
            if (client.isAccount() == true) {
                User user = userRepository.findByEmail(client.getEmail());
                if (user == null) {
                    UserRegistrationDto registrationDto = new UserRegistrationDto(client.getFirstName(),
                            client.getLastName(), client.getEmail(), client.getEmail(), "Client");
                    userService.save(registrationDto);
                }
            }
        }
        return "Success";
    }

    @PutMapping("{id}")
    public ResponseEntity<Client> updateClient(@PathVariable long id, @RequestBody Client clientDetails) {
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
        if (client.isAccount() == true) {
            User user = userRepository.findByEmail(client.getEmail());
            if (user == null) {
                UserRegistrationDto registrationDto = new UserRegistrationDto(client.getFirstName(),
                        client.getLastName(), client.getEmail(), client.getEmail(), "Client");
                userService.save(registrationDto);
            }
        }
        return ResponseEntity.ok(client);
    }
}
