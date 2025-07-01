package com.tripplanner.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "friends", indexes = {
    @Index(name = "idx_friend_requester", columnList = "requester_id"),
    @Index(name = "idx_friend_addressee", columnList = "addressee_id"),
    @Index(name = "idx_friend_status", columnList = "status")
})
@EntityListeners(AuditingEntityListener.class)
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime acceptedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addressee_id", nullable = false)
    private User addressee;

    // Enums
    public enum Status {
        PENDING,
        ACCEPTED,
        DECLINED,
        BLOCKED
    }

    // Constructors
    public Friend() {}

    public Friend(User requester, User addressee) {
        this.requester = requester;
        this.addressee = addressee;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
        if (status == Status.ACCEPTED && acceptedAt == null) {
            this.acceptedAt = LocalDateTime.now();
        }
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(LocalDateTime acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public User getAddressee() {
        return addressee;
    }

    public void setAddressee(User addressee) {
        this.addressee = addressee;
    }

    // Helper methods
    public boolean isAccepted() {
        return status == Status.ACCEPTED;
    }

    public boolean isPending() {
        return status == Status.PENDING;
    }

    public User getOtherUser(User currentUser) {
        return requester.getId().equals(currentUser.getId()) ? addressee : requester;
    }
}