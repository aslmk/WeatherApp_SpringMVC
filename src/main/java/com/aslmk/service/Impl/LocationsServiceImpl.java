package com.aslmk.service.Impl;

import com.aslmk.dto.LocationsDto;
import com.aslmk.model.Locations;
import com.aslmk.model.Users;
import com.aslmk.repository.LocationsRepository;
import com.aslmk.service.LocationsService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LocationsServiceImpl implements LocationsService {
    private LocationsRepository locationsRepository;

    @Autowired
    public LocationsServiceImpl(LocationsRepository locationsRepository) {
        this.locationsRepository = locationsRepository;
    }

    @Override
    public void save(LocationsDto locationsDto, Users user) {
        Locations locations = new Locations();

        locations.setName(locationsDto.getName());
        locations.setLatitude(locationsDto.getLatitude());
        locations.setLongitude(locationsDto.getLongitude());
        locations.setUser(user);

        locationsRepository.save(locations);

    }


    @Override
    @Transactional
    public void deleteLocationById(long locationId) {
        locationsRepository.deleteLocationById(locationId);
    }

    @Override
    public List<Locations> getLocationsByUserId(Long userId) {
        return locationsRepository.findAllByUserId(userId);
    }

    @Override
    public Locations findLocationByCityName(Users user, String cityName) {
        return locationsRepository.findLocationByNameAndUser(cityName, user);
    }
}
