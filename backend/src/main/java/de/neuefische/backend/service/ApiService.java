package de.neuefische.backend.service;

import de.neuefische.backend.controller.errorhandling.ApiUnexpectedResponseException;
import de.neuefische.backend.controller.errorhandling.ApiNoResponseException;
import de.neuefische.backend.controller.errorhandling.InvalidApiKeyException;
import de.neuefische.backend.model.SearchStock;
import de.neuefische.backend.model.Stock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Slf4j
@Service
public class ApiService {

    @Value("${neuefische.capstone.div.tracker}")
    private String API_KEY;

    @Value("${neuefische.capstone.div.tracker2}")
    private String API_KEY2;

    private final WebClient webClient;

    @Autowired
    public ApiService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Stock getProfileBySymbol(String symbol) {

        ResponseEntity<Stock[]> response = webClient.get()
                .uri("/profile/" + symbol + "?apikey=" + API_KEY)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .toEntity(Stock[].class)
                .onErrorMap(err -> new ApiNoResponseException("Error: " + err))
                .block();

        if(response == null || response.getBody() == null) {
            throw new ApiUnexpectedResponseException("unexpected Api response!");
        }
        if(response.getBody().length == 0) {
            throw new NoSuchElementException("Stock with symbol: " + symbol + " does not exist");
        }
        return response.getBody()[0];
    }

    public List<Stock> getPrice(List<String> symbolList) {
        List<Stock> profileStockList = new ArrayList<>();
        for (String symbol : symbolList) {
            ResponseEntity<Stock[]> response = webClient.get()
                    .uri("/profile/" + symbol + "?apikey=" + API_KEY2)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .toEntity(Stock[].class)
                    .onErrorMap(err -> new ApiNoResponseException("Error: " + err))
                    .block();

            if (response == null || response.getBody() == null) {
                throw new ApiUnexpectedResponseException("unexpected Api response!");
            }
            profileStockList.add(response.getBody()[0]);
        }
        return profileStockList;
    }

    public List<SearchStock> stockSearchResult(String company) {
        String[] exchangeList = {"NASDAQ", "NYSE"};
        Set<SearchStock> setList = new HashSet<>();
        for (String exchange : exchangeList) {
            ResponseEntity<List<SearchStock>> response =  webClient.get()
                    .uri("/search-name?query=" + company + "&limit=10&exchange=" + exchange + "&apikey=" + API_KEY)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .toEntityList(SearchStock.class)
                    .onErrorMap(err -> new ApiNoResponseException("Error: " + err))
                    .block();

            if(response == null || response.getBody() == null) {
                throw new ApiUnexpectedResponseException("unexpected Api response!");
            }
            if(response.getBody().isEmpty()) {
                log.info("search for " + company + " was without result");
            }
            // special case for invalid Apikeys return null values
            if(response.getBody().size() == 1
                    && response.getBody().get(0).getName() == null
                    && response.getBody().get(0).getSymbol() == null) {
                throw new InvalidApiKeyException("Invalid ApiKey, Body: " + response.getBody());
            }
            setList.addAll(response.getBody());
        }
        return new ArrayList<>(setList);
    }
}
