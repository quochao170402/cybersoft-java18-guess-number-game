package cybersoft.javabackend.java18.game.dao.impl;

import cybersoft.javabackend.java18.game.dao.DBConnect;
import cybersoft.javabackend.java18.game.dao.PlayerDao;
import cybersoft.javabackend.java18.game.model.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerDaoImpl implements PlayerDao {

    private static PlayerDaoImpl dao = null;
    private PlayerDaoImpl(){

    }

    public static PlayerDaoImpl getInstance(){
        if (dao == null) dao = new PlayerDaoImpl();
        return dao;
    }

    private final Connection connection = DBConnect.getINSTANCE().getConnection();

    @Override
    public Player findByUsername(String username){
        if (connection == null){
            return null;
        }
        List<Player> players = new ArrayList<>();
        String query = "select * from player where username = ?";
        try {
            PreparedStatement  statement = connection.prepareStatement(query);
            statement.setString(1,username);
            ResultSet result = statement.executeQuery();
            while (result.next()){
                players.add(new Player(
                        result.getString("username"),
                        result.getString("password"),
                        result.getString("name"))
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return players.size() == 0 ? null : players.get(0);
    }

    @Override
    public boolean insert(Player player){
        if (connection == null){
            return false;
        }
        String query = "insert into player ( username, password, name) " +
                "values (?,?,?)";
        try {
            PreparedStatement  statement = connection.prepareStatement(query);
            statement.setString(1,player.getUsername());
            statement.setString(2,player.getPassword());
            statement.setString(3,player.getName());
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean existedByUsername(String username) {
        if (connection == null){
            return false;
        }
        List<Player> players = new ArrayList<>();
        String query = "select * from player where username = ?";
        try {
            PreparedStatement  statement = connection.prepareStatement(query);
            statement.setString(1,username);
            ResultSet result = statement.executeQuery();
            while (result.next()){
                players.add(new Player(
                        result.getString("username"),
                        result.getString("password"),
                        result.getString("name"))
                );
            }
        } catch (SQLException e) {
            return false;
        }
        return players.size() != 0;
    }

//    public String getIdByUsername(String username) {
//        if (connection == null){
//            return null;
//        }
//        List<Player> players = new ArrayList<>();
//        String query = "select id from player where username = ?";
//        String id = null;
//        try {
//            PreparedStatement  statement = connection.prepareStatement(query);
//            statement.setString(1,username);
//            ResultSet result = statement.executeQuery();
//            while (result.next()){
//                id = result.getString(1);
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return id;
//    }
}
