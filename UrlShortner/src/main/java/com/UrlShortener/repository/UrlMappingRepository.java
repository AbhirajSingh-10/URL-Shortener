package com.UrlShortener.repository;

import com.UrlShortener.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping,Long> {


    Optional<UrlMapping> findByShortCode(String shortCode);

    long deleteByExpirationDateBefore(LocalDateTime now);
}
