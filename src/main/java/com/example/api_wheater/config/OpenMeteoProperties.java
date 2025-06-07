package com.example.api_wheater.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "openmeteo")
public class OpenMeteoProperties {
    private String url;
}
