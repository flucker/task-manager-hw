package org.example.taskmanager11.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.taskmanager11.services.ClientService;
import org.example.taskmanager11.utils.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ClientController implements AppController{

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("register")
    public String register() {
        return "register";
    }

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("password")
    public String changePassword() {
        return "password";
    }

    @PostMapping("register")
    public String register(@RequestParam String login,
                           @RequestParam String password) {
        String salt = Utils.generateRandomString(10);
        String hash = Utils.passwordHash(salt, password);

        clientService.addClient(login, salt, hash);
        return "redirect:/login";
    }

    @PostMapping("login")
    public String login(@RequestParam String login,
                        @RequestParam String password,
                        HttpSession session) {
        if (clientService.checkClient(login, password)) {
            session.setAttribute("login", login);
            return "redirect:/";
        } else
            return "redirect:/login";
    }

    @PostMapping("password")
    public String changePassword(@RequestParam String password,
                        HttpSession session) {
        checkAuthorized(session);
        String login = session.getAttribute("login").toString();
        clientService.changePassword(login, password);
        return "redirect:/";
    }

    @GetMapping("logout")
    public String logout(HttpSession session) {
        session.removeAttribute("login");
        return "redirect:/";
    }
}
