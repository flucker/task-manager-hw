package org.example.taskmanager11.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Controller
public interface AppController {
    @ExceptionHandler(value = NotAuthorizedException.class)
    default String onException() {
        return "login";
    }

    default void checkAuthorized(HttpSession session) {
        if (session.getAttribute("login") == null)
            throw new NotAuthorizedException();
    }
}
