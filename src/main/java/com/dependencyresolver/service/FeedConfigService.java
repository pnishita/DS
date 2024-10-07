package com.dependencyresolver.service;

import com.dependencyresolver.entity.FeedConfig;
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
    public Map<Long, Set<Long>> findGroupContainingFeedId(Long currentFeedId) {
        log.info("Fetching Groups");
        List<Long> groupIds = feedConfigRepo.findGroupIdsByFeedId(currentFeedId);

        return groupIds.stream()
                .collect(Collectors.toMap(
                        groupId -> groupId,
                        groupId -> feedConfigRepo.findByGroupId(groupId).stream()
                                .map(fc -> fc.getFeed().getId())
                                .collect(Collectors.toSet())
                ));
    }
    public boolean isFeedInAnyGroup(Long feedId) {
        Map<Long, Set<Long>> groups = findGroupContainingFeedId(feedId);
        return groups.values().stream()
                .anyMatch(set -> set.contains(feedId));
    }
}
