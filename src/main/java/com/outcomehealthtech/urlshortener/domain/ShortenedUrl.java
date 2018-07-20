package com.outcomehealthtech.urlshortener.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Entity
@NoArgsConstructor
public class ShortenedUrl {

    public static final String HTTP_LOCALHOST_8080 = "http://localhost:8080/";

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @NotNull
    private String originalUrl;

    @NotNull
    @JsonIgnore
    private String shortCode;

    @JsonProperty
    private String shortenedUrl() {
        return getShortenedUrl();
    }

    public ShortenedUrl(String originalUrl, String shortCode) {
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
    }

    public String getShortenedUrl() {
        return HTTP_LOCALHOST_8080 + shortCode;
    }

    public static class Builder {

        private String originalUrl;
        private String shortCode;

        public ShortenedUrl build() {
            return new ShortenedUrl(originalUrl, shortCode);
        }

        public Builder originalUrl(String url) {
            this.originalUrl = url;
            return this;
        }

        public Builder shortCode(String code) {
            this.shortCode = code;
            return this;
        }
    }
}
