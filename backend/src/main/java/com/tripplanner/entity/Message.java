package com.tripplanner.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messages", indexes = {
    @Index(name = "idx_message_trip", columnList = "trip_id"),
    @Index(name = "idx_message_sender", columnList = "sender_id"),
    @Index(name = "idx_message_created", columnList = "created_at")
})
@EntityListeners(AuditingEntityListener.class)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 2000)
    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private MessageType messageType = MessageType.TEXT;

    @Column(nullable = false)
    @Builder.Default
    private Boolean edited = false;

    private LocalDateTime editedAt;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id")
    private User recipient; // For private messages

    // Enums
    public enum MessageType {
        TEXT,
        IMAGE,
        FILE,
        SYSTEM,
        NOTIFICATION
    }

    // Constructor for creating message with basic fields
    public Message(User sender, String content, MessageType messageType) {
        this.sender = sender;
        this.content = content;
        this.messageType = messageType;
        this.edited = false;
    }

    // Helper methods
    public boolean isPrivateMessage() {
        return recipient != null;
    }

    public boolean isTripMessage() {
        return trip != null;
    }

    public void markAsEdited() {
        this.edited = true;
        this.editedAt = LocalDateTime.now();
    }
}