package com.tripplanner.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
@Table(name = "notifications", indexes = {
    @Index(name = "idx_notification_user", columnList = "user_id"),
    @Index(name = "idx_notification_read", columnList = "is_read"),
    @Index(name = "idx_notification_created", columnList = "created_at")
})
@EntityListeners(AuditingEntityListener.class)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false)
    private String title;

    @Size(max = 1000)
    private String content;

    @Column(nullable = false)
    @Builder.Default
    @Setter(AccessLevel.NONE) // Custom setter needed for business logic
    private Boolean isRead = false;

    private String actionUrl;
    private String data; // JSON data for additional information

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime readAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Enums
    public enum NotificationType {
        FRIEND_REQUEST,
        FRIEND_ACCEPTED,
        TRIP_INVITATION,
        TRIP_UPDATE,
        MESSAGE,
        AVAILABILITY_UPDATE,
        SYSTEM
    }

    // Constructor for creating notification with basic fields
    public Notification(User user, NotificationType type, String title, String content) {
        this.user = user;
        this.type = type;
        this.title = title;
        this.content = content;
        this.isRead = false;
    }

    // Custom setter for isRead with business logic
    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
        if (isRead && readAt == null) {
            this.readAt = LocalDateTime.now();
        }
    }

    // Helper methods
    public void markAsRead() {
        this.isRead = true;
        this.readAt = LocalDateTime.now();
    }
}