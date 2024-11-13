package com.home.assignment.urlshortener.repository;

import com.home.assignment.urlshortener.model.ShortUrl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ShortUrlRepositoryTest {

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @Test
    void shouldFindUrlsWithExpiredTTL() {
        // Given
        ShortUrl expiredUrl = new ShortUrl();
        expiredUrl.setId("expired");
        expiredUrl.setOriginalUrl("https://expired.com");
        expiredUrl.setExpirationTime(LocalDateTime.now().minusDays(1));

        ShortUrl validUrl = new ShortUrl();
        validUrl.setId("valid");
        validUrl.setOriginalUrl("https://valid.com");
        validUrl.setExpirationTime(LocalDateTime.now().plusDays(1));

        shortUrlRepository.save(expiredUrl);
        shortUrlRepository.save(validUrl);

        // When
        List<ShortUrl> expiredUrls = shortUrlRepository.findAllByExpirationTimeBefore(LocalDateTime.now());

        // Then
        assertThat(expiredUrls).hasSize(1).extracting(ShortUrl::getId).containsExactly("expired");
    }

    @Test
    void shouldDeleteUrlsWithExpiredTTL() {
        // Given
        ShortUrl expiredUrl = new ShortUrl();
        expiredUrl.setId("expired");
        expiredUrl.setOriginalUrl("https://expired.com");
        expiredUrl.setExpirationTime(LocalDateTime.now().minusDays(1));

        shortUrlRepository.save(expiredUrl);

        // When
        assertThat(shortUrlRepository.deleteAllByExpirationTimeBefore(LocalDateTime.now())).isEqualTo(1);

        // Then
        assertThat(shortUrlRepository.findById("expired")).isEmpty();
    }
}
