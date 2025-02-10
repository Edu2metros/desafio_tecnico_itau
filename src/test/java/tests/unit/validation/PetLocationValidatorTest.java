package tests.unit.validation;

import domain.model.PetLocation;
import domain.service.validation.PetLocationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class PetLocationValidatorTest {

    private PetLocationValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PetLocationValidator();
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
}