package com.tripplanner.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@Table(name = "trips", indexes = {
    @Index(name = "idx_trip_creator", columnList = "creator_id"),
    @Index(name = "idx_trip_share_token", columnList = "share_token"),
    @Index(name = "idx_trip_dates", columnList = "start_date, end_date")
})
@EntityListeners(AuditingEntityListener.class)
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 100)
    @Column(nullable = false)
    private String name;

    @Size(max = 1000)
    private String description;

    @Size(max = 200)
    private String location;

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isPublic = false;

    @Column(unique = true)
    @Builder.Default
    private String shareToken = UUID.randomUUID().toString();

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    // Budget and cost tracking
    private Double estimatedBudget;
    
    @Builder.Default
    private String currency = "USD";

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<TripMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Availability> availabilities = new ArrayList<>();

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Message> messages = new ArrayList<>();

    // Constructor with custom share token generation
    public Trip() {
        this.shareToken = UUID.randomUUID().toString();
        this.isPublic = false;
        this.active = true;
        this.currency = "USD";
        this.members = new ArrayList<>();
        this.availabilities = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    // Constructor for creating trip with basic fields
    public Trip(String name, String description, User creator) {
        this();
        this.name = name;
        this.description = description;
        this.creator = creator;
    }

    // Helper methods
    public void generateNewShareToken() {
        this.shareToken = UUID.randomUUID().toString();
    }

    public boolean isCreator(User user) {
        return creator != null && creator.getId().equals(user.getId());
    }
}