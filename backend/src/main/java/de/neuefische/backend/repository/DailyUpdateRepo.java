package de.neuefische.backend.repository;

import de.neuefische.backend.model.DailyUpdate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyUpdateRepo extends MongoRepository<DailyUpdate, String> {
    DailyUpdate findByName(String name);
    boolean existsByName(String name);
}
