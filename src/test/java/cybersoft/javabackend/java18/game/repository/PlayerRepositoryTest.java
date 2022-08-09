package cybersoft.javabackend.java18.game.repository;

import cybersoft.javabackend.java18.game.repository.impl.PlayerRepositoryImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlayerRepositoryTest {
    private PlayerRepository repository;

    @BeforeAll
    public void setupTest() {
        repository = PlayerRepositoryImpl.getRepository();
    }

    @Test
    void shouldGetAllWorkCorrectly() {
        assertEquals(101, repository.getAll().size());
    }

    @Test
    void shouldFindByUserNameWorkCorrectly() {
        assertNotNull(repository.findByUsername("admin"));
    }
}
