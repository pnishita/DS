package com.dependencyresolver.repository;

import com.dependencyresolver.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepo extends JpaRepository<Feed,Long> {
    Feed findByFeedName(String feedName);
}
