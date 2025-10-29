package com.jpmc.midascore.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User recipient;

    private BigDecimal amount;

    private BigDecimal incentive;

    private Long timestamp;

    public TransactionRecord() {}

    // ✅ Old-style constructor (keeps previous tasks working)
    public TransactionRecord(User sender, User recipient, BigDecimal amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.incentive = BigDecimal.ZERO; // default
        this.timestamp = System.currentTimeMillis();
    }

    // ✅ New-style constructor (for Incentive API)
    public TransactionRecord(User sender, User recipient, BigDecimal amount, BigDecimal incentive, Long timestamp) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.incentive = incentive;
        this.timestamp = timestamp;
    }

    // Getters
    public Long getId() { return id; }
    public User getSender() { return sender; }
    public User getRecipient() { return recipient; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal getIncentive() { return incentive; }
    public Long getTimestamp() { return timestamp; }

    // Setters
    public void setSender(User sender) { this.sender = sender; }
    public void setRecipient(User recipient) { this.recipient = recipient; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setIncentive(BigDecimal incentive) { this.incentive = incentive; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
}
