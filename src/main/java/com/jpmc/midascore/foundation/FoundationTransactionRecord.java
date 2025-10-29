package com.jpmc.midascore.foundation;
import com.jpmc.midascore.entity.User;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class FoundationTransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User recipient;

    private BigDecimal amount;

    public FoundationTransactionRecord() {}

    public FoundationTransactionRecord(User sender, User recipient, BigDecimal amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
    }

    // Getters and setters
    public Long getId() { return id; }

    public User getSender() { return sender; }
    public void setSender(User sender) { this.sender = sender; }

    public User getRecipient() { return recipient; }
    public void setRecipient(User recipient) { this.recipient = recipient; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
