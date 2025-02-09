package tests.unit.validation;

import domain.model.PetLocation;
import domain.service.validation.PetLocationValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PetLocationValidatorTest {
    private final PetLocationValidator validator = new PetLocationValidator();

    @Test
    void testValidate_ThrowsExceptionWhenSensorIdIsNull() {
        PetLocation petLocation = new PetLocation();
        petLocation.setLatitude(-23.55);
        petLocation.setLongitude(-46.63);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(petLocation));
        assertEquals("Sensor ID cannot be null", exception.getMessage());
    }
}
