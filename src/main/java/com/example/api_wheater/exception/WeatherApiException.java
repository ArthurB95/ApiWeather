package com.example.api_wheater.exception;

public class WeatherApiException extends RuntimeException {
    public WeatherApiException(String message) {
        super("Erro na API do tempo (Open-Meteo): " + message);
    }
}
