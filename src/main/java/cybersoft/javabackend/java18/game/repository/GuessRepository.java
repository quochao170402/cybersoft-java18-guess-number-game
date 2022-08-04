package cybersoft.javabackend.java18.game.repository;

import cybersoft.javabackend.java18.game.dao.GuessDao;
import cybersoft.javabackend.java18.game.dao.impl.GuessDaoImpl;
import cybersoft.javabackend.java18.game.model.Guess;

import java.util.List;

public class GuessRepository {

    private final GuessDao dao;

    public GuessRepository() {
        dao = GuessDaoImpl.getInstance();
    }

    public List<Guess> findByGameId(String id) {
        return dao.findByGameId(id);
    }

    public void save(Guess guess) {
        dao.insert(guess);
    }


}
