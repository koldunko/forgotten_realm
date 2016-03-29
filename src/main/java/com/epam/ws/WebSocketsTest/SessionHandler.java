package com.epam.ws.WebSocketsTest;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;

@ApplicationScoped
public class SessionHandler {
	private Set<Session> sessions = new HashSet<>();

	public void addSession(Session session) {
		sessions.add(session);
	}
	
	public void removeSession(Session session) {
		sessions.remove(session);
	}
	
	public void sendToAll(String message) {

	}
}
