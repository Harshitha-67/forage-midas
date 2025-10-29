package com.jpmc.midascore.foundation.controller;

import com.jpmc.midascore.entity.User;
import com.jpmc.midascore.foundation.FoundationTransactionRecord;
import com.jpmc.midascore.foundation.service.FoundationTransactionService;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/foundation")
public class FoundationTransactionController {

    private final FoundationTransactionService foundationService;
    private final UserRepository userRepository;

    public FoundationTransactionController(FoundationTransactionService foundationService,
                                           UserRepository userRepository) {
        this.foundationService = foundationService;
        this.userRepository = userRepository;
    }

    @PostMapping("/transfer")
    public FoundationTransactionRecord transfer(@RequestParam Long senderId,
                                                @RequestParam Long recipientId,
                                                @RequestParam BigDecimal amount) {

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new IllegalArgumentException("Recipient not found"));

        return foundationService.createFoundationTransaction(senderId, recipientId, amount);
    }
}
