package de.neuefische.backend.service;

import de.neuefische.backend.dto.CreateStockDto;
import de.neuefische.backend.model.SearchStock;
import de.neuefische.backend.model.Stock;
import de.neuefische.backend.repository.DailyUpdateRepo;
import de.neuefische.backend.repository.StockRepo;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
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

    @Test
    void getStockById_whenIdIsValid() {
        //GIVEN
        when(stockRepo.findById("123")).thenReturn(Optional.of(Stock.builder()
                .id("123")
                .symbol("AAPL")
                .shares(10)
                .costPrice(28056)
                .value(28030)
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .price(28.03)
                .totalReturn(-26)
                .build()));

        //WHEN
        Stock actual = stockService.getStockById("123");

        //THEN
        Stock expected = Stock.builder()
                .id("123")
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
                .costPrice(420616)
                .shares(20)
                .value(280720)
                .price(140.36)
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(-139896)
                .build();
        CreateStockDto newStock = CreateStockDto.builder()
                .symbol("AAPL")
                .costPrice(280560)
                .shares(10)
                .build();
        when(stockRepo.save(updatedStock)).thenReturn(Stock.builder()
                .id("234")
                .symbol("AAPL")
                .costPrice(420616)
                .shares(20)
                .value(280720)
                .price(140.36)
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(-139896)
                .build());
        when(stockRepo.findBySymbol(newStock.getSymbol())).thenReturn(Stock.builder()
                .id("234")
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

        //WHEN
        Stock actual = stockService.updateStock(newStock);

        //THEN
        Stock expected = Stock.builder()
                .id("234")
                .symbol("AAPL")
                .costPrice(420616)
                .shares(20)
                .value(280720)
                .price(140.36)
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(-139896)
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
                .costPrice(-140504)
                .shares(5)
                .value(70180)
                .price(140.36)
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(210684)
                .build();
        CreateStockDto newStock = CreateStockDto.builder()
                .symbol("AAPL")
                .costPrice(-280560)
                .shares(-5)
                .build();
        when(stockRepo.save(updatedStock)).thenReturn(Stock.builder()
                .id("234")
                .symbol("AAPL")
                .costPrice(-140504)
                .shares(5)
                .value(70180)
                .price(140.36)
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(210684)
                .build());
        when(stockRepo.findBySymbol(newStock.getSymbol())).thenReturn(Stock.builder()
                .id("234")
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

        //WHEN
        Stock actual = stockService.updateStock(newStock);

        //THEN
        Stock expected = Stock.builder()
                .id("234")
                .symbol("AAPL")
                .costPrice(-140504)
                .shares(5)
                .value(70180)
                .price(140.36)
                .companyName("Apple Inc.")
                .website("https://www.apple.com")
                .image("https://financialmodelingprep.com/image-stock/AAPL.png")
                .totalReturn(210684)
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
                .costPrice(-280560)
                .shares(-10)
                .build();
        when(stockRepo.findBySymbol(newStock.getSymbol())).thenReturn(Stock.builder()
                .id("234")
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

        //WHEN
        Stock actual = stockService.updateStock(newStock);

        //THEN
        verify(stockRepo).findBySymbol("AAPL");
        verify(stockRepo).deleteById("234");
        assertNull(actual);
    }

}
