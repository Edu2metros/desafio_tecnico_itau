package domain.service;

import domain.dto.PetLocationDTO;
import domain.exception.PetLocationNotFoundException;
import domain.model.PetLocation;
import domain.port.in.PetLocationIn;
import domain.port.out.PetLocationOut;
import domain.service.transform.PetLocationTransformer;
import domain.service.validation.PetLocationValidator;
import infrastructure.adapter.external.PetLocationPositionStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetLocationService implements PetLocationIn {

    private final PetLocationOut petLocationOut;
    private final PetLocationValidator validator;
    private final PetLocationTransformer transformer;

    @Autowired
    public PetLocationService(PetLocationOut petLocationOut, PetLocationValidator validator, PetLocationTransformer transformer) {
        this.petLocationOut = petLocationOut;
        this.validator = validator;
        this.transformer = transformer;
    }

    @Override
    public PetLocationDTO saveLocation(PetLocation petLocation) {
        validator.validate(petLocation);
        transformer.enrichWithAddress(petLocation);
        PetLocation savedLocation = petLocationOut.save(petLocation);
        return transformer.toDTO(savedLocation);
    }

    @Override
    public PetLocationDTO updateLocationBySensorId(String sensorId, PetLocation petLocation) {
        PetLocation petLocationToUpdate = petLocationOut.findBySensorId(sensorId);
        if (petLocationToUpdate == null) {
            throw new PetLocationNotFoundException("Pet location not found for sensor ID: " + sensorId);
        }
        transformer.updateLocationFields(petLocationToUpdate, petLocation);
        transformer.enrichWithAddress(petLocationToUpdate);
        PetLocation updatedLocation = petLocationOut.save(petLocationToUpdate);
        return transformer.toDTO(updatedLocation);
    }

    @Override
    public PetLocationDTO updateLocationByPatch(String sensorId, PetLocation petLocation) {
        PetLocation petLocationToUpdate = petLocationOut.findBySensorId(sensorId);
        if (petLocationToUpdate == null) {
            throw new PetLocationNotFoundException("Pet location not found for sensor ID: " + sensorId);
        }
        validator.validatePatch(sensorId, petLocation);
        transformer.updateLocationFields(petLocationToUpdate, petLocation);
        transformer.enrichWithAddress(petLocationToUpdate);
        PetLocation updatedLocation = petLocationOut.save(petLocationToUpdate);
        return transformer.toDTO(updatedLocation);
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
        return locations.stream().map(transformer::toDTO).collect(Collectors.toList());
    }

    public PetLocationDTO getLastLocationBySensorId(String sensorId) {
        PetLocation petLocation = petLocationOut.findBySensorId(sensorId);
        return transformer.toDTO(petLocation);
    }
}
