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
@Table(name = "group_members", indexes = {
    @Index(name = "idx_group_member_group", columnList = "group_id"),
    @Index(name = "idx_group_member_user", columnList = "user_id")
})
@EntityListeners(AuditingEntityListener.class)
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.MEMBER;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Enums
    public enum Role {
        CREATOR,
        ADMIN,
        MEMBER
    }

    // Constructor for creating group member with basic fields
    public GroupMember(Group group, User user, Role role) {
        this.group = group;
        this.user = user;
        this.role = role;
    }

    // Helper methods
    public boolean isCreator() {
        return role == Role.CREATOR;
    }

    public boolean isAdmin() {
        return role == Role.ADMIN || role == Role.CREATOR;
    }
}