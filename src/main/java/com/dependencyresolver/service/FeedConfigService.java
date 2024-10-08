package com.dependencyresolver.service;

import com.dependencyresolver.entity.Feed;
import com.dependencyresolver.entity.FeedGroup;
import com.dependencyresolver.repository.FeedConfigRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
@Slf4j
@Service
public class FeedConfigService {
    private final FeedConfigRepo feedConfigRepo;
    public FeedConfigService(FeedConfigRepo feedConfigRepo) {
        this.feedConfigRepo = feedConfigRepo;
    }
    @Cacheable("groups")
    public Map<String, Set<Long>> findGroupContainingFeedId(String  currentFeedName) {
        log.info("Fetching Groups");
        List<FeedGroup> groups = feedConfigRepo.findGroupsByFeedName(currentFeedName);
        return groups.stream()
                .collect(Collectors.toMap(
                        FeedGroup::getGroupName,
                        group -> feedConfigRepo.findFeedsInGroup(group.getGroupName()).stream()
                                .map(fc -> fc.getFeed().getId())
                                .collect(Collectors.toSet())
                ));
    }
    public boolean isFeedInAnyGroup(Feed feed) {
        return findGroupContainingFeedId(feed.getFeedName()).values().stream()
                .anyMatch(set -> set.contains(feed.getId()));
    }
}
