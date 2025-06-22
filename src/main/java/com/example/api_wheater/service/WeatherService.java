package com.example.api_wheater.service;

import com.example.api_wheater.api.NominatimClient;
import com.example.api_wheater.api.OpenMeteoClient;
import com.example.api_wheater.exception.CepNotFoundException;
import com.example.api_wheater.exception.GeneralServiceException;
import com.example.api_wheater.exception.GeolocationApiException;
import com.example.api_wheater.exception.WeatherApiException;
import com.example.api_wheater.model.WeatherResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;
@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {
    private final NominatimClient nominatimClient;
    private final OpenMeteoClient openMeteoClient;

    @Cacheable(value = "weather", key = "#zipCode")
    public WeatherResponse getWeatherByZip(String zipCode) {
        try {
            var location = nominatimClient.getLocation(zipCode);

            if (isEmpty(location)) {
                throw new CepNotFoundException(zipCode);
            }

            var weather = openMeteoClient.getWeather(location.lat, location.lon);

            weather.setZipCode(zipCode);
            weather.setLocation(location.getDisplay_name());
            weather.setLatitude(location.getLat());
            weather.setLongitude(location.getLon());
            weather.setFromCache(false);

            return weather;

        } catch (CepNotFoundException | GeolocationApiException | WeatherApiException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao consultar o clima para o CEP {}: {}", zipCode, e.getMessage(), e);
            throw new GeneralServiceException(e.getMessage());
        }
    }
}
