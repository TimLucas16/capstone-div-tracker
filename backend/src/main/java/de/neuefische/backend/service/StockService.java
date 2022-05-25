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
    private final ApiService apiService;

    @Autowired
    public StockService(StockRepo repo, ApiService apiService) {
        this.repo = repo;
        this.apiService = apiService;
    }

    public Stock addNewStock(CreateStockDto newStock) {
        if (newStock.getShares() <= 0 || newStock.getCostPrice() <= 0 || newStock.getSymbol() == null) {
            throw new IllegalArgumentException("shares or course can´t be 0 or less");
        }
        Stock apiStock = apiService.getProfileBySymbol(newStock.getSymbol());
        Stock stock = Stock.builder()
                .symbol(newStock.getSymbol())
                .shares(newStock.getShares())
                .costPrice(newStock.getCostPrice())
                .value(calcValue(apiStock.getPrice(), newStock.getShares()))
                .companyName(apiStock.getCompanyName())
                .website(apiStock.getWebsite())
                .image(apiStock.getImage())
                .price(apiStock.getPrice())
                .build();
        return repo.insert(stock);
    }

    public List<Stock> getAllStocks() {
        changePrice(getStockPrice());
        return repo.findAll();
    }

    public List<String> getAllSymbols() {
        return repo.findAll().stream()
                .map(Stock::getSymbol)
                .toList();
    }

    public void changePrice(List<Stock> stockList) {

        for (Stock stock : stockList) {
            Stock tempStock = repo.findBySymbol(stock.getSymbol());
            tempStock.setPrice(stock.getPrice());
            tempStock.setValue(calcValue(stock.getPrice(), tempStock.getShares()));
            repo.save(tempStock);
        }
    }

    public List<Stock> getStockPrice() {
        List<String> symbolList = getAllSymbols();
        return apiService.getPrice(symbolList);
    }

    public double calcValue(double price, double shares) {
        return price * shares;
    }
}










