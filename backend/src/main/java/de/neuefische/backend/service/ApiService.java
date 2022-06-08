package de.neuefische.backend.service;

import de.neuefische.backend.model.SearchStock;
import de.neuefische.backend.model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

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
                .uri("/profile/" + symbol + "?apikey=" + API_KEY2)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .toEntity(Stock[].class)
                .block();

        if(response == null || response.getBody() == null) {
            throw new RuntimeException("API-ERROR");
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
                    .block();

            if (response == null || response.getBody() == null) {
                throw new RuntimeException("API-ERROR");
            }
            profileStockList.add(response.getBody()[0]);
        }
        return profileStockList;
    }

    public List<SearchStock> stockSearchResult(String company) {

        List<SearchStock> listNasdaq =  webClient.get()
                .uri("/search-name?query=" + company + "&limit=10&exchange=NASDAQ&apikey=" + API_KEY)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .toEntityList(SearchStock.class)
                .block()
                .getBody();

        List<SearchStock> listNyse =  webClient.get()
                .uri("/search-name?query=" + company + "&limit=10&exchange=NYSE&apikey=" + API_KEY)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .toEntityList(SearchStock.class)
                .block()
                .getBody();

        Set<SearchStock> setList = new HashSet<>(listNasdaq);
        setList.addAll(listNyse);

        return new ArrayList<>(setList);
    }
}
