package com.example.urlshortener.service;

import com.example.urlshortener.entity.UrlMapping;
import com.example.urlshortener.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Random;

@Service
public class UrlService {

    private static final String BASE62 =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final int CODE_LENGTH = 6;

    private final UrlRepository repository;
    private final Random random = new Random();

    public UrlService(UrlRepository repository) {
        this.repository = repository;
    }

    // Create short URL
    public UrlMapping shortenUrl(String originalUrl) {
        validateUrl(originalUrl);

        String shortCode;
        do {
            shortCode = generateShortCode();
        } while (repository.existsByShortCode(shortCode));

        UrlMapping url = new UrlMapping();
        url.setOriginalUrl(originalUrl);
        url.setShortCode(shortCode);

        return repository.save(url);
    }

    // Redirect logic + click count
    public UrlMapping getAndUpdate(String shortCode) {
        UrlMapping url = repository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("Short URL not found"));

        url.incrementClickCount();
        return repository.save(url);
    }

    // Analytics
    public UrlMapping getAnalytics(String shortCode) {
        return repository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("Short URL not found"));
    }

    // Helpers
    private String generateShortCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(BASE62.charAt(random.nextInt(BASE62.length())));
        }
        return sb.toString();
    }

    private void validateUrl(String url) {
        try {
            new URI(url);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid URL");
        }
    }
}