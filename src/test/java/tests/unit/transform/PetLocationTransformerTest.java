package tests.unit.transform;

import domain.dto.PetLocationDTO;
import domain.model.PetLocation;
import domain.service.transform.PetLocationTransformer;
import infrastructure.adapter.external.PetLocationPositionStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PetLocationTransformerTest {

    @Mock
    private PetLocationPositionStack positionStack;

    @InjectMocks
    private PetLocationTransformer transformer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEnrichWithAddress() {
        PetLocation petLocation = new PetLocation();
        transformer.enrichWithAddress(petLocation);
        verify(positionStack, times(1)).searchLocation(petLocation);
    }

    @Test
    void testToDTO() {
        PetLocation petLocation = new PetLocation();
        PetLocationDTO dto = transformer.toDTO(petLocation);
        assertNotNull(dto);
        assertEquals(petLocation.getCountry(), dto.getCountry());
        assertEquals(petLocation.getState(), dto.getState());
        assertEquals(petLocation.getCity(), dto.getCity());
        assertEquals(petLocation.getNeighborhood(), dto.getNeighborhood());
        assertEquals(petLocation.getAddress(), dto.getAddress());

    }

    @Test
    void testUpdateLocationFields() {
        PetLocation source = new PetLocation();
        source.setLatitude(10.0);
        source.setLongitude(20.0);
        source.setTimestamp(OffsetDateTime.parse("2025-12-31T23:59:59Z"));

        PetLocation target = new PetLocation();
        transformer.updateLocationFields(target, source);

        assertEquals(10.0, target.getLatitude());
        assertEquals(20.0, target.getLongitude());
        assertEquals(OffsetDateTime.parse("2025-12-31T23:59:59Z"), target.getTimestamp());
    }

    @Test
    void testUpdateLocationFieldsWithNullValues() {
        PetLocation source = new PetLocation();
        PetLocation target = new PetLocation();
        target.setLatitude(5.0);
        target.setLongitude(15.0);
        source.setTimestamp(OffsetDateTime.parse("2025-12-31T23:59:59Z"));

        transformer.updateLocationFields(target, source);

        assertEquals(5.0, target.getLatitude());
        assertEquals(15.0, target.getLongitude());
        assertEquals(OffsetDateTime.parse("2025-12-31T23:59:59Z"), target.getTimestamp());
    }
}
