package com.bookshelf.idp.notificationservice.dto;

import com.bookshelf.idp.notificationservice.entity.NotificationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {
    private UUID id;
    private String userEmail;
    private NotificationType type;
    private String message;
    private UUID bookId;
    private Boolean isRead;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
