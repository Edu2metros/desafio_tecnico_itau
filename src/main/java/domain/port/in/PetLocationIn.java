package domain.port.in;

import domain.dto.PetLocationDTO;
import domain.model.PetLocation;

public interface PetLocationIn {
    PetLocationDTO saveLocation(PetLocation petLocation);
    PetLocationDTO updateLocationBySensorId(String sensorId, PetLocation petLocation);
    PetLocationDTO updateLocationByPatch(String sensorId, PetLocation petLocation);
    void deleteLocationBySensorId(String sensorId);
}
