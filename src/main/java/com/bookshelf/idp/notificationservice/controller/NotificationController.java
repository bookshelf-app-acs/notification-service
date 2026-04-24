package com.bookshelf.idp.notificationservice.controller;

import com.bookshelf.idp.notificationservice.dto.CreateNotificationRequestDto;
import com.bookshelf.idp.notificationservice.dto.NotificationResponseDto;
import com.bookshelf.idp.notificationservice.dto.UnreadCountDto;
import com.bookshelf.idp.notificationservice.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<NotificationResponseDto> create(@Valid @RequestBody CreateNotificationRequestDto dto) {
        return new ResponseEntity<>(notificationService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<List<NotificationResponseDto>> getMyNotifications(
            @AuthenticationPrincipal String userEmail) {
        return new ResponseEntity<>(notificationService.getMyNotifications(userEmail), HttpStatus.OK);
    }

    @GetMapping("/me/unread-count")
    public ResponseEntity<UnreadCountDto> getMyUnreadCount(@AuthenticationPrincipal String userEmail) {
        return new ResponseEntity<>(notificationService.getMyUnreadCount(userEmail), HttpStatus.OK);
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<NotificationResponseDto> markAsRead(
            @PathVariable UUID id,
            @AuthenticationPrincipal String userEmail) {
        return new ResponseEntity<>(notificationService.markAsRead(id, userEmail), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal String userEmail) {
        notificationService.delete(id, userEmail);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NotificationResponseDto>> getAll() {
        return new ResponseEntity<>(notificationService.getAll(), HttpStatus.OK);
    }
}