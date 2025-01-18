package com.aslmk.service;

import com.aslmk.dto.LocationDto;
import com.aslmk.model.Location;
import com.aslmk.model.User;

import java.util.List;

public interface LocationService {

    void save(LocationDto locationDto, User user);

    void deleteLocationById(long locationId);

    List<Location> getLocationsByUserId(Long userId);

    Location findLocationById(long locationId);
}
