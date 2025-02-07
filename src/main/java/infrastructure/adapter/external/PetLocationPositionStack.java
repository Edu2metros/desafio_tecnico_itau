package infrastructure.adapter.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.model.PetLocation;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Service
public class PetLocationPositionStack {

    private static final String API_URL = "http://api.positionstack.com/v1/reverse";
    private static final String ACCESS_KEY = "4ca99e8eca0f31e9bc2c707ab1120c19";

    public PetLocation searchLocation(PetLocation petLocation) {
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
            petLocation.setAddress(data.path("label").asText());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return petLocation;
    }
}