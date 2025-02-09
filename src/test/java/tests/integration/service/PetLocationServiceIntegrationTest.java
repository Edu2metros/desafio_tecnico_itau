package tests.integration.service;

import domain.dto.PetLocationDTO;
import domain.model.PetLocation;
import domain.port.out.PetLocationOut;
import domain.service.PetLocationService;
import domain.service.transform.PetLocationTransformer;
import domain.service.validation.PetLocationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PetLocationServiceIntegrationTest {

    private PetLocationService petLocationService;
    private PetLocationOut petLocationOut;
    private PetLocationValidator validator;
    private PetLocationTransformer transformer;

    @BeforeEach
    void setUp() {
        petLocationOut = mock(PetLocationOut.class);
        validator = new PetLocationValidator();
        transformer = new PetLocationTransformer(null);
        petLocationService = new PetLocationService(petLocationOut, validator, transformer);
    }

    @Test
    void testSaveLocation_WithValidData() {
        PetLocation petLocation = new PetLocation();
        petLocation.setSensorId("789");
        petLocation.setLatitude(-23.55);
        petLocation.setLongitude(-46.63);

        when(petLocationOut.save(any())).thenReturn(petLocation);
        when(transformer.toDTO(any())).thenReturn(new PetLocationDTO(petLocation));

        PetLocationDTO result = petLocationService.saveLocation(petLocation);

        assertNotNull(result);
        assertEquals("Brazil", result.getCountry());
    }
}
