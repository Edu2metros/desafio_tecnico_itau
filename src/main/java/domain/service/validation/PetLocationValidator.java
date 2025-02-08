package domain.service.validation;

import domain.model.PetLocation;
import org.springframework.stereotype.Component;

@Component
public class PetLocationValidator {

    public void validate(PetLocation petLocation) {
        if (petLocation.getSensorId() == null) {
            throw new IllegalArgumentException("Sensor ID cannot be null");
        }
        if (petLocation.getLatitude() == null || petLocation.getLongitude() == null) {
            throw new IllegalArgumentException("Latitude and Longitude cannot be null");
        }
    }

    public void validatePatch(String sensorId, PetLocation petLocation) {
        if (petLocation.getSensorId() != null && !petLocation.getSensorId().equals(sensorId)) {
            throw new IllegalArgumentException("Sensor ID cannot be updated");
        }
    }
}
