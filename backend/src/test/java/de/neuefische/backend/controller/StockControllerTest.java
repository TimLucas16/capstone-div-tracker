package de.neuefische.backend.controller;

import de.neuefische.backend.model.Stock;
import de.neuefische.backend.repository.StockRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StockControllerTest {

    @Autowired
    private StockRepo stockRepo;

    @Autowired
    private WebTestClient testClient;

    @BeforeEach
    public void cleanUp() {
        stockRepo.deleteAll();
    }

    @Test
    void addNewStock() {
        //GIVEN
        Stock stock = Stock.builder()
                .name("Apple")
                .symbol("AAPL")
                .price(10)
                .shares(10)
                .build();

        //WHEN
        Stock actual = testClient.post()
                .uri("api/stock")
                .bodyValue(stock)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Stock.class)
                .returnResult()
                .getResponseBody();

        //Then
        assertNotNull(actual);
        assertNotNull(actual.getId());

        Stock expected = Stock.builder()
                .id(actual.getId())
                .name("Apple")
                .symbol("AAPL")
                .price(10)
                .shares(10)
                .build();

        assertEquals(24, actual.getId().length());
        assertEquals(expected, actual);
    }

    @Test
    void addNewStock_whenAmountOfSharesIsNotValid_shouldThrowException() {
        //GIVEN
        Stock stock = Stock.builder()
                .name("Apple")
                .symbol("AAPL")
                .price(10)
                .shares(0)
                .build();

        //WHEN
        testClient.post()
                .uri("api/stock")
                .bodyValue(stock)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void getAllStocks() {
        //GIVEN
        Stock stock = Stock.builder()
                .name("Apple")
                .symbol("AAPL")
                .price(10)
                .shares(0)
                .build();
        stockRepo.insert(stock);

        //WHEN
        List<Stock> actual = testClient.get()
                .uri("/api/stock")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Stock.class)
                .returnResult()
                .getResponseBody();

        //THEN
        List<Stock> expected = List.of(Stock.builder()
                .id(stock.getId())
                .name("Apple")
                .symbol("AAPL")
                .price(10)
                .shares(0)
                .build());
        assertEquals(expected, actual);
    }
}