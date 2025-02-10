package infrastructure.adapter.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.model.PetLocation;
import domain.service.validation.PetLocationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Service
public class PetLocationPositionStack {

    private static final String API_URL = "http://api.positionstack.com/v1/reverse";
    private static final String ACCESS_KEY = "24b8151dc2e598d0841ec0fea17b2297";

    private final PetLocationValidator validator;

    @Autowired
    public PetLocationPositionStack(PetLocationValidator validator) {
        this.validator = validator;
    }

    public PetLocation searchLocation(PetLocation petLocation) {
        validator.validateCoordinates(petLocation);
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("access_key", ACCESS_KEY)
                .queryParam("query", petLocation.getLatitude() + "," + petLocation.getLongitude())
                .toUriString();

        String response = restTemplate.getForObject(url, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode data = root.path("data").get(0);
            petLocation.setCountry(data.path("country").asText());
            petLocation.setState(data.path("region").asText());
            petLocation.setCity(data.path("locality").asText());
            petLocation.setNeighborhood(data.path("neighbourhood").asText());
            petLocation.setAddress(data.path("label").asText().split(",")[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return petLocation;
    }
}