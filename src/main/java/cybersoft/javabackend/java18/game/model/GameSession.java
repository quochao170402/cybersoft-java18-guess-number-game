package cybersoft.javabackend.java18.game.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameSession implements Serializable {
    private final int targetNumber;
    private Random random = null;
    private final String id;
    private List<Guess> guesses;
    private final LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isCompleted;
    private boolean isActive;
    private final String username; // username

    public GameSession(String username, int indexId) {
        this.id = "GAME" + String.format("%05d", indexId);
        this.targetNumber = getRandomInt();
        this.guesses = new ArrayList<>();
        this.startTime = LocalDateTime.now();
        this.username = username;
        isActive = true;
        isCompleted = false;
    }

    public GameSession( String id, int targetNumber, LocalDateTime startTime,
                       LocalDateTime endTime, boolean isCompleted,
                       boolean isActive, String username) {
        this.targetNumber = targetNumber;
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isCompleted = isCompleted;
        this.isActive = isActive;
        this.username = username;
        this.guesses = new ArrayList<>();
    }

    private int getRandomInt() {
        if (random == null) {
            random = new Random();
        }
        return random.nextInt(1000 - 1) + 1;
    }

    public int getTargetNumber() {
        return targetNumber;
    }

    public List<Guess> getGuesses() {
        return guesses;
    }

    public float getTime() {
        return (float) Duration.between(startTime,endTime).getSeconds()/60;
    }

    public String getTimeFormatted(){
        return String.format("%.2f",getTime());
    }

    public void finished() {
        isCompleted = true;
        isActive = false;
        endTime = LocalDateTime.now();
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "GameSession{" +
                "targetNumber=" + targetNumber +
                ", id='" + id + '\'' +
                ", guesses=" + guesses +
                ", isCompleted=" + isCompleted +
                ", isActive=" + isActive +
                ", username='" + username + '\'' +
                '}'+"\n";
    }

    public void setGuesses(List<Guess> guesses) {
        this.guesses = guesses;
    }
}
