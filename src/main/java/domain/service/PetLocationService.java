package domain.service;

import domain.dto.PetLocationDTO;
import domain.exception.PetLocationNotFoundException;
import domain.model.PetLocation;
import domain.port.in.PetLocationIn;
import domain.port.out.PetLocationOut;
import infrastructure.adapter.external.PetLocationPositionStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        if(petLocationOut.existsBySensorId(petLocation.getSensorId())) {
            throw new RuntimeException("Sensor ID already exists");
        }
        petLocation = petLocationPositionStack.searchLocation(petLocation);
        PetLocation savedLocation = petLocationOut.save(petLocation);
        return convertToDTO(savedLocation);
    }

    @Override
    public PetLocationDTO getLastLocationById(Long id) {
        PetLocation petLocation = petLocationOut.findById(id);
        return convertToDTO(petLocation);
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
}
