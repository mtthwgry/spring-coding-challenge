package com.outcomehealthtech.urlshortener.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShortenRequest {

    private String url;

}
