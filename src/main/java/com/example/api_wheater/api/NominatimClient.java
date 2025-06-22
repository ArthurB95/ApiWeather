package com.example.api_wheater.api;

import com.example.api_wheater.config.NominatimProperties;
import com.example.api_wheater.exception.GeolocationApiException;
import com.example.api_wheater.model.Location;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class NominatimClient {
    private final RestTemplate restTemplate;
    private final NominatimProperties nominatimProperties;

    public Location getLocation(String zipCode) {
        try {
            String url = UriComponentsBuilder.fromUriString(nominatimProperties.getUrl())
                    .queryParam("format", "json")
                    .queryParam("postalcode", zipCode)
                    .queryParam("country", "Brazil")
                    .queryParam("limit", 1)
                    .build().toUriString();

            Location[] result = restTemplate.getForObject(url, Location[].class);
            return result != null && result.length > 0 ? result[0] : null;
        }  catch (Exception e) {
            log.error("Erro ao chamar Nominatim API: {}", e.getMessage(), e);
            throw new GeolocationApiException(e.getMessage());
        }
    }
}
