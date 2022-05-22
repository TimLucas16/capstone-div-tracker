package de.neuefische.backend.service;

import de.neuefische.backend.model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
public class ApiService {

//    @Value("${neuefische.capstone.div.tracker}")
//    private String API_KEY;

    private String apikey = "9eafa28db1b3dd701c8d112b5bbe75d9";
    private final WebClient webClient;

    @Autowired
    public ApiService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Stock getProfileBySymbol(String symbol) {

        Stock[] stock = webClient.get()
                .uri("/profile/" + symbol + "?apikey=" + apikey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .toEntity(Stock[].class)
                .block()
                .getBody();
        return stock[0];
    }
}
