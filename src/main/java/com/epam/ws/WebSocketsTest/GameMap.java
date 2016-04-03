package com.epam.ws.WebSocketsTest;

import java.util.Random;

public class GameMap {

	private int[][] map; 
	private int tileCount = 6;
	
	public GameMap() {
		map = generateRandomMap(50, 37);
	}
	
	public int[][] getMap() {
		return map;
	}
	
	private int[][] generateRandomMap(int width, int height) {
		int[][] map = new int[height][width];
		Random random = new Random();
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				map[i][j] = random.nextInt(tileCount);
			}
		}
		
		return map;
	}
}
