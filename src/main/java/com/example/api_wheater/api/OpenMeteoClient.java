package com.example.api_wheater.api;

import com.example.api_wheater.config.OpenMeteoProperties;
import com.example.api_wheater.exception.WeatherApiException;
import com.example.api_wheater.model.WeatherResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenMeteoClient {
    private final RestTemplate restTemplate;
    private final OpenMeteoProperties openMeteoProperties;

    public WeatherResponse getWeather(double lat, double lon) {
        try {
            String url = UriComponentsBuilder.fromUriString(openMeteoProperties.getUrl())
                    .queryParam("latitude", lat)
                    .queryParam("longitude", lon)
                    .queryParam("current_weather", true)
                    .queryParam("daily", "temperature_2m_max,temperature_2m_min")
                    .queryParam("timezone", "auto")
                    .build().toUriString();

            String jsonStr = restTemplate.getForObject(url, String.class);
            JSONObject json = new JSONObject(jsonStr);

            WeatherResponse response = new WeatherResponse();
            response.currentTemperature = json.getJSONObject("current_weather").getDouble("temperature");

            var daily = json.getJSONObject("daily");
            var min = daily.getJSONArray("temperature_2m_min");
            var max = daily.getJSONArray("temperature_2m_max");
            var dates = daily.getJSONArray("time");

            response.setMinTemperature(min.getDouble(0));
            response.setMaxTemperature(max.getDouble(0));

            List<WeatherResponse.Forecast> forecasts = new ArrayList<>();
            for (int i = 0; i < dates.length(); i++) {
                WeatherResponse.Forecast f = new WeatherResponse.Forecast();
                f.day = dates.getString(i);
                f.min = min.getDouble(i);
                f.max = max.getDouble(i);
                forecasts.add(f);
            }
            response.forecast = forecasts;

            return response;
        } catch (Exception e) {
            log.error("Erro ao chamar OpenMeteo API: {}", e.getMessage(), e);
            throw new WeatherApiException(e.getMessage());
        }
    }
}
