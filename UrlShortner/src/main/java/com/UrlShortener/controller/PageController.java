package com.UrlShortener.controller;

import com.UrlShortener.dto.UrlStatsResponse;
import com.UrlShortener.exception.AliasAlreadyExistsException;
import com.UrlShortener.exception.UrlNotFoundException;
import com.UrlShortener.service.UrlShortenerService;
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
    public String handleShortenForm(@RequestParam("longUrl") String longUrl,
                                    @RequestParam(name="customAlias", required = false) String customAlias,
                                    @RequestParam(name="expiry", required = false) Integer expiry,
                                    Model model
    ) {

        model.addAttribute("originalUrl", longUrl);

        try{
            String shortCode = urlShortenerService.shortenUrl(longUrl,customAlias,expiry);
            String fullShortCode = "http://localhost:8080/" + shortCode;

            model.addAttribute("shortUrlResult", fullShortCode);
        }catch (AliasAlreadyExistsException e){
            model.addAttribute("aliasError", e.getMessage());
        }


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