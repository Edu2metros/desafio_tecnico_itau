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

    public void validateCoordinates(PetLocation petLocation) {
        if (petLocation.getLatitude() < -90 || petLocation.getLatitude() > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90 degrees");
        }
        else if (petLocation.getLongitude() < -180 || petLocation.getLongitude() > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180 degrees");
        }
    }
}
