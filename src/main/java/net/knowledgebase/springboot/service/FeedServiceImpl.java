package net.knowledgebase.springboot.service;

import net.knowledgebase.springboot.model.Feed;
import net.knowledgebase.springboot.repository.FeedRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedServiceImpl implements FeedService{

    private FeedRepository feedRepository;

    public FeedServiceImpl(FeedRepository feedRepository) {
        super();
        this.feedRepository = feedRepository;
    }

    @Override
    public List<Feed> getAllFeeds() {
        return feedRepository.findAll();
    }

    @Override
    public Feed saveFeed(Feed feed) {
        return feedRepository.save(feed);
    }

    @Override
    public Feed getFeedById(Long id) {
        return feedRepository.findById(id).get();
    }

    @Override
    public Feed updateFeed(Feed feed) {
        return feedRepository.save(feed);
    }

    @Override
    public void deleteFeedById(Long id) {
        feedRepository.deleteById(id);
    }
}
