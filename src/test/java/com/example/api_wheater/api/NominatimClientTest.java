package com.example.api_wheater.api;

import com.example.api_wheater.config.NominatimProperties;
import com.example.api_wheater.exception.GeolocationApiException;
import com.example.api_wheater.model.Location;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NominatimClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private NominatimProperties nominatimProperties;

    @InjectMocks
    private NominatimClient nominatimClient;

    @Test
    @DisplayName("Deve retornar a primeira posição do array de Location quando a API retorna uma lista com dados")
    void shouldReturnFirstLocationWhenApiReturnsValidArray() {
        String zipCode = "60140061";
        String expectedUrl = "https://nominatim.openstreetmap.org/search";
        Location location = new Location();
        location.lat = -3.7345359;
        location.lon =  -38.5205943;
        location.display_name = "Belo Horizonte, MG";

        when(nominatimProperties.getUrl()).thenReturn(expectedUrl);
        when(restTemplate.getForObject(anyString(), eq(Location[].class)))
                .thenReturn(new Location[]{location});

        Location result = nominatimClient.getLocation(zipCode);

        assertThat(result).isNotNull();
        assertThat(result.lat).isEqualTo(-3.7345359);
        assertThat(result.lon).isEqualTo(-38.5205943);
        assertThat(result.display_name).isEqualTo("Belo Horizonte, MG");
    }

    @Test
    @DisplayName("Deve retornar null quando a API retorna um array vazio")
    void shouldReturnNullWhenApiReturnsEmptyArray() {
        when(nominatimProperties.getUrl()).thenReturn("https://nominatim.openstreetmap.org/search");
        when(restTemplate.getForObject(anyString(), eq(Location[].class)))
                .thenReturn(new Location[]{});

        Location result = nominatimClient.getLocation("99999999");

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Deve retornar null quando a API retorna null")
    void shouldReturnNullWhenApiReturnsNull() {
        when(nominatimProperties.getUrl()).thenReturn("https://nominatim.openstreetmap.org/search");
        when(restTemplate.getForObject(anyString(), eq(Location[].class)))
                .thenReturn(null);

        Location result = nominatimClient.getLocation("99999999");

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Deve lançar GeolocationApiException quando ocorre uma exceção ao chamar a API")
    void shouldThrowGeolocationApiExceptionWhenRestTemplateFails() {
        when(nominatimProperties.getUrl()).thenReturn("https://nominatim.openstreetmap.org/search");
        when(restTemplate.getForObject(anyString(), eq(Location[].class)))
                .thenThrow(new RuntimeException("API fora do ar"));

        assertThatThrownBy(() -> nominatimClient.getLocation("30130010"))
                .isInstanceOf(GeolocationApiException.class)
                .hasMessageContaining("API fora do ar");
    }

}
