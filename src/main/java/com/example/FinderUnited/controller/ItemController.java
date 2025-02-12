package com.example.FinderUnited.controller;

import com.example.FinderUnited.business.dto.DefaultResponseDto;
import com.example.FinderUnited.business.dto.ItemRequestDto;
import com.example.FinderUnited.business.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.FinderUnited.business.dto.DefaultResponseDto.Status.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/save")
    public ResponseEntity<DefaultResponseDto> saveItem(@Valid @RequestBody ItemRequestDto requestDto) {
        return ResponseEntity.ok(
                new DefaultResponseDto(
                        SUCCESS,
                        Map.of(
                                "data", itemService.saveItem(requestDto)
                        ),
                        "Item is created successfully."
                )
        );
    }

    @PostMapping("/all/{status}")
    public ResponseEntity<DefaultResponseDto> getAllItems(@PathVariable String status) {
        return ResponseEntity.ok(
                new DefaultResponseDto(
                        SUCCESS,
                        Map.of(
                                "data", itemService.getAllItems(status)
                        ),
                        "All Items is fetched successfully."
                )
        );
    }

    @PostMapping("/all/current/user/{status}")
    public ResponseEntity<DefaultResponseDto> getAllItemsByUser(@PathVariable String status) {
        return ResponseEntity.ok(
                new DefaultResponseDto(
                        SUCCESS,
                        Map.of(
                                "data", itemService.getItemsByUser(status)
                        ),
                        "All Items is fetched successfully."
                )
        );
    }

    @PutMapping("/claim")
    public ResponseEntity<DefaultResponseDto> claimItem(@PathVariable String itemId) {
        return ResponseEntity.ok(
                new DefaultResponseDto(
                        SUCCESS,
                        Map.of(
                                "data", itemService.claimItem(itemId)
                        ),
                        "Item claimed successfully"
                )
        );
    }

    @GetMapping("/claim/all")
    public ResponseEntity<DefaultResponseDto> claimedItemsByUser() {
        return ResponseEntity.ok(
                new DefaultResponseDto(
                        SUCCESS,
                        Map.of(
                                "data", itemService.claimedItemsByUser()
                        ),
                        "Items feteched successfully"
                )
        );
    }
}
