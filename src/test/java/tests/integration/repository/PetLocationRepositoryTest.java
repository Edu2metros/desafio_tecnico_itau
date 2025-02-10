package tests.integration.repository;

import domain.exception.PetLocationNotFoundException;
import domain.model.PetLocation;
import infrastructure.PetLocationMain;
import infrastructure.adapter.repository.JpaPetLocationRepository;
import infrastructure.adapter.repository.PetLocationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = PetLocationMain.class)
class PetLocationRepositoryTest {

    @Autowired
    private JpaPetLocationRepository jpaPetLocationRepository;

    @Test
    void testSaveAndFindBySensorId() {
        PetLocationRepository repository = new PetLocationRepository(jpaPetLocationRepository);

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
        assertEquals("123", found.getSensorId());
        assertNotNull(found.getTimestamp());
    }

    @Test
    void testFindBySensorIdNotFound() {
        PetLocationRepository repository = new PetLocationRepository(jpaPetLocationRepository);

        assertThrows(PetLocationNotFoundException.class, () -> repository.findBySensorId("999"));
    }

    @Test
    void testExistsBySensorId() {
        PetLocationRepository repository = new PetLocationRepository(jpaPetLocationRepository);

        assertFalse(repository.existsBySensorId("123"));

        PetLocation petLocation = new PetLocation();
        petLocation.setSensorId("123");
        petLocation.setLatitude(-23.55);
        petLocation.setLongitude(-46.63);
        petLocation.setTimestamp(OffsetDateTime.now());

        repository.save(petLocation);

        assertTrue(repository.existsBySensorId("123"));
        assertFalse(repository.existsBySensorId("999"));
    }

    @Test
    void testDeleteBySensorId() {
        PetLocationRepository repository = new PetLocationRepository(jpaPetLocationRepository);

        PetLocation petLocation = new PetLocation();
        petLocation.setSensorId("456");
        petLocation.setLatitude(-23.55);
        petLocation.setLongitude(-46.63);
        petLocation.setTimestamp(OffsetDateTime.now());
        repository.save(petLocation);

        repository.deleteBySensorId("456");
        assertFalse(repository.existsBySensorId("456"));
        assertThrows(PetLocationNotFoundException.class, () -> repository.findBySensorId("456"));
    }

    @Test
    void testFindAll() {
        PetLocationRepository repository = new PetLocationRepository(jpaPetLocationRepository);

        PetLocation petLocation1 = new PetLocation();
        petLocation1.setSensorId("123");
        petLocation1.setLatitude(-23.55);
        petLocation1.setLongitude(-46.63);
        petLocation1.setTimestamp(OffsetDateTime.now());

        PetLocation petLocation2 = new PetLocation();
        petLocation2.setSensorId("456");
        petLocation2.setLatitude(-23.56);
        petLocation2.setLongitude(-46.64);
        petLocation2.setTimestamp(OffsetDateTime.now());

        repository.save(petLocation1);
        repository.save(petLocation2);

        List<PetLocation> locations = repository.findAll();
        assertEquals(2, locations.size());
        assertTrue(locations.stream().anyMatch(l -> "123".equals(l.getSensorId())));
        assertTrue(locations.stream().anyMatch(l -> "456".equals(l.getSensorId())));
    }
}