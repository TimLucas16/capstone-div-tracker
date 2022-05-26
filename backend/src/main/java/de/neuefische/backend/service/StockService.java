package de.neuefische.backend.service;

import de.neuefische.backend.dto.CreateStockDto;
import de.neuefische.backend.model.DailyUpdate;
import de.neuefische.backend.model.Portfolio;
import de.neuefische.backend.model.Stock;
import de.neuefische.backend.repository.DailyUpdateRepo;
import de.neuefische.backend.repository.StockRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StockService {

    private final StockRepo repo;
    private final DailyUpdateRepo dURepo;
    private final ApiService apiService;

    @Autowired
    public StockService(StockRepo repo, DailyUpdateRepo dURepo, ApiService apiService) {
        this.repo = repo;
        this.dURepo = dURepo;
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
        checkForDailyUpdate();
        return repo.findAll();
    }

    public List<String> getAllSymbols() {
        return repo.findAll().stream()
                .map(Stock::getSymbol)
                .toList();
    }

    public Portfolio getPortfolioValues() {
        return Portfolio.builder()
                .pfValue(calcPortfolioValue())
                .pfTotalReturnAbsolute(calcPfTotalReturnAbs())
                .pfTotalReturnPercent(calcPfTotalReturnPercent())
                .build();
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

    public void checkForDailyUpdate() {
        String name = "Portfolio";
        if(!dURepo.existsByName(name)) {
            dURepo.insert(DailyUpdate.builder()
                    .name(name)
                    .updateDay(LocalDate.of(2022,5,25))
                    .build());
        }

        LocalDate dateTimer = dURepo.findByName(name).getUpdateDay();

        if(!dateTimer.isEqual(LocalDate.now())) {
            updateStock(getUpdatedStock());
            DailyUpdate newDate = dURepo.findByName(name);
            newDate.setUpdateDay(LocalDate.now());
            dURepo.save(newDate);
        }
    }

    public double calcPortfolioValue() {
        return repo.findAll().stream().mapToDouble(Stock::getValue).sum();
    }

    public double calcPfTotalReturnAbs() {
        return calcTotalReturn(repo.findAll().stream().mapToDouble(Stock::getValue).sum(),
                repo.findAll().stream().mapToDouble(Stock::getCostPrice).sum());
    }

    public double calcPfTotalReturnPercent() {
        return (calcPfTotalReturnAbs() / calcPortfolioValue())*100;
    }

    public static double calcValue(double price, double shares) {
        return Math.round((price * shares) * 100)/100.0;
    }

    public static double calcTotalReturn(double value, double costPrice) {
        return Math.round((value - costPrice) * 100)/100.0;
    }
}










