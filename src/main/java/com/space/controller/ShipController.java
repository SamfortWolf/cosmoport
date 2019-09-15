package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/rest")
public class ShipController {

    private ShipService shipService;
    @Autowired
    public void setShipService(ShipService shipService) {
        this.shipService = shipService;
    }

    @RequestMapping(value = "/ships", method = RequestMethod.GET)
    public List<Ship> getAllShips (
            @RequestParam (value = "name", required = false) String name,
            @RequestParam (value = "planet", required = false) String planet,
            @RequestParam (value = "shipType", required = false) ShipType shipType,
            @RequestParam (value = "after", required = false) Long prodYearAfter,
            @RequestParam (value = "before", required = false) Long prodYearBefore,
            @RequestParam (value = "isUsed", required = false) Boolean isUsed,
            @RequestParam (value = "minSpeed", required = false) Double speedMin,
            @RequestParam (value = "maxSpeed", required = false) Double speedMax,
            @RequestParam (value = "minCrewSize", required = false) Integer crewSizeMin,
            @RequestParam (value = "maxCrewSize", required = false) Integer crewSizeMax,
            @RequestParam (value = "minRating", required = false) Double ratingMin,
            @RequestParam (value = "maxRating", required = false) Double ratingMax,
            @RequestParam (required = false, defaultValue = "ID") ShipOrder order,
            @RequestParam (required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam (required = false, defaultValue = "3") Integer pageSize
            ){
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));
        return shipService.findAllShips(Specification.where(shipService.filterByName(name)
                        .and(shipService.filterByPlanet(planet))
                        .and(shipService.filterByProdDate(prodYearAfter,prodYearBefore))
                        .and(shipService.filterByCrewSize(crewSizeMin,crewSizeMax))
                        .and(shipService.filterByUsage(isUsed))
                        .and(shipService.filterBySpeed(speedMin,speedMax))
                        .and(shipService.filterByRating(ratingMin,ratingMax))
                        .and(shipService.filterByType(shipType)))
                , pageable).getContent();
    }

    @RequestMapping(value = "/ships/count", method = RequestMethod.GET)
    public Integer getShipsCount (@RequestParam (value = "name", required = false) String name,
                                  @RequestParam (value = "planet", required = false) String planet,
                                  @RequestParam (value = "shipType", required = false) ShipType shipType,
                                  @RequestParam (value = "after", required = false) Long prodYearAfter,
                                  @RequestParam (value = "before", required = false) Long prodYearBefore,
                                  @RequestParam (value = "isUsed", required = false) Boolean isUsed,
                                  @RequestParam (value = "minSpeed", required = false) Double speedMin,
                                  @RequestParam (value = "maxSpeed", required = false) Double speedMax,
                                  @RequestParam (value = "minCrewSize", required = false) Integer crewSizeMin,
                                  @RequestParam (value = "maxCrewSize", required = false) Integer crewSizeMax,
                                  @RequestParam (value = "minRating", required = false) Double ratingMin,
                                  @RequestParam (value = "maxRating", required = false) Double ratingMax)
                                  {
        return shipService.getShipsCount(Specification.where(shipService.filterByName(name)
                        .and(shipService.filterByPlanet(planet))
                        .and(shipService.filterByProdDate(prodYearAfter,prodYearBefore))
                        .and(shipService.filterByCrewSize(crewSizeMin,crewSizeMax))
                        .and(shipService.filterByUsage(isUsed))
                        .and(shipService.filterBySpeed(speedMin,speedMax))
                        .and(shipService.filterByRating(ratingMin,ratingMax))
                        .and(shipService.filterByType(shipType))));
    }
}