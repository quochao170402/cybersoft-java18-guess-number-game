package cybersoft.javabackend.java18.game.repository;

import cybersoft.javabackend.java18.game.repository.impl.GameSessionRepositoryImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameSessionRepositoryTest {
    GameSessionRepository repository;

    @BeforeAll
    void setup() {
        repository = GameSessionRepositoryImpl.getInstance();
    }

    @Test
    void shouldFindByUsernameWorkCorrectly() {
        assertNotNull(repository.findByUsername("admin"));

        assertNotNull(repository.findByUsername("abcdeafa"));
    }

    @Test
    void shouldRankingWithPaginationWorkCorrectly() {
        assertNotNull(repository.rankingWithPagination(1));
        assertNotNull(repository.rankingWithPagination(2));
        assertNotNull(repository.rankingWithPagination(3));
    }

}
