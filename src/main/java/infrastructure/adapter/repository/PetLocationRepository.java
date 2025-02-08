package infrastructure.adapter.repository;

import domain.exception.PetLocationNotFoundException;
import domain.model.PetLocation;
import domain.port.out.PetLocationOut;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PetLocationRepository implements PetLocationOut {

    private final JpaPetLocationRepository jpaPetLocationRepository;

    @Autowired
    public PetLocationRepository(JpaPetLocationRepository jpaPetLocationRepository) {
        this.jpaPetLocationRepository = jpaPetLocationRepository;
    }

    @Override
    public PetLocation save(PetLocation petLocation) {
        return jpaPetLocationRepository.save(petLocation);
    }

    @Override
    public PetLocation findBySensorId(String sensorId) {
        PetLocation petLocation = jpaPetLocationRepository.findBySensorId(sensorId);
        if (petLocation == null) {
            throw new PetLocationNotFoundException("Pet location not found for sensor ID: " + sensorId);
        }
        return petLocation;
    }

    @Override
    public boolean existsBySensorId(String sensorId) {
        return jpaPetLocationRepository.existsBySensorId(sensorId);
    }

    @Override
    @Transactional
    public void deleteBySensorId(String sensorId) {
        jpaPetLocationRepository.deleteBySensorId(sensorId);
    }

    @Override
    public List<PetLocation> findAll() {
        return jpaPetLocationRepository.findAll();
    }

}