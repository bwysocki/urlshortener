package com.home.assignment.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UrlShortenRequest(
        @NotBlank(message = "Original URL cannot be blank") String originalUrl,
        @Size(max = 8, message = "Custom ID cannot exceed 8 characters") String customId,
        @Positive(message = "TTL (in seconds) must be a positive value") Long ttl
) {
}
