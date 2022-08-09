package cybersoft.javabackend.java18.game.model;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameSession implements Serializable {
    private int targetNumber;
    private String id;
    private LocalDateTime startTime;
    private String username; // username
    private Random random = null;
    private List<Guess> guesses;
    private LocalDateTime endTime;
    private boolean isCompleted;
    private boolean isActive;
    private int guessesSize;

    public GameSession(String username, int indexId) {
        this.id = "GAME" + String.format("%05d", indexId);
        this.targetNumber = getRandomInt();
        this.guesses = new ArrayList<>();
        this.startTime = LocalDateTime.now();
        this.username = username;
        isActive = true;
        isCompleted = false;
    }

    public GameSession() {

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

    public void setGuesses(List<Guess> guesses) {
        this.guesses = guesses;
    }


    public float getTime() {
        return (float) Duration.between(startTime, endTime).getSeconds() / 60;
    }

    public String getTimeFormatted() {
        return String.format("%.2f", getTime());
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
                '}' + "\n";
    }

    // fluent style api
    public GameSession id(String id) {
        this.id = id;
        return this;
    }

    public GameSession targetNumber(int targetNumber) {
        this.targetNumber = targetNumber;
        return this;
    }

    public GameSession startTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public GameSession endTime(LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public GameSession isCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
        return this;
    }

    public GameSession isActive(boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public GameSession username(String username) {
        this.username = username;
        return this;
    }

}
