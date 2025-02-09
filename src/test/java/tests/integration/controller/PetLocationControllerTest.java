package tests.integration.controller;

import domain.dto.PetLocationDTO;
import domain.service.PetLocationService;
import infrastructure.PetLocationMain;
import infrastructure.adapter.controller.PetLocationController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ContextConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(PetLocationController.class)
@ContextConfiguration(classes = PetLocationMain.class)
class PetLocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetLocationService petLocationService;

    @Test
    void testGetLastLocationBySensorId() throws Exception {
        when(petLocationService.getLastLocationBySensorId("123")).thenReturn(null);

        mockMvc.perform(get("/location/123"))
                .andExpect(status().isOk());
    }

    private org.mockito.internal.stubbing.BaseStubbing<Object> when(PetLocationDTO lastLocationBySensorId) {
        return null;
    }
}
