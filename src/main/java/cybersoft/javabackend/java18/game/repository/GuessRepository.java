package cybersoft.javabackend.java18.game.repository;

import cybersoft.javabackend.java18.game.model.Guess;

import java.util.List;

public interface GuessRepository {
    List<Guess> findByGameId(String gameId);

    void insert(Guess guess);
}
