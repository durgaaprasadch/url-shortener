package com.example.urlshortener.controller;

import com.example.urlshortener.entity.UrlMapping;
import com.example.urlshortener.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
public class UrlController {

    private final UrlService service;

    public UrlController(UrlService service) {
        this.service = service;
    }

    // CREATE SHORT URL (SIMPLE)
    @PostMapping("/api/shorten")
    public Map<String, String> shorten(@RequestBody Map<String, String> request) {
        UrlMapping url = service.shortenUrl(request.get("originalUrl"));
        return Map.of(
                "shortUrl", "http://localhost:8081/" + url.getShortCode()
        );
    }

    // REDIRECT
    @GetMapping("/{shortCode}")
    public void redirect(@PathVariable String shortCode,
                         HttpServletResponse response) throws IOException {

        UrlMapping url = service.getAndUpdate(shortCode);
        response.sendRedirect(url.getOriginalUrl());
    }

    // ANALYTICS
    @GetMapping("/api/analytics/{shortCode}")
    public UrlMapping analytics(@PathVariable String shortCode) {
        return service.getAnalytics(shortCode);
    }
}