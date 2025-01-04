package com.example.FinderUnited.business.service.impl;

import com.example.FinderUnited.business.dto.ItemRequestDto;
import com.example.FinderUnited.business.service.AuthenticationService;
import com.example.FinderUnited.business.service.ItemService;
import com.example.FinderUnited.business.service.LocationService;
import com.example.FinderUnited.business.service.exceptions.UserInfoException;
import com.example.FinderUnited.data.entities.Item;
import com.example.FinderUnited.data.entities.Location;
import com.example.FinderUnited.data.entities.User;
import com.example.FinderUnited.data.enums.ItemStatus;
import com.example.FinderUnited.data.repositories.ItemRepository;
import com.example.FinderUnited.data.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
