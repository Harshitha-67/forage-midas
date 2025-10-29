package com.jpmc.midascore.foundation.service;

import com.jpmc.midascore.entity.User;
import com.jpmc.midascore.foundation.FoundationTransactionRecord;
import com.jpmc.midascore.foundation.repository.FoundationTransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class FoundationTransactionService {

    private final UserRepository userRepository;
    private final FoundationTransactionRecordRepository foundationTransactionRecordRepository;

    public FoundationTransactionService(UserRepository userRepository,
                                        FoundationTransactionRecordRepository foundationTransactionRecordRepository) {
        this.userRepository = userRepository;
        this.foundationTransactionRecordRepository = foundationTransactionRecordRepository;
    }

    @Transactional
    public FoundationTransactionRecord createFoundationTransaction(Long senderId, Long recipientId, BigDecimal amount) {
        Optional<User> senderOpt = userRepository.findById(senderId);
        Optional<User> recipientOpt = userRepository.findById(recipientId);

        if (senderOpt.isEmpty() || recipientOpt.isEmpty()) {
            throw new IllegalArgumentException("Sender or recipient not found");
        }

        User sender = senderOpt.get();
        User recipient = recipientOpt.get();

        if (sender.getBalance() < amount.doubleValue()) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        // Update balances
        sender.setBalance(sender.getBalance() - amount.floatValue());
        recipient.setBalance(recipient.getBalance() + amount.floatValue());

        userRepository.save(sender);
        userRepository.save(recipient);

        // Record transaction
        FoundationTransactionRecord record = new FoundationTransactionRecord(sender, recipient, amount);
        return foundationTransactionRecordRepository.save(record);
    }
}
