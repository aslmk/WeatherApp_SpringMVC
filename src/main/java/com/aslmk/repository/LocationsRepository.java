package com.aslmk.repository;

import com.aslmk.model.Locations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationsRepository extends JpaRepository<Locations, Integer> {
    Locations findByUserId(long userId);

    Locations findLocationById(int locationId);
}
