package tests.integration.controller;

import domain.dto.PetLocationDTO;
import domain.exception.PetLocationNotFoundException;
import domain.model.PetLocation;
import domain.service.PetLocationService;
import infrastructure.PetLocationMain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = PetLocationMain.class)
class PetLocationControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private PetLocationService petLocationService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testGetAllLocations() throws Exception {
        when(petLocationService.getAllLocations()).thenReturn(List.of(new PetLocationDTO()));

        mockMvc.perform(get("/location"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetLastLocationBySensorId() throws Exception {
        PetLocationDTO petLocationDTO = new PetLocationDTO();
        petLocationDTO.setCountry("Brazil");

        when(petLocationService.getLastLocationBySensorId(anyString())).thenReturn(petLocationDTO);

        mockMvc.perform(get("/location/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country").value("Brazil"));
    }

    @Test
    void testReceiveLocation() throws Exception {
        PetLocation petLocation = new PetLocation();
        petLocation.setSensorId("789");
        petLocation.setLatitude(-23.55);
        petLocation.setLongitude(-46.63);
        petLocation.setTimestamp(OffsetDateTime.parse("2021-08-01T10:00:00Z"));
        PetLocationDTO petLocationDTO = new PetLocationDTO();
        petLocationDTO.setCountry("Brazil");
        when(petLocationService.saveLocation(any(PetLocation.class))).thenReturn(petLocationDTO);
        mockMvc.perform(post("/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sensorId\":\"789\",\"latitude\":-23.55,\"longitude\":-46.63,\"timestamp\":\"2021-08-01T10:00:00Z\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.country").value("Brazil"));
    }

    @Test
    void testUpdateLocationBySensorId() throws Exception {
        PetLocation petLocation = new PetLocation();
        petLocation.setSensorId("789");
        petLocation.setLatitude(-23.55);
        petLocation.setLongitude(-46.63);
        petLocation.setTimestamp(OffsetDateTime.parse("2021-08-01T10:00:00Z"));

        PetLocationDTO petLocationDTO = new PetLocationDTO();
        petLocationDTO.setCountry("Brazil");

        when(petLocationService.updateLocationBySensorId(anyString(), any(PetLocation.class))).thenReturn(petLocationDTO);

        mockMvc.perform(put("/location/789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sensorId\":\"789\",\"latitude\":-23.55,\"longitude\":-46.63, \"timestamp\":\"2021-08-01T10:00:00Z\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country").value("Brazil"));
    }

    @Test
    void testUpdateLocationByPatch() throws Exception {
        PetLocation petLocation = new PetLocation();
        petLocation.setLatitude(-23.55);

        PetLocationDTO petLocationDTO = new PetLocationDTO();
        petLocationDTO.setCountry("Brazil");

        when(petLocationService.updateLocationByPatch(anyString(), any(PetLocation.class))).thenReturn(petLocationDTO);

        mockMvc.perform(patch("/location/789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"latitude\":-23.55}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country").value("Brazil"));
    }

    @Test
    void testDeleteExistingLocation() throws Exception {
        doNothing().when(petLocationService).deleteLocationBySensorId("789");

        mockMvc.perform(delete("/location/789"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteNonExistentLocation() throws Exception {
        doThrow(new PetLocationNotFoundException("Pet location not found for sensor ID: 999"))
                .when(petLocationService).deleteLocationBySensorId("999");

        mockMvc.perform(delete("/location/999"))
                .andExpect(status().isNotFound());
    }
    @Test
    void testReceiveLocationInvalidArgument() throws Exception {
        when(petLocationService.saveLocation(any(PetLocation.class)))
                .thenThrow(new IllegalArgumentException("Invalid location data"));

        mockMvc.perform(post("/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sensorId\":\"789\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"latitude\":\"Latitude is required\",\"timestamp\":\"Timestamp is required\",\"longitude\":\"Longitude is required\"}"));
    }

    @Test
    void testUpdateLocationBySensorIdNotFound() throws Exception {
        when(petLocationService.updateLocationBySensorId(anyString(), any(PetLocation.class)))
                .thenThrow(new PetLocationNotFoundException("Not found"));

        mockMvc.perform(put("/location/789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sensorId\":\"789\",\"latitude\":-23.55,\"longitude\":-46.63,\"timestamp\":\"2021-08-01T10:00:00Z\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Not found"));
    }

    @Test
    void testReceiveLocationRuntimeException() throws Exception {
        when(petLocationService.saveLocation(any(PetLocation.class)))
                .thenThrow(new RuntimeException("Database conflict"));

        mockMvc.perform(post("/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sensorId\":\"789\",\"latitude\":-23.55,\"longitude\":-46.63,\"timestamp\":\"2021-08-01T10:00:00Z\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Database conflict"));
    }
}

