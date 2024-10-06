package com.dependencyresolver.controller;
import com.dependencyresolver.dto.NotificationDTO;

import com.dependencyresolver.entity.Notification;
import com.dependencyresolver.service.FeedConfigService;
import com.dependencyresolver.service.FeedService;
import com.dependencyresolver.service.NotificationReceiverService;
import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
public class NotificationRecieverController {
private final FeedConfigService feedConfigService;
    private final NotificationReceiverService notificationReceiverService;
    private final FeedService feedService;
    @Autowired
    public NotificationRecieverController(FeedConfigService feedConfigService,FeedService feedService,NotificationReceiverService notificationReceiverService){
        this.notificationReceiverService=notificationReceiverService;
        this.feedService=feedService;
        this.feedConfigService=feedConfigService;

    }
    Gson g=new Gson();
    @PostMapping("/notification")
    public ResponseEntity<String> sendNotification(@RequestBody String receivedNotification) {
        if(ObjectUtils.isEmpty(receivedNotification)){
            return new ResponseEntity<>("Body is Null or Empty!", HttpStatus.NO_CONTENT);
        }
        if (receivedNotification == null || receivedNotification.isEmpty()) {
            return new ResponseEntity<>("Body is Null or Empty!", HttpStatus.NO_CONTENT);
        }
        log.info("Payload received from upstream: {}", receivedNotification);
        NotificationDTO notificationDTO = g.fromJson(receivedNotification, NotificationDTO.class);
        if (notificationDTO.getCob() == null ||
                notificationDTO.getEventDateTime() == null ||
                notificationDTO.getMessage() == null ||
               notificationDTO.getAddress()==null) {
            return ResponseEntity.badRequest().body("Invalid JSON: Required fields are missing");
        }
        Notification notification = fromDTO(notificationDTO);
        notificationReceiverService.saveNotification(notification);
        return new ResponseEntity<>("Notification sent successfully!", HttpStatus.OK);
    }
    public Notification fromDTO(NotificationDTO notificationDTO) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime eventDateTime = LocalDateTime.parse(notificationDTO.getEventDateTime(), formatter);
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate cob = LocalDate.parse(notificationDTO.getCob(), formatter2);

        return Notification.builder()
                .cob(cob)
                .address(notificationDTO.getAddress())
                .message(notificationDTO.getMessage())
                .eventId(notificationDTO.getEventId())
                .eventDateTime(eventDateTime)
                .feed(feedService.getFeedByName(notificationDTO.getFeedName()))
                .build();
    }


}