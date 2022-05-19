package de.neuefische.backend.service;

import de.neuefische.backend.dto.CreateStockDto;
import de.neuefische.backend.model.Stock;
import de.neuefische.backend.repository.StockRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    private final StockRepo repo;

    @Autowired
    public StockService(StockRepo repo) {
        this.repo = repo;
    }

    public Stock addNewStock(CreateStockDto newStock) {
        if(newStock.getAmountOfShares() <= 0) {
            throw new IllegalArgumentException("Number of shares can´t be 0 or less");
        }
        Stock stock = new Stock();
        stock.setName(newStock.getName());
        stock.setSymbol(newStock.getSymbol());
        stock.setAmountOfShares(newStock.getAmountOfShares());
        return repo.insert(stock);
    }
}
