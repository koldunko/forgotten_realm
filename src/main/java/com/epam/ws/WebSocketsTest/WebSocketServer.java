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
import com.fasterxml.jackson.databind.node.ObjectNode;

@ApplicationScoped
@ServerEndpoint(value = "/ws")
public class WebSocketServer {

	private static final Map<Session, Player> clients = new ConcurrentHashMap<>();
	private static final GameState world = new GameState();
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final GameMap currentMap = new GameMap();

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
			player.move(-16, 0);
		} else if ("right".equals(message)) {
			player.move(16, 0);
		} else if ("down".equals(message)) {
			player.move(0, 16);
		} else if ("up".equals(message)) {
			player.move(0, -16);
		} else if ("loadMap".equals(message)) {
			sendPrivateMessage(session, new JsonMessage("map", currentMap));
		}
		
		sendMessageToAll(new JsonMessage("players", world.getPlayers()));
		
		System.out.println("Message: " + message);
	}
	
	private void sendPrivateMessage(Session session, JsonMessage jsonMessage) {
		try {
			String message = mapper.writeValueAsString(jsonMessage);
			session.getBasicRemote().sendText(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendMessageToAll(JsonMessage jsonMessage) {
		for (Map.Entry<Session, Player> e : clients.entrySet()) {
			try {
				String message = mapper.writeValueAsString(jsonMessage);
				e.getKey().getBasicRemote().sendText(message);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	@OnError
	public void onError(Session session, Throwable t) {
		world.removePlayer(clients.get(session));
		clients.remove(session);
		
		t.printStackTrace();
	}

}
