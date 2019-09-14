package com.space.service;

import com.space.model.Ship;
import java.util.List;

public interface ShipService {
    List<Ship> findAllShips ();
    Ship getShipByID (Long id);
    void deleteShipByID (Long id);
    Integer getshipsCount ();
}