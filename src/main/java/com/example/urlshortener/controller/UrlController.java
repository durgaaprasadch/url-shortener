package com.example.urlshortener.controller;

import com.example.urlshortener.dto.ShortenRequest;
import com.example.urlshortener.entity.UrlMapping;
import com.example.urlshortener.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
public class UrlController {

    private final UrlService service;

    public UrlController(UrlService service) {
        this.service = service;
    }

    // =========================
    // CREATE SHORT URL
    // =========================
    @PostMapping("/api/shorten")
    public ResponseEntity<Map<String, String>> shorten(
            @RequestBody ShortenRequest request,
            HttpServletRequest httpRequest
    ) {
        UrlMapping mapping = service.createShortUrl(request.getOriginalUrl());

        // Dynamically build base URL (works for localhost & Railway)
        String baseUrl = httpRequest.getRequestURL()
                .toString()
                .replace(httpRequest.getRequestURI(), "");

        String shortUrl = baseUrl + "/" + mapping.getShortCode();

        return ResponseEntity.ok(Map.of("shortUrl", shortUrl));
    }

    // =========================
    // REDIRECT SHORT URL
    // =========================
    @GetMapping("/{code}")
    public void redirect(
            @PathVariable String code,
            HttpServletResponse response
    ) throws IOException {
        String originalUrl = service.getOriginalUrl(code);
        response.sendRedirect(originalUrl);
    }
}
