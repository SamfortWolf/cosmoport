package com.space.service;

import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipServiceImp implements ShipService {
    private ShipRepository shipRepository;

    @Autowired
    public void setShipRepository(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }


    @Override
    public List<Ship> findAllShips() {
        return  shipRepository.findAll();
    }

    @Override
    public Ship getShipByID(Long id) {
        return shipRepository.findAll().get(id.intValue());
    }


    @Override
    public void deleteShipByID(Long id) {

    }

    @Override
    public Integer getshipsCount() {
        return shipRepository.findAll().size();
    }
}