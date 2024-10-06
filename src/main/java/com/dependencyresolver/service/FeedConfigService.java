package com.dependencyresolver.service;

import com.dependencyresolver.repository.FeedConfigRepo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FeedConfigService {
    private final FeedConfigRepo feedConfigRepo;
    public FeedConfigService(FeedConfigRepo feedConfigRepo){
        this.feedConfigRepo=feedConfigRepo;
    }

    @Cacheable("groups")
    public Map<String, Set<String>> getAllGroups(){
        return feedConfigRepo.findAll().stream()
                .collect(Collectors.groupingBy(
                        feedConfig -> feedConfig.getFeedGroup().getGroupName(),
                        Collectors.mapping(feedConfig -> feedConfig.getFeed().getFeedName(), Collectors.toSet())
                ));
    }

    public Set<String> getFeedsInFeedGroup() {
        return getAllGroups().values().stream()
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

}
