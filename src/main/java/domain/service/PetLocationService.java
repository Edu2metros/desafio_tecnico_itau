package domain.service;

import domain.dto.PetLocationDTO;
import domain.exception.PetLocationNotFoundException;
import domain.model.PetLocation;
import domain.port.in.PetLocationIn;
import domain.port.out.PetLocationOut;
import infrastructure.adapter.external.PetLocationPositionStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetLocationService implements PetLocationIn {

    private final PetLocationOut petLocationOut;
    private final PetLocationPositionStack petLocationPositionStack;

    @Autowired
    public PetLocationService(PetLocationOut petLocationOut, PetLocationPositionStack petLocationPositionStack) {
        this.petLocationOut = petLocationOut;
        this.petLocationPositionStack = petLocationPositionStack;
    }

    @Override
    public PetLocationDTO saveLocation(PetLocation petLocation) {
        if(petLocation.getSensorId() == null) {
            throw new IllegalArgumentException("Sensor ID cannot be null");
        }
        if(petLocationOut.existsBySensorId(petLocation.getSensorId())) {
            throw new RuntimeException("Sensor ID already exists");
        }
        petLocation = petLocationPositionStack.searchLocation(petLocation);
        PetLocation savedLocation = petLocationOut.save(petLocation);
        return convertToDTO(savedLocation);
    }

    public PetLocationDTO getLastLocationBySensorId(String sensorId) {
        PetLocation petLocation = petLocationOut.findBySensorId(sensorId);
        return convertToDTO(petLocation);
    }

    @Override
    public PetLocationDTO updateLocationBySensorId(String sensorId, PetLocation petLocation) {
        PetLocation petLocationToUpdate = petLocationOut.findBySensorId(sensorId);
        if (petLocationToUpdate == null) {
            throw new PetLocationNotFoundException("Pet location not found for sensor ID: " + sensorId);
        }
        petLocationToUpdate.setLatitude(petLocation.getLatitude());
        petLocationToUpdate.setLongitude(petLocation.getLongitude());
        petLocationToUpdate.setTimestamp(petLocation.getTimestamp());
        petLocationToUpdate = petLocationPositionStack.searchLocation(petLocationToUpdate);
        PetLocation updatedLocation = petLocationOut.save(petLocationToUpdate);
        return convertToDTO(updatedLocation);
    }

    public PetLocationDTO convertToDTO(PetLocation petLocation) {
        return new PetLocationDTO(petLocation);
    }

    public PetLocationDTO updateLocationByPatch(String sensorId, PetLocation petLocation) {
        PetLocation petLocationToUpdate = petLocationOut.findBySensorId(sensorId);
        if (petLocationToUpdate == null) {
            throw new PetLocationNotFoundException("Pet location not found for sensor ID: " + sensorId);
        }
        if (petLocation.getSensorId() != null && !petLocation.getSensorId().equals(sensorId)) {
            throw new IllegalArgumentException("Sensor ID cannot be updated");
        }
        if (petLocation.getLatitude() != null) {
            petLocationToUpdate.setLatitude(petLocation.getLatitude());
        }
        if (petLocation.getLongitude() != null) {
            petLocationToUpdate.setLongitude(petLocation.getLongitude());
        }
        if (petLocation.getTimestamp() != null) {
            petLocationToUpdate.setTimestamp(petLocation.getTimestamp());
        }

        petLocationToUpdate = petLocationPositionStack.searchLocation(petLocationToUpdate);
        PetLocation updatedLocation = petLocationOut.save(petLocationToUpdate);
        return convertToDTO(updatedLocation);
    }


    @Override
    public void deleteLocationBySensorId(String sensorId) {
        PetLocation petLocation = petLocationOut.findBySensorId(sensorId);
        if (petLocation == null) {
            throw new PetLocationNotFoundException("Pet location not found for sensor ID: " + sensorId);
        }
        petLocationOut.deleteBySensorId(sensorId);
    }

    public List<PetLocationDTO> getAllLocations() {
        List<PetLocation> locations = petLocationOut.findAll();
        return locations.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
}
