package com.tripplanner.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    // Constructor for creating availability with basic fields
    public Availability(Trip trip, User user, LocalDate startDate, LocalDate endDate) {
        this.trip = trip;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
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