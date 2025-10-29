package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.service.TransactionService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionListener {

    private final TransactionService transactionService;

    public TransactionListener(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @KafkaListener(topics = "${midas.kafka.topic}", groupId = "midas-group")
    public void listen(Transaction transaction) {
        System.out.println("✅ Received Transaction:");
        System.out.println("Sender ID: " + transaction.getSenderId());
        System.out.println("Recipient ID: " + transaction.getRecipientId());
        System.out.println("Amount: " + transaction.getAmount());

        // Process transaction
        transactionService.processTransaction(
                transaction.getSenderId(),
                transaction.getRecipientId(),
                (float) transaction.getAmount()   // ✅ cast double -> float
        );
    }
}
