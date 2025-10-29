package com.jpmc.midascore.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users") // ✅ Avoid 'user' reserved keyword in H2
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Use IDENTITY for auto-increment
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private float balance;

    protected User() {
        // JPA needs a default constructor
    }

    public User(String name, float balance) {
        this.name = name;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return String.format("User[id=%d, name='%s', balance=%.2f]", id, name, balance);
    }
}
