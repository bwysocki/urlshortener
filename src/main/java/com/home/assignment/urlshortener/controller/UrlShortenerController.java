package com.home.assignment.urlshortener.controller;

import com.home.assignment.urlshortener.dto.UrlShortenRequest;
import com.home.assignment.urlshortener.dto.UrlShortenResponse;
import com.home.assignment.urlshortener.service.UrlShortenerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(UrlShortenerController.PATH)
@Tag(name = "URL Shortener", description = "API for shortening URLs")
public class UrlShortenerController {

    public static final String PATH = "/api/v1/urls";

    @Autowired
    private UrlShortenerService urlShortenerService;

    @Operation(summary = "Create a short URL")
    @PostMapping
    public ResponseEntity<UrlShortenResponse> createShortUrl(@Valid @RequestBody UrlShortenRequest request) {
        String id = urlShortenerService.createShortUrl(request);
        String shortUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(PATH + "/{id}")
                .buildAndExpand(id)
                .toUriString();

        return ResponseEntity.status(HttpStatus.CREATED).body(new UrlShortenResponse(shortUrl));
    }

    @Operation(summary = "Redirect to the original URL")
    @GetMapping("/{id}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String id) {
        var shortUrl = urlShortenerService.getShortUrl(id);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(shortUrl.getOriginalUrl())).build();
    }

    @Operation(summary = "Delete a short URL")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShortUrl(@PathVariable String id) {
        urlShortenerService.deleteShortUrl(id);
        return ResponseEntity.noContent().build();
    }
}
