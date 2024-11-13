package com.home.assignment.urlshortener.controller;

import com.home.assignment.urlshortener.dto.UrlShortenRequest;
import com.home.assignment.urlshortener.error.NoSuchEntityException;
import com.home.assignment.urlshortener.model.ShortUrl;
import com.home.assignment.urlshortener.service.UrlShortenerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UrlShortenerController.class)
class UrlShortenerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlShortenerService urlShortenerService;

    private static final String PATH = "/api/v1/urls";

    @BeforeEach
    void setUp() {
        reset(urlShortenerService);
    }

    @Test
    void createShortUrl_validRequest_returnsCreatedResponse() throws Exception {
        String id = "abc123";

        when(urlShortenerService.createShortUrl(any(UrlShortenRequest.class))).thenReturn(id);

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"originalUrl\": \"http://example.com\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortUrl").value("http://localhost/api/v1/urls/abc123"));
    }

    @Test
    void createShortUrl_invalidRequest_returnsBadRequest() throws Exception {
        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"originalUrl\": \"\"}")) // Invalid: originalUrl is blank
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("/api/v1/errors/2"))
                .andExpect(jsonPath("$.title").value("Bad request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Invalid request"))
                .andExpect(jsonPath("$.instance").value(PATH))
                .andExpect(jsonPath("$.errorCode").value(2))
                .andExpect(jsonPath("$.arguments.originalUrl").value("Original URL cannot be blank"));
    }


    @Test
    void redirectToOriginalUrl_validId_returnsRedirect() throws Exception {
        String id = "abc123";
        String originalUrl = "http://example.com";
        ShortUrl shortUrl = new ShortUrl(id, originalUrl, null);

        when(urlShortenerService.getShortUrl(id)).thenReturn(shortUrl);

        mockMvc.perform(get(PATH + "/{id}", id))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(header().string("Location", originalUrl));
    }

    @Test
    void redirectToOriginalUrl_invalidId_returnsNotFound() throws Exception {
        String id = "invalid123";

        when(urlShortenerService.getShortUrl(id)).thenThrow(new NoSuchEntityException("ShortUrl", id));

        mockMvc.perform(get(PATH + "/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value("/api/v1/errors/1"))
                .andExpect(jsonPath("$.title").value("Missing entity"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("The operation could not be completed because one of the core entities was not recognised"))
                .andExpect(jsonPath("$.instance").value(PATH + "/" + id))
                .andExpect(jsonPath("$.errorCode").value(1))
                .andExpect(jsonPath("$.arguments.id").value("invalid123"))
                .andExpect(jsonPath("$.arguments.entityType").value("ShortUrl"));
    }


    @Test
    void deleteShortUrl_validId_returnsNoContent() throws Exception {
        String id = "abc123";

        doNothing().when(urlShortenerService).deleteShortUrl(id);

        mockMvc.perform(delete(PATH + "/{id}", id))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteShortUrl_invalidId_returnsNotFound() throws Exception {
        String id = "invalid123";

        // Simulate exception thrown by the service
        doThrow(new NoSuchEntityException("ShortUrl", id)).when(urlShortenerService).deleteShortUrl(id);

        mockMvc.perform(delete(PATH + "/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value("/api/v1/errors/1"))
                .andExpect(jsonPath("$.title").value("Missing entity"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("The operation could not be completed because one of the core entities was not recognised"))
                .andExpect(jsonPath("$.instance").value(PATH + "/" + id))
                .andExpect(jsonPath("$.errorCode").value(1))
                .andExpect(jsonPath("$.arguments.id").value("invalid123"))
                .andExpect(jsonPath("$.arguments.entityType").value("ShortUrl"));
    }

}
