package com.epam.ws.WebSocketsTest;

import java.io.IOException;

import javax.websocket.Session;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GameSession extends Thread {

	private static final int FPS = 60;
	private Session session;
	private GameState state;
	private volatile boolean closed;
	private ObjectMapper mapper = new ObjectMapper();
	private Player player;
	private boolean changed;

	public GameSession(Session session, GameState state, Player player) {
		this.session = session;
		this.state = state;
		this.player = player;
	}

	@Override
	public void run() {
		try {
			while (!closed) {
				if (changed) {
					changed = false;
					String msg = mapper.writeValueAsString(state.getPlayers());
					System.out.println(msg);
					session.getBasicRemote().sendText(msg);
				}
				Thread.sleep(1000 / FPS);
			}
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isClosed() {
		return closed;
	}
	
	public void setClosed(boolean closed) {
		this.closed = closed;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setChanged(boolean changed) {
		this.changed = changed;
	}

}
