package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.User;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final RestTemplate restTemplate;

    public TransactionService(UserRepository userRepository,
                              TransactionRepository transactionRepository,
                              RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public void processTransaction(Long senderId, Long recipientId, float amount) {

        Optional<User> senderOpt = userRepository.findById(senderId);
        Optional<User> recipientOpt = userRepository.findById(recipientId);

        if (senderOpt.isEmpty() || recipientOpt.isEmpty()) return;

        User sender = senderOpt.get();
        User recipient = recipientOpt.get();

        BigDecimal senderBalance = BigDecimal.valueOf(sender.getBalance());
        BigDecimal recipientBalance = BigDecimal.valueOf(recipient.getBalance());
        BigDecimal transferAmount = BigDecimal.valueOf(amount);

        // Check if sender has enough balance
        if (senderBalance.compareTo(transferAmount) < 0) return;

        // Deduct from sender
        sender.setBalance(senderBalance.subtract(transferAmount).floatValue());

        // Call Incentive API (optional)
        com.jpmc.midascore.foundation.Transaction txDto =
                new com.jpmc.midascore.foundation.Transaction();
        txDto.setSenderId(senderId);
        txDto.setRecipientId(recipientId);
        txDto.setAmount(amount);

        Incentive incentive = null;
        try {
            incentive = restTemplate.postForObject(
                    "http://localhost:8080/incentive",
                    txDto,
                    Incentive.class
            );
        } catch (Exception e) {
            System.out.println("⚠️ Incentive API error: " + e.getMessage());
        }

        float incentiveAmount = (incentive != null && incentive.getAmount() != null)
                ? incentive.getAmount().floatValue()
                : 0f;

        // Add transfer + incentive to recipient
        recipient.setBalance(recipientBalance
                .add(transferAmount)
                .add(BigDecimal.valueOf(incentiveAmount))
                .floatValue());

        // Save both users
        userRepository.save(sender);
        userRepository.save(recipient);

        // Record transaction
        TransactionRecord record = new TransactionRecord(
                sender,
                recipient,
                BigDecimal.valueOf(amount),
                BigDecimal.valueOf(incentiveAmount),
                System.currentTimeMillis()
        );
        transactionRepository.save(record);

        // ✅ Dynamically log Waldorf’s current balance if present
        userRepository.findByName("Waldorf").ifPresent(waldorf -> {
            System.out.println("✅ Current Waldorf Balance: " + waldorf.getBalance());
        });
    }

    // Inner class for Incentive API response
    public static class Incentive {
        private BigDecimal amount;
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
    }
}