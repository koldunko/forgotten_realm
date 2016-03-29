package com.epam.ws.WebSocketsTest;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ApplicationScoped
@ServerEndpoint(value = "/ws")
public class WebSocketServer {

	private static final Map<Session, Player> clients = new ConcurrentHashMap<>();
	private static final GameState world = new GameState();
	private static final ObjectMapper mapper = new ObjectMapper();

	@OnOpen
	public void onOpen(final Session session) {
		System.out.println("A client connected " + session.getId());
		
		Player player = new Player();
		
		world.addPlayer(player);
		clients.put(session, player);
	}

	@OnClose
	public void onClose(Session session) {
		world.removePlayer(clients.get(session));
		clients.remove(session);
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		Player player = clients.get(session);

		if ("left".equals(message)) {
			player.move(-10, 0);
		} else if ("right".equals(message)) {
			player.move(10, 0);
		} else if ("down".equals(message)) {
			player.move(0, 10);
		} else if ("up".equals(message)) {
			player.move(0, -10);
		}
		
		
		try {
			String msg = mapper.writeValueAsString(world.getPlayers());
			sendMessageToAll(msg);
		} catch (IOException	 e) {
			e.printStackTrace();
		}
		
		System.out.println("Message: " + message);
	}

	private void sendMessageToAll(String message) throws IOException {
		for (Map.Entry<Session, Player> e : clients.entrySet()) {
			e.getKey().getBasicRemote().sendText(message);
		}
	}

	@OnError
	public void onError(Session session, Throwable t) {
		world.removePlayer(clients.get(session));
		clients.remove(session);
		
		t.printStackTrace();
	}

}
