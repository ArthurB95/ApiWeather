package com.example.api_wheater.exception;

public class GeneralServiceException extends RuntimeException {
    public GeneralServiceException(String message) {
        super("Erro inesperado: " + message);
    }
}
