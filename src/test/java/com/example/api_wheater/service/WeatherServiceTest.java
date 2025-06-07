package com.example.api_wheater.service;

import com.example.api_wheater.api.NominatimClient;
import com.example.api_wheater.api.OpenMeteoClient;
import com.example.api_wheater.exception.CepNotFoundException;
import com.example.api_wheater.exception.GeneralServiceException;
import com.example.api_wheater.exception.GeolocationApiException;
import com.example.api_wheater.model.Location;
import com.example.api_wheater.model.WeatherResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {
    @Mock
    private NominatimClient nominatimClient;

    @Mock
    private OpenMeteoClient openMeteoClient;

    @InjectMocks
    private WeatherService weatherService;

    @Test
    @DisplayName("Deve lançar CepNotFoundException quando a API de geolocalização retorna nulo ou vazio")
    void deve_lancar_CepNotFoundException_quando_a_API_de_geolocalizacao_retorna_nulo_ou_vazio() {
        String zipCode = "99999-999";
        when(nominatimClient.getLocation(zipCode)).thenReturn(null);

        assertThatThrownBy(() -> weatherService.getWeatherByZip(zipCode))
                .isInstanceOf(CepNotFoundException.class)
                .hasMessageContaining(zipCode);
    }

    @Test
    @DisplayName("Deve propagar GeolocationApiException quando ocorrer falha ao buscar a localização")
    void deve_propagar_GeolocationApiException_quando_ocorrer_falha_ao_buscar_a_localizacao() {
        String zipCode = "30130-010";
        when(nominatimClient.getLocation(zipCode))
                .thenThrow(new GeolocationApiException("Erro ao acessar API de geolocalização"));

        assertThatThrownBy(() -> weatherService.getWeatherByZip(zipCode))
                .isInstanceOf(GeolocationApiException.class)
                .hasMessageContaining("Erro ao acessar API de geolocalização");
    }

    @Test
    @DisplayName("Deve retornar WeatherResponse corretamente quando o CEP é válido e ambas as APIs respondem com sucesso")
    void deve_retornar_WeatherResponse_corretamente_quando_o_CEP_e_valido_e_ambas_as_APIS_respondem_com_sucesso() {
        String zipCode = "30130-010";
        Location location = new Location();
        location.display_name = "Belo Horizonte, MG";
        location.lat = -3.7345359;
        location.lon = -38.5205943;

        WeatherResponse expectedWeather = new WeatherResponse();
        expectedWeather.minTemperature = 25.0;

        when(nominatimClient.getLocation(zipCode)).thenReturn(location);
        when(openMeteoClient.getWeather(-3.7345359, -38.5205943)).thenReturn(expectedWeather);

        WeatherResponse actual = weatherService.getWeatherByZip(zipCode);

        assertThat(actual).isNotNull();
        assertThat(actual.zipCode).isEqualTo(zipCode);
        assertThat(actual.location).isEqualTo("Belo Horizonte, MG");
        assertThat(actual.latitude).isEqualTo(-3.7345359);
        assertThat(actual.longitude).isEqualTo(-38.5205943);
        assertThat(actual.fromCache).isFalse();
        assertThat(actual.minTemperature).isEqualTo(25.0);
    }

    @Test
    @DisplayName("Deve lançar GeneralServiceException quando ocorrer uma exceção inesperada no serviço")
    void deve_lancar_GeneralServiceException_quando_ocorrer_uma_excecao_inesperada_no_servico() {
        String zipCode = "30130-010";
        when(nominatimClient.getLocation(zipCode))
                .thenThrow(new RuntimeException("Erro inesperado"));

        assertThatThrownBy(() -> weatherService.getWeatherByZip(zipCode))
                .isInstanceOf(GeneralServiceException.class)
                .hasMessageContaining("Erro inesperado");
    }
}
