package com.example.FinderUnited.data.repositories;

import com.example.FinderUnited.data.entities.Location;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends MongoRepository<Location, String> {
}
