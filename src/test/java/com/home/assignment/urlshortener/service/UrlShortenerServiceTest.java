package com.home.assignment.urlshortener.service;

import com.home.assignment.urlshortener.dto.UrlShortenRequest;
import com.home.assignment.urlshortener.error.ConflictException;
import com.home.assignment.urlshortener.error.NoSuchEntityException;
import com.home.assignment.urlshortener.model.ShortUrl;
import com.home.assignment.urlshortener.repository.ShortUrlRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
public class UrlShortenerServiceTest {

    @Autowired
    private UrlShortenerService shortUrlService;

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @Test
    void createShortUrl_withCustomId_savesSuccessfully() {
        UrlShortenRequest request = new UrlShortenRequest("http://example.com", "custom123", null);

        String result = shortUrlService.createShortUrl(request);

        assertThat(result).isEqualTo("custom123");
        ShortUrl savedUrl = shortUrlRepository.findById("custom123").orElseThrow();
        assertThat(savedUrl.getOriginalUrl()).isEqualTo("http://example.com");
    }

    @Test
    void createShortUrl_withGeneratedId_savesSuccessfully() {
        UrlShortenRequest request = new UrlShortenRequest("http://example.com", null, null);

        String result = shortUrlService.createShortUrl(request);

        assertThat(result).isNotNull();
        ShortUrl savedUrl = shortUrlRepository.findById(result).orElseThrow();
        assertThat(savedUrl.getOriginalUrl()).isEqualTo("http://example.com");
    }

    @Test
    void createShortUrl_withExistingCustomId_throwsConflictException() {
        shortUrlRepository.save(new ShortUrl("custom12", "http://example.com", null));

        UrlShortenRequest request = new UrlShortenRequest("http://another.com", "custom12", null);

        assertThatThrownBy(() -> shortUrlService.createShortUrl(request))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("custom12");
    }

    @Test
    void getShortUrl_whenExists_returnsShortUrl() {
        shortUrlRepository.save(new ShortUrl("test123", "http://example.com", null));

        ShortUrl result = shortUrlService.getShortUrl("test123");

        assertThat(result.getId()).isEqualTo("test123");
        assertThat(result.getOriginalUrl()).isEqualTo("http://example.com");
    }

    @Test
    void getShortUrl_whenExpired_throwsNoSuchEntityException() {
        shortUrlRepository.save(new ShortUrl("test123", "http://example.com", LocalDateTime.now().minusDays(1)));

        assertThatThrownBy(() -> shortUrlService.getShortUrl("test123"))
                .isInstanceOf(NoSuchEntityException.class)
                .hasMessageContaining("test123");
    }

    @Test
    void deleteShortUrl_whenExists_deletesSuccessfully() {
        shortUrlRepository.save(new ShortUrl("test123", "http://example.com", null));

        shortUrlService.deleteShortUrl("test123");

        assertThat(shortUrlRepository.existsById("test123")).isFalse();
    }

    @Test
    void cleanUp_removesExpiredUrls() {
        shortUrlRepository.save(new ShortUrl("expired1", "http://example.com", LocalDateTime.now().minusDays(1)));
        shortUrlRepository.save(new ShortUrl("valid1", "http://example.com", LocalDateTime.now().plusDays(1)));

        shortUrlService.cleanUp();

        assertThat(shortUrlRepository.existsById("expired1")).isFalse();
        assertThat(shortUrlRepository.existsById("valid1")).isTrue();
    }

}
