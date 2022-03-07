package net.javaguides.springboot.web;

import net.javaguides.springboot.model.Audit;
import net.javaguides.springboot.model.Client;
import net.javaguides.springboot.service.AuditService;
import net.javaguides.springboot.service.ClientService;
import net.javaguides.springboot.service.CompanyService;
import net.javaguides.springboot.service.SettingsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ClientController {

    private ClientService clientService;
    private CompanyService companyService;
    private AuditService auditService;
    private SettingsService settingsService;

    public ClientController(ClientService clientService, CompanyService companyService, AuditService auditService, SettingsService settingsService) {
        super();
        this.clientService = clientService;
        this.companyService = companyService;
        this.auditService = auditService;
        this.settingsService = settingsService;
    }

    @GetMapping("/clients")
    public String listClients(Model model, Model settingsModel){
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

    @GetMapping("/clients/new")
    public String createClientForm(Model clientModel, Model companyModel, Model settingsModel){
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

    @PostMapping("/clients")
    public String saveClient(@ModelAttribute("client") Client client){
        try{
            Audit audit = new Audit("Client '" + client.getName() + "' added");
            auditService.saveAudit(audit);
            clientService.saveClient(client);
            return "redirect:/clients?success";
        } catch(Exception e){
            return "redirect:/clients?fail";
        }
    }

    @GetMapping("/clients/edit/{id}")
    public String editClientForm(@PathVariable Long id, Model clientModel, Model companyModel, Model settingsModel) {
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

    @PostMapping("/clients/{id}")
    public String updateClient(@PathVariable Long id,
                                @ModelAttribute("client") Client client,
                                Model model) {
        try {
            Client existingClient = clientService.getClientById(id);
            if (!existingClient.getName().equals(client.getName())) {
                Audit audit = new Audit("Client " + client.getId() + " updated. Name updated from '" + existingClient.getName() + "' to '" + client.getName() + "'");
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
            existingClient.setName(client.getName());
            existingClient.setPhone(client.getPhone());
            existingClient.setEmail(client.getEmail());
            existingClient.setCompany(client.getCompany());
            existingClient.setMobile(client.getMobile());
            clientService.updateClient(existingClient);
            return "redirect:/clients?success";
        } catch (Exception e) {
            return "redirect:/clients?fail";
        }
    }

    @GetMapping("/clients/{id}")
    public String deleteClient(@PathVariable Long id) {
        try {
            Client client = clientService.getClientById(id);
            Audit audit = new Audit("Client '" + client.getName() + "' deleted");
            auditService.saveAudit(audit);
            clientService.deleteClientById(id);
            return "redirect:/clients?success";
        } catch (Exception e) {
            return "redirect:/clients?fail";
        }
    }
}
