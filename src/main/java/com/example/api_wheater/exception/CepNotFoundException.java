package com.example.api_wheater.exception;

public class CepNotFoundException extends RuntimeException {
    public CepNotFoundException(String zipCode) {
        super("CEP inválido ou inexistente: " + zipCode);
    }
}
