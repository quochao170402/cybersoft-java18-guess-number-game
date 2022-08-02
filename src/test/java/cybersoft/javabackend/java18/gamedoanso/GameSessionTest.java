package cybersoft.javabackend.java18.gamedoanso;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@TestInstance(Lifecycle.PER_CLASS)
class GameSessionTest {
	private Game game;
	
//	private Executable startGame = () -> game.start();
	
	@BeforeAll
	public void setupTest() {
		game = new Game();
	}
	
	@Test
	void shouldStartedSuccessfully() {
		assertDoesNotThrow(() -> game.start());
	}
}
