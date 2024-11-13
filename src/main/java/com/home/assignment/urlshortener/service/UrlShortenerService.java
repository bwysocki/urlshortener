package com.home.assignment.urlshortener.service;

import com.home.assignment.urlshortener.dto.UrlShortenRequest;
import com.home.assignment.urlshortener.error.ConflictException;
import com.home.assignment.urlshortener.error.NoSuchEntityException;
import com.home.assignment.urlshortener.model.ShortUrl;
import com.home.assignment.urlshortener.repository.ShortUrlRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class UrlShortenerService {

    private static final Logger logger = LoggerFactory.getLogger(UrlShortenerService.class);

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    public String createShortUrl(UrlShortenRequest request) {
        logger.debug("Processing createShortUrl request: {}", request);

        String id;

        // Use the custom ID if provided, otherwise generate a unique one
        if (request.customId() != null) {
            id = request.customId();

            // Check if the custom ID already exists
            if (shortUrlRepository.existsById(id)) {
                logger.warn("Conflict: Short URL with ID '{}' already exists.", id);
                throw new ConflictException(ShortUrl.class.getSimpleName(), id);
            }
            logger.debug("Using custom ID for Short URL: {}", id);
        } else {
            id = generateUniqueId();
            logger.debug("Generated unique ID for Short URL: {}", id);
        }

        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setId(id);
        shortUrl.setOriginalUrl(request.originalUrl());

        if (request.ttl() != null) {
            shortUrl.setExpirationTime(LocalDateTime.now().plusSeconds(request.ttl()));
            logger.debug("Set TTL for Short URL: {}", shortUrl.getExpirationTime());
        }

        shortUrlRepository.save(shortUrl);
        logger.info("Short URL created successfully with ID: {}", id);

        return id;
    }

    public ShortUrl getShortUrl(String id) {
        logger.debug("Fetching Short URL with ID: {}", id);

        ShortUrl shortUrl = shortUrlRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Short URL with ID '{}' not found.", id);
                    return new NoSuchEntityException(ShortUrl.class.getSimpleName(), id);
                });

        if (shortUrl.getExpirationTime() != null && shortUrl.getExpirationTime().isBefore(LocalDateTime.now())) {
            logger.warn("Short URL with ID '{}' has expired.", id);
            throw new NoSuchEntityException(ShortUrl.class.getSimpleName(), id);
        }

        logger.info("Successfully retrieved Short URL with ID: {}", id);
        return shortUrl;
    }

    public void deleteShortUrl(String id) {
        logger.debug("Attempting to delete Short URL with ID: {}", id);
        if (!shortUrlRepository.existsById(id)) {
            logger.warn("Short URL with ID '{}' does not exist.", id);
            throw new NoSuchEntityException(ShortUrl.class.getSimpleName(), id);
        }
        shortUrlRepository.deleteById(id);
        logger.info("Successfully deleted Short URL with ID: {}", id);
    }

    public void cleanUp() {
        logger.debug("Starting cleanup of expired Short URLs.");
        int deletedCount = shortUrlRepository.deleteAllByExpirationTimeBefore(LocalDateTime.now());
        if (deletedCount > 0) {
            logger.info("Deleted {} expired URLs.", deletedCount);
        } else {
            logger.debug("No expired URLs found for deletion.");
        }
    }

    private String generateUniqueId() {
        return UUID.randomUUID().toString().substring(0, 8); //its dummy way of generating unique id -> just for this assignment
    }


}
