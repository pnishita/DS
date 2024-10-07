package com.dependencyresolver.repository;

import com.dependencyresolver.entity.FeedConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedConfigRepo extends JpaRepository<FeedConfig,Long> {


    @Query("SELECT DISTINCT fc.feedGroup.groupId FROM FeedConfig fc WHERE fc.feed.id = :feedId")
    List<Long> findGroupIdsByFeedId(@Param("feedId") Long feedId);

    @Query("SELECT fc FROM FeedConfig fc WHERE fc.feedGroup.groupId = :groupId")
    List<FeedConfig> findByGroupId(@Param("groupId") Long groupId);
}
