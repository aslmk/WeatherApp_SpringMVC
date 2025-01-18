package com.aslmk.repository;

import com.aslmk.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Integer> {

    Optional<Location> findLocationById(long locationId);

    List<Location> findAllByUserId(long userId);

    void deleteLocationById(long locationId);
}
