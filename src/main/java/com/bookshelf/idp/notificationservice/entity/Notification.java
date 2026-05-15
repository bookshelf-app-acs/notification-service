package com.bookshelf.idp.notificationservice.entity;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Notification {
    private UUID id;
    private String userEmail;
    private NotificationType type;
    private String message;
    private UUID bookId;
    private Boolean isRead;
    private LocalDateTime createdAt;
}