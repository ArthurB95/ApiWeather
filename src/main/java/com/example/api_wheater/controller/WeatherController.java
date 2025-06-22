package com.example.api_wheater.controller;

import com.example.api_wheater.model.WeatherResponse;
import com.example.api_wheater.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
@Validated
public class WeatherController {
    private final WeatherService weatherService;
    private final CacheManager cacheManager;

    public WeatherController(WeatherService weatherService, CacheManager cacheManager) {
        this.weatherService = weatherService;
        this.cacheManager = cacheManager;
    }

    @Operation(summary = "Obter previsão do tempo por CEP",
            description = "Retorna dados de clima atual e previsão de temperatura mínima e máxima para os próximos dias.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados de clima retornados com sucesso"),
            @ApiResponse(responseCode = "400", description = "CEP inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "CEP não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping
    public WeatherResponse getWeather(@RequestParam String zipCode) {
        Cache cache = cacheManager.getCache("weather");
        boolean fromCache = cache != null && cache.get(zipCode) != null;

        WeatherResponse response = weatherService.getWeatherByZip(zipCode);
        response.fromCache = fromCache;

        return response;    }
}
