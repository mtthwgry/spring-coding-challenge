package com.outcomehealthtech.urlshortener.web;

import com.outcomehealthtech.urlshortener.domain.ShortenedUrl;
import com.outcomehealthtech.urlshortener.repository.ShortenedUrlRepository;
import com.outcomehealthtech.urlshortener.service.UrlShorteningService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UrlShortenerController.class)
public class UrlShortenerControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UrlShorteningService urlShorteningService;

    @MockBean
    private ShortenedUrlRepository shortenedUrlRepository;

    private String shortCode = "f34dc1";
    private String originalUrl = "http://google.com";

    private ShortenedUrl buildShortenedUrl() {
        return new ShortenedUrl.Builder()
                   .originalUrl(originalUrl)
                   .shortCode(shortCode)
                   .build();
    }

    @Test
    public void whenGetRoot_thenReturnString() throws Exception {
        mvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.TEXT_PLAIN))
            .andExpect(content().string("The fabulous URL shortener."));
    }

    @Test
    public void whenGetNonExistentShortCode_thenReturnNotFound() throws Exception {
        mvc.perform(get("/foo"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void whenGetValidShortCode_thenRedirectToOriginalUrl() throws Exception {
        ShortenedUrl shortenedUrl = buildShortenedUrl();

        when(shortenedUrlRepository.findFirstByShortCode(shortCode))
            .thenReturn(shortenedUrl);

        mvc.perform(get("/" + shortCode))
            .andExpect(redirectedUrl(originalUrl));
    }

    @Test
    public void whenPostShorten_thenReturnShortenedUrlJson() throws Exception {
        String requestBody = "{ \"url\": \"" + originalUrl + "\" }";
        ShortenedUrl shortenedUrl = buildShortenedUrl();

        when(urlShorteningService.shorten(originalUrl))
            .thenReturn(shortenedUrl);

        mvc.perform(post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$").exists())
            .andExpect(jsonPath("$.originalUrl").value(originalUrl))
            .andExpect(jsonPath("$.shortenedUrl").value(shortenedUrl.getShortenedUrl()));
    }

}
