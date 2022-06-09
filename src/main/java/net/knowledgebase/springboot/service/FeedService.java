package net.knowledgebase.springboot.service;

import net.knowledgebase.springboot.model.Feed;

import java.util.List;

public interface FeedService {
    List<Feed> getAllFeeds();

    Feed saveFeed(Feed feed);

    Feed getFeedById(Long id);

    Feed updateFeed(Feed feed);

    void deleteFeedById(Long id);

}
