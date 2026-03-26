package com.urlshrt.UrlShortener.controller;

import com.urlshrt.UrlShortener.dto.UrlStatsResponse;
import com.urlshrt.UrlShortener.exception.UrlNotFoundException;
import com.urlshrt.UrlShortener.service.UrlShortenerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    private final UrlShortenerService urlShortenerService;

    public PageController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @PostMapping("/shorten-web")
    public String handleShortenForm(@RequestParam("longUrl") String longUrl, Model model) {
        String shortCode = urlShortenerService.shortenUrl(longUrl);


        String fullShortUrl = "http://localhost:8080/" + shortCode;


        model.addAttribute("originalUrl", longUrl);


        model.addAttribute("shortUrlResult", fullShortUrl);

        return "index";
    }

    @PostMapping("/check-stats")
    public String handleStatsCheckForm(@RequestParam("checkShortCode") String shortCode, Model model){

        try{
            UrlStatsResponse stats = urlShortenerService.getStats(shortCode);

            model.addAttribute("urlStats",stats);

        }catch (UrlNotFoundException e){
            model.addAttribute("statsError", "Statistics not found for short code : "+ shortCode);
        }

        return "index";
    }
}