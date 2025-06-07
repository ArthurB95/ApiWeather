package com.example.api_wheater.controller;

import com.example.api_wheater.model.WeatherResponse;
import com.example.api_wheater.service.WeatherService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherControllerTest {

    @Mock
    private WeatherService weatherService;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @InjectMocks
    private WeatherController weatherController;

    @Test
    @DisplayName("Deve retornar previsão do tempo corretamente quando o CEP não está em cache")
    void deve_retornar_precisao_do_tempo_corretamente_quando_o_CEP_nao_esta_em_cache() {
        String zip = "30140071";

        when(cacheManager.getCache("weather")).thenReturn(cache);
        when(cache.get(zip)).thenReturn(null);

        WeatherResponse mockedResponse = new WeatherResponse();
        mockedResponse.fromCache = false;

        when(weatherService.getWeatherByZip(zip)).thenReturn(mockedResponse);

        WeatherResponse result = weatherController.getWeather(zip);

        assertThat(result).isNotNull();
        assertThat(result.fromCache).isFalse();
        verify(weatherService).getWeatherByZip(zip);
    }

    @Test
    @DisplayName("Deve retornar previsão do tempo com flag fromCache true quando o CEP estiver em cache")
    void deve_retornar_previsao_do_tempo_com_flag_fromCache_true_quando_o_CEP_estiver_em_cache() {
        String zip = "30140071";

        when(cacheManager.getCache("weather")).thenReturn(cache);
        when(cache.get(zip)).thenReturn(mock(Cache.ValueWrapper.class));

        WeatherResponse mockedResponse = new WeatherResponse();
        mockedResponse.fromCache = false; // o controller sobrescreve isso

        when(weatherService.getWeatherByZip(zip)).thenReturn(mockedResponse);

        WeatherResponse result = weatherController.getWeather(zip);

        assertThat(result.fromCache).isTrue();
    }

    @Test
    @DisplayName("Deve tratar situação onde o CacheManager retorna null e continuar executando corretamente")
    void deve_tratar_situacao_onde_o_CacheManager_retorna_null_e_continuar_executando_corretamente() {
        String zip = "99999999";

        when(cacheManager.getCache("weather")).thenReturn(null);

        WeatherResponse mockedResponse = new WeatherResponse();
        mockedResponse.fromCache = false;

        when(weatherService.getWeatherByZip(zip)).thenReturn(mockedResponse);

        WeatherResponse result = weatherController.getWeather(zip);

        assertThat(result.fromCache).isFalse();
    }

    @Test
    @DisplayName("Deve invocar o serviço de clima exatamente uma vez com o CEP fornecido")
    void deve_invocar_o_servico_de_clima_exatamente_uma_vez_com_o_CEP_fornecido() {
        String zip = "60140061";

        when(cacheManager.getCache("weather")).thenReturn(null);

        WeatherResponse mockedResponse = new WeatherResponse();
        mockedResponse.fromCache = false;

        when(weatherService.getWeatherByZip(zip)).thenReturn(mockedResponse);

        weatherController.getWeather(zip);

        verify(weatherService, times(1)).getWeatherByZip(zip);
    }

    @Test
    @DisplayName("Deve preencher corretamente todos os campos do WeatherResponse após chamada do controller")
    void deve_preencher_corretamente_todos_os_campos_do_WeatherResponse_apos_chamada_do_controller() {
        String zip = "30140071";

        when(cacheManager.getCache("weather")).thenReturn(null);

        WeatherResponse mockedResponse = new WeatherResponse();
        mockedResponse.zipCode = zip;
        mockedResponse.latitude = -19.8157;
        mockedResponse.longitude = -43.9542;
        mockedResponse.currentTemperature = 25.0;
        mockedResponse.location = "Belo Horizonte, MG";

        when(weatherService.getWeatherByZip(zip)).thenReturn(mockedResponse);

        WeatherResponse result = weatherController.getWeather(zip);

        assertThat(result.zipCode).isEqualTo("30140071");
        assertThat(result.latitude).isEqualTo(-19.8157);
        assertThat(result.longitude).isEqualTo(-43.9542);
        assertThat(result.currentTemperature).isEqualTo(25.0);
        assertThat(result.location).isEqualTo("Belo Horizonte, MG");
    }
}
