package com.jpmc.midascore.controller;

import com.jpmc.midascore.entity.User;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.foundation.Balance;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/balance")
public class BalanceController {

    private final UserRepository userRepository;

    public BalanceController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public Balance getBalance() {
        // Find user named Waldorf (the one whose balance changes)
        User waldorf = userRepository.findByName("Waldorf").orElse(null);

        if (waldorf == null) {
            return new Balance(0.0f);
        }

        return new Balance(waldorf.getBalance());
    }
}
