package com.game.pacman.world.enteties.creatures.agent;

import com.game.pacman.world.tiles.Tile;

public class RandomStrategy extends Strategy {
	
	private float lastX, lastY;
	private int lastXDir, lastYDir;

	private boolean decisionTime;
	private long lastTime;
	private int timer;
	private double threshold;
	private static float DPS = 1f; // decisions per second

	public RandomStrategy(final int[][] matrix) {
		super(matrix);
		lastXDir = 0;
		lastYDir = 0;
		
		decisionTime = false;
		lastTime = System.nanoTime();
		timer = 0;
		threshold = 1000_000_000 / DPS;
	}

	@Override
	public void findPath(float currentX, float currentY, float playerX, float playerY) {
		decisionTime = false;
		lastX = currentX;
		lastY = currentY;
		int logicalX = (int) currentX/Tile.TILESIZE;
		int logicalY = (int) currentY/Tile.TILESIZE;

		long now = System.nanoTime();
		long timeDiff = now - lastTime;
		lastTime = now;
		timer += timeDiff; // increase time
		if(timer >= threshold) {
			decisionTime = true;
			timer = 0;
		}

		updateMatrix(logicalX, logicalY, (int) playerX/Tile.TILESIZE, (int) playerY/Tile.TILESIZE);
		if(maxNeighbor(logicalX, logicalY) > 1)
			agent.setStrategy(new BreadCrumbsStrategy(matrix));
	}

	private int maxNeighbor(int currentX, int currentY) {
		int max = 0;
		max = Math.max(max, matrix[(currentY-1 + heigth) % heigth][currentX]); 					   // up
		max = Math.max(max, matrix[currentY]			 	      [(currentX+1) % width]); 		   // right
		max = Math.max(max, matrix[(currentY+1) % heigth]		  [currentX]); 					   // down
		max = Math.max(max, matrix[currentY]		     		  [(currentX-1 + width) % width]); // left
		return max;
	}

	@Override
	public int getYDir(float currentX, float currentY) {
		if(decisionTime && currentY == lastY) { // is stuck?
			lastYDir = computeYDir((int) currentX/Tile.TILESIZE, (int) currentY/Tile.TILESIZE);
		}
		return lastYDir;
	}

	@Override
	public int getXDir(float currentX, float currentY) {
		if(decisionTime && currentX == lastX) { // is stuck?
			lastXDir = computeXDir((int) currentX/Tile.TILESIZE, (int) currentY/Tile.TILESIZE);
		}
		return lastXDir;
	}

	int computeYDir(int logicalX, int logicalY) {
		int up = (logicalY - 1 >= 0) ? matrix[logicalY-1][logicalX] : 0;
		int down = (logicalY + 1 < heigth) ? matrix[logicalY+1][logicalX] : 0; 
		if(up == 0 && down > 0)      // down
			return 1;
		else if(up > 0 && down == 0) // up
			return -1;
		else {						 // both
			double rand = Math.random() * 10;
			return (rand < 5) ? -1 : 1;
		}
	}

	int computeXDir(int logicalX, int logicalY) {
		int left = (logicalX - 1 >= 0) ? matrix[logicalY][logicalX-1] : 0;
		int right = (logicalX + 1 < width) ? matrix[logicalY][logicalX+1] : 0; 
		if(left == 0 && right > 0) 		// right
			return 1;
		else if(left > 0 && right == 0) // left
			return -1;
		else {						    // both
			double rand = Math.random() * 10;
			return (rand < 5) ? -1 : 1;
		}
	}

}
