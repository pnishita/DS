package com.dependencyresolver.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
@Slf4j
@Service
public class DependencyResolver {
    private static final Logger logger = LoggerFactory.getLogger(DependencyResolver.class);
    private final FeedConfigService feedConfigService;
    private final NotificationReceiverService notificationReceiverService;
    private final Map<Long, Integer> groupVersions = new HashMap<>();

    @Autowired
    public DependencyResolver(FeedConfigService feedConfigService,NotificationReceiverService notificationReceiverService){
        this.feedConfigService=feedConfigService;
        this.notificationReceiverService=notificationReceiverService;
    }
    public void resolveFeedGroups(LocalDate cob, long currentFeedId) {
        Map<Long, Set<Long>> groups = feedConfigService.findGroupContainingFeedId(currentFeedId);
        log.info("Fetching Groups :{}" ,groups);

        Set<Long> feedIdList = getFeedIdsFromNotifications(cob);
        log.info("Fetching List Of feed IDs :{}" ,feedIdList);

        groups.forEach((groupId, groupFeedIds) -> {
            if (feedIdList.containsAll(groupFeedIds)) {
                groupVersions.merge(groupId, 1, (oldValue, newValue) -> groupFeedIds.contains(currentFeedId) ? oldValue + 1 : oldValue);
                logger.info("Group {} resolved: version {}", groupId, groupVersions.get(groupId));
            }
        });
        printResolvedGroups();
    }

    private Set<Long> getFeedIdsFromNotifications(LocalDate cob) {
        return notificationReceiverService.findLatestNotificationsByCob(cob).stream()
                .map(notification -> notification.getFeed().getId())
                .collect(Collectors.toSet());
    }

    private void printResolvedGroups() {
        logger.info("Resolved Groups:");
        groupVersions.forEach((groupId, version) ->
                logger.info("[Group {} - version: {}]", groupId, version)
        );
    }
}