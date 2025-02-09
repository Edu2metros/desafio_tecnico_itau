package tests.unit.service;

import domain.dto.PetLocationDTO;
import domain.exception.PetLocationNotFoundException;
import domain.model.PetLocation;
import domain.port.out.PetLocationOut;
import domain.service.PetLocationService;
import domain.service.transform.PetLocationTransformer;
import domain.service.validation.PetLocationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PetLocationServiceTest {
    private PetLocationOut petLocationOut;
    private PetLocationValidator validator;
    private PetLocationTransformer transformer;
    private PetLocationService petLocationService;

    @BeforeEach
    void setUp() {
        petLocationOut = mock(PetLocationOut.class);
        validator = mock(PetLocationValidator.class);
        transformer = mock(PetLocationTransformer.class);
        petLocationService = new PetLocationService(petLocationOut, validator, transformer);
    }

    @Test
    void testSaveLocation_Success() {
        PetLocation petLocation = new PetLocation();
        petLocation.setSensorId("123");
        petLocation.setLatitude(-23.5505);
        petLocation.setLongitude(-46.6333);

        PetLocationDTO petLocationDTO = new PetLocationDTO(petLocation);

        when(petLocationOut.save(any())).thenReturn(petLocation);
        when(transformer.toDTO(any())).thenReturn(petLocationDTO);

        PetLocationDTO result = petLocationService.saveLocation(petLocation);

        assertNotNull(result);
        assertEquals("123", result.getCountry());
    }

    @Test
    void testUpdateLocation_NotFound() {
        when(petLocationOut.findBySensorId(anyString())).thenThrow(new PetLocationNotFoundException("Not found"));

        PetLocation petLocation = new PetLocation();
        petLocation.setSensorId("999");

        assertThrows(PetLocationNotFoundException.class, () -> petLocationService.updateLocationBySensorId("999", petLocation));
    }
}