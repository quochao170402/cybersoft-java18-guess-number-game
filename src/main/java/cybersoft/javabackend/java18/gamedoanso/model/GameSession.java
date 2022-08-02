package cybersoft.javabackend.java18.gamedoanso.model;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameSession implements Serializable {
    private static int indexId = 1;
    private final int targetNumber;
    private Random random = null;
    private String id;
    private List<Guess> guesses;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isCompleted;
    private boolean isActive;
    private String username; // username

    public GameSession(String username) {
        this.id = "GAME" + String.format("%05d", indexId++);
        this.targetNumber = getRandomInt();
        this.guesses = new ArrayList<>();
        this.startTime = LocalDateTime.now();
        this.username = username;
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

    public long getTime() {
        return Duration.between(startTime, endTime).toMillis();
    }

    public long getMinutes(){
        return Duration.between(startTime,endTime).toMinutes();
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
