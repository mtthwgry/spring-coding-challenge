package com.outcomehealthtech.urlshortener.repository;

import com.outcomehealthtech.urlshortener.domain.ShortenedUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortenedUrlRepository extends JpaRepository<ShortenedUrl, Long> {

    ShortenedUrl findFirstByShortCode(String shortCode);
}
