package infrastructure.adapter.repository;

import domain.model.PetLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPetLocationRepository extends JpaRepository<PetLocation, Long> {
    PetLocation findBySensorId(String sensorId);
    boolean existsBySensorId(String sensorId);
}
