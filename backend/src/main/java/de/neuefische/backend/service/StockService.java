package de.neuefische.backend.service;

import de.neuefische.backend.dto.CreateStockDto;
import de.neuefische.backend.model.Stock;
import de.neuefische.backend.repository.StockRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        if(newStock.getShares() <= 0 || newStock.getPrice() <= 0 || newStock.getSymbol() == null) {
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
        getStockPrice();
        return repo.findAll();
    }

    public void getStockPrice() {


//        LocalDate dateTimer = LocalDate.of(2022, 5, 22);
//
//        if (!dateTimer.isEqual(LocalDate.now())) {
//            System.out.println("läuft!");
//            System.out.println(dateTimer);
//            System.out.println(LocalDate.now());
//            dateTimer = LocalDate.now();
//            System.out.println(dateTimer);
//        }

    }
}
