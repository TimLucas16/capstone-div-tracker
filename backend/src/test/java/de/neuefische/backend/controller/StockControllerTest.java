package de.neuefische.backend.controller;

import de.neuefische.backend.model.Stock;
import de.neuefische.backend.repository.StockRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StockControllerTest {

    @Autowired
    private StockRepo stockRepo;

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient testClient;

    @BeforeEach
    void cleanUp() {
        stockRepo.deleteAll();
    }

    @Test
    void addNewStock() {
        //GIVEN
        Stock stock = Stock.builder().name("Apple").symbol("AAPL").amountOfShares(10).build();

        //WHEN
        Stock actual = testClient.post()
                .uri("stock")
                .bodyValue(stock)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Stock.class)
                .returnResult()
                .getResponseBody();

        //Then
        assertNotNull(actual);
        assertNotNull(actual.getId());
        Stock expected = Stock.builder().id(actual.getId()).name("Apple").symbol("AAPL").amountOfShares(10).build();
        assertEquals(24, actual.getId().length());
        assertEquals(expected, actual);
    }
}