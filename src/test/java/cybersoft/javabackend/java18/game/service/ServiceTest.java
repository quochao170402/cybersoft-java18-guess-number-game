package cybersoft.javabackend.java18.game.service;

import cybersoft.javabackend.java18.game.service.impl.GameServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServiceTest {
    private GameServiceImpl service;

    @BeforeAll
    void setup() {
        service = GameServiceImpl.getService();
    }

}
