package net.knowledgebase.springboot.web;

import net.knowledgebase.springboot.model.Audit;
import net.knowledgebase.springboot.model.Feed;
import net.knowledgebase.springboot.service.AuditService;
import net.knowledgebase.springboot.service.FeedService;
import net.knowledgebase.springboot.service.SettingsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Controller
public class FeedController {

    private FeedService feedService;
    private SettingsService settingsService;
    private AuditService auditService;

    public FeedController(FeedService feedService, SettingsService settingsService, AuditService auditService) {
        super();
        this.feedService = feedService;
        this.settingsService = settingsService;
        this.auditService = auditService;
    }

    @GetMapping("/feed")
    public String listFeed(Model model, Model settingsModel) {
        model.addAttribute("feed", feedService.getAllFeeds());
        model.addAttribute("byDateAdded", Comparator.comparing(Feed::getDateAdded).reversed());
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "feed";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/feed/new")
    public String createFeedForm(Model model, Model settingsModel) {
        Feed feed = new Feed();
        model.addAttribute("feed", feed);
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "create_feed";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/feed")
    public String saveFeed(@ModelAttribute("feed") Feed feed) {
        try {
            feed.setDateAdded(new Date());
            feedService.saveFeed(feed);
            Audit audit = new Audit("New feed created. Subject: " + feed.getSubject());
            auditService.saveAudit(audit);
            return "redirect:/feed?success";
        } catch (Exception e) {
            return "redirect:/feed?fail";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/feed/edit/{id}")
    public String editDocumentForm(@PathVariable Long id, Model model, Model settingsModel) {
        model.addAttribute("feed", feedService.getFeedById(id));
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "edit_feed";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/feed/edit/{id}")
    public String updateFeed(@PathVariable Long id, @ModelAttribute("feed") Feed feed, Model model) {
        try {
            Feed existingFeed = feedService.getFeedById(id);
            existingFeed.setId(feed.getId());
            existingFeed.setSubject(feed.getSubject());
            existingFeed.setContent(feed.getContent());
            feedService.updateFeed(existingFeed);
            Audit audit = new Audit("Feed updated. Subject: " + feed.getSubject());
            auditService.saveAudit(audit);
            return "redirect:/feed?success";
        } catch (Exception e) {
            return "redirect:/feed?fail";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/feed/{id}")
    public String deleteFeed(@PathVariable Long id) {
        Feed existingFeed = feedService.getFeedById(id);
        feedService.deleteFeedById(id);
        Audit audit = new Audit("Feed deleted. Subject: " + existingFeed.getSubject());
        auditService.saveAudit(audit);
        return "redirect:/feed";
    }

    @GetMapping("/feed/view_feed/{id}")
    public String viewFeedForm(@PathVariable Long id, Model model, Model settingsModel) {
        model.addAttribute("feed", feedService.getFeedById(id));
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "view_feed";
    }
}
