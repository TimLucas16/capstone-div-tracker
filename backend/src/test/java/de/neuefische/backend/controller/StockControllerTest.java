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

@WireMockTest(httpPort = 8484)
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
    }

    @Test
    void addNewStock() {
        //GIVEN
        CreateStockDto stock = CreateStockDto.builder()
                .symbol("AAPL")
                .price(10)
                .shares(10)
                .build();

        // Mock FMP API
        String json = "[ {\n" +
                "  \"symbol\" : \"AAPL\",\n" +
                "  \"price\" : 137.59,\n" +
                "  \"beta\" : 1.194642,\n" +
                "  \"volAvg\" : 98319826,\n" +
                "  \"mktCap\" : 2226921668608,\n" +
                "  \"lastDiv\" : 0.89,\n" +
                "  \"range\" : \"123.13-182.94\",\n" +
                "  \"changes\" : 0.23999023,\n" +
                "  \"companyName\" : \"Apple Inc.\",\n" +
                "  \"currency\" : \"USD\",\n" +
                "  \"cik\" : \"0000320193\",\n" +
                "  \"isin\" : \"US0378331005\",\n" +
                "  \"cusip\" : \"037833100\",\n" +
                "  \"exchange\" : \"NASDAQ Global Select\",\n" +
                "  \"exchangeShortName\" : \"NASDAQ\",\n" +
                "  \"industry\" : \"Consumer Electronics\",\n" +
                "  \"website\" : \"https://www.apple.com\",\n" +
                "  \"description\" : \"Apple Inc. designs, manufactures, and markets smartphones, personal computers, tablets, wearables, and accessories worldwide. It also sells various related services. In addition, the company offers iPhone, a line of smartphones; Mac, a line of personal computers; iPad, a line of multi-purpose tablets; AirPods Max, an over-ear wireless headphone; and wearables, home, and accessories comprising AirPods, Apple TV, Apple Watch, Beats products, HomePod, and iPod touch. Further, it provides AppleCare support services; cloud services store services; and operates various platforms, including the App Store that allow customers to discover and download applications and digital content, such as books, music, video, games, and podcasts. Additionally, the company offers various services, such as Apple Arcade, a game subscription service; Apple Music, which offers users a curated listening experience with on-demand radio stations; Apple News+, a subscription news and magazine service; Apple TV+, which offers exclusive original content; Apple Card, a co-branded credit card; and Apple Pay, a cashless payment service, as well as licenses its intellectual property. The company serves consumers, and small and mid-sized businesses; and the education, enterprise, and government markets. It distributes third-party applications for its products through the App Store. The company also sells its products through its retail and online stores, and direct sales force; and third-party cellular network carriers, wholesalers, retailers, and resellers. Apple Inc. was incorporated in 1977 and is headquartered in Cupertino, California.\",\n" +
                "  \"ceo\" : \"Mr. Timothy Cook\",\n" +
                "  \"sector\" : \"Technology\",\n" +
                "  \"country\" : \"US\",\n" +
                "  \"fullTimeEmployees\" : \"154000\",\n" +
                "  \"phone\" : \"14089961010\",\n" +
                "  \"address\" : \"1 Apple Park Way\",\n" +
                "  \"city\" : \"Cupertino\",\n" +
                "  \"state\" : \"CALIFORNIA\",\n" +
                "  \"zip\" : \"95014\",\n" +
                "  \"dcfDiff\" : 2.07176,\n" +
                "  \"dcf\" : 139.662,\n" +
                "  \"image\" : \"https://financialmodelingprep.com/image-stock/AAPL.png\",\n" +
                "  \"ipoDate\" : \"1980-12-12\",\n" +
                "  \"defaultImage\" : false,\n" +
                "  \"isEtf\" : false,\n" +
                "  \"isActivelyTrading\" : true,\n" +
                "  \"isAdr\" : false,\n" +
                "  \"isFund\" : false\n" +
                "} ]";
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