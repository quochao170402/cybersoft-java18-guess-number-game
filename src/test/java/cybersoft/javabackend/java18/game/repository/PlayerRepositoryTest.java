package cybersoft.javabackend.java18.game.repository;

import cybersoft.javabackend.java18.game.repository.impl.PlayerRepositoryImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlayerRepositoryTest {
    PlayerRepository repository;

    @BeforeAll
    void setup() {
        repository = PlayerRepositoryImpl.getRepository();
    }

    @Test
    void shouldExistedByUsernameWorkCorrectly() {
        assertTrue(repository.existedByUsername("admin"));
        assertFalse(repository.existedByUsername("010101010"));
    }

    @Test
    void shouldFindByUsernameWorkCorrectly() {
        assertNotNull(repository.findByUsername("admin"));
        assertNull(repository.findByUsername("0101010"));
    }

}
