package com.UrlShortener.service;

import com.UrlShortener.dto.UrlStatsResponse;
import com.UrlShortener.exception.AliasAlreadyExistsException;
import com.UrlShortener.exception.UrlNotFoundException;
import com.UrlShortener.model.UrlMapping;
import com.UrlShortener.repository.UrlMappingRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UrlShortenerService {
    private final UrlMappingRepository urlMappingRepository;

    private static final String BASE62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public UrlShortenerService(UrlMappingRepository urlMappingRepository) {
        this.urlMappingRepository = urlMappingRepository;
    }


    @Transactional
    public String shortenUrl(String originalUrl, String customAlias, Integer hoursToExpire){

        if(StringUtils.hasText(customAlias)){
            Optional<UrlMapping> existingMapping = urlMappingRepository.findByShortCode(customAlias);

            if(existingMapping.isPresent()){
                throw new AliasAlreadyExistsException("Alias '"+customAlias+"' is already in use.");
            }

            UrlMapping newUrlMapping = new UrlMapping();
            newUrlMapping.setOriginalUrl(originalUrl);
            newUrlMapping.setCreationDate(LocalDateTime.now());
            newUrlMapping.setShortCode(customAlias);

            if(hoursToExpire!=null){
                newUrlMapping.setExpirationDate(LocalDateTime.now().plusHours(hoursToExpire));
            }

            urlMappingRepository.save(newUrlMapping);

            return customAlias;
        }else {
            UrlMapping urlMapping = new UrlMapping();
            urlMapping.setOriginalUrl(originalUrl);
            urlMapping.setCreationDate(LocalDateTime.now());

            if(hoursToExpire!=null){
                urlMapping.setExpirationDate(LocalDateTime.now().plusHours(hoursToExpire));
            }

            UrlMapping savedEntity = urlMappingRepository.save(urlMapping);

            String shortCode = encodeBase62(savedEntity.getId());
            savedEntity.setShortCode(shortCode);

            urlMappingRepository.save(savedEntity);

            return shortCode;
        }

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

        if(urlMapping.getExpirationDate() != null && urlMapping.getExpirationDate().isBefore(LocalDateTime.now())){
            throw new UrlNotFoundException("This link is expired and no longer active.");
        }

        String fullShortUrl = "https://shrt-url.up.railway.app/" + urlMapping.getShortCode();

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
