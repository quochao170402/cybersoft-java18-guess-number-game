package cybersoft.javabackend.java18.game.mapper;

import cybersoft.javabackend.java18.game.model.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerMapper implements RowMapper<Player> {
    @Override
    public Player map(ResultSet results) throws SQLException {
        return new Player()
                .username(results.getString("username"))
                .password(results.getString("password"))
                .name(results.getString("name"));
    }
}
