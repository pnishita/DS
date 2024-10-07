package com.dependencyresolver.repository;

import com.dependencyresolver.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface NotificationRepo extends JpaRepository<Notification,Long> {
//    @Query("SELECT n FROM Notification n WHERE n.id IN (" +
//            "SELECT MAX(n.id) FROM Notification n WHERE n.cob = :cob AND n.feed.id IN (" +
//            "SELECT fc.feed.id FROM FeedConfig fc WHERE fc.feedGroup.groupId = :groupId) " +
//            "GROUP BY n.feed.id)")
//    List<Notification> findLatestNotificationsByGroupAndCob(@Param("groupId") Long groupId, @Param("cob") LocalDate cob);
    @Query("SELECT n FROM Notification n WHERE n.id IN (" +
            "SELECT MAX(n.id) FROM Notification n WHERE n.cob = :cob AND n.feed.id IN (" +
            "SELECT DISTINCT fc.feed.id FROM FeedConfig fc) " +
            "GROUP BY n.feed.id)")
    List<Notification> findLatestNotificationsByCob(@Param("cob") LocalDate cob);


    @Query("SELECT n FROM Notification n WHERE n.cob = :cob AND n.feed.id IN :feedGroupIds")
    List<Notification> findLatestNotificationsByCobAndFeedIds(@Param("cob") LocalDate cob, @Param("feedGroupIds") Set<Long> feedGroupIds);

}

