package com.epam.ws.WebSocketsTest;

public class JsonMessage {
	private String type;
	private Object message;

	public JsonMessage() {

	}

	public JsonMessage(String type, Object message) {
		this.type = type;
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

}
