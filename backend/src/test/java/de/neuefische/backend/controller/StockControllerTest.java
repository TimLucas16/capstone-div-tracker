package de.neuefische.backend.controller;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import de.neuefische.backend.dto.CreateStockDto;
import de.neuefische.backend.model.Portfolio;
import de.neuefische.backend.model.SearchStock;
import de.neuefische.backend.model.Stock;
import de.neuefische.backend.repository.StockRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest(httpPort = 8484)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StockControllerTest {

    @Autowired
    private StockRepo stockRepo;

    @Autowired
    private WebTestClient testClient;

    @Value("${neuefische.capstone.div.tracker}")
    private String API_KEY;

    @Value("${neuefische.capstone.div.tracker2}")
    private String API_KEY2;

    @BeforeEach
    public void cleanUp() {
        stockRepo.deleteAll();
    }

    @Test
    void addNewStock() {
        //GIVEN
        CreateStockDto stock = CreateStockDto.builder()
                .symbol("AAPL")
                .costPrice(new BigDecimal("1400.43"))
                .shares(BigDecimal.TEN)
                .build();

        // Mock FMP API
        stubFor(get("/profile/" + "AAPL" + "?apikey=" + API_KEY)
                .willReturn(okJson(json)));

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
                .costPrice(new BigDecimal("1400.43"))
                .shares(BigDecimal.TEN)
                .value(new BigDecimal("1403.60"))
                .price(new BigDecimal("140.36"))
                .totalReturn(new BigDecimal("3.17"))
                .totalReturnPercent(new BigDecimal("0.2300"))
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .isin("US0378331005")
                .build();

        assertEquals(24, actual.getId().length());
        assertEquals(expected, actual);
    }

    @Test
    void addNewStock_whenAmountOfSharesIsNotValid_shouldThrowException() {
        //GIVEN
        CreateStockDto stock = CreateStockDto.builder()
                .symbol("AAPL")
                .costPrice(new BigDecimal("280.56"))
                .shares(BigDecimal.ZERO)
                .build();

        //WHEN
        testClient.post()
                .uri("api/stock")
                .bodyValue(stock)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void getAllStocks() {
        //GIVEN
        Stock stock = Stock.builder()
                .symbol("AAPL")
                .costPrice(new BigDecimal("1400.56"))
                .shares(BigDecimal.TEN)
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .build();
        stockRepo.insert(stock);

        stubFor(get("/profile/" + "AAPL" + "?apikey=" + API_KEY2)
                .willReturn(okJson(json)));

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
                .costPrice(new BigDecimal("1400.56"))
                .shares(BigDecimal.TEN)
                .value(new BigDecimal("1403.6"))
                .price(new BigDecimal("140.36"))
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(new BigDecimal("3.04"))
                .build());
        assertEquals(expected, actual);
    }

    @Test
    void getPortfolioValues() {
        //GIVEN
        Stock stock = Stock.builder()
                .symbol("AAPL")
                .costPrice(new BigDecimal("1400.56"))
                .shares(BigDecimal.TEN)
                .value(new BigDecimal("1403.60"))
                .price(new BigDecimal("140.36"))
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(new BigDecimal("3.04"))
                .build();
        stockRepo.insert(stock);

        // Mock FMP API
        stubFor(get("/profile/" + "AAPL" + "?apikey=" + API_KEY2)
                .willReturn(okJson(json)));

        //WHEN
        Portfolio actual = testClient.get()
                .uri("/api/stock/portfolio")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Portfolio.class)
                .returnResult()
                .getResponseBody();

        //THEN
        Portfolio expected = Portfolio.builder()
                .pfTotalReturnPercent(new BigDecimal("0.2200"))
                .pfTotalReturnAbsolute(new BigDecimal("3.04"))
                .pfValue(new BigDecimal("1403.60"))
                .build();
        assertEquals(expected, actual);
    }

    @Test
    void getStockById_whenIdIsValid() {
        //GIVEN
        Stock stock = Stock.builder()
                .symbol("AAPL")
                .costPrice(new BigDecimal("1400.56"))
                .shares(BigDecimal.TEN)
                .value(new BigDecimal("1403.60"))
                .price(new BigDecimal("140.36"))
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(new BigDecimal("3.04"))
                .build();
        stockRepo.insert(stock);

        //WHEN
        Stock actual = testClient.get()
                .uri("/api/stock/" + stock.getId())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Stock.class)
                .returnResult()
                .getResponseBody();

        //THEN
        Stock expected = Stock.builder()
                .id(actual.getId())
                .symbol("AAPL")
                .costPrice(new BigDecimal("1400.56"))
                .shares(BigDecimal.TEN)
                .value(new BigDecimal("1403.60"))
                .price(new BigDecimal("140.36"))
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(new BigDecimal("3.04"))
                .build();
        assertEquals(expected, actual);
    }

    @Test
    void getStockById_whenIdIsNotValid() {
        //GIVEN
        Stock stock = Stock.builder()
                .symbol("AAPL")
                .costPrice(new BigDecimal("1400.56"))
                .shares(BigDecimal.TEN)
                .value(new BigDecimal("1403.60"))
                .price(new BigDecimal("140.36"))
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(new BigDecimal("3.04"))
                .build();
        stockRepo.insert(stock);

        //WHEN
        testClient.get()
                .uri("/api/stock/" + "123")
                .exchange()
                //THEN
                .expectStatus().is4xxClientError();
    }

    @Test
    void updateStock_whenIncreaseStock() {
        //GIVEN
        CreateStockDto stock = CreateStockDto.builder()
                .symbol("AAPL")
                .costPrice(new BigDecimal("1500.00"))
                .shares(BigDecimal.TEN)
                .build();

        Stock stockDB = Stock.builder()
                .symbol("AAPL")
                .costPrice(new BigDecimal("1400.56"))
                .shares(BigDecimal.TEN)
                .value(new BigDecimal("1403.60"))
                .price(new BigDecimal("140.36"))
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(new BigDecimal("3.04"))
                .totalReturnPercent(new BigDecimal("0.2200"))
                .build();
        stockRepo.insert(stockDB);

        //WHEN
        Stock actual = testClient.put()
                .uri("api/stock")
                .bodyValue(stock)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Stock.class)
                .returnResult()
                .getResponseBody();

        //THEN
        Stock expected = Stock.builder()
                .id(actual.getId())
                .symbol("AAPL")
                .costPrice(new BigDecimal("2900.56"))
                .shares(new BigDecimal("20"))
                .value(new BigDecimal("2807.20"))
                .price(new BigDecimal("140.36"))
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(new BigDecimal("-93.36"))
                .totalReturnPercent(new BigDecimal("-3.2200"))
                .build();
        assertEquals(expected, actual);
    }

    @Test
    void updateStock_whenDecreaseStock() {
        //GIVEN
        CreateStockDto stock = CreateStockDto.builder()
                .symbol("AAPL")
                .costPrice(new BigDecimal("-1500.00"))
                .shares(new BigDecimal("-5"))
                .build();

        Stock stockDB = Stock.builder()
                .symbol("AAPL")
                .costPrice(new BigDecimal("1400.56"))
                .shares(BigDecimal.TEN)
                .value(new BigDecimal("1403.60"))
                .price(new BigDecimal("140.36"))
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(new BigDecimal("3.04"))
                .totalReturnPercent(new BigDecimal("0.2200"))
                .build();
        stockRepo.insert(stockDB);

        //WHEN
        Stock actual = testClient.put()
                .uri("api/stock")
                .bodyValue(stock)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Stock.class)
                .returnResult()
                .getResponseBody();

        //THEN
        Stock expected = Stock.builder()
                .id(actual.getId())
                .symbol("AAPL")
                .costPrice(new BigDecimal("-99.44"))
                .shares(new BigDecimal("5"))
                .value(new BigDecimal("701.80"))
                .price(new BigDecimal("140.36"))
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(new BigDecimal("801.24"))
                .totalReturnPercent(new BigDecimal("-805.7500"))
                .build();
        assertEquals(expected, actual);
    }

    @Test
    void updateStock_whenSharesIsZero() {
        //GIVEN
        CreateStockDto stock = CreateStockDto.builder()
                .symbol("AAPL")
                .costPrice(new BigDecimal("-1500.00"))
                .shares(new BigDecimal("-10"))
                .build();

        Stock stockDB = Stock.builder()
                .symbol("AAPL")
                .costPrice(new BigDecimal("1400.56"))
                .shares(BigDecimal.TEN)
                .value(new BigDecimal("1403.60"))
                .price(new BigDecimal("140.36"))
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(new BigDecimal("3.04"))
                .build();
        stockRepo.insert(stockDB);

        //WHEN
        Stock actual = testClient.put()
                .uri("api/stock")
                .bodyValue(stock)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Stock.class)
                .returnResult()
                .getResponseBody();

        //THEN
        assertNull(actual);
    }

    @Test
    void stockSearchResult_whenThereIsAResult() {
        //GIVEN
        stubFor(get("/search-name?query=" + "weru" + "&limit=10&exchange=NASDAQ&apikey=" + API_KEY)
                .willReturn(okJson(searchJson)));
        stubFor(get("/search-name?query=" + "weru" + "&limit=10&exchange=NYSE&apikey=" + API_KEY)
                .willReturn(okJson(searchJson)));

        //WHEN
        List<SearchStock> actual = testClient.get()
                .uri("/api/stock/search/" + "weru")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(SearchStock.class)
                .returnResult()
                .getResponseBody();

        //Then
        List<SearchStock> expected = List.of(
                SearchStock.builder().name("PowerUp Acquisition Corp.").symbol("PWUP").build(),
                SearchStock.builder().name("PowerUp Acquisition Corp.").symbol("PWUPU").build()
        );
        assertEquals(expected, actual);
    }

    @Test
    void stockSearchResult_whenThereIsNoResult() {
        //GIVEN
        stubFor(get("/search-name?query=" + "ggg" + "&limit=10&exchange=NASDAQ&apikey=" + API_KEY)
                .willReturn(okJson(searchJsonEmpty)));
        stubFor(get("/search-name?query=" + "ggg" + "&limit=10&exchange=NYSE&apikey=" + API_KEY)
                .willReturn(okJson(searchJsonEmpty)));

        //WHEN
        List<SearchStock> actual = testClient.get()
                .uri("/api/stock/search/" + "ggg")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(SearchStock.class)
                .returnResult()
                .getResponseBody();

        //Then
        List<SearchStock> expected = List.of();
        assertEquals(expected, actual);
    }

    @Test
    void stockSearchResult_withInvalidApikey() {
        //GIVEN
        stubFor(get("/search-name?query=" + "ggg" + "&limit=10&exchange=NASDAQ&apikey=" + API_KEY + "q")
                .willReturn(okJson(searchJsonInvalidApikey)));

        //WHEN
        testClient.get()
                .uri("/api/stock/search/" + "ggg")
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void stockSearchResult_withUnexpectedResponse() {
        //GIVEN
        stubFor(get("/search-name?query=" + "ggg" + "&limit=10&exchange=NASDAQ&apikey=" + API_KEY)
                .willReturn(null));

        //WHEN
        testClient.get()
                .uri("/api/stock/search/" + "ggg")
                .exchange()
                .expectStatus().is5xxServerError();
    }

    String json = """
            [ {
              "symbol" : "AAPL",
              "price" : 140.36,
              "companyName" : "Apple Inc.",
              "isin" : "US0378331005",
              "website" : "https://www.apple.com",
              "image" : "https://financialmodelingprep.com/image-stock/AAPL.png"
            } ]""";

    String searchJson = """
            [ {
              "symbol" : "PWUP",
              "name" : "PowerUp Acquisition Corp."
            }, {
              "symbol" : "PWUPU",
              "name" : "PowerUp Acquisition Corp."
            } ]""";

    String searchJsonEmpty = """
            []""";

    String searchJsonInvalidApikey = """
            [ {
              "symbol" : null
              "name" : null
            } ]""";

}