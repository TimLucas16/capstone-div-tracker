package de.neuefische.backend.controller;

import de.neuefische.backend.dto.CreateStockDto;
import de.neuefische.backend.model.Portfolio;
import de.neuefische.backend.model.SearchStock;
import de.neuefische.backend.model.Stock;
import de.neuefische.backend.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Stock updateStock(@RequestBody CreateStockDto updatedstock) {
        return service.updateStock(updatedstock);
    }

    @GetMapping("{id}")
    public Stock getStockById(@PathVariable String id) {
        return service.getStockById(id);
    }

    @GetMapping(path = "search/{company}")
    public SearchStock[] stockSearchResult(@PathVariable String company) {
        return service.stockSearchResult(company);
    }
}

