package cybersoft.javabackend.java18.game.security;

public class Token {
    private String selector;
    private String validator;
    private String username;

    public Token() {
    }

    public Token(String selector, String validator, String username) {
        this.selector = selector;
        this.validator = validator;
        this.username = username;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getValidator() {
        return validator;
    }

    public void setValidator(String validator) {
        this.validator = validator;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
