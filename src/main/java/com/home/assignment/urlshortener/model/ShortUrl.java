package com.home.assignment.urlshortener.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class ShortUrl {

    @Id
    private String id;

    @Column(nullable = false)
    private String originalUrl;

    private LocalDateTime expirationTime;

    public ShortUrl() {
        //for jpa
    }

    public ShortUrl(String id, String originalUrl, LocalDateTime expirationTime) {
        this.id = id;
        this.originalUrl = originalUrl;
        this.expirationTime = expirationTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }
}
