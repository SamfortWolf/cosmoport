package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ShipService {
    //main methods
    List<Ship> findAllShips (Specification spec);
    Page<Ship> findAllShips (Specification spec, Pageable pageable);
    ResponseEntity findShipByID (Long id);
    ResponseEntity deleteShipByID (Long id);
    ResponseEntity updateShip (Long id, Ship ship);
    ResponseEntity createNewShip (Ship newShip);
    Integer getShipsCount (Specification spec);
    Boolean isExistingShip (Long id);

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