package com.example.api_wheater.api;

import com.example.api_wheater.config.NominatimProperties;
import com.example.api_wheater.exception.GeolocationApiException;
import com.example.api_wheater.model.Location;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class NominatimClient {
    private final RestTemplate restTemplate;
    private final NominatimProperties nominatimProperties;

    public NominatimClient(RestTemplate restTemplate, NominatimProperties nominatimProperties) {
        this.restTemplate = restTemplate;
        this.nominatimProperties = nominatimProperties;
    }

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
            throw new GeolocationApiException(e.getMessage());
        }
    }
}
