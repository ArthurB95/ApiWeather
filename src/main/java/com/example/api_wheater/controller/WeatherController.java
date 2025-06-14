package com.example.api_wheater.controller;

import com.example.api_wheater.model.WeatherResponse;
import com.example.api_wheater.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
public class WeatherController {
    private final WeatherService weatherService;
    private final CacheManager cacheManager;

    public WeatherController(WeatherService weatherService, CacheManager cacheManager) {
        this.weatherService = weatherService;
        this.cacheManager = cacheManager;
    }

    @GetMapping
    public WeatherResponse getWeather(@RequestParam String zipCode) {
        Cache cache = cacheManager.getCache("weather");
        boolean fromCache = cache != null && cache.get(zipCode) != null;

        WeatherResponse response = weatherService.getWeatherByZip(zipCode);
        response.fromCache = fromCache;

        return response;    }
}
