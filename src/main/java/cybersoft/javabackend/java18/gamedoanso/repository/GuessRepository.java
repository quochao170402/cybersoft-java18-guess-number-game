package cybersoft.javabackend.java18.gamedoanso.repository;

import cybersoft.javabackend.java18.gamedoanso.model.Guess;
import cybersoft.javabackend.java18.gamedoanso.store.GameStoreHolder;

import java.util.List;

public class GuessRepository {

    private final List<Guess> guesses;

    public GuessRepository() {
        guesses = GameStoreHolder.getStore().getGuesses();
    }

    public List<Guess> findByGameId(String id) {
        return guesses.stream()
                .filter(guess -> guess.getGameId().equals(id))
                .toList();
    }

    public void save(Guess guess) {
        System.out.println("guess repository Inserting guess...."+ guess);
        guesses.add(guess);
    }

    public List<Guess> getAll(){
        return guesses;
    }
}
