package com.example.FinderUnited.business.service.impl;

import com.example.FinderUnited.business.dto.ItemRequestDto;
import com.example.FinderUnited.business.service.AuthenticationService;
import com.example.FinderUnited.business.service.ItemService;
import com.example.FinderUnited.business.service.LocationService;
import com.example.FinderUnited.business.service.exceptions.ConcurrencyException;
import com.example.FinderUnited.business.service.exceptions.UserInfoException;
import com.example.FinderUnited.data.entities.Item;
import com.example.FinderUnited.data.entities.Location;
import com.example.FinderUnited.data.entities.User;
import com.example.FinderUnited.data.enums.ItemStatus;
import com.example.FinderUnited.data.repositories.ItemRepository;
import com.example.FinderUnited.data.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final LocationService locationService;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    @Override
    public Item saveItem(ItemRequestDto request) {
        User user = authenticationService.getAuthenticatedUser();

        Location location = Location.builder()
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();

        location = locationService.saveLocation(location);

        Item item = Item.builder()
                .name(request.getName())
                .imageUrl(request.getImageUrl())
                .ownerId(null)
                .locationId(location.getId())
                .founderId(user.getId())
                .status(ItemStatus.FOUND)
                .createdAt(System.currentTimeMillis())
                .requestClaimers(new HashSet<>())
                .build();
        return itemRepository.save(item);
    }

    @Override
    public List<Item> getAllItems(String status) {
        if (status != null) {
            try {
                ItemStatus itemStatus = ItemStatus.valueOf(status.toLowerCase());
                return itemRepository
                        .findAll()
                        .stream()
                        .filter(item -> item.getStatus().equals(itemStatus))
                        .toList();
            } catch (Exception e) {
                throw new UserInfoException("Invalid Status!");
            }
        }
        return itemRepository.findAll();
    }

    @Override
    public List<Item> getItemsByUser(String status) {
        User user = authenticationService.getAuthenticatedUser();

        List<Item> items = itemRepository.findItemsByOwnerId(user.getId());

        if (status != null) {
            try {
                ItemStatus itemStatus = ItemStatus.valueOf(status.toLowerCase());
                return items
                        .stream()
                        .filter(item -> item.getStatus().equals(itemStatus))
                        .toList();
            } catch (Exception e) {
                throw new UserInfoException("Invalid Status!");
            }
        }
        return items;
    }

    @Override
    @Transactional
    public Item claimItem(String itemId) {
        User user = authenticationService.getAuthenticatedUser();

        int maxRetries = 3, attempts = 0;

        while (true) {
            try {
                Optional<Item> optionalItem = itemRepository.findById(itemId);
                if (optionalItem.isEmpty()) {
                    throw new UserInfoException("Item is not found!");
                }
                Item item = optionalItem.get();
                if (item.getRequestClaimers() == null) {
                    item.setRequestClaimers(new HashSet<>());
                }

                if (user.getItemIds() == null) {
                    user.setItemIds(new HashSet<>());
                }
                if (item.getRequestClaimers().contains(user.getId())) {
                    item.getRequestClaimers().remove(user.getId());
                    user.getItemIds().remove(itemId);
                } else {
                    item.getRequestClaimers().add(user.getId());
                    user.getItemIds().add(itemId);
                }
                userRepository.save(user);
                return itemRepository.save(item);
            } catch (OptimisticLockingFailureException e) {
                attempts++;
                if (attempts >= maxRetries) {
                    throw new ConcurrencyException("The item was modified by another user. Please try again.");
                }
            }
        }
    }

    @Override
    public Set<Item> claimedItemsByUser() {
        User user = authenticationService.getAuthenticatedUser();
        return user.getItemIds().stream().map(
                id -> {
                    Optional<Item> optionalItem = itemRepository.findById(id);
                    if (optionalItem.isEmpty()) {
                        throw new UserInfoException("Invalid id " + id);
                    }
                    return optionalItem.get();
                }
        ).collect(Collectors.toSet());
    }
}
