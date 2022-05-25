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
                .totalReturn(calcTotalReturn((calcValue(apiStock.getPrice(), newStock.getShares())), newStock.getCostPrice()))
                .build();
        return repo.insert(stock);
    }

    public List<Stock> getAllStocks() {
        updateStock(getUpdatedStock());
        return repo.findAll();
    }

    public List<String> getAllSymbols() {
        return repo.findAll().stream()
                .map(Stock::getSymbol)
                .toList();
    }

    public void updateStock(List<Stock> stockList) {

        for (Stock stock : stockList) {
            Stock tempStock = repo.findBySymbol(stock.getSymbol());
            tempStock.setPrice(stock.getPrice());
            tempStock.setValue(calcValue(stock.getPrice(), tempStock.getShares()));
            tempStock.setTotalReturn(calcTotalReturn((calcValue(stock.getPrice(), tempStock.getShares())), tempStock.getCostPrice()));
            repo.save(tempStock);
        }
    }

    public List<Stock> getUpdatedStock() {
        List<String> symbolList = getAllSymbols();
        return apiService.getPrice(symbolList);
    }

    public static double calcValue(double price, double shares) {
        return Math.round((price * shares) * 100)/100.0;
    }

    public static double calcTotalReturn(double value, double costPrice) {
        return Math.round((value - costPrice) * 100)/100.0;
    }
}










