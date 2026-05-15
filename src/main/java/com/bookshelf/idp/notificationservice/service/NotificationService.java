package com.bookshelf.idp.notificationservice.service;

import com.bookshelf.idp.notificationservice.client.DatabaseServiceClient;
import com.bookshelf.idp.notificationservice.dto.CreateNotificationRequestDto;
import com.bookshelf.idp.notificationservice.dto.NotificationResponseDto;
import com.bookshelf.idp.notificationservice.dto.UnreadCountDto;
import com.bookshelf.idp.notificationservice.entity.Notification;
import com.bookshelf.idp.notificationservice.exception.ForbiddenException;
import com.bookshelf.idp.notificationservice.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

    private final DatabaseServiceClient dbClient;

    public NotificationService(DatabaseServiceClient dbClient) {
        this.dbClient = dbClient;
    }

    public NotificationResponseDto create(CreateNotificationRequestDto dto) {
        Notification notification = new Notification();
        notification.setUserEmail(dto.getUserEmail());
        notification.setType(dto.getType());
        notification.setMessage(dto.getMessage());
        notification.setBookId(dto.getBookId());
        notification.setIsRead(false);
        return toDto(dbClient.save(notification));
    }

    public List<NotificationResponseDto> getMyNotifications(String userEmail) {
        return dbClient.findByUserEmail(userEmail).stream().map(this::toDto).toList();
    }

    public UnreadCountDto getMyUnreadCount(String userEmail) {
        return new UnreadCountDto(dbClient.countUnread(userEmail));
    }

    public NotificationResponseDto markAsRead(UUID id, String userEmail) {
        Notification notification = dbClient.findById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found"));
        if (!notification.getUserEmail().equals(userEmail)) {
            throw new ForbiddenException("You cannot modify this notification");
        }
        return toDto(dbClient.markAsRead(id));
    }

    public void delete(UUID id, String userEmail) {
        Notification notification = dbClient.findById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found"));
        if (!notification.getUserEmail().equals(userEmail)) {
            throw new ForbiddenException("You cannot delete this notification");
        }
        dbClient.delete(id);
    }

    public List<NotificationResponseDto> getAll() {
        return dbClient.findAll().stream().map(this::toDto).toList();
    }

    private NotificationResponseDto toDto(Notification n) {
        NotificationResponseDto dto = new NotificationResponseDto();
        dto.setId(n.getId());
        dto.setUserEmail(n.getUserEmail());
        dto.setType(n.getType());
        dto.setMessage(n.getMessage());
        dto.setBookId(n.getBookId());
        dto.setIsRead(n.getIsRead());
        dto.setCreatedAt(n.getCreatedAt());
        return dto;
    }
}