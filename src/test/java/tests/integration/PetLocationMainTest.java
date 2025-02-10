package tests.integration;

import infrastructure.PetLocationMain;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(classes = PetLocationMain.class)
class PetLocationMainTest {
    @Test
    void applicationStartsSuccessfully() {
        assertDoesNotThrow(() -> PetLocationMain.main(new String[]{}));
    }
}
