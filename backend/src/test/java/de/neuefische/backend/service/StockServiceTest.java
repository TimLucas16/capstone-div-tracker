package de.neuefische.backend.service;

import de.neuefische.backend.dto.CreateStockDto;
import de.neuefische.backend.model.Stock;
import de.neuefische.backend.repository.StockRepo;
import org.junit.jupiter.api.Test;


import java.text.NumberFormat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockServiceTest {

    private final StockRepo stockRepo = mock(StockRepo.class);
    private final ApiService apiService = mock(ApiService.class);

    private final StockService stockService = new StockService(stockRepo, apiService);

    @Test
    void addNewStock() {
        //GIVEN
        Stock stockToInsert = Stock.builder()
                .symbol("AAPL")
                .shares(10)
                .costPrice(280.56)
                .value(280.35)
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .price(28.0345)
                .totalReturn(-0.21)
                .build();

        when(stockRepo.insert(stockToInsert)).thenReturn(Stock.builder()
                .id("123456")
                .symbol("AAPL")
                .shares(10)
                .costPrice(280.56)
                .value(280.35)
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .price(28.0345)
                .totalReturn(-0.21)
                .build());

        when(apiService.getProfileBySymbol("AAPL")).thenReturn(Stock.builder()
                .companyName("Apple Inc.")
                .price(28.0345)
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .build());

        //WHEN
        CreateStockDto newStock = CreateStockDto.builder()
                .symbol("AAPL")
                .costPrice(280.56)
                .shares(10)
                .build();
        Stock actual = stockService.addNewStock(newStock);

        //THEN
        Stock expected = Stock.builder()
                .id("123456")
                .symbol("AAPL")
                .costPrice(280.56)
                .shares(10)
                .value(280.35)
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .price(28.0345)
                .totalReturn(-0.21)
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

    @Test
    void calcValuea() {
        assertEquals(143.66, StockService.calcValue(14.3655, 10));
        assertEquals(143.62, StockService.calcValue(14.3624, 10));
    }

    @Test
    void calcTotalReturn() {
        assertEquals(-0.34,StockService.calcTotalReturn(265.87, 266.21));
    }
}
