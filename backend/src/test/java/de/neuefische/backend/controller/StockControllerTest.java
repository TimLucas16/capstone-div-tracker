package de.neuefische.backend.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import de.neuefische.backend.dto.CreateStockDto;
import de.neuefische.backend.model.Stock;
import de.neuefische.backend.repository.StockRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StockControllerTest {

    @Autowired
    private StockRepo stockRepo;

    @Autowired
    private WebTestClient testClient;

    @Value("${neuefische.capstone.div.tracker}")
    private String API_KEY;

    @BeforeEach
    public void cleanUp() {
        stockRepo.deleteAll();
    }

    @Test
    void test_something_with_wiremock(WireMockRuntimeInfo wmRuntimeInfo) {
        // The static DSL will be automatically configured for you
        stubFor(get("/profile/" + "AAPL" + "?apikey=" + API_KEY).willReturn(ok()));

        // Instance DSL can be obtained from the runtime info parameter
        WireMock wireMock = wmRuntimeInfo.getWireMock();
        wireMock.register(get("/instance-dsl").willReturn(ok()));

        // Info such as port numbers is also available
        int port = wmRuntimeInfo.getHttpPort();

        // Do some testing...
    }

    @Test
    void addNewStock() {
        //GIVEN
        CreateStockDto stock = CreateStockDto.builder()
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
                .companyName("Apple Inc.")
                .symbol("AAPL")
                .price(10)
                .shares(10)
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .build();

        assertEquals(24, actual.getId().length());
        assertEquals(expected, actual);
    }

    @Test
    void addNewStock_whenAmountOfSharesIsNotValid_shouldThrowException() {
        //GIVEN
        CreateStockDto stock = CreateStockDto.builder()
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
                .symbol("AAPL")
                .price(10)
                .shares(10)
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
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
                .symbol("AAPL")
                .price(10)
                .shares(10)
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .build());
        assertEquals(expected, actual);
    }
}