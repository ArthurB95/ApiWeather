package com.example.api_wheater.exception;

public class GeolocationApiException extends RuntimeException {
    public GeolocationApiException(String message) {
        super("Erro na API de geolocalização (Nominatim): " + message);
    }
}