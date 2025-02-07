package infrastructure.adapter.controller;

import domain.exception.PetLocationNotFoundException;
import domain.model.PetLocation;
import domain.service.PetLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/location")
public class PetLocationController {
    private final PetLocationService petLocationService;

    @Autowired
    public PetLocationController(PetLocationService petLocationService) {
        this.petLocationService = petLocationService;
    }

    @GetMapping
    public ResponseEntity<?> getLastLocation(@RequestParam Long id) {
        try {
            return new ResponseEntity<>(petLocationService.getLastLocationById(id), HttpStatus.OK);
        } catch (PetLocationNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/sensor")
    public ResponseEntity<?> getLastLocationBySensorId(@RequestParam String sensorId) {
        try {
            return new ResponseEntity<>(petLocationService.getLastLocationBySensorId(sensorId), HttpStatus.OK);
        } catch (PetLocationNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> receiveLocation(@RequestBody PetLocation petLocation) {
        try {
            return new ResponseEntity<>(petLocationService.saveLocation(petLocation), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateLocationBySensorId(@RequestParam String sensorId, @RequestBody PetLocation petLocation) {
        try {
            return new ResponseEntity<>(petLocationService.updateLocationBySensorId(sensorId, petLocation), HttpStatus.OK);
        } catch (PetLocationNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
