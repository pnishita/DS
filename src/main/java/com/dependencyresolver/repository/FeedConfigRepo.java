package com.dependencyresolver.repository;

import com.dependencyresolver.entity.FeedConfig;
import com.dependencyresolver.entity.FeedGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedConfigRepo extends JpaRepository<FeedConfig,Long> {
    //feed present in which groups
    // Return Group name
    @Query("SELECT fg FROM FeedConfig fc " +
            "JOIN fc.feed f " +
            "JOIN fc.feedGroup fg " +
            "WHERE f.feedName = :feedName")
    List<FeedGroup> findGroupsByFeedName(@Param("feedName") String feedName);


    //All feeds Present in that group
    @Query("SELECT fc FROM FeedConfig fc WHERE fc.feedGroup.groupName = :groupName")
    List<FeedConfig> findFeedsInGroup(@Param("groupName") String groupName);
}
