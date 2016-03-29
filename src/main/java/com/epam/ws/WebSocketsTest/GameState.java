package com.epam.ws.WebSocketsTest;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameState {
	private List<Player> players = new CopyOnWriteArrayList<>();
	
	public GameState() {
		
	}
	
	public void addPlayer(Player player) {
		players.add(player);
	}
	
	public void removePlayer(Player player) {
		players.remove(player);
	}
	
	public List<Player> getPlayers() {
		return players;
	}
}
