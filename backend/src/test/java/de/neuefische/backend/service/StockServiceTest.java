package de.neuefische.backend.service;

import de.neuefische.backend.dto.CreateStockDto;
import de.neuefische.backend.model.Stock;
import de.neuefische.backend.repository.DailyUpdateRepo;
import de.neuefische.backend.repository.StockRepo;
import org.junit.jupiter.api.Test;

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
                .shares(10)
                .costPrice(28056)
                .value(28030)
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .price(28.03)
                .totalReturn(-26)
                .build();

        when(stockRepo.insert(stockToInsert)).thenReturn(Stock.builder()
                .id("123456")
                .symbol("AAPL")
                .shares(10)
                .costPrice(28056)
                .value(28030)
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .price(28.03)
                .totalReturn(-26)
                .build());

        when(apiService.getProfileBySymbol("AAPL")).thenReturn(Stock.builder()
                .companyName("Apple Inc.")
                .price(28.03)
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .build());

        //WHEN
        CreateStockDto newStock = CreateStockDto.builder()
                .symbol("AAPL")
                .costPrice(28056)
                .shares(10)
                .build();
        Stock actual = stockService.addNewStock(newStock);

        //THEN
        Stock expected = Stock.builder()
                .id("123456")
                .symbol("AAPL")
                .costPrice(28056)
                .shares(10)
                .value(28030)
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .price(28.03)
                .totalReturn(-26)
                .build();
        verify(stockRepo).insert(stockToInsert);
        assertEquals(expected, actual);
    }

    @Test
    void addNewStock_whenAmountOfSharesIsZeroOrLess_shouldThrowException() {
        //GIVEN + WHEN
        CreateStockDto newStock = CreateStockDto.builder()
                .symbol("AAPL")
                .costPrice(10)
                .shares(0).build();

        //THEN
        assertThrows(IllegalArgumentException.class, () -> stockService.addNewStock(newStock));
    }

}
