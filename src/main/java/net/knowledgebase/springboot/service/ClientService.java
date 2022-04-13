package net.knowledgebase.springboot.service;

import net.knowledgebase.springboot.model.Client;

import java.util.List;

public interface ClientService {
    List<Client> getAllClients();

    Client saveClient(Client client);

    Client getClientById(Long id);

    Client updateClient(Client client);

    void deleteClientById(Long id);
}
