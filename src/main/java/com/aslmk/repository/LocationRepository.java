package com.aslmk.repository;

import com.aslmk.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Integer> {

    Location findLocationById(long locationId);

    List<Location> findAllByUserId(long userId);

    void deleteLocationById(long locationId);
}
