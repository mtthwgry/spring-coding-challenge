package com.outcomehealthtech.urlshortener.web;

import com.outcomehealthtech.urlshortener.domain.RequestObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UrlShortenerController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

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
}
