package com.example.api_wheater.service;

import com.example.api_wheater.api.NominatimClient;
import com.example.api_wheater.api.OpenMeteoClient;
import com.example.api_wheater.exception.CepNotFoundException;
import com.example.api_wheater.exception.GeneralServiceException;
import com.example.api_wheater.exception.GeolocationApiException;
import com.example.api_wheater.exception.WeatherApiException;
import com.example.api_wheater.model.WeatherResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class WeatherService {
    private final NominatimClient nominatimClient;
    private final OpenMeteoClient openMeteoClient;

    public WeatherService(NominatimClient nominatimClient,
                          OpenMeteoClient openMeteoClient) {
        this.nominatimClient = nominatimClient;
        this.openMeteoClient = openMeteoClient;
    }

    @Cacheable(value = "weather", key = "#zipCode")
    public WeatherResponse getWeatherByZip(String zipCode) {
        try {
            var location = nominatimClient.getLocation(zipCode);

            if (isEmpty(location)) {
                throw new CepNotFoundException(zipCode);
            }

            var weather = openMeteoClient.getWeather(location.lat, location.lon);

            weather.zipCode = zipCode;
            weather.location = location.display_name;
            weather.latitude = location.lat;
            weather.longitude = location.lon;
            weather.fromCache = false;

            return weather;

        } catch (CepNotFoundException | GeolocationApiException | WeatherApiException e) {
            throw e;
        } catch (Exception e) {
            throw new GeneralServiceException(e.getMessage());
        }
    }
}
