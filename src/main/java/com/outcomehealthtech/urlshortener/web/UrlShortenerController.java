package com.outcomehealthtech.urlshortener.web;

import com.outcomehealthtech.urlshortener.domain.ShortenRequest;
import com.outcomehealthtech.urlshortener.domain.ShortenedUrl;
import com.outcomehealthtech.urlshortener.repository.ShortenedUrlRepository;
import com.outcomehealthtech.urlshortener.service.UrlShorteningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

@RestController
public class UrlShortenerController {

    private final UrlShorteningService shorteningService;
    private final ShortenedUrlRepository repository;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public UrlShortenerController(UrlShorteningService shorteningService, ShortenedUrlRepository repository) {
        this.shorteningService = shorteningService;
        this.repository = repository;
    }

    @GetMapping("/")
    public ResponseEntity<String> root() {
        return ResponseEntity.ok()
            .contentType(MediaType.TEXT_PLAIN)
            .body("The fabulous URL shortener.");
    }

    @PostMapping("/shorten")
    public ResponseEntity<ShortenedUrl> shorten(@RequestBody ShortenRequest requestBody) {
        ShortenedUrl shortenedUrl = shorteningService.shorten(requestBody.getUrl());

        return ResponseEntity.created(URI.create(shortenedUrl.getShortenedUrl()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(shortenedUrl);
    }

    @GetMapping("/{shortCode}")
    public void redirectToOriginalUrl( @PathVariable String shortCode, HttpServletResponse response) throws IOException {
        ShortenedUrl shortenedUrl = repository.findFirstByShortCode(shortCode);

        if (shortenedUrl == null) {
            log.info("ShortenedUrl not found for: {}", shortCode);
            response.sendError(404, "Short code \"" + shortCode + "\" not found.");
        } else {
            log.info("ShortenedUrl found for: {}. Redirecting to: {}", shortCode, shortenedUrl.getOriginalUrl());
            response.sendRedirect(shortenedUrl.getOriginalUrl());
        }
    }
}
