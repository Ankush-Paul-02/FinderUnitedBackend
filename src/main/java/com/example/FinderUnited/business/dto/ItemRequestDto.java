package com.example.FinderUnited.business.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {

    private String name;

    private String imageUrl;

    private double latitude;

    private double longitude;
}
