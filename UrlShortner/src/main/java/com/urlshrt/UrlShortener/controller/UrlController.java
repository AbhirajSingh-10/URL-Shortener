package com.urlshrt.UrlShortener.controller;

import com.urlshrt.UrlShortener.dto.ShortenUrlRequest;
import com.urlshrt.UrlShortener.dto.ShortenUrlResponse;
import com.urlshrt.UrlShortener.dto.UrlStatsResponse;
import com.urlshrt.UrlShortener.service.UrlShortenerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class UrlController {

    private final UrlShortenerService urlShortenerService;

    public UrlController(UrlShortenerService urlShortenerService){
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping("api/v1/url/shorten")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@Valid @RequestBody ShortenUrlRequest request){
        String shortCode = urlShortenerService.shortenUrl(request.url());

        String fullShortUrl = "http://localhost:8080/"+shortCode;


        ShortenUrlResponse response = new ShortenUrlResponse(fullShortUrl);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode){
        String originalUrl = urlShortenerService.getOriginalUrlAndIncrementClicks(shortCode);

//        HttpHeaders headers = new HttpHeaders();
//
//        headers.setLocation(URI.create(originalUrl));
//
//        return new ResponseEntity<>(headers,HttpStatus.FOUND);

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(originalUrl)).build();
    }


    @GetMapping("api/v1/url/{shortCode}")
    public ResponseEntity<UrlStatsResponse> getUrlStats(@PathVariable String shortCode){
        return ResponseEntity.status(HttpStatus.FOUND).body(urlShortenerService.getStats(shortCode));
    }

}
