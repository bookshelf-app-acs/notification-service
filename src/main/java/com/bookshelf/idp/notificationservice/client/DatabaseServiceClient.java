package com.bookshelf.idp.notificationservice.client;

import com.bookshelf.idp.notificationservice.entity.Notification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class DatabaseServiceClient {

    private final RestTemplate restTemplate;

    @Value("${database.service.url}")
    private String dbUrl;

    public DatabaseServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Notification save(Notification notification) {
        return restTemplate.postForObject(dbUrl + "/internal/notifications", notification, Notification.class);
    }

    public List<Notification> findByUserEmail(String email) {
        Notification[] notifications = restTemplate.getForObject(
                dbUrl + "/internal/notifications/user/" + email, Notification[].class);
        return notifications != null ? Arrays.asList(notifications) : List.of();
    }

    public Long countUnread(String email) {
        return restTemplate.getForObject(
                dbUrl + "/internal/notifications/user/" + email + "/unread-count", Long.class);
    }

    public Optional<Notification> findById(UUID id) {
        try {
            return Optional.ofNullable(restTemplate.getForObject(
                    dbUrl + "/internal/notifications/" + id, Notification.class));
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

    public Notification markAsRead(UUID id) {
        ResponseEntity<Notification> response = restTemplate.exchange(
                dbUrl + "/internal/notifications/" + id + "/read",
                HttpMethod.PATCH, null, Notification.class);
        return response.getBody();
    }

    public void delete(UUID id) {
        restTemplate.delete(dbUrl + "/internal/notifications/" + id);
    }

    public List<Notification> findAll() {
        Notification[] notifications = restTemplate.getForObject(
                dbUrl + "/internal/notifications", Notification[].class);
        return notifications != null ? Arrays.asList(notifications) : List.of();
    }
}