package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.validator.UserValidator;

import javax.validation.Valid;


@Controller
public class UserController {
    private final UserService userService;
    private final UserValidator userValidator;
    private final PasswordEncoder encoder;

    @Autowired
    public UserController(UserService userService, UserValidator userValidator, PasswordEncoder encoder) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.encoder = encoder;
    }


    @GetMapping("/admin")
    public String index(Model model) {
        model.addAttribute("users", userService.index());
        return "index";
    }
    @GetMapping("/admin/{id}")
    public String show(@PathVariable("id") Long id, Model model) {

        model.addAttribute("user", userService.show(id));
        return "show";
    }

    @GetMapping("/user")
    public String showInfoUser(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", userService.showInfoUser(user.getId()));
        return "user";
    }
    @GetMapping("/admin/new")
    public String newUser(@ModelAttribute("user") User user) {
        return "new";
    }

    @PostMapping("/admin")
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {

        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "new";
        }

        user.setPassword(encoder.encode(user.getPassword()));
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.show(id));
        return "edit";
    }

    @PatchMapping("/admin/{id}")
    public String update(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "edit";
        }

        userService.update(user);
        return "redirect:/admin/{id}";
    }

    @DeleteMapping("admin/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
