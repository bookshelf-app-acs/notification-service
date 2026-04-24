package com.bookshelf.idp.notificationservice.service;

import com.bookshelf.idp.notificationservice.dto.CreateNotificationRequestDto;
import com.bookshelf.idp.notificationservice.dto.NotificationResponseDto;
import com.bookshelf.idp.notificationservice.dto.UnreadCountDto;
import com.bookshelf.idp.notificationservice.entity.Notification;
import com.bookshelf.idp.notificationservice.exception.ForbiddenException;
import com.bookshelf.idp.notificationservice.exception.NotFoundException;
import com.bookshelf.idp.notificationservice.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public NotificationResponseDto create(CreateNotificationRequestDto dto) {
        Notification notification = new Notification();
        notification.setUserEmail(dto.getUserEmail());
        notification.setType(dto.getType());
        notification.setMessage(dto.getMessage());
        notification.setBookId(dto.getBookId());
        notification.setIsRead(false);
        return toDto(notificationRepository.save(notification));
    }

    public List<NotificationResponseDto> getMyNotifications(String userEmail) {
        return notificationRepository.findByUserEmailOrderByCreatedAtDesc(userEmail)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public UnreadCountDto getMyUnreadCount(String userEmail) {
        Long count = notificationRepository.countByUserEmailAndIsReadFalse(userEmail);
        return new UnreadCountDto(count);
    }

    public NotificationResponseDto markAsRead(UUID id, String userEmail) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found"));
        if (!notification.getUserEmail().equals(userEmail)) {
            throw new ForbiddenException("You cannot modify this notification");
        }
        notification.setIsRead(true);
        return toDto(notificationRepository.save(notification));
    }

    public void delete(UUID id, String userEmail) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found"));
        if (!notification.getUserEmail().equals(userEmail)) {
            throw new ForbiddenException("You cannot delete this notification");
        }
        notificationRepository.delete(notification);
    }

    public List<NotificationResponseDto> getAll() {
        return notificationRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
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