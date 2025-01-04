package com.example.FinderUnited.data.entities;

import com.example.FinderUnited.data.enums.ItemStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "items")
public class Item {

    @Id
    private String id;

    private String name;

    private String imageUrl;

    private String ownerId;

    private String locationId;

    private String founderId;

    private ItemStatus status;

    private Long createdAt;
}
