package tests.integration.repository;

import domain.model.PetLocation;
import infrastructure.PetLocationMain;
import infrastructure.adapter.repository.JpaPetLocationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = PetLocationMain.class)
class PetLocationRepositoryTest {

    @Autowired
    private JpaPetLocationRepository repository;

    @Test
    void testSaveAndFindBySensorId() {
        PetLocation petLocation = new PetLocation();
        petLocation.setSensorId("123");
        petLocation.setLatitude(-23.55);
        petLocation.setLongitude(-46.63);
        petLocation.setTimestamp(OffsetDateTime.now());
        
        repository.save(petLocation);
        PetLocation found = repository.findBySensorId("123");

        assertNotNull(found);
        assertEquals(-23.55, found.getLatitude());
        assertEquals(-46.63, found.getLongitude());
        assertNotNull(found.getTimestamp());
    }

    @Test
    void testDeleteBySensorId() {
        PetLocation petLocation = new PetLocation();
        petLocation.setSensorId("456");
        repository.save(petLocation);

        repository.deleteBySensorId("456");
        assertNull(repository.findBySensorId("456"));
    }
}
