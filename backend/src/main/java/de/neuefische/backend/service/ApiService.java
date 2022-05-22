package de.neuefische.backend.service;

import de.neuefische.backend.model.Stock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
public class ApiService {
    private String apiKey = "";
    private final WebClient webClient;

    public ApiService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Stock getProfileBySymbol(String symbol) {

        Stock[] stock = webClient.get()
                    .uri("/profile/" + symbol + "?apikey=" + apiKey)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .toEntity(Stock[].class)
                    .block()
                    .getBody();
        return stock[0];
    }
}
