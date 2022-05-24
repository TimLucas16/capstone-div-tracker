package de.neuefische.backend.service;

import de.neuefische.backend.dto.CreateStockDto;
import de.neuefische.backend.model.Stock;
import de.neuefische.backend.repository.StockRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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
        if (newStock.getShares() <= 0 || newStock.getPrice() <= 0 || newStock.getSymbol() == null) {
            throw new IllegalArgumentException("shares or course can´t be 0 or less");
        }
        Stock apiStock = apiService.getProfileBySymbol(newStock.getSymbol());
        Stock stock = Stock.builder()
                .symbol(newStock.getSymbol())
                .shares(newStock.getShares())
                .price(newStock.getPrice())
                .companyName(apiStock.getCompanyName())
                .website(apiStock.getWebsite())
                .image(apiStock.getImage())
                .build();
        return repo.insert(stock);
    }

    public List<Stock> getAllStocks() {
        updateStockPrice();
        return repo.findAll();
    }

    public List<String> getAllSymbols() {
        List<Stock> stockList = repo.findAll();
        return stockList.stream()
                .map(symbol -> symbol.getSymbol())
                .toList();
    }

    public void updateStockPrice() {
        changePrice(getStockPrice());
    }

    public List<Stock> getStockPrice() {
        List<String> symbolList = getAllSymbols();
        return apiService.getPrice(symbolList);
    }

    public void changePrice(List<Stock> stockList) {
        for(int i = 0; i < stockList.size(); i++) {
            Stock tempStock = repo.findBySymbol(stockList.get(i).getSymbol());
            tempStock.setPrice(stockList.get(i).getPrice());
            repo.save(tempStock);
        }
    }

}









//        LocalDate dateTimer = LocalDate.of(2022, 5, 22);
//
//        if (!dateTimer.isEqual(LocalDate.now())) {
//            dateTimer = LocalDate.now();
//        }  --Java.instant--
