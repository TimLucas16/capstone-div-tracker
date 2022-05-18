package de.neuefische.backend.controller;

import de.neuefische.backend.dto.CreateStock;
import de.neuefische.backend.model.Stock;
import de.neuefische.backend.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final StockService service;

    @Autowired
    public StockController(StockService service) {
        this.service = service;
    }

    @PostMapping
    public Stock addNewStock(@RequestBody CreateStock newStock) {
        return service.addNewStock(newStock);
    }
}
