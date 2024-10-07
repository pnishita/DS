package com.dependencyresolver.controller;
import com.dependencyresolver.dto.NotificationDTO;
import com.dependencyresolver.entity.Feed;
import com.dependencyresolver.entity.Notification;
import com.dependencyresolver.service.*;
import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;
@Slf4j
@RestController
public class NotificationReceiverController {
    private final FeedConfigService feedConfigService;
    private final NotificationReceiverService notificationReceiverService;
    private final FeedService feedService;
    private final DependencyResolver dependencyResolver;
    Gson gson = new Gson();

    @Autowired
    public NotificationReceiverController(DependencyResolver dependencyResolver,
                                          FeedConfigService feedConfigService,
                                          FeedService feedService,
                                          NotificationReceiverService notificationReceiverService) {
        this.notificationReceiverService = notificationReceiverService;
        this.feedService = feedService;
        this.feedConfigService = feedConfigService;
        this.dependencyResolver = dependencyResolver;
    }

    @PostMapping("/notification")
    public ResponseEntity<String> sendNotification(@RequestBody String receivedNotification) {
        if (ObjectUtils.isEmpty(receivedNotification)) {
            return new ResponseEntity<>("Body is Null or Empty!", HttpStatus.NO_CONTENT);
        }
        log.info("Payload received from upstream: {}", receivedNotification);
        NotificationDTO notificationDTO;
        try {
            notificationDTO = gson.fromJson(receivedNotification, NotificationDTO.class);
        } catch (JsonSyntaxException e) {
            log.error("Invalid JSON format", e);
            return new ResponseEntity<>("Invalid JSON format", HttpStatus.BAD_REQUEST);
        }
        if (notificationDTO.getCob() == null ||
                notificationDTO.getEventDateTime() == null ||
                notificationDTO.getMessage() == null ||
                notificationDTO.getAddress() == null) {
            return new ResponseEntity<>("Invalid JSON: Required fields are missing", HttpStatus.BAD_REQUEST);
        }
        Feed feed = feedService.getFeedByName(notificationDTO.getFeedName());
        if (feed == null) {
            log.error("Feed not found for the name: {}", notificationDTO.getFeedName());
            return new ResponseEntity<>("Feed not found for the provided feed name", HttpStatus.BAD_REQUEST);
        }
        if (!feedConfigService.isFeedInAnyGroup(feed.getId())) {
            log.info("Feed is not present in any group: {}", notificationDTO);
            return new ResponseEntity<>("Feed is not present in any group", HttpStatus.BAD_REQUEST);
        }
        Notification notification = NotificationBuilder.fromDTOToEntity(notificationDTO, feed);
        try {
            notificationReceiverService.saveNotification(notification);
            log.info("Notification stored in database");
            dependencyResolver.resolveFeedGroups(notification.getCob(), feed.getId());
        } catch (Exception e) {
            log.error("Error saving notification", e);
            return new ResponseEntity<>("Error saving notification", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Notification sent successfully!", HttpStatus.OK);
    }
    @GetMapping("/groups")
    public ResponseEntity<Map<Long, Set<Long>>> getFilteredGroups(@RequestParam Long currentFeedId) {
        Map<Long, Set<Long>> groups = feedConfigService.findGroupContainingFeedId(currentFeedId);
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }
}
