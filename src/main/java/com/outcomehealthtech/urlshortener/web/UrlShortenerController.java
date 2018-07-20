package com.outcomehealthtech.urlshortener.web;

import com.outcomehealthtech.urlshortener.domain.RequestObject;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

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
    public String root() {
        return "The fabulous URL shortener.";
    }

    @GetMapping("/foo")
    public ResponseEntity<Object> foo(@RequestParam String key) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(key.length());
    }

    @PostMapping("/bar")
    public ResponseEntity<Object> bar(@RequestBody RequestObject requestObject) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestObject.getKey().length());
    }

    @PostMapping("/shorten")
    public ResponseEntity<ShortenedUrl> shorten(@RequestBody ShortenRequest request) {
        ShortenedUrl shortenedUrl = shorteningService.shorten(request.getUrl());

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(shortenedUrl);
    }

    @GetMapping("/{shortCode}")
    public RedirectView redirectToOriginalUrl(@PathVariable String shortCode) {
        ShortenedUrl shortenedUrl = repository.findFirstByShortCode(shortCode);
        RedirectView redirect = new RedirectView();

        if (shortenedUrl == null) {
            log.info("ShortenedUrl not found for: {}. Redirecting to root", shortCode);
            redirect.setUrl(ShortenedUrl.HTTP_LOCALHOST_8080);
        } else {
            log.info("ShortenedUrl found for: {}. Redirecting to: {}", shortCode, shortenedUrl.getOriginalUrl());
            redirect.setUrl(shortenedUrl.getOriginalUrl());
        }

        return redirect;
    }
}
