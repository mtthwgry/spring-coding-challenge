package com.outcomehealthtech.urlshortener.service;

import com.outcomehealthtech.urlshortener.domain.ShortenedUrl;
import com.outcomehealthtech.urlshortener.repository.ShortenedUrlRepository;
import org.hibernate.service.spi.InjectService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UrlShorteningServiceTests {

    @Spy
    private ShortenedUrlRepository shortenedUrlRepository;

    @Spy
    @InjectMocks
    private UrlShorteningService service;

    private String anotherShortCode = "ce9a01";
    private String shortCode = "ff49de";
    private String url = "http://google.com";

    @Test
    public void shorten_savesTheShortenedUrl() {
        doReturn(shortCode).when(service).generateUniqueShortCode();

        service.shorten(url);

        ShortenedUrl shortenedUrl = new ShortenedUrl.Builder()
            .originalUrl(url)
            .shortCode(shortCode)
            .build();

        verify(shortenedUrlRepository, times(1)).save(shortenedUrl);
    }

    @Test
    public void shorten_whenShortCodeExists_SavesUniqueShortCode() {
        doReturn(shortCode, anotherShortCode).when(service).newShortCode();
        doReturn(mock(ShortenedUrl.class)).when(shortenedUrlRepository).findFirstByShortCode(shortCode);

        service.shorten(url);

        ShortenedUrl anotherShortenedUrl = new ShortenedUrl.Builder()
            .originalUrl(url)
            .shortCode(anotherShortCode)
            .build();

        verify(shortenedUrlRepository, times(1)).save(anotherShortenedUrl);
    }

    @Test
    public void generateUniqueShortCode_returnsUniqueShortCode() {
        doReturn(shortCode, anotherShortCode).when(service).newShortCode();
        doReturn(mock(ShortenedUrl.class)).when(shortenedUrlRepository).findFirstByShortCode(shortCode);

        assertEquals(anotherShortCode, service.generateUniqueShortCode());
    }
}
