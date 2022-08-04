package cybersoft.javabackend.java18.game.model;

import java.util.List;

public class Player {
	private String username;
	private String password;
	private String name;
	private List<GameSession> gameSessions;
	
	public Player() {
	}

	public Player(String username, String password, String name) {
		this.username = username;
		this.password = password;
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public List<GameSession> getGames() {
		return gameSessions;
	}
	
	
}
