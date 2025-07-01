package com.tripplanner.controller;

import com.tripplanner.dto.UserResponse;
import com.tripplanner.security.UserPrincipal;
import com.tripplanner.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "User management endpoints")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Get current user profile", description = "Get the profile of the currently authenticated user")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        UserResponse userResponse = userService.getUserById(userPrincipal.getId());
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user by ID", description = "Get user profile by user ID")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        UserResponse userResponse = userService.getUserById(userId);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/search")
    @Operation(summary = "Search users", description = "Search users by username, email, or name")
    public ResponseEntity<List<UserResponse>> searchUsers(@RequestParam String q) {
        List<UserResponse> users = userService.searchUsers(q);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/me")
    @Operation(summary = "Update current user profile", description = "Update the profile of the currently authenticated user")
    public ResponseEntity<UserResponse> updateCurrentUser(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody UserResponse userUpdate) {
        UserResponse userResponse = userService.updateUser(userPrincipal.getId(), userUpdate);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/me")
    @Operation(summary = "Delete current user account", description = "Deactivate the currently authenticated user account")
    public ResponseEntity<?> deleteCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        userService.deleteUser(userPrincipal.getId());
        return ResponseEntity.ok().body("{\"message\": \"User account deactivated successfully!\"}");
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete user account (Admin only)", description = "Deactivate a user account (Admin only)")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().body("{\"message\": \"User account deactivated successfully!\"}");
    }
}