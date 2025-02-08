package infrastructure.adapter.repository;

import domain.model.PetLocation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaPetLocationRepository extends JpaRepository<PetLocation, Long> {
    PetLocation findBySensorId(String sensorId);
    @Transactional
    void deleteBySensorId(String sensorId);
    boolean existsBySensorId(String sensorId);
    List<PetLocation> findAll();
}
