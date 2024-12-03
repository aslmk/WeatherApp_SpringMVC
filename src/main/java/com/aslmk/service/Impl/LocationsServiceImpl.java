package com.aslmk.service.Impl;

import com.aslmk.model.Locations;
import com.aslmk.repository.LocationsRepository;
import com.aslmk.service.LocationsService;
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
    public List<Locations> getLocations() {
        return locationsRepository.findAll();
    }
}
