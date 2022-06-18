package de.neuefische.backend.service;

import de.neuefische.backend.dto.CreateStockDto;
import de.neuefische.backend.model.DailyUpdate;
import de.neuefische.backend.model.SearchStock;
import de.neuefische.backend.model.Stock;
import de.neuefische.backend.repository.DailyUpdateRepo;
import de.neuefische.backend.repository.StockRepo;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockServiceTest {

    private final StockRepo stockRepo = mock(StockRepo.class);
    private final DailyUpdateRepo dURepo = mock(DailyUpdateRepo.class);
    private final ApiService apiService = mock(ApiService.class);

    private final StockService stockService = new StockService(stockRepo, dURepo, apiService);

    @Test
    void addNewStock() {
        //GIVEN
        Stock stockToInsert = Stock.builder()
                .symbol("AAPL")
                .shares(BigDecimal.TEN)
                .costPrice(new BigDecimal("280.56"))
                .value(new BigDecimal("280.30"))
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .price(new BigDecimal("28.03"))
                .totalReturn(new BigDecimal("-0.26"))
                .totalReturnPercent(new BigDecimal("-0.0900"))
                .build();

        when(stockRepo.insert(stockToInsert)).thenReturn(Stock.builder()
                .id("123456")
                .symbol("AAPL")
                .shares(BigDecimal.TEN)
                .costPrice(new BigDecimal("280.56"))
                .value(new BigDecimal("280.30"))
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .price(new BigDecimal("28.03"))
                .totalReturn(new BigDecimal("-0.26"))
                .totalReturnPercent(new BigDecimal("-0.0900"))
                .build());

        when(apiService.getProfileBySymbol("AAPL")).thenReturn(Stock.builder()
                .companyName("Apple Inc.")
                .price(new BigDecimal("28.03"))
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .build());

        //WHEN
        CreateStockDto newStock = CreateStockDto.builder()
                .symbol("AAPL")
                .costPrice(new BigDecimal("280.56"))
                .shares(BigDecimal.TEN)
                .build();
        Stock actual = stockService.addNewStock(newStock);

        //THEN
        Stock expected = Stock.builder()
                .id("123456")
                .symbol("AAPL")
                .costPrice(new BigDecimal("280.56"))
                .shares(BigDecimal.TEN)
                .value(new BigDecimal("280.30"))
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .price(new BigDecimal("28.03"))
                .totalReturn(new BigDecimal("-0.26"))
                .totalReturnPercent(new BigDecimal("-0.0900"))
                .build();
        verify(stockRepo).insert(stockToInsert);
        assertEquals(expected, actual);
    }

    @Test
    void addNewStock_whenAmountOfSharesIsZeroOrLess_shouldThrowException() {
        //GIVEN + WHEN
        CreateStockDto newStock = CreateStockDto.builder()
                .symbol("AAPL")
                .costPrice(BigDecimal.TEN)
                .shares(BigDecimal.ZERO).build();

        //THEN
        assertThrows(IllegalArgumentException.class, () -> stockService.addNewStock(newStock));
    }

    @Test
    void getAllStocks() {
        //GIVEN
        Stock stock1 = Stock.builder()
                .id("123456")
                .symbol("AAPL")
                .costPrice(new BigDecimal("280.56"))
                .shares(BigDecimal.TEN)
                .value(new BigDecimal("280.30"))
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .price(new BigDecimal("28.03"))
                .totalReturn(new BigDecimal("-0.26"))
                .totalReturnPercent(new BigDecimal("-0.0900"))
                .build();

        Stock stock2 = Stock.builder()
                .id("789")
                .symbol("MSFT")
                .costPrice(new BigDecimal("2300"))
                .shares(BigDecimal.TEN)
                .value(new BigDecimal("2476.5"))
                .companyName("Microsoft Corporation")
                .website("https://www.microsoft.com")
                .image("https://financialmodelingprep.com/image-stock/MSFT.png")
                .price(new BigDecimal("247.65"))
                .totalReturn(new BigDecimal("176.50"))
                .totalReturnPercent(new BigDecimal("7.67"))
                .build();
        when(stockRepo.findAll()).thenReturn(List.of(stock1, stock2));
        when(dURepo.findByName("Portfolio")).thenReturn(DailyUpdate.builder()
                .name("Portfolio")
                .updateDay(LocalDate.now())
                .build());

        //WHEN
        List<Stock> actual = stockService.getAllStocks();

        //THEN
        List<Stock> expected = List.of(
                Stock.builder()
                        .id("123456")
                        .symbol("AAPL")
                        .costPrice(new BigDecimal("280.56"))
                        .shares(BigDecimal.TEN)
                        .value(new BigDecimal("280.30"))
                        .companyName("Apple Inc.")
                        .website("https://www.apple.com")
                        .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                        .price(new BigDecimal("28.03"))
                        .totalReturn(new BigDecimal("-0.26"))
                        .totalReturnPercent(new BigDecimal("-0.0900"))
                        .build(),
                Stock.builder()
                        .id("789")
                        .symbol("MSFT")
                        .costPrice(new BigDecimal("2300"))
                        .shares(BigDecimal.TEN)
                        .value(new BigDecimal("2476.5"))
                        .companyName("Microsoft Corporation")
                        .website("https://www.microsoft.com")
                        .image("https://financialmodelingprep.com/image-stock/MSFT.png")
                        .price(new BigDecimal("247.65"))
                        .totalReturn(new BigDecimal("176.50"))
                        .totalReturnPercent(new BigDecimal("7.67"))
                        .build());
        verify(stockRepo).findAll();
        assertEquals(expected, actual);
    }

    @Test
    void getAllSymbols() {
        //GIVEN
        Stock stock1 = Stock.builder()
                .id("123456")
                .symbol("AAPL")
                .costPrice(new BigDecimal("280.56"))
                .shares(BigDecimal.TEN)
                .value(new BigDecimal("280.30"))
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .price(new BigDecimal("28.03"))
                .totalReturn(new BigDecimal("-0.26"))
                .totalReturnPercent(new BigDecimal("-0.0900"))
                .build();

        Stock stock2 = Stock.builder()
                .id("789")
                .symbol("MSFT")
                .costPrice(new BigDecimal("2300"))
                .shares(BigDecimal.TEN)
                .value(new BigDecimal("2476.5"))
                .companyName("Microsoft Corporation")
                .website("https://www.microsoft.com")
                .image("https://financialmodelingprep.com/image-stock/MSFT.png")
                .price(new BigDecimal("247.65"))
                .totalReturn(new BigDecimal("176.50"))
                .totalReturnPercent(new BigDecimal("7.67"))
                .build();
        when(stockRepo.findAll()).thenReturn(List.of(stock1, stock2));

        //WHEN
        List<String> actual = stockService.getAllSymbols();

        //THEN
        List<String> expected = List.of("AAPL", "MSFT");
        verify(stockRepo).findAll();
        assertEquals(expected, actual);
    }

    @Test
    void getStockById_whenIdIsValid() {
        //GIVEN
        when(stockRepo.findById("123")).thenReturn(Optional.of(Stock.builder()
                .id("123")
                .symbol("AAPL")
                .shares(BigDecimal.TEN)
                .costPrice(new BigDecimal("280.56"))
                .value(new BigDecimal("280.30"))
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .price(new BigDecimal("28.03"))
                .totalReturn(new BigDecimal("-0.26"))
                .build()));

        //WHEN
        Stock actual = stockService.getStockById("123");

        //THEN
        Stock expected = Stock.builder()
                .id("123")
                .symbol("AAPL")
                .shares(BigDecimal.TEN)
                .costPrice(new BigDecimal("280.56"))
                .value(new BigDecimal("280.30"))
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .price(new BigDecimal("28.03"))
                .totalReturn(new BigDecimal("-0.26"))
                .build();

        verify(stockRepo).findById("123");
        assertEquals(expected, actual);
    }

    @Test
    void getStockById_whenIdIsNotValid_shouldThrowException() {
        //GIVEN
        when(stockRepo.findById("123")).thenReturn(Optional.empty());

        //WHEN THEN
        assertThrows(NoSuchElementException.class, () -> stockService.getStockById("123"));
        verify(stockRepo).findById("123");
    }

    @Test
    void updateStock_whenIncreaseStock() {
        //GIVEN
        Stock updatedStock = Stock.builder()
                .id("234")
                .symbol("AAPL")
                .costPrice(new BigDecimal("2706.16"))
                .shares(new BigDecimal("20"))
                .value(new BigDecimal("2807.20"))
                .price(new BigDecimal("140.36"))
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(new BigDecimal("101.04"))
                .totalReturnPercent(new BigDecimal("3.7300"))
                .build();
        CreateStockDto newStock = CreateStockDto.builder()
                .symbol("AAPL")
                .costPrice(new BigDecimal("1305.60"))
                .shares(BigDecimal.TEN)
                .build();
        when(stockRepo.save(updatedStock)).thenReturn(Stock.builder()
                .id("234")
                .symbol("AAPL")
                .costPrice(new BigDecimal("2706.16"))
                .shares(new BigDecimal("20"))
                .value(new BigDecimal("2807.20"))
                .price(new BigDecimal("140.36"))
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(new BigDecimal("101.04"))
                .totalReturnPercent(new BigDecimal("3.7300"))
                .build());
        when(stockRepo.findBySymbol(newStock.getSymbol())).thenReturn(Stock.builder()
                .id("234")
                .symbol("AAPL")
                .costPrice(new BigDecimal("1400.56"))
                .shares(BigDecimal.TEN)
                .value(new BigDecimal("1403.60"))
                .price(new BigDecimal("140.36"))
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(new BigDecimal("0.304"))
                .totalReturnPercent(new BigDecimal("0.02"))
                .build());

        //WHEN
        Stock actual = stockService.updateStock(newStock);

        //THEN
        Stock expected = Stock.builder()
                .id("234")
                .symbol("AAPL")
                .costPrice(new BigDecimal("2706.16"))
                .shares(new BigDecimal("20"))
                .value(new BigDecimal("2807.20"))
                .price(new BigDecimal("140.36"))
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(new BigDecimal("101.04"))
                .totalReturnPercent(new BigDecimal("3.7300"))
                .build();

        verify(stockRepo).findBySymbol("AAPL");
        verify(stockRepo).save(updatedStock);
        assertEquals(expected, actual);
    }

    @Test
    void updateStock_whenDecreaseStock() {
        //GIVEN
        Stock updatedStock = Stock.builder()
                .id("234")
                .symbol("AAPL")
                .costPrice(new BigDecimal("594.96"))
                .shares(new BigDecimal("5"))
                .value(new BigDecimal("701.80"))
                .price(new BigDecimal("140.36"))
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(new BigDecimal("106.84"))
                .totalReturnPercent(new BigDecimal("17.9600"))
                .build();
        CreateStockDto newStock = CreateStockDto.builder()
                .symbol("AAPL")
                .costPrice(new BigDecimal("-805.60"))
                .shares(new BigDecimal("-5"))
                .build();
        when(stockRepo.save(updatedStock)).thenReturn(Stock.builder()
                .id("234")
                .symbol("AAPL")
                .costPrice(new BigDecimal("594.96"))
                .shares(new BigDecimal("5"))
                .value(new BigDecimal("701.80"))
                .price(new BigDecimal("140.36"))
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(new BigDecimal("106.84"))
                .totalReturnPercent(new BigDecimal("17.9600"))
                .build());
        when(stockRepo.findBySymbol(newStock.getSymbol())).thenReturn(Stock.builder()
                .id("234")
                .symbol("AAPL")
                .costPrice(new BigDecimal("1400.56"))
                .shares(BigDecimal.TEN)
                .value(new BigDecimal("1403.60"))
                .price(new BigDecimal("140.36"))
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(new BigDecimal("3.04"))
                .totalReturnPercent(new BigDecimal("0.22"))
                .build());

        //WHEN
        Stock actual = stockService.updateStock(newStock);

        //THEN
        Stock expected = Stock.builder()
                .id("234")
                .symbol("AAPL")
                .costPrice(new BigDecimal("594.96"))
                .shares(new BigDecimal("5"))
                .value(new BigDecimal("701.80"))
                .price(new BigDecimal("140.36"))
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(new BigDecimal("106.84"))
                .totalReturnPercent(new BigDecimal("17.9600"))
                .build();

        verify(stockRepo).findBySymbol("AAPL");
        verify(stockRepo).save(updatedStock);
        assertEquals(expected, actual);
    }

    @Test
    void updateStock_whenSaresIsZero() {
        //GIVEN
        CreateStockDto newStock = CreateStockDto.builder()
                .symbol("AAPL")
                .costPrice(new BigDecimal("-2805.60"))
                .shares(new BigDecimal("-10"))
                .build();
        when(stockRepo.findBySymbol(newStock.getSymbol())).thenReturn(Stock.builder()
                .id("234")
                .symbol("AAPL")
                .costPrice(new BigDecimal("1400.56"))
                .shares(BigDecimal.TEN)
                .value(new BigDecimal("1403.60"))
                .price(new BigDecimal("140.36"))
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(new BigDecimal("3.04"))
                .build());

        //WHEN
        Stock actual = stockService.updateStock(newStock);

        //THEN
        verify(stockRepo).findBySymbol("AAPL");
        verify(stockRepo).deleteById("234");
        assertNull(actual);
    }

    @Test
    void stockSearchResult_whenThereIsAResult() {
        //GIVEN
        when(apiService.stockSearchResult("weru")).thenReturn(List.of(
                SearchStock.builder().name("PowerUp Acquisition Corp.").symbol("PWUP").build(),
                SearchStock.builder().name("PowerUp Acquisition Corp.").symbol("PWUPW").build(),
                SearchStock.builder().name("PowerUp Acquisition Corp.").symbol("PWUPU").build()
        ));

        //WHEN
        List<SearchStock> actual = stockService.stockSearchResult("weru");

        //THEN
        List<SearchStock> expected = List.of(
                SearchStock.builder().name("PowerUp Acquisition Corp.").symbol("PWUP").build(),
                SearchStock.builder().name("PowerUp Acquisition Corp.").symbol("PWUPW").build(),
                SearchStock.builder().name("PowerUp Acquisition Corp.").symbol("PWUPU").build()
        );
        verify(apiService).stockSearchResult("weru");
        assertEquals(expected, actual);
    }

    @Test
    void stockSearchResult_whenThereIsNoResult() {
        //GIVEN
        when(apiService.stockSearchResult("ggg")).thenReturn(List.of());

        //WHEN
        List<SearchStock> actual = stockService.stockSearchResult("ggg");

        //THEN
        List<SearchStock> expected = List.of();
        verify(apiService).stockSearchResult("ggg");
        assertEquals(expected, actual);
    }

    @Test
    void calcPfTotalReturnPercent() {
        //GIVEN
        when(stockRepo.findAll()).thenReturn(List.of(Stock.builder()
                .id("123")
                .symbol("AAPL")
                .costPrice(new BigDecimal("1400.56"))
                .shares(new BigDecimal("10"))
                .value(new BigDecimal("1403.60"))
                .price(new BigDecimal("140.36"))
                .totalReturn(new BigDecimal("3.04"))
                .build()));

        //WHEN
        BigDecimal actual = stockService.calcPfTotalReturnPercent();

        //THEN
        assertEquals(new BigDecimal("0.2200"), actual);
    }
}
