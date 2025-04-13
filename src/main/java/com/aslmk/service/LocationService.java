package com.aslmk.service;

import com.aslmk.dto.LocationDto;
import com.aslmk.model.Location;
import com.aslmk.model.User;

import java.util.List;
import java.util.Optional;

public interface LocationService {

    void saveLocation(LocationDto locationDto, User user);

    void deleteLocationById(long locationId);

    List<Location> getLocationsByUserId(Long userId);

    Optional<Location> findLocationById(long locationId);
}
