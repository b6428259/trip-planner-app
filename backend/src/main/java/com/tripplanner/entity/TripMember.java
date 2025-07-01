package com.tripplanner.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
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
    private Role role = Role.MEMBER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
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

    // Constructors
    public TripMember() {}

    public TripMember(Trip trip, User user, Role role) {
        this.trip = trip;
        this.user = user;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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