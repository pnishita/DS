package com.dependencyresolver.service;

import com.dependencyresolver.entity.Feed;
import com.dependencyresolver.repository.FeedRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class FeedService {
    private final FeedRepo feedRepo;
    @Autowired
    public FeedService(FeedRepo feedRepo){
      this.feedRepo=feedRepo;
    }
    @Cacheable("feed")
    public Feed getFeedByName(String feedName){
        return feedRepo.findByFeedName(feedName);
    }
}
