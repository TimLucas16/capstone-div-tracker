package de.neuefische.backend.controller;

import de.neuefische.backend.dto.CreateStockDto;
import de.neuefische.backend.model.Portfolio;
import de.neuefische.backend.model.SearchStock;
import de.neuefische.backend.model.Stock;
import de.neuefische.backend.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final StockService service;

    @Autowired
    public StockController(StockService service) {
        this.service = service;
    }

    @PostMapping
    public Stock addNewStock(@RequestBody CreateStockDto newStock) {
        log.info("Post new stock was called with body: " + newStock);
        return service.addNewStock(newStock);
    }

    @GetMapping
    public List<Stock> getAllStocks() {
        return service.getAllStocks();
    }

    @GetMapping(path = "portfolio")
    public Portfolio getPortfolioValues() {
        return service.getPortfolioValues();
    }

    @PutMapping
    public Stock updateStock(@RequestBody CreateStockDto updatedStock) {
        log.info("Put update stock was called with body: " + updatedStock);
        return service.updateStock(updatedStock);
    }

    @GetMapping("{id}")
    public Stock getStockById(@PathVariable String id) {
        return service.getStockById(id);
    }

    @GetMapping(path = "search/{company}")
    public List<SearchStock> stockSearchResult(@PathVariable String company) {
        return service.stockSearchResult(company);
    }
}
