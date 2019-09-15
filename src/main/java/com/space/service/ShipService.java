package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;

public interface ShipService {
    List<Ship> findAllShips (Specification spec);
    Page<Ship> findAllShips (Specification spec, Pageable pageable);
    Ship getShipByID (Long id);
    void deleteShipByID (Long id);
    Integer getShipsCount (Specification spec);

    Double ratingCalculator (Ship ship);
    //Specifications for dynamic filtration, see scripts.js
    Specification<Ship> filterByName(String name);
    Specification<Ship> filterByPlanet(String name);
    Specification<Ship> filterByType(ShipType shipType);
    Specification<Ship> filterByProdDate(Long prodYearAfter, Long prodDateBefore);
    Specification<Ship> filterByUsage(Boolean isUsed);
    Specification<Ship> filterBySpeed(Double speedMin, Double speedMax);
    Specification<Ship> filterByCrewSize(Integer crewSizeMin, Integer crewSizeMax);
    Specification<Ship> filterByRating(Double ratingMin, Double ratingMax);

}