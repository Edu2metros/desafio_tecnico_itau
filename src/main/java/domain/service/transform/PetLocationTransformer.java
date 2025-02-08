package domain.service.transform;

import domain.dto.PetLocationDTO;
import domain.model.PetLocation;
import infrastructure.adapter.external.PetLocationPositionStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PetLocationTransformer {

    private final PetLocationPositionStack positionStack;

    @Autowired
    public PetLocationTransformer(PetLocationPositionStack positionStack) {
        this.positionStack = positionStack;
    }

    public void enrichWithAddress(PetLocation petLocation) {
        positionStack.searchLocation(petLocation);
    }

    public PetLocationDTO toDTO(PetLocation petLocation) {
        return new PetLocationDTO(petLocation);
    }

    public void updateLocationFields(PetLocation target, PetLocation source) {
        if (source.getLatitude() != null) {
            target.setLatitude(source.getLatitude());
        }
        if (source.getLongitude() != null) {
            target.setLongitude(source.getLongitude());
        }
        if (source.getTimestamp() != null) {
            target.setTimestamp(source.getTimestamp());
        }
    }
}

