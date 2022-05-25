package de.neuefische.backend.repository;

import de.neuefische.backend.model.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepo extends MongoRepository<Stock, String> {
    Stock findBySymbol(String symbol);
}
