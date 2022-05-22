package de.neuefische.backend.service;

import de.neuefische.backend.dto.CreateStockDto;
import de.neuefische.backend.model.Stock;
import de.neuefische.backend.repository.StockRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    private final StockRepo repo;

    @Autowired
    public StockService(StockRepo repo) {
        this.repo = repo;
    }

    public Stock addNewStock(CreateStockDto newStock) {
        if(newStock.getShares() <= 0 || newStock.getPrice() <= 0) {
            throw new IllegalArgumentException("Number of shares or course can´t be 0 or less");
        }
        Stock stock = Stock.builder()
                .name(newStock.getName())
                .symbol(newStock.getSymbol())
                .shares(newStock.getShares())
                .price(newStock.getPrice())
                .build();
        return repo.insert(stock);
    }

    public List<Stock> getAllStocks() {
        return repo.findAll();
    }
}
