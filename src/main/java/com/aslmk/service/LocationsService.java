package com.aslmk.service;

import com.aslmk.dto.LocationsDto;
import com.aslmk.model.Locations;
import com.aslmk.model.Users;

import java.util.List;

public interface LocationsService {
    List<Locations> getLocations();

    void save(LocationsDto locationsDto, Users user);

    Locations findByUserId(Long userId);

    Locations findLocationById(int locationId);

    void deleteLocationById(int locationId);

    List<Locations> getLocationsByUserId(Long userId);
}
