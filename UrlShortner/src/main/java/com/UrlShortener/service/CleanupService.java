package com.UrlShortener.service;

import com.UrlShortener.repository.UrlMappingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CleanupService {

    private static final Logger logger = LoggerFactory.getLogger(CleanupService.class);

    private final UrlMappingRepository urlMappingRepository;

    public CleanupService(UrlMappingRepository urlMappingRepository){
        this.urlMappingRepository = urlMappingRepository;
    }

    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    public void cleanupExpiredUrls(){
        logger.info("Starting scheduled job : Cleaning up expired URL Mappings...");

        LocalDateTime now = LocalDateTime.now();

        long deletedCount = urlMappingRepository.deleteByExpirationDateBefore(now);

        if(deletedCount>0){
            logger.info("Finished scheduled job : Successfully deleted {} expired URL mappings.", deletedCount);
        }else
            logger.info("Finished scheduled job : No expired URL mapping found to delete.");

    }
}
