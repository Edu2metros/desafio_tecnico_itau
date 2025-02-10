package integration.service;

import domain.dto.PetLocationDTO;
import domain.exception.PetLocationNotFoundException;
import domain.model.PetLocation;
import domain.service.PetLocationService;
import infrastructure.PetLocationMain;
import infrastructure.adapter.external.PetLocationPositionStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = PetLocationMain.class)
@Transactional
class PetLocationServiceTest {

    @Autowired
    private PetLocationService petLocationService;

    @MockBean
    private PetLocationPositionStack petLocationPositionStack;

    @BeforeEach
    void setUp() {
        when(petLocationPositionStack.searchLocation(any(PetLocation.class)))
                .thenAnswer(invocation -> {
                    PetLocation pet = invocation.getArgument(0);

                    if (pet.getLatitude() == -23.55 && pet.getLongitude() == -46.63) {
                        pet.setAddress("Rua Frederico Alvarenga 65");
                        pet.setNeighborhood("Se");
                    } else if (pet.getLatitude() == -23.56 && pet.getLongitude() == -46.64) {
                        pet.setAddress("Avenida Vinte E Tres De Maio 77");
                        pet.setNeighborhood("Liberdade District");
                    } else {
                        pet.setAddress("Rua Do Lavapes 131");
                        pet.setNeighborhood("Liberdade District");
                    }

                    pet.setCountry("Brazil");
                    pet.setState("Sao Paulo");
                    pet.setCity("São Paulo");
                    return pet;
                });
    }

    @Test
    void testSaveLocation() {
        PetLocation petLocation = new PetLocation();
        petLocation.setSensorId("123");
        petLocation.setLatitude(-23.55);
        petLocation.setLongitude(-46.63);
        petLocation.setTimestamp(OffsetDateTime.now());

        PetLocationDTO savedLocation = petLocationService.saveLocation(petLocation);

        assertNotNull(savedLocation);
        assertEquals("Brazil", savedLocation.getCountry());
        assertEquals("Sao Paulo", savedLocation.getState());
        assertEquals("São Paulo", savedLocation.getCity());
        assertEquals("Se", savedLocation.getNeighborhood());
        assertEquals("Rua Frederico Alvarenga 65", savedLocation.getAddress());
    }

    @Test
    void testUpdateLocationBySensorId() {
        PetLocation petLocation = new PetLocation();
        petLocation.setSensorId("123");
        petLocation.setLatitude(-23.55);
        petLocation.setLongitude(-46.63);
        petLocation.setTimestamp(OffsetDateTime.now());

        petLocationService.saveLocation(petLocation);

        PetLocation updatedPetLocation = new PetLocation();
        updatedPetLocation.setLatitude(-23.56);
        updatedPetLocation.setLongitude(-46.64);

        PetLocationDTO updatedLocation = petLocationService.updateLocationBySensorId("123", updatedPetLocation);

        assertNotNull(updatedLocation);
        assertEquals("Brazil", updatedLocation.getCountry());
        assertEquals("Sao Paulo", updatedLocation.getState());
        assertEquals("São Paulo", updatedLocation.getCity());
        assertEquals("Liberdade District", updatedLocation.getNeighborhood());
        assertEquals("Avenida Vinte E Tres De Maio 77", updatedLocation.getAddress());
    }

    @Test
    void testUpdateLocationByPatch() {
        PetLocation petLocation = new PetLocation();
        petLocation.setSensorId("123");
        petLocation.setLatitude(-23.55);
        petLocation.setLongitude(-46.63);
        petLocation.setTimestamp(OffsetDateTime.now());

        petLocationService.saveLocation(petLocation);

        PetLocation patchPetLocation = new PetLocation();
        patchPetLocation.setLatitude(-23.56);

        PetLocationDTO updatedLocation = petLocationService.updateLocationByPatch("123", patchPetLocation);

        assertNotNull(updatedLocation);
        assertEquals("Brazil", updatedLocation.getCountry());
        assertEquals("Sao Paulo", updatedLocation.getState());
        assertEquals("São Paulo", updatedLocation.getCity());
        assertEquals("Liberdade District", updatedLocation.getNeighborhood());
        assertEquals("Rua Do Lavapes 131", updatedLocation.getAddress());
    }

    @Test
    void testDeleteLocationBySensorId() {
        PetLocation petLocation = new PetLocation();
        petLocation.setSensorId("123");
        petLocation.setLatitude(-23.55);
        petLocation.setLongitude(-46.63);
        petLocation.setTimestamp(OffsetDateTime.now());

        petLocationService.saveLocation(petLocation);

        petLocationService.deleteLocationBySensorId("123");

        assertThrows(PetLocationNotFoundException.class, () -> petLocationService.getLastLocationBySensorId("123"));
    }

    @Test
    void testGetAllLocations() {
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

        petLocationService.saveLocation(petLocation1);
        petLocationService.saveLocation(petLocation2);

        List<PetLocationDTO> locations = petLocationService.getAllLocations();
        assertEquals(2, locations.size());
    }

    @Test
    void testGetLastLocationBySensorId() {
        PetLocation petLocation = new PetLocation();
        petLocation.setSensorId("123");
        petLocation.setLatitude(-23.55);
        petLocation.setLongitude(-46.63);
        petLocation.setTimestamp(OffsetDateTime.now());

        petLocationService.saveLocation(petLocation);

        PetLocationDTO foundLocation = petLocationService.getLastLocationBySensorId("123");

        assertNotNull(foundLocation);
        assertEquals("Brazil", foundLocation.getCountry());
        assertEquals("Sao Paulo", foundLocation.getState());
        assertEquals("São Paulo", foundLocation.getCity());
        assertEquals("Se", foundLocation.getNeighborhood());
        assertEquals("Rua Frederico Alvarenga 65", foundLocation.getAddress());
    }

    @Test
    void testExistsBySensorId() {
        PetLocation petLocation = new PetLocation();
        petLocation.setSensorId("123");
        petLocation.setLatitude(-23.55);
        petLocation.setLongitude(-46.63);
        petLocation.setTimestamp(OffsetDateTime.now());

        petLocationService.saveLocation(petLocation);

        assertTrue(petLocationService.existsBySensorId("123"));
        assertFalse(petLocationService.existsBySensorId("999"));
    }
}
