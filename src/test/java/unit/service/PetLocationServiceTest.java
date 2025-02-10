package unit.service;

import domain.dto.PetLocationDTO;
import domain.exception.PetLocationNotFoundException;
import domain.model.PetLocation;
import domain.port.out.PetLocationOut;
import domain.service.PetLocationService;
import domain.service.transform.PetLocationTransformer;
import domain.service.validation.PetLocationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    void testSaveLocation() {
        PetLocation petLocation = new PetLocation();
        petLocation.setSensorId("123");
        petLocation.setLatitude(-23.55);
        petLocation.setLongitude(-46.63);
        petLocation.setTimestamp(OffsetDateTime.now());

        PetLocationDTO petLocationDTO = new PetLocationDTO(petLocation);

        when(petLocationOut.save(any())).thenReturn(petLocation);
        when(transformer.toDTO(any())).thenReturn(petLocationDTO);

        PetLocationDTO result = petLocationService.saveLocation(petLocation);

        assertNotNull(result);
        verify(validator).validate(petLocation);
        verify(transformer).enrichWithAddress(petLocation);
        verify(petLocationOut).save(petLocation);
        verify(transformer).toDTO(petLocation);
    }

    @Test
    void testUpdateLocationBySensorId() {
        PetLocation petLocation = new PetLocation();
        petLocation.setSensorId("123");
        petLocation.setLatitude(-23.55);
        petLocation.setLongitude(-46.63);
        petLocation.setTimestamp(OffsetDateTime.now());

        PetLocationDTO petLocationDTO = new PetLocationDTO(petLocation);

        when(petLocationOut.findBySensorId("123")).thenReturn(petLocation);
        when(petLocationOut.save(any())).thenReturn(petLocation);
        when(transformer.toDTO(any())).thenReturn(petLocationDTO);

        PetLocationDTO result = petLocationService.updateLocationBySensorId("123", petLocation);

        assertNotNull(result);
        verify(transformer).updateLocationFields(any(), eq(petLocation));
        verify(transformer).enrichWithAddress(any());
        verify(petLocationOut).save(any());
        verify(transformer).toDTO(any());
    }

    @Test
    void testUpdateLocationBySensorId_NotFound() {
        when(petLocationOut.findBySensorId("123")).thenReturn(null);

        PetLocation petLocation = new PetLocation();
        petLocation.setSensorId("123");

        assertThrows(PetLocationNotFoundException.class, () -> petLocationService.updateLocationBySensorId("123", petLocation));
    }

    @Test
    void testUpdateLocationByPatch() {
        PetLocation petLocation = new PetLocation();
        petLocation.setSensorId("123");
        petLocation.setLatitude(-23.55);
        petLocation.setLongitude(-46.63);
        petLocation.setTimestamp(OffsetDateTime.now());

        PetLocationDTO petLocationDTO = new PetLocationDTO(petLocation);

        when(petLocationOut.findBySensorId("123")).thenReturn(petLocation);
        when(petLocationOut.save(any())).thenReturn(petLocation);
        when(transformer.toDTO(any())).thenReturn(petLocationDTO);

        PetLocationDTO result = petLocationService.updateLocationByPatch("123", petLocation);

        assertNotNull(result);
        verify(validator).validatePatch("123", petLocation);
        verify(transformer).updateLocationFields(any(), eq(petLocation));
        verify(transformer).enrichWithAddress(any());
        verify(petLocationOut).save(any());
        verify(transformer).toDTO(any());
    }

    @Test
    void testUpdateLocationByPatch_NotFound() {
        when(petLocationOut.findBySensorId("123")).thenReturn(null);

        PetLocation petLocation = new PetLocation();
        petLocation.setSensorId("123");

        assertThrows(PetLocationNotFoundException.class, () -> petLocationService.updateLocationByPatch("123", petLocation));
    }

    @Test
    void testDeleteLocationBySensorId() {
        PetLocation petLocation = new PetLocation();
        petLocation.setSensorId("123");

        when(petLocationOut.findBySensorId("123")).thenReturn(petLocation);

        petLocationService.deleteLocationBySensorId("123");

        verify(petLocationOut).deleteBySensorId("123");
    }

    @Test
    void testDeleteLocationBySensorId_NotFound() {
        when(petLocationOut.findBySensorId("123")).thenReturn(null);

        assertThrows(PetLocationNotFoundException.class, () -> petLocationService.deleteLocationBySensorId("123"));
    }

    @Test
    void testGetAllLocations() {
        PetLocation petLocation1 = new PetLocation();
        PetLocation petLocation2 = new PetLocation();

        when(petLocationOut.findAll()).thenReturn(Arrays.asList(petLocation1, petLocation2));
        when(transformer.toDTO(any())).thenReturn(new PetLocationDTO(petLocation1), new PetLocationDTO(petLocation2));

        List<PetLocationDTO> result = petLocationService.getAllLocations();

        assertEquals(2, result.size());
        verify(petLocationOut).findAll();
        verify(transformer, times(2)).toDTO(any());
    }

    @Test
    void testGetLastLocationBySensorId() {
        PetLocation petLocation = new PetLocation();
        petLocation.setSensorId("123");

        PetLocationDTO petLocationDTO = new PetLocationDTO(petLocation);

        when(petLocationOut.findBySensorId("123")).thenReturn(petLocation);
        when(transformer.toDTO(any())).thenReturn(petLocationDTO);

        PetLocationDTO result = petLocationService.getLastLocationBySensorId("123");

        assertNotNull(result);
        verify(petLocationOut).findBySensorId("123");
        verify(transformer).toDTO(petLocation);
    }

    @Test
    void testExistsBySensorId() {
        when(petLocationOut.existsBySensorId("123")).thenReturn(true);
        when(petLocationOut.existsBySensorId("999")).thenReturn(false);

        assertTrue(petLocationService.existsBySensorId("123"));
        assertFalse(petLocationService.existsBySensorId("999"));

        verify(petLocationOut).existsBySensorId("123");
        verify(petLocationOut).existsBySensorId("999");
    }
}