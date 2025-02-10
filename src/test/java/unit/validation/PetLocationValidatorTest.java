package unit.validation;

import domain.model.PetLocation;
import domain.service.validation.PetLocationValidator;
import infrastructure.adapter.repository.PetLocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PetLocationValidatorTest {

    @Mock
    private PetLocationRepository petLocationRepository;

    private PetLocationValidator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new PetLocationValidator(petLocationRepository);
    }

    @Test
    void testValidate_ValidPetLocation() {
        PetLocation petLocation = new PetLocation();
        petLocation.setSensorId("123");
        petLocation.setLatitude(-23.55);
        petLocation.setLongitude(-46.63);

        validator.validate(petLocation);
    }

    @Test
    void testValidate_NullSensorId() {
        PetLocation petLocation = new PetLocation();
        petLocation.setLatitude(-23.55);
        petLocation.setLongitude(-46.63);

        assertThrows(IllegalArgumentException.class, () -> validator.validate(petLocation), "Sensor ID cannot be null");
    }

    @Test
    void testValidate_NullLatitude() {
        PetLocation petLocation = new PetLocation();
        petLocation.setSensorId("123");
        petLocation.setLongitude(-46.63);

        assertThrows(IllegalArgumentException.class, () -> validator.validate(petLocation), "Latitude and Longitude cannot be null");
    }

    @Test
    void testValidate_NullLongitude() {
        PetLocation petLocation = new PetLocation();
        petLocation.setSensorId("123");
        petLocation.setLatitude(-23.55);

        assertThrows(IllegalArgumentException.class, () -> validator.validate(petLocation), "Latitude and Longitude cannot be null");
    }

    @Test
    void testValidatePatch_ValidPatch() {
        PetLocation petLocation = new PetLocation();
        petLocation.setLatitude(-23.55);

        validator.validatePatch("123", petLocation);
    }

    @Test
    void testValidatePatch_InvalidSensorId() {
        PetLocation petLocation = new PetLocation();
        petLocation.setSensorId("456");

        assertThrows(IllegalArgumentException.class, () -> validator.validatePatch("123", petLocation), "Sensor ID cannot be updated");
    }

    @Test
    void testInvalidLatitude() {
        PetLocation petLocation = new PetLocation();
        petLocation.setLatitude(-91.0);
        petLocation.setLongitude(50.0);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> validator.validateCoordinates(petLocation));
        assertEquals("Latitude must be between -90 and 90 degrees", exception.getMessage());
    }

    @Test
    void testInvalidLongitude() {
        PetLocation petLocation = new PetLocation();
        petLocation.setLatitude(45.0);
        petLocation.setLongitude(181.0);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> validator.validateCoordinates(petLocation));
        assertEquals("Longitude must be between -180 and 180 degrees", exception.getMessage());
    }

}