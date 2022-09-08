package net.knowledgebase.springboot.web;

import net.bytebuddy.utility.RandomString;
import net.knowledgebase.springboot.model.Audit;
import net.knowledgebase.springboot.model.Client;
import net.knowledgebase.springboot.model.User;
import net.knowledgebase.springboot.repository.UserRepository;
import net.knowledgebase.springboot.service.*;
import net.knowledgebase.springboot.web.dto.UserRegistrationDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ClientController {

    private ClientService clientService;
    private CompanyService companyService;
    private AuditService auditService;
    private SettingsService settingsService;
    private UserRepository userRepository;
    private UserService userService;

    public ClientController(ClientService clientService, CompanyService companyService, AuditService auditService, SettingsService settingsService, UserRepository userRepository, UserService userService) {
        super();
        this.clientService = clientService;
        this.companyService = companyService;
        this.auditService = auditService;
        this.settingsService = settingsService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/clients")
    public String listClients(Model model, Model settingsModel) {
        model.addAttribute("clients", clientService.getAllClients());
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "clients";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/clients/new")
    public String createClientForm(Model clientModel, Model companyModel, Model settingsModel) {
        companyModel.addAttribute("company", companyService.getAllCompanies());
        Client client = new Client();
        clientModel.addAttribute("client", client);
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "create_client";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/clients")
    public String saveClient(@ModelAttribute("client") Client client, HttpServletRequest request) {
        try {
            Audit audit = new Audit("Client '" + client.getFirstName() + " " + client.getLastName() + "' added");
            auditService.saveAudit(audit);
            clientService.saveClient(client);
            if (client.isAccount()) {
                User user = userRepository.findByEmail(client.getEmail());
                if (user == null) {
                    UserRegistrationDto registrationDto = new UserRegistrationDto(client.getFirstName(), client.getLastName(), client.getEmail(), null, "Client", RandomString.make(30));
                    userService.save(registrationDto);
                }
            }
            return "redirect:/clients?success";
        } catch (Exception e) {
            return "redirect:/clients?fail";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/clients/edit/{id}")
    public String editClientForm(@PathVariable String id, Model clientModel, Model companyModel, Model settingsModel) {
        companyModel.addAttribute("company", companyService.getAllCompanies());
        clientModel.addAttribute("client", clientService.getClientById(id));
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "edit_client";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/clients/{id}")
    public String updateClient(@PathVariable String id,
                               @ModelAttribute("client") Client client,
                               Model model, HttpServletRequest request) {
        try {
            Client existingClient = clientService.getClientById(id);
            if (!existingClient.getFirstName().equals(client.getFirstName())) {
                Audit audit = new Audit("Client " + client.getId() + " updated. Name updated from '" + existingClient.getFirstName() + "' to '" + client.getFirstName() + "'");
                auditService.saveAudit(audit);
            }
            if (!existingClient.getCompany().equals(client.getCompany())) {
                Audit audit = new Audit("Client " + client.getId() + " updated. Company updated from '" + existingClient.getCompany() + "' to '" + client.getCompany() + "'");
                auditService.saveAudit(audit);
            }
            if (!existingClient.getEmail().equals(client.getEmail())) {
                Audit audit = new Audit("Client " + client.getId() + " updated. Email updated from '" + existingClient.getEmail() + "' to '" + client.getEmail() + "'");
                auditService.saveAudit(audit);
            }
            if (!existingClient.getPhone().equals(client.getPhone())) {
                Audit audit = new Audit("Client " + client.getId() + " updated. Phone updated from '" + existingClient.getPhone() + "' to '" + client.getPhone() + "'");
                auditService.saveAudit(audit);
            }
            if (!existingClient.getMobile().equals(client.getMobile())) {
                Audit audit = new Audit("Client " + client.getId() + " updated. Mobile updated from '" + existingClient.getMobile() + "' to '" + client.getMobile() + "'");
                auditService.saveAudit(audit);
            }
            existingClient.setId(id);
            existingClient.setFirstName(client.getFirstName());
            existingClient.setLastName(client.getLastName());
            existingClient.setPhone(client.getPhone());
            existingClient.setEmail(client.getEmail());
            existingClient.setCompany(client.getCompany());
            existingClient.setMobile(client.getMobile());
            existingClient.setAccount(client.isAccount());
            existingClient.setSuspended(client.isSuspended());
            clientService.updateClient(existingClient);

            if (existingClient.isAccount()) {
                User user = userRepository.findByEmail(existingClient.getEmail());
                if (user == null) {
                    UserRegistrationDto registrationDto = new UserRegistrationDto(existingClient.getFirstName(), existingClient.getLastName(), existingClient.getEmail(), null, "Client", RandomString.make(30));
                    userService.save(registrationDto);
                }
            }
            return "redirect:/clients?success";
        } catch (Exception e) {
            return "redirect:/clients?fail";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/clients/{id}")
    public String deleteClient(@PathVariable String id) {
        try {
            Client client = clientService.getClientById(id);
            Audit audit = new Audit("Client '" + client.getFirstName() + " " + client.getLastName() + "' deleted");
            auditService.saveAudit(audit);
            clientService.deleteClientById(id);
            return "redirect:/clients?success";
        } catch (Exception e) {
            return "redirect:/clients?fail";
        }
    }
}
