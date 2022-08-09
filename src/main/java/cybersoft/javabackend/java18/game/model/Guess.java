package cybersoft.javabackend.java18.game.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Guess {
    private String gameId;
    private int number;
    private LocalDateTime time;
    private int result;

    public Guess(String gameId, int number, int result) {
        this.number = number;
        this.gameId = gameId;
        this.result = result;
        time = LocalDateTime.now();
    }

    public Guess() {

    }

    public String getGameId() {
        return gameId;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getNumber() {
        return number;
    }

    public String getTimeFormatted() {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public LocalDateTime getTime() {
        return time;
    }

    // fluent style api
    public Guess gameId(String gameId) {
        this.gameId = gameId;
        return this;
    }

    public Guess number(int number) {
        this.number = number;
        return this;
    }

    public Guess time(LocalDateTime time) {
        this.time = time;
        return this;
    }

    public Guess result(int result) {
        this.result = result;
        return this;
    }
}
