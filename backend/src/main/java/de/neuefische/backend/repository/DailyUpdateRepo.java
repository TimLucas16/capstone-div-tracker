package de.neuefische.backend.repository;

import de.neuefische.backend.model.Portfolio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyUpdateRepo extends MongoRepository<Portfolio, String> {
    Portfolio findByName(String name);
    boolean existsByName(String name);
}
