package com.dependencyresolver.service;

import com.dependencyresolver.dto.NotificationResponse;
import com.dependencyresolver.dto.ResolvedGroupDTO;
import com.dependencyresolver.entity.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
@Slf4j
@Service
public class DependencyResolver {
    private final NotificationReceiverService notificationReceiverService;
    private final ResponseSender responseSender;
    @Autowired
    public DependencyResolver(NotificationReceiverService notificationReceiverService,
                              ResponseSender responseSender) {
        this.notificationReceiverService = notificationReceiverService;
        this.responseSender=responseSender;
    }
    public void resolveFeedGroups(LocalDate cob, Map<String, Set<Long>> groups) {
        log.info("Fetching Groups :{}", groups);

        List<ResolvedGroupDTO> resolvedGroupDTOS=new ArrayList<>();
        groups.forEach((groupName, groupFeedIds) -> {
            List<Notification> latestNotifications=notificationReceiverService.findLatestNotificationsByCobAndFeedGroup(cob,groupFeedIds);
            log.info("List of Notifications in group {}:{}", groupName, latestNotifications);
            Set<Long> notificationFeedIds = latestNotifications.stream()
                    .map(notification -> notification.getFeed().getId())
                    .collect(Collectors.toSet());
            boolean allFeedIdsPresent = groupFeedIds.stream()
                    .allMatch(notificationFeedIds::contains);
            if (allFeedIdsPresent) {
                log.info("All groupFeedIds are present in the latestNotifications for group {}.", groupName);
                // Convert Notifications to NotificationResponse
                NotificationResponse[] notificationResponses = latestNotifications.stream()
                        .map(notification -> new NotificationResponse(notification.getFeed().getFeedName(),
                                notification.getMessage(), notification.getAddress()))
                        .toArray(NotificationResponse[]::new);
                // Build the Response
                ResolvedGroupDTO resolvedGroupDTO = ResolvedGroupDTO.builder()
                        .cob(cob)
                        .groupId(groupName)
                        .inboundFeeds(notificationResponses)
                        .build();
                resolvedGroupDTOS.add(resolvedGroupDTO);
                responseSender.sendResolvedGroups(resolvedGroupDTOS);
                // Log the Response
                log.info("Resolved Group Response: {}", resolvedGroupDTO);
            } else {
                log.warn("Not all groupFeedIds are present in the latestNotifications for group {}.", groupName);
            }
        });
    }
}