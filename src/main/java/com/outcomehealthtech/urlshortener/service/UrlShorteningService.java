package com.outcomehealthtech.urlshortener.service;

import com.outcomehealthtech.urlshortener.domain.ShortenedUrl;
import com.outcomehealthtech.urlshortener.repository.ShortenedUrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UrlShorteningService {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    private ShortenedUrlRepository repository;

    public UrlShorteningService(ShortenedUrlRepository repository) {
        this.repository = repository;
    }

    public ShortenedUrl shorten(String url) {
        String shortCode = generateUniqueShortCode();
        ShortenedUrl shortenedUrl = new ShortenedUrl.Builder()
            .originalUrl(url)
            .shortCode(shortCode)
            .build();

        return repository.save(shortenedUrl);
    }

    String generateUniqueShortCode() {
        String shortCode = newShortCode();
        log.info("Generated shortCode: {}", shortCode);

        ShortenedUrl existingRecord = repository.findFirstByShortCode(shortCode);

        if (existingRecord == null) {
            log.info("Using shortCode: {}", shortCode);
            return shortCode;
        } else {
            log.info("Regenerating, shortCode already exists: {}", shortCode);
            return generateUniqueShortCode();
        }
    }

    String newShortCode() {
        int randomNumber = new Random().nextInt(0x1000000);
        return String.format("%06x", randomNumber);
    }
}
