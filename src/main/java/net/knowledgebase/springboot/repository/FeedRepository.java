package net.knowledgebase.springboot.repository;

import net.knowledgebase.springboot.model.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long> {
}
