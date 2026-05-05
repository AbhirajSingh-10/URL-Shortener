package com.UrlShortener.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

public record ShortenUrlRequest(
        @NotEmpty(message = "Url cannot be empty.")
        @URL(message = "A valid URL format required.")
        String url,

        String customAlias,

        @Min(value = 1, message = "Hours to expire must be positive number")
        Integer hoursToExpire
        ) {
}
