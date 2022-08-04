package cybersoft.javabackend.java18.game.dao;

import cybersoft.javabackend.java18.game.model.Guess;

import java.util.List;

public interface GuessDao {
    List<Guess> findByGameId(String id);
    boolean insert(Guess guess);
}
