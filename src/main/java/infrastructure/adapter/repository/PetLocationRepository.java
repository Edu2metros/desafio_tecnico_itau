package infrastructure.adapter.repository;

import domain.exception.PetLocationNotFoundException;
import domain.model.PetLocation;
import domain.port.out.PetLocationOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
    public PetLocation findById(Long id) {
        return jpaPetLocationRepository.findById(id)
                .orElseThrow(() -> new PetLocationNotFoundException("Pet location not found for ID: " + id));
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
}