package com.example.FinderUnited.business.service;

import com.example.FinderUnited.business.dto.ItemRequestDto;
import com.example.FinderUnited.data.entities.Item;

import java.util.List;
import java.util.Set;

public interface ItemService {

    Item saveItem(ItemRequestDto request);

    List<Item> getAllItems(String status);

    List<Item> getItemsByUser(String status);

    Item claimItem(String itemId);

    Set<Item> claimedItemsByUser();
}
