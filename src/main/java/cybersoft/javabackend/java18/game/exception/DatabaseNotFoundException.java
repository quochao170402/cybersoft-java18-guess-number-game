package cybersoft.javabackend.java18.game.exception;

public class DatabaseNotFoundException extends RuntimeException {

    public DatabaseNotFoundException(String e) {
        super(e);
    }

    public DatabaseNotFoundException() {
        super();
    }
}
