package cybersoft.javabackend.java18.game;

import cybersoft.javabackend.java18.game.service.GameService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServiceTest {
    private GameService service;

    @BeforeAll
    void setup() {
        service = GameService.getService();
    }

}
