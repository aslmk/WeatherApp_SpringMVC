package com.aslmk.repository;

import com.aslmk.model.Locations;
import com.aslmk.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocationsRepository extends JpaRepository<Locations, Integer> {
    Locations findByUserId(long userId);

    Locations findLocationById(int locationId);

    List<Locations> findAllByUserId(long userId);

    Locations findLocationByNameAndUser(String name, Users user);

    void deleteLocationById(long locationId);
}
