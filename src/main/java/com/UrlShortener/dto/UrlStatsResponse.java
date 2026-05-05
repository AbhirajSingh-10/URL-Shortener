package com.UrlShortener.dto;

import java.time.LocalDateTime;

public record UrlStatsResponse(
        String originalUrl,
        String shortUrl,
        LocalDateTime creationDate,
        long clickCount
) {
}
