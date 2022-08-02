package cybersoft.javabackend.java18.gamedoanso.model;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class Guess {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final int number;
    private final String gameId;
    private final LocalDateTime time;
    private String result;

    public Guess(int number, String gameId) {
        this.number = number;
        this.gameId = gameId;
        time = LocalDateTime.now();
    }

    public String getGameId() {
        return gameId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String turnResult) {
        this.result = turnResult;
    }

    public int getNumber() {
        return number;
    }

    public String getTime() {
        return time.format(formatter);
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
