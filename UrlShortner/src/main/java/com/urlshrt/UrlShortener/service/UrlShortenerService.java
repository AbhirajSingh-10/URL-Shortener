package com.urlshrt.UrlShortener.service;

import com.urlshrt.UrlShortener.dto.UrlStatsResponse;
import com.urlshrt.UrlShortener.exception.UrlNotFoundException;
import com.urlshrt.UrlShortener.model.UrlMapping;
import com.urlshrt.UrlShortener.repository.UrlMappingRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UrlShortenerService {
    private final UrlMappingRepository urlMappingRepository;

    private static final String BASE62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public UrlShortenerService(UrlMappingRepository urlMappingRepository) {
        this.urlMappingRepository = urlMappingRepository;
    }


    @Transactional
    public String shortenUrl(String originalUrl){
        UrlMapping urlMapping = new UrlMapping();

        urlMapping.setOriginalUrl(originalUrl);

        urlMapping.setCreationDate(LocalDateTime.now());

        UrlMapping savedEntity =urlMappingRepository.save(urlMapping);

        String shortCode = encodeBase62(savedEntity.getId());

        savedEntity.setShortCode(shortCode);

        urlMappingRepository.save(savedEntity);

        return shortCode;

    }

    @Transactional
    public String getOriginalUrlAndIncrementClicks(String shortCode){
        UrlMapping urlMapping = urlMappingRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("Url not found with shortCode :"+shortCode));

        urlMapping.setClickCount(urlMapping.getClickCount()+1);
        urlMappingRepository.save(urlMapping);

        return urlMapping.getOriginalUrl();

    }

    public UrlStatsResponse getStats(String shortCode){
        UrlMapping urlMapping = urlMappingRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("Url not found with shortcode :"+ shortCode));

        String fullShortUrl = "http://localhost:8080/" + urlMapping.getShortCode();

        return new UrlStatsResponse(
                urlMapping.getOriginalUrl(),
                fullShortUrl,
                urlMapping.getCreationDate(),
                urlMapping.getClickCount()
        );
    }

    private String encodeBase62(Long number) {
        if(number == 0) return String.valueOf(BASE62_CHARS.charAt(0));

        StringBuilder sb = new StringBuilder();
        long num = number;
        while(num>0){
            int rem = (int)(num%62);
            sb.append(BASE62_CHARS.charAt(rem));
            num /= 62;

        }

        return sb.reverse().toString();
    }
}
