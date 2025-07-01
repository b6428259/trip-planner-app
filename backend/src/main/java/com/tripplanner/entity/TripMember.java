package com.tripplanner.entity;

import jakarta.persistence.*;
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
@Table(name = "trip_members", indexes = {
    @Index(name = "idx_trip_member_trip", columnList = "trip_id"),
    @Index(name = "idx_trip_member_user", columnList = "user_id")
})
@EntityListeners(AuditingEntityListener.class)
public class TripMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.MEMBER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Status status = Status.PENDING;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Enums
    public enum Role {
        CREATOR,
        ADMIN,
        MEMBER
    }

    public enum Status {
        PENDING,
        ACCEPTED,
        DECLINED
    }

    // Constructor for creating trip member with basic fields
    public TripMember(Trip trip, User user, Role role) {
        this.trip = trip;
        this.user = user;
        this.role = role;
        this.status = Status.PENDING;
    }

    // Helper methods
    public boolean isCreator() {
        return role == Role.CREATOR;
    }

    public boolean isAdmin() {
        return role == Role.ADMIN || role == Role.CREATOR;
    }

    public boolean isAccepted() {
        return status == Status.ACCEPTED;
    }
}