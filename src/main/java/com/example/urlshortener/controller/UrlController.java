package com.example.urlshortener.controller;

import com.example.urlshortener.dto.ShortenRequest;
import com.example.urlshortener.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UrlController {

    private final UrlService service;

    public UrlController(UrlService service) {
        this.service = service;
    }

    // CREATE SHORT URL
    @PostMapping("/shorten")
    public ResponseEntity<Map<String, String>> shorten(@RequestBody ShortenRequest request) {
        String shortUrl = service.createShortUrl(request.getOriginalUrl());
        return ResponseEntity.ok(Map.of("shortUrl", shortUrl));
    }

    // REDIRECT
    @GetMapping("/{code}")
    public void redirect(@PathVariable String code, HttpServletResponse response) throws IOException {
        String originalUrl = service.getOriginalUrl(code);
        response.sendRedirect(originalUrl);
    }
}
