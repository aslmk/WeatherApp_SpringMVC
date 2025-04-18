package com.aslmk.service.Impl;

import com.aslmk.dto.LocationDto;
import com.aslmk.exception.LocationAlreadyAddedException;
import com.aslmk.exception.ServiceException;
import com.aslmk.model.Location;
import com.aslmk.model.User;
import com.aslmk.repository.LocationRepository;
import com.aslmk.service.LocationService;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public void saveLocation(LocationDto locationDto, User user) throws LocationAlreadyAddedException {
        try {
            Location location = Location.builder()
                    .name(locationDto.getName())
                    .latitude(locationDto.getLatitude())
                    .longitude(locationDto.getLongitude())
                    .user(user)
                    .build();

            locationRepository.save(location);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException cve) {
                String constraintName = cve.getConstraintName();

                if (constraintName == null) {
                    throw new ServiceException("Unknown database constraint exception: " + e.getMessage());
                }

                if (constraintName.equals("uniquelocationcoordinatesconstraint")) {
                    throw new LocationAlreadyAddedException("You are already added this location.");
                }

            } else {
                throw new ServiceException("Unexpected error occurred while saving location: " + e.getMessage());
            }
        }
    }


    @Override
    @Transactional
    public void deleteLocationById(long locationId) {
        locationRepository.deleteLocationById(locationId);
    }

    @Override
    public List<Location> getLocationsByUserId(Long userId) {
        return locationRepository.findAllByUserId(userId);
    }

    @Override
    public Optional<Location> findLocationById(long locationId) {
        return locationRepository.findLocationById(locationId);
    }
}
