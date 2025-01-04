package com.example.FinderUnited.data.repositories;

import com.example.FinderUnited.data.entities.File;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends MongoRepository<File, String> {
}
