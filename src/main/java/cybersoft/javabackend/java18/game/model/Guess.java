package cybersoft.javabackend.java18.game.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Guess {
    private final String gameId;
    private final String username;
    private final int number;
    private String result;
    private final LocalDateTime time;

    public Guess(String gameId, String username, int number) {
        this.number = number;
        this.gameId = gameId;
        this.username = username;
        time = LocalDateTime.now();
    }

    public Guess(String gameId, String username, int number, String result, LocalDateTime time) {
        this.gameId = gameId;
        this.username = username;
        this.number = number;
        this.result = result;
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

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "Guess{" +
                "number=" + number +
                ", gameId='" + gameId + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
