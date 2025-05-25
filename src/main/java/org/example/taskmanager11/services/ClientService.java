package org.example.taskmanager11.services;

import org.example.taskmanager11.repo.ClientRepository;
import org.example.taskmanager11.model.Client;
import org.example.taskmanager11.utils.Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional
    public void addClient(String login, String salt, String password) {
        Client client = clientRepository.findByLogin(login);
        if (client != null) {
            throw new RuntimeException("Client already exists: " + login);
        }

        client = new Client(login, salt, password);
        clientRepository.save(client);
    }

    @Transactional(readOnly = true)
    public boolean checkClient(String login, String password) {
        Client client = clientRepository.findByLogin(login);
        if (client == null)
            return false;

        String hash = Utils.passwordHash(client.getSalt(), password);
        return hash.equals(client.getPassword());
    }

    @Transactional
    public void changePassword(String login, String password) {
        Client client = clientRepository.findByLogin(login);
        String salt = Utils.generateRandomString(10);
        String hash = Utils.passwordHash(salt, password);
        client.setSalt(salt);
        client.setPassword(hash);
        clientRepository.save(client);
    }
}
