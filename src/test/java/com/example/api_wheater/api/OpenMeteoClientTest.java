package com.example.api_wheater.api;

import com.example.api_wheater.config.OpenMeteoProperties;
import com.example.api_wheater.exception.WeatherApiException;
import com.example.api_wheater.model.WeatherResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenMeteoClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private OpenMeteoProperties openMeteoProperties;

    @InjectMocks
    private OpenMeteoClient openMeteoClient;

    @Test
    @DisplayName("Deve retornar objeto WeatherResponse corretamente quando a resposta da API for válida")
    void deve_retornar_objeto_WeatherResponse_corretamente_quando_a_resposta_da_API_for_válida() {
        when(openMeteoProperties.getUrl()).thenReturn("https://api.open-meteo.com/v1/forecast");

        String jsonMock = """
            {
              "current_weather": {
                "temperature": 25.0
              },
              "daily": {
                "temperature_2m_min": [20.0, 21.0],
                "temperature_2m_max": [30.0, 29.0],
                "time": ["2024-01-01", "2024-01-02"]
              }
            }
        """;

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonMock);

        WeatherResponse response = openMeteoClient.getWeather(-3.7, -38.5);

        assertThat(response).isNotNull();
        assertThat(response.currentTemperature).isEqualTo(25.0);
        assertThat(response.minTemperature).isEqualTo(20.0);
        assertThat(response.maxTemperature).isEqualTo(30.0);
        assertThat(response.forecast).hasSize(2);
        assertThat(response.forecast.get(0).day).isEqualTo("2024-01-01");
    }

    @Test
    @DisplayName("Deve lançar WeatherApiException quando ocorrer erro ao chamar RestTemplate")
    void deve_lancar_WeatherApiException_quando_ocorrer_erro_ao_chamar_RestTemplate() {
        when(openMeteoProperties.getUrl()).thenReturn("https://api.open-meteo.com/v1/forecast");

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenThrow(new RuntimeException("Erro de rede"));

        assertThrows(WeatherApiException.class, () -> openMeteoClient.getWeather(-3.7, -38.5));
    }

    @Test
    @DisplayName("Deve construir corretamente a lista de previsões diárias no retorno")
    void deve_destruir_corretamente_a_lista_de_previsoes_diarias_no_retorno() {
        when(openMeteoProperties.getUrl()).thenReturn("https://api.open-meteo.com/v1/forecast");

        String jsonMock = """
            {
              "current_weather": { "temperature": 22.0 },
              "daily": {
                "temperature_2m_min": [18.0, 19.0, 20.0],
                "temperature_2m_max": [28.0, 27.0, 26.0],
                "time": ["2024-01-01", "2024-01-02", "2024-01-03"]
              }
            }
        """;

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonMock);

        WeatherResponse response = openMeteoClient.getWeather(-3.7, -38.5);

        List<WeatherResponse.Forecast> forecasts = response.forecast;
        assertThat(forecasts).hasSize(3);
        assertThat(forecasts.get(2).day).isEqualTo("2024-01-03");
        assertThat(forecasts.get(2).min).isEqualTo(20.0);
        assertThat(forecasts.get(2).max).isEqualTo(26.0);
    }

    @Test
    @DisplayName("Deve preencher corretamente temperatura atual, mínima e máxima no objeto WeatherResponse")
    void deve_preencher_corretamente_temperatura_atual_minima_e_maxima_no_objeto_WeatherResponse() {
        when(openMeteoProperties.getUrl()).thenReturn("https://api.open-meteo.com/v1/forecast");

        String jsonMock = """
            {
              "current_weather": { "temperature": 19.5 },
              "daily": {
                "temperature_2m_min": [17.0],
                "temperature_2m_max": [23.0],
                "time": ["2024-01-01"]
              }
            }
        """;

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonMock);

        WeatherResponse response = openMeteoClient.getWeather(-3.7, -38.5);

        assertThat(response.currentTemperature).isEqualTo(19.5);
        assertThat(response.minTemperature).isEqualTo(17.0);
        assertThat(response.maxTemperature).isEqualTo(23.0);
    }
}
