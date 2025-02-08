package infrastructure.adapter.controller;

import domain.exception.PetLocationNotFoundException;
import domain.model.PetLocation;
import domain.service.PetLocationService;
import jakarta.validation.Valid;
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
    public ResponseEntity<?> getAllLocations() {
        return ResponseEntity.ok(petLocationService.getAllLocations());
    }

    @GetMapping("/{sensorId}")
    public ResponseEntity<?> getLastLocationBySensorId(@PathVariable String sensorId) {
        try {
            return new ResponseEntity<>(petLocationService.getLastLocationBySensorId(sensorId), HttpStatus.OK);
        } catch (PetLocationNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> receiveLocation(@Valid @RequestBody PetLocation petLocation) {
        try {
            return new ResponseEntity<>(petLocationService.saveLocation(petLocation), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{sensorId}")
    public ResponseEntity<?> updateLocationBySensorId(@PathVariable String sensorId, @Valid @RequestBody PetLocation petLocation) {
        try {
            return new ResponseEntity<>(petLocationService.updateLocationBySensorId(sensorId, petLocation), HttpStatus.OK);
        } catch (PetLocationNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{sensorId}")
    public ResponseEntity<?> updateLocationByPatch(@PathVariable String sensorId, @RequestBody PetLocation petLocation) {
        try {
            return new ResponseEntity<>(petLocationService.updateLocationByPatch(sensorId, petLocation), HttpStatus.OK);
        } catch (PetLocationNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{sensorId}")
    public ResponseEntity<?> deleteLocationBySensorId(@PathVariable String sensorId) {
        try {
            petLocationService.deleteLocationBySensorId(sensorId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (PetLocationNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
