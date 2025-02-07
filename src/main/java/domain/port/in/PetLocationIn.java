package domain.port.in;

import domain.dto.PetLocationDTO;
import domain.model.PetLocation;

public interface PetLocationIn {
    PetLocationDTO saveLocation(PetLocation petLocation);
    PetLocationDTO getLastLocationById(Long id);
    PetLocationDTO updateLocationBySensorId(String sensorId, PetLocation petLocation);
}
