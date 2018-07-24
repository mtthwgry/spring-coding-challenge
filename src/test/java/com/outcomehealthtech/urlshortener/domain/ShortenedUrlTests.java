package com.outcomehealthtech.urlshortener.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShortenedUrlTests {

    private String originalUrl = "http://google.com/";
    private String shortCode = "123456";
    private ShortenedUrl shortenedUrl = buildShortenedUrl();

    private ShortenedUrl buildShortenedUrl() {
        return new ShortenedUrl.Builder()
                   .originalUrl(originalUrl)
                   .shortCode(shortCode)
                   .build();
    }

    @Test
    public void builderCreatesValidShortenedUrlTest() {
        ShortenedUrl manualShortenedUrl = new ShortenedUrl();
        manualShortenedUrl.setOriginalUrl(originalUrl);
        manualShortenedUrl.setShortCode(shortCode);

        assertEquals(manualShortenedUrl, shortenedUrl);
    }

    @Test
    public void getShortenedUrlTest() {
        String shortenedUrlString = ShortenedUrl.HTTP_LOCALHOST_8080 + shortCode;

        assertEquals(shortenedUrlString, shortenedUrl.getShortenedUrl());
    }

}
