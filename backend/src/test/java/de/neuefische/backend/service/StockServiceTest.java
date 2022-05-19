package de.neuefische.backend.service;

import de.neuefische.backend.dto.CreateStockDto;
import de.neuefische.backend.model.Stock;
import de.neuefische.backend.repository.StockRepo;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockServiceTest {

    private final StockRepo stockRepo = mock(StockRepo.class);

    private final StockService stockService = new StockService(stockRepo);

    @Test
    void addNewStock() {
        //GIVEN
        Stock stockToInsert = Stock.builder().name("Apple").symbol("AAPL").course(10).amountOfShares(10).build();
        when(stockRepo.insert(stockToInsert)).thenReturn(Stock.builder().id("123456").name("Apple").symbol("AAPL").course(10).amountOfShares(10).build());

        //WHEN
        CreateStockDto newStock = CreateStockDto.builder().name("Apple").symbol("AAPL").course(10).amountOfShares(10).build();
        Stock actual = stockService.addNewStock(newStock);

        //THEN
        Stock expected = Stock.builder().id("123456").name("Apple").symbol("AAPL").course(10).amountOfShares(10).build();
        verify(stockRepo).insert(stockToInsert);
        assertEquals(expected, actual);
    }

    @Test
    void addNewStock_whenAmountOfSharesIsZeroOrLess_shouldThrowException() {
        //GIVEN + WHEN
        CreateStockDto newStock = CreateStockDto.builder().name("Apple").symbol("AAPL").amountOfShares(0).build();

        //THEN
        assertThrows(IllegalArgumentException.class, () -> stockService.addNewStock(newStock));
    }
}
