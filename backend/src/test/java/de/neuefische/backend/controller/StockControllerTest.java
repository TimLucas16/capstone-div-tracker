package de.neuefische.backend.controller;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import de.neuefische.backend.dto.CreateStockDto;
import de.neuefische.backend.model.Portfolio;
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
                .costPrice(140043)
                .shares(10)
                .build();

        // Mock FMP API
        stubFor(get("/profile/" + "AAPL" + "?apikey=" + API_KEY2)
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
                .costPrice(140043)
                .shares(10)
                .value(140360)
                .price(140.36)
                .totalReturn(317)
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
                .costPrice(28056)
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
                .costPrice(140056)
                .shares(10)
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
                .costPrice(140056)
                .shares(10)
                .value(140360)
                .price(140.36)
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(304)
                .build());
        assertEquals(expected, actual);
    }

    @Test
    void getPortfolioValues() {
        //GIVEN
        Stock stock = Stock.builder()
                .symbol("AAPL")
                .costPrice(140056)
                .shares(10)
                .value(140360)
                .price(140.36)
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(304)
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
                .pfTotalReturnPercent(0.22)
                .pfTotalReturnAbsolute(304)
                .pfValue(140360)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    void getStockById_whenIdIsValid() {
        //GIVEN
        Stock stock = Stock.builder()
                .symbol("AAPL")
                .costPrice(140056)
                .shares(10)
                .value(140360)
                .price(140.36)
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(304)
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
                .costPrice(140056)
                .shares(10)
                .value(140360)
                .price(140.36)
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(304)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    void getStockById_whenIdIsNotValid() {
        //GIVEN
        Stock stock = Stock.builder()
                .symbol("AAPL")
                .costPrice(140056)
                .shares(10)
                .value(140360)
                .price(140.36)
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(304)
                .build();
        stockRepo.insert(stock);

        //WHEN
        testClient.get()
                .uri("/api/stock/" + "123")
                .exchange()
                //THEN
                .expectStatus().is5xxServerError();
    }

    @Test
    void increaseStock_whenIncreaseStock() {
        //GIVEN
        CreateStockDto stock = CreateStockDto.builder()
                .symbol("AAPL")
                .costPrice(150000)
                .shares(10)
                .build();

        Stock stockDB = Stock.builder()
                .symbol("AAPL")
                .costPrice(140056)
                .shares(10)
                .value(140360)
                .price(140.36)
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(304)
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
                .costPrice(290056)
                .shares(20)
                .value(280720)
                .price(140.36)
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(-9336)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    void increaseStock_whenDecreaseStock() {
        //GIVEN
        CreateStockDto stock = CreateStockDto.builder()
                .symbol("AAPL")
                .costPrice(-150000)
                .shares(-5)
                .build();

        Stock stockDB = Stock.builder()
                .symbol("AAPL")
                .costPrice(140056)
                .shares(10)
                .value(140360)
                .price(140.36)
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(304)
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
                .costPrice(-9944)
                .shares(5)
                .value(70180)
                .price(140.36)
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(80124)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    void increaseStock_whenSharesIsZero() {
        //GIVEN
        CreateStockDto stock = CreateStockDto.builder()
                .symbol("AAPL")
                .costPrice(-150000)
                .shares(-10)
                .build();

        Stock stockDB = Stock.builder()
                .symbol("AAPL")
                .costPrice(140056)
                .shares(10)
                .value(140360)
                .price(140.36)
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(304)
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

    String json = """
            [ {
              "symbol" : "AAPL",
              "price" : 140.36,
              "beta" : 1.194642,
              "volAvg" : 98131227,
              "mktCap" : 2271754584064,
              "lastDiv" : 0.89,
              "range" : "123.13-182.94",
              "changes" : -2.75,
              "companyName" : "Apple Inc.",
              "currency" : "USD",
              "cik" : "0000320193",
              "isin" : "US0378331005",
              "cusip" : "037833100",
              "exchange" : "NASDAQ Global Select",
              "exchangeShortName" : "NASDAQ",
              "industry" : "Consumer Electronics",
              "website" : "https://www.apple.com",
              "description" : "Apple Inc. designs, manufactures, and markets smartphones, personal computers, tablets, wearables, and accessories worldwide. It also sells various related services. In addition, the company offers iPhone, a line of smartphones; Mac, a line of personal computers; iPad, a line of multi-purpose tablets; AirPods Max, an over-ear wireless headphone; and wearables, home, and accessories comprising AirPods, Apple TV, Apple Watch, Beats products, HomePod, and iPod touch. Further, it provides AppleCare support services; cloud services store services; and operates various platforms, including the App Store that allow customers to discover and download applications and digital content, such as books, music, video, games, and podcasts. Additionally, the company offers various services, such as Apple Arcade, a game subscription service; Apple Music, which offers users a curated listening experience with on-demand radio stations; Apple News+, a subscription news and magazine service; Apple TV+, which offers exclusive original content; Apple Card, a co-branded credit card; and Apple Pay, a cashless payment service, as well as licenses its intellectual property. The company serves consumers, and small and mid-sized businesses; and the education, enterprise, and government markets. It distributes third-party applications for its products through the App Store. The company also sells its products through its retail and online stores, and direct sales force; and third-party cellular network carriers, wholesalers, retailers, and resellers. Apple Inc. was incorporated in 1977 and is headquartered in Cupertino, California.",
              "ceo" : "Mr. Timothy Cook",
              "sector" : "Technology",
              "country" : "US",
              "fullTimeEmployees" : "154000",
              "phone" : "14089961010",
              "address" : "1 Apple Park Way",
              "city" : "Cupertino",
              "state" : "CALIFORNIA",
              "zip" : "95014",
              "dcfDiff" : 2.07176,
              "dcf" : 142.432,
              "image" : "https://financialmodelingprep.com/image-stock/AAPL.png",
              "ipoDate" : "1980-12-12",
              "defaultImage" : false,
              "isEtf" : false,
              "isActivelyTrading" : true,
              "isAdr" : false,
              "isFund" : false
            } ]""";

}