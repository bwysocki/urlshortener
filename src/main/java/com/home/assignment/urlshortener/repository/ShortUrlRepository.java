package com.home.assignment.urlshortener.repository;

import com.home.assignment.urlshortener.model.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, String> {

    List<ShortUrl> findAllByExpirationTimeBefore(LocalDateTime now);

    int deleteAllByExpirationTimeBefore(LocalDateTime now);;
}
