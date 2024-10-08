package com.dependencyresolver.service;

import com.dependencyresolver.entity.Notification;
import com.dependencyresolver.repository.NotificationRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class NotificationReceiverService {
    private final NotificationRepo notificationRepo;
    @Autowired
    public NotificationReceiverService(NotificationRepo notificationRepo){
        this.notificationRepo=notificationRepo;
    }
    public void saveNotification(Notification notification){
        notificationRepo.save(notification);
    }

    public List<Notification> findLatestNotificationsByCobAndFeedGroup(LocalDate cob, Set<Long> feedIds) {
        return notificationRepo.findLatestNotificationsByCobAndFeedIds(cob, feedIds);
    }
}
