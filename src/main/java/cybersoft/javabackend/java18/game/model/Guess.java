package cybersoft.javabackend.java18.game.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Guess {
    private final String gameId;
    private final int number;
    private final LocalDateTime time;
    private String result;

    public Guess(String gameId, int number) {
        this.number = number;
        this.gameId = gameId;
        time = LocalDateTime.now();
    }

    public Guess(String gameId, int number, LocalDateTime time) {
        this.gameId = gameId;
        this.number = number;
        this.time = time;
    }

    public String getGameId() {
        return gameId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
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
}
