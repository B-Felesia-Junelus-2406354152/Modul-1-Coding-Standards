package id.ac.ui.cs.advprog.eshop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class EshopApplicationTests {

    @Test
    void contextLoads() {
        assertTrue(true, "The application context should load successfully");
    }

    @Test
    void testMainMethod() {
        assertDoesNotThrow(() -> EshopApplication.main(new String[] {}));
    }
}
