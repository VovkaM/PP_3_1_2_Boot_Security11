package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.validator.UserValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserValidator userValidator;
    private final UserService userService;
    private final PasswordEncoder encoder;

    @Autowired
    public AuthController(UserValidator userValidator, UserService userService, PasswordEncoder encoder) {
        this.userValidator = userValidator;
        this.userService = userService;
        this.encoder = encoder;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user")User user) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid User user, BindingResult result) {
        userValidator.validate(user, result);

        if (result.hasErrors()) {
            return "/auth/registration";
        }

        user.setPassword(encoder.encode(user.getPassword()));
        userService.registration(user);

        return "redirect:/auth/login";
    }
}
