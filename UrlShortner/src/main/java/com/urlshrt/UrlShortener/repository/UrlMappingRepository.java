package com.urlshrt.UrlShortener.repository;

import com.urlshrt.UrlShortener.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping,Long> {


    Optional<UrlMapping> findByShortCode(String shortCode);
}
