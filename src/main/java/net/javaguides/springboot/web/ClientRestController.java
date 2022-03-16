package net.javaguides.springboot.web;

import net.javaguides.springboot.exception.InternalServerErrorException;
import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Client;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.repository.ClientRepository;
import net.javaguides.springboot.repository.UserRepository;
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

    public ClientRestController(ClientRepository clientRepository, UserService userService,
                                UserRepository userRepository) {
        this.clientRepository = clientRepository;
        this.userService = userService;
        this.userRepository = userRepository;
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
        return clientRepository.save(client);
    }

    @PostMapping("/multiple")
    public String createMultipleClients(@RequestBody List<Client> clients) {
        for (Client client : clients) {
            clientRepository.save(client);
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
        client.setFirstName(clientDetails.getFirstName());
        client.setAccount(clientDetails.isAccount());
        client.setEmail(clientDetails.getEmail());
        client.setCompany(clientDetails.getCompany());
        client.setMobile(clientDetails.getMobile());
        client.setPhone(clientDetails.getPhone());
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
