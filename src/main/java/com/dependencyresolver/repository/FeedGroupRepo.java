package com.dependencyresolver.repository;

import com.dependencyresolver.entity.FeedGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedGroupRepo extends JpaRepository<FeedGroup,Long> {
}
