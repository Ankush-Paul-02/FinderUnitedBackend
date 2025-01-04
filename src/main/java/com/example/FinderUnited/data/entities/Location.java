package com.example.FinderUnited.data.entities;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "items")
public class Location {

    @Id
    private String id;

    @NotNull(message = "latitude can't be empty.")
    private double latitude;

    @NotNull(message = "longitude can't be empty.")
    private double longitude;
}
