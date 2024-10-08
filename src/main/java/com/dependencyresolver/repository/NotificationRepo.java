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
    @Query("SELECT n FROM Notification n WHERE (n.feed.id, n.id) IN (" +
            "SELECT n.feed.id, MAX(n.id) as max_id FROM Notification n " +
            "WHERE n.feed.id IN :feedIds AND n.cob = :cob " +
            "GROUP BY n.feed.id)")
    List<Notification> findLatestNotificationsByCobAndFeedIds(@Param("cob") LocalDate cob, @Param("feedIds") Set<Long> feedIds);


}

