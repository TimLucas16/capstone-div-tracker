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
        if (newStock.getShares() <= 0 || newStock.getPrice() <= 0 || newStock.getSymbol() == null) {
            throw new IllegalArgumentException("shares or course can´t be 0 or less");
        }
        Stock apiStock = apiService.getProfileBySymbol(newStock.getSymbol());
        Stock stock = Stock.builder()
                .symbol(newStock.getSymbol())
                .shares(newStock.getShares())
                .price(newStock.getPrice())
                .value(calcValue(newStock.getPrice(), newStock.getShares()))
                .companyName(apiStock.getCompanyName())
                .website(apiStock.getWebsite())
                .image(apiStock.getImage())
                .build();
        return repo.insert(stock);
    }

    public List<Stock> getAllStocks() {
        changePrice(getStockPrice());
        return repo.findAll();
    }

    public List<String> getAllSymbols() {
        List<Stock> stockList = repo.findAll();
        return stockList.stream()
                .map(Stock::getSymbol)
                .toList();
    }

    public void changePrice(List<Stock> stockList) {
//        stockList.stream()
//                .map(tempStock -> repo.findBySymbol(tempStock.getSymbol()))
//                .map(someStock -> someStock.setPrice(tempStock.getPrice())


        for(int i = 0; i < stockList.size(); i++) {
            Stock tempStock = repo.findBySymbol(stockList.get(i).getSymbol());
            tempStock.setPrice(stockList.get(i).getPrice());
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










