package unit.external;

import domain.model.PetLocation;
import domain.service.validation.PetLocationValidator;
import infrastructure.adapter.external.PetLocationPositionStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PetLocationPositionStackTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private PetLocationValidator validator;

    @InjectMocks
    private PetLocationPositionStack petLocationPositionStack;

    private static final String MOCK_RESPONSE = """
            {
                "data": [
                    {
                        "country": "Brazil",
                        "region": "São Paulo",
                        "locality": "São Paulo",
                        "neighbourhood": "Centro",
                        "label": "Rua Frederico Alvarenga 65"
                    }
                ]
            }
            """;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearchLocation() throws Exception {
        PetLocation petLocation = new PetLocation();
        petLocation.setLatitude(-23.55);
        petLocation.setLongitude(-46.63);

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(MOCK_RESPONSE);

        PetLocation updatedLocation = petLocationPositionStack.searchLocation(petLocation);

        assertNotNull(updatedLocation);
        assertEquals("Brazil", updatedLocation.getCountry());
        assertEquals("Sao Paulo", updatedLocation.getState());
        assertEquals("São Paulo", updatedLocation.getCity());
        assertEquals("Se", updatedLocation.getNeighborhood());
        assertEquals("Rua Frederico Alvarenga 65", updatedLocation.getAddress());
    }
}