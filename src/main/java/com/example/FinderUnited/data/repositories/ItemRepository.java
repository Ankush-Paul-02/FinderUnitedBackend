package com.example.FinderUnited.data.repositories;

import com.example.FinderUnited.data.entities.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends MongoRepository<Item, String> {

    List<Item> findItemsByOwnerId(String userId);
}
