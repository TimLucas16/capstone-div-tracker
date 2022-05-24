package de.neuefische.backend.service;

import de.neuefische.backend.model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class ApiService {

    @Value("${neuefische.capstone.div.tracker}")
    private String API_KEY;

    private final WebClient webClient;

    @Autowired
    public ApiService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Stock getProfileBySymbol(String symbol) {

        Stock[] stock = webClient.get()
                .uri("/profile/" + symbol + "?apikey=" + API_KEY)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .toEntity(Stock[].class)
                .block()
                .getBody();
        return stock[0];
    }


    public List<Stock> getPrice(List<String> symbolList) {
        List<Stock> profileStockList = new ArrayList<>();

        for (String symbol : symbolList) {
            Stock[] stock = webClient.get()
                    .uri("/quote-short/" + symbol + "?apikey=" + API_KEY)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .toEntity(Stock[].class)
                    .block()
                    .getBody();

            profileStockList.add(stock[0]);
        }
        return profileStockList;
    }
}









//    public List<Stock> getPrice2(List<Stock> stockList) {
//
//        stockList.stream().map(stock -> stock.getSymbol())
//                                .map(symbol -> webClient.get()
//                                        .uri("/quote-short/" + symbol + API_KEY)
//                                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                                        .retrieve()
//                                        .toEntity(Stock[].class)
//                                        .block()
//                                        .getBody())
//                                .toList();
//    }

