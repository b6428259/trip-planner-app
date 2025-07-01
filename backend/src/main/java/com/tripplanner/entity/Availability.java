package com.tripplanner.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "availabilities", indexes = {
    @Index(name = "idx_availability_trip", columnList = "trip_id"),
    @Index(name = "idx_availability_user", columnList = "user_id"),
    @Index(name = "idx_availability_dates", columnList = "start_date, end_date")
})
@EntityListeners(AuditingEntityListener.class)
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    private String notes;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Constructors
    public Availability() {}

    public Availability(Trip trip, User user, LocalDate startDate, LocalDate endDate) {
        this.trip = trip;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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
    public boolean overlaps(LocalDate otherStart, LocalDate otherEnd) {
        return startDate.isBefore(otherEnd) || startDate.isEqual(otherEnd) &&
               (endDate.isAfter(otherStart) || endDate.isEqual(otherStart));
    }

    public boolean contains(LocalDate date) {
        return (startDate.isBefore(date) || startDate.isEqual(date)) &&
               (endDate.isAfter(date) || endDate.isEqual(date));
    }
}