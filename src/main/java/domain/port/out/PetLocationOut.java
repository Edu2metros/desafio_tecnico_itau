package domain.port.out;

import domain.model.PetLocation;

public interface PetLocationOut {
    PetLocation save(PetLocation pet);
    PetLocation findById(Long id);
    PetLocation findBySensorId(String sensorId);
    boolean existsBySensorId(String sensorId);
}
