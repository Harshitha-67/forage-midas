package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.User;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class DatabaseConduit {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public DatabaseConduit(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void processTransaction(User sender, User recipient, float amount) {
        if (sender == null || recipient == null) return;
        if (sender.getBalance() < amount) return;

        // Adjust balances
        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount);

        // Save users
        userRepository.save(sender);
        userRepository.save(recipient);

        // Record transaction
        TransactionRecord record = new TransactionRecord(sender, recipient, BigDecimal.valueOf(amount));
        transactionRepository.save(record);
    }

    public Optional<User> findUserByName(String name) {
        return userRepository.findByName(name);
    }
}
