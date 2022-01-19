package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> index();
    User show(Long id);
    void save(User user);
    void update(User newUser);
    void delete(Long id);
    User showInfoUser(Long id);
    void registration(User user);
}