package com.example.FinderUnited.business.service.impl;

import com.example.FinderUnited.business.service.LocationService;
import com.example.FinderUnited.data.entities.Location;
import com.example.FinderUnited.data.repositories.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Override
    public Location saveLocation(Location location) {
        return locationRepository.save(location);
    }
}
