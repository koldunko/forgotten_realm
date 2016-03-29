package com.epam.ws.WebSocketsTest;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ApplicationScoped
@ServerEndpoint(value = "/ws")
public class WebSocketServer {

	private Random random = new Random();
	private static Map<Session, GameSession> sessions = new ConcurrentHashMap<>();
	private static GameState state = new GameState();

	@OnOpen
	public void onOpen(final Session session) {
		System.out.println("A client connected " + session.getId());
		Player player = new Player();
		state.addPlayer(player);
		GameSession gameSession = new GameSession(session, state, player);
		sessions.put(session, gameSession);
		gameSession.start();
	}

	@OnClose
	public void onClose(Session session) {
		sessions.get(session).setClosed(true);
		state.removePlayer(sessions.get(session).getPlayer());
		sessions.remove(session);
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		Player player = sessions.get(session).getPlayer();

		if ("left".equals(message)) {
			player.move(-10, 0);
		} else if ("right".equals(message)) {
			player.move(10, 0);
		} else if ("down".equals(message)) {
			player.move(0, 10);
		} else if ("up".equals(message)) {
			player.move(0, -10);
		}
		
		for (Map.Entry<Session, GameSession> e : sessions.entrySet()) {
			e.getValue().setChanged(true);
		}
		
		System.out.println("Message: " + message);
	}

	@OnError
	public void onError(Session session, Throwable t) {
		sessions.get(session).setClosed(true);
		state.removePlayer(sessions.get(session).getPlayer());
		sessions.remove(session);
		t.printStackTrace();
	}

}
