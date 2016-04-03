package com.epam.ws.WebSocketsTest;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Player {
	private static int i = 0;
	
	private String name;
	private int x;
	private int y;
	
	public Player() {
		this.name = "Player " + ++i;
		this.x = 352;
		this.y = 352;
	}
	
	public void move(int x, int y) {
		this.x += x;
		this.y += y;
	}
	
	@Override
	public String toString() {
		return String.format("{ \"name\": \"%s\", \"x\": \"%s\", \"y\": \"%s\" }", name, x, y);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}
}
