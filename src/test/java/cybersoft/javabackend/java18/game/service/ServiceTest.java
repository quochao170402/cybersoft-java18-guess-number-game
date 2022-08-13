package cybersoft.javabackend.java18.game.service;

import cybersoft.javabackend.java18.game.service.impl.GameServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServiceTest {
    private GameServiceImpl service;

    @BeforeAll
    void setup() {
        service = GameServiceImpl.getService();
    }

    @Test
    void shouldLoginWorkCorrectly() {
        assertEquals("admin", service.login("admin", "123").getUsername());
        assertEquals("Bùi Quốc Hào", service.login("admin", "123").getName());
    }

    @Test
    void shouldRegisterWorkCorrectly() {
        assertNull(service.register("admin", "123", "Bùi Quốc Hào"));
        assertNotNull(service.register("admin123", "123", "Bùi Quốc Hào"));
    }

    @Test
    void shouldCreateGameWorkCorrectly() {
        
    }

}
