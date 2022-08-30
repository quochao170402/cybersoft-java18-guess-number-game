package cybersoft.javabackend.java18.game.repository;

import cybersoft.javabackend.java18.game.repository.impl.GuessRepositoryImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GuessRepositoryTest {
    GuessRepository repository;

    @BeforeAll
    void setup() {
        repository = GuessRepositoryImpl.getInstance();
    }

    @Test
    void shouldFindByGameId() {
        assertNotNull(repository.findByGameId("GAME00001"));
        assertEquals(5, repository.findByGameId("GAME00001").size());
        assertEquals(0, repository.findByGameId("ABCD").size());
    }
}
