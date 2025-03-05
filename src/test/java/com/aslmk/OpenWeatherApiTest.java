package com.aslmk;

import com.aslmk.openWeatherApi.LocationCoordinatesResponse;
import com.aslmk.openWeatherApi.OpenWeatherService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class OpenWeatherApiTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OpenWeatherService openWeatherService;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.bindTo(restTemplate)
                                          .bufferContent()
                                          .build();
    }

    @Test
    void getLocationCoordinatesTest() throws Exception {
        String city = "Shanghai";
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?units=metric&appid=f64a5c6eda435181b8506eb41a331336&q=" + city;


        String mockResponse = "{\"coord\":{\"lat\": 31.2222,\"lon\": 121.4581}}";

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(apiUrl)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mockResponse));

        LocationCoordinatesResponse response = openWeatherService.getLocationCoordinates(city);

        mockServer.verify();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(new BigDecimal("31.2222"), response.getCoord().getLat());
        Assertions.assertEquals(new BigDecimal("121.4581"), response.getCoord().getLon());
    }


}
