package com.home.assignment.urlshortener.job;

import com.home.assignment.urlshortener.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class UrlCleanupJob {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @Scheduled(fixedRate = 10000) // just for this task
    public void removeExpiredUrls() {
        urlShortenerService.cleanUp();
    }

}
