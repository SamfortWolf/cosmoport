package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ShipServiceImp implements ShipService {
    private ShipRepository shipRepository;
    @Autowired
    public void setShipRepository(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    @Override
    public Page<Ship> findAllShips(Specification spec, Pageable pageable) {
        return shipRepository.findAll(spec, pageable);
    }

    @Override
    public List<Ship> findAllShips(Specification spec) {
        return  shipRepository.findAll(spec);
    }

    @Override
    public ResponseEntity findShipByID(Long id) {
        return ResponseEntity.of(shipRepository.findById(id));
    }
    public Boolean isExistingShip (Long id){
        return shipRepository.existsById(id);
    }

    @Override
    public ResponseEntity deleteShipByID(Long id) {
        shipRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public void updateShip(Long id, Ship ship) {

    }

    @Override
    public ResponseEntity createNewShip(Ship newShip) {
        Calendar before = Calendar.getInstance();
        before.set(3019,0,1);
        Calendar after = Calendar.getInstance();
        after.set(2800,0,1);
        if (newShip.getName()==null ||
        newShip.getPlanet()==null ||
        newShip.getSpeed() == null ||
        newShip.getProdDate()==null ||
        newShip.getCrewSize()==null ||
        newShip.getShipType()==null) {
            return ResponseEntity.badRequest().build();
        }
        else if (newShip.getName().length()>50 |
        newShip.getPlanet().length()>50 |
        newShip.getName().equals("") |
        newShip.getPlanet().equals("") |
        (Math.round(newShip.getSpeed()*100.)/100.)<0.01 |
        (Math.round(newShip.getSpeed()*100.)/100.)>0.99 |
        newShip.getCrewSize()<1 |
        newShip.getCrewSize()>9999 |
        newShip.getProdDate().getTime()<0 |
        newShip.getProdDate().getTime()<(after.getTimeInMillis())|
                newShip.getProdDate().getTime()>(before.getTimeInMillis())){
            return ResponseEntity.badRequest().build();
        }
        else {
            if (newShip.getUsed()==null)
                newShip.setUsed(false);
            Double rating = ratingCalculator(newShip);
            newShip.setRating(rating);
            return ResponseEntity.ok(shipRepository.save(newShip));
        }
    }

    @Override
    public Integer getShipsCount(Specification spec) {
        return shipRepository.findAll(spec).size();
    }

    @Override//calculate rating of new ship in create method of controller
    public Double ratingCalculator(Ship ship) {
        Calendar prodCal = Calendar.getInstance();
        prodCal.setTime(ship.getProdDate());
        int prodYear = prodCal.get(Calendar.YEAR);
        int currentYear = 3019;
        return (80*ship.getSpeed()*(ship.getUsed()?0.5:1))/(currentYear-prodYear+1);
    }

    //specs for filtering:
    @Override
    public Specification<Ship> filterByName(String name) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return name == null ? null : criteriaBuilder.like(root.get("name"), "%" + name + "%");
            }
        };
    }
    @Override
    public Specification<Ship> filterByPlanet(String planet) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return planet == null ? null : criteriaBuilder.like(root.get("planet"), "%" + planet + "%");
            }
        };
    }
    @Override
    public Specification<Ship> filterByType(ShipType shipType) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return shipType == null? null : criteriaBuilder.equal(root.get("shipType"), shipType);
            }
        };
    }
    @Override
    public Specification<Ship> filterByProdDate(Long prodYearAfter, Long prodDateBefore) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (prodYearAfter == null && prodDateBefore == null) {
                    return null;
                }
                if (prodYearAfter == null) {
                    Date before = new Date(prodDateBefore);
                    return criteriaBuilder.lessThanOrEqualTo(root.get("prodDate"), before);
                }
                if (prodDateBefore == null) {
                    Date after = new Date(prodYearAfter);
                    return criteriaBuilder.greaterThanOrEqualTo(root.get("prodDate"), after);
                }
                Date before = new Date(prodDateBefore);
                Date after = new Date(prodYearAfter);
                return criteriaBuilder.between(root.get("prodDate"), after, before);
            }
        };
    }
    @Override
    public Specification<Ship> filterByUsage(Boolean isUsed) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (isUsed==null) {
                    return null;
                }
                else if (isUsed){
                    return criteriaBuilder.isTrue(root.get("isUsed"));
                }
                else {
                    return criteriaBuilder.isFalse(root.get("isUsed"));
                }
            }
        };
    }
    @Override
    public Specification<Ship> filterBySpeed(Double speedMin, Double speedMax) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (speedMin==null && speedMax==null){
                    return null;
                }
                if (speedMin==null){
                    return criteriaBuilder.lessThanOrEqualTo(root.get("speed"),speedMax);
                }
                if (speedMax==null){
                    return criteriaBuilder.greaterThanOrEqualTo(root.get("speed"),speedMin);
                }
                return criteriaBuilder.between(root.get("speed"), speedMin, speedMax);
            }
        };
    }
    @Override
    public Specification<Ship> filterByCrewSize(Integer crewSizeMin, Integer crewSizeMax) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (crewSizeMin==null && crewSizeMax==null){
                    return null;
                }
                else if (crewSizeMin==null){
                    return criteriaBuilder.lessThanOrEqualTo(root.get("crewSize"),crewSizeMax);
                }
                else if (crewSizeMax==null){
                    return  criteriaBuilder.greaterThanOrEqualTo(root.get("crewSize"),crewSizeMin);
                }
                else
                return criteriaBuilder.between(root.get("crewSize"),crewSizeMin,crewSizeMax);
            }
        };
    }
    @Override
    public Specification<Ship> filterByRating(Double ratingMin, Double ratingMax) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (ratingMin==null && ratingMax==null){
                    return null;
                }
                else if (ratingMin==null){
                    return criteriaBuilder.lessThanOrEqualTo(root.get("rating"),ratingMax);
                }
                else if (ratingMax==null){
                    return  criteriaBuilder.greaterThanOrEqualTo(root.get("rating"),ratingMin);
                }
                else
                    return criteriaBuilder.between(root.get("rating"),ratingMin,ratingMax);
            }
        };
    }

}