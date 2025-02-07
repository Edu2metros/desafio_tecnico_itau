package domain.exception;

public class PetLocationNotFoundException extends RuntimeException {
    public PetLocationNotFoundException(String message) {
        super(message);
    }
}
