package com.dependencyresolver.service;

import com.dependencyresolver.dto.NotificationDTO;
import com.dependencyresolver.entity.Feed;
import com.dependencyresolver.entity.Notification;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class NotificationBuilder {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private NotificationBuilder() { }
    public static Notification fromDTOToEntity(NotificationDTO notificationDTO, Feed feed) {
        if (notificationDTO == null || feed == null) {
            throw new IllegalArgumentException("NotificationDTO and Feed must not be null");
        }

        LocalDateTime eventDateTime;
        LocalDate cob;

        try {
            eventDateTime = LocalDateTime.parse(notificationDTO.getEventDateTime(), formatter);
            cob = LocalDate.parse(notificationDTO.getCob(), formatter2);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format", e);
        }

        return Notification.builder()
                .cob(cob)
                .address(notificationDTO.getAddress())
                .message(notificationDTO.getMessage())
                .eventId(notificationDTO.getEventId())
                .eventDateTime(eventDateTime)
                .feed(feed)
                .build();
    }
}
