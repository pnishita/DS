package com.dependencyresolver.service;

import com.dependencyresolver.dto.NotificationDTO;
import com.dependencyresolver.entity.Feed;
import com.dependencyresolver.entity.Notification;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificationBuilder {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static Notification fromDTOToEntity(NotificationDTO notificationDTO, Feed feed) {
        LocalDateTime eventDateTime = LocalDateTime.parse(notificationDTO.getEventDateTime(), formatter);
        LocalDate cob = LocalDate.parse(notificationDTO.getCob(), formatter2);

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
