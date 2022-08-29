package cybersoft.javabackend.java18.game.repository.impl;

import cybersoft.javabackend.java18.game.repository.AbstractRepository;
import cybersoft.javabackend.java18.game.repository.TokenRepository;
import cybersoft.javabackend.java18.game.security.Token;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TokenRepositoryImpl extends AbstractRepository<Token> implements TokenRepository {
    private static TokenRepository repository = null;

    private TokenRepositoryImpl() {

    }

    public static TokenRepository getRepository() {
        if (repository == null) repository = new TokenRepositoryImpl();
        return repository;
    }

    @Override
    public Token getToken(String selector) {
        return executeQuerySingle(connection -> {

            String query = """
                    select selector, validator, username
                    from token
                    where selector = ?;
                    """;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, selector);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                Token token = new Token();
                token.setSelector(results.getString("selector"));
                token.setValidator(results.getString("validator"));
                token.setUsername(results.getString("username"));
                return token;
            }
            return null;
        });
    }

    @Override
    public boolean resetToken(String selector, String validator) {
        return executeUpdate(connection -> {

            String query = """
                    update token
                    set validator = ?
                    where selector = ?;
                    """;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, validator);
            statement.setString(2, selector);
            return statement.executeUpdate();
        }) != 0;
    }

    @Override
    public boolean insert(Token token) {
        return executeUpdate(connection -> {
            String query = """
                    insert into token (selector, validator, username)
                    values (?, ?, ?);
                    """;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, token.getSelector());
            statement.setString(2, token.getValidator());
            statement.setString(3, token.getUsername());

            return statement.executeUpdate();
        }) != 0;
    }

    @Override
    public void deleteToken(String selector) {
        executeUpdate(connection -> {
            String query = """
                    delete from token
                    where selector = ?;
                    """;

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, selector);
            return statement.executeUpdate();
        });
    }
}
