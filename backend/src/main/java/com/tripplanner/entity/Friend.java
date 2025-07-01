package com.tripplanner.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    @Builder.Default
    @Setter(AccessLevel.NONE) // Custom setter needed for business logic
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

    // Constructor for creating friend relationship
    public Friend(User requester, User addressee) {
        this.requester = requester;
        this.addressee = addressee;
        this.status = Status.PENDING;
    }

    // Custom setter for status with business logic
    public void setStatus(Status status) {
        this.status = status;
        if (status == Status.ACCEPTED && acceptedAt == null) {
            this.acceptedAt = LocalDateTime.now();
        }
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