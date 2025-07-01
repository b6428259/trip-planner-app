package com.tripplanner.repository;

import com.tripplanner.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.active = true AND (u.username LIKE %:search% OR u.email LIKE %:search% OR u.firstName LIKE %:search% OR u.lastName LIKE %:search%)")
    List<User> searchUsers(@Param("search") String search);

    @Query("SELECT u FROM User u WHERE u.active = true")
    List<User> findAllActiveUsers();

    @Query("SELECT u FROM User u JOIN u.tripMemberships tm WHERE tm.trip.id = :tripId AND tm.status = 'ACCEPTED'")
    List<User> findTripMembers(@Param("tripId") Long tripId);

    @Query("SELECT u FROM User u JOIN u.groupMemberships gm WHERE gm.group.id = :groupId")
    List<User> findGroupMembers(@Param("groupId") Long groupId);
}