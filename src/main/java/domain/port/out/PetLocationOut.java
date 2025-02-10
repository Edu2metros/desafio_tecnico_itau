package domain.port.out;

import domain.model.PetLocation;
import java.util.List;

public interface PetLocationOut {
    PetLocation save(PetLocation pet);
    PetLocation findBySensorId(String sensorId);
    boolean existsBySensorId(String sensorId);
    void deleteBySensorId(String sensorId);
    List<PetLocation> findAll();
}
