package com.bookshelf.idp.notificationservice.dto;

import com.bookshelf.idp.notificationservice.entity.NotificationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateNotificationRequestDto {

    @NotBlank(message = "User email is required")
    @Email(message = "Invalid email format")
    private String userEmail;

    @NotNull(message = "Notification type is required")
    private NotificationType type;

    @NotBlank(message = "Message is required")
    private String message;

    private UUID bookId;
}
