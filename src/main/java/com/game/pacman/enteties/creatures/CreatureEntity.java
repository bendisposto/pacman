package com.game.pacman.enteties.creatures;

import com.game.pacman.enteties.Entity;
import com.game.pacman.levels.World;
import com.game.pacman.tiles.Tile;

public abstract class CreatureEntity extends Entity {

	private static float BOUNDS_COVER = 0.80f; // proportion of how much bounds cover body

	public static final int DEFAULT_HEALH = 10;
	public static final float DEFAULT_SPEED = 3.0f;
	
	enum Direction { UP, DOWN, LEFT, RIGHT };
	protected Direction dir;

	private float dx, dy;

	protected int health;
	protected float speed;
	//protected World world;

	public CreatureEntity(int x, int y, int width, int height) {
		super(x, y, width, height);
		//world = w;
		health = DEFAULT_HEALH;
		speed = DEFAULT_SPEED;

		// Override default settings of bounds to not cover whole body
		float offset = Tile.TILESIZE * (1-BOUNDS_COVER);
		bounds.x = (int)(offset/2);
		bounds.y = (int)(offset/2);
		bounds.width = (int) (Tile.TILESIZE * BOUNDS_COVER);
		bounds.height = (int) (Tile.TILESIZE * BOUNDS_COVER);
	}
	
	/**
	 * Stops the creature from moving
	 */
	public void stop() {
		dx = 0;
		dy = 0;
	}
	
	/**
	 * Moves creature if user has has sent signal
	 * (Uses template pattern)
	 */
	public void move(World w) {
		moveX(w);
		moveY(w);
	}
	
	/**
	 * Moves creature in x direction if no collision with solid tiles
	 */
	public void moveX(World w) {
		float newX = x + dx + bounds.x;
		if(dx > 0) { // right
			// check upper right and lower right corners
			if(!collidesWithSolid(newX + bounds.width, y + bounds.y, w) &&
			   !collidesWithSolid(newX + bounds.width, y + bounds.y + bounds.height, w)) {
				dir = Direction.RIGHT;
				x += dx;
			}
		}
		else if(dx < 0) { // left
			// check upper left and lower left corners
			if(!collidesWithSolid(newX, y + bounds.y, w) &&
			   !collidesWithSolid(newX, y + bounds.y + bounds.height, w)) {
				dir = Direction.LEFT;
				x += dx;
			}
		}
		else { // not moving in x
			
		}
	}
	
	/**
	 * Moves creature in y direction if no collision with solid tiles
	 */
	public void moveY(World w) {
		float newY = y + dy + bounds.y;
		if(dy > 0) { // down
			// check lower left and lower right corners
			if(!collidesWithSolid(x + bounds.x, newY + bounds.height, w) &&
			   !collidesWithSolid(x + bounds.x + bounds.width, newY + bounds.height, w)) {
				dir = Direction.DOWN;
				y += dy;
			}
			
		}
		else if(dy < 0) { // up
			// check upper left and upper right corners
			if(!collidesWithSolid(x + bounds.x, newY, w) &&
			   !collidesWithSolid(x + bounds.x + bounds.width, newY, w)) {
				dir = Direction.UP;
				y += dy;
			}
		}
		else { // not moving in y
			
		}
	}
	
	// What to do when colliding with enemy
	public abstract void enemyCollision(int x, int y, int dx, int dy);


	private boolean collidesWithSolid(float x, float y, World world) {
		return world.getTile((int)(x/Tile.TILESIZE), (int) (y/Tile.TILESIZE)).isSolid();
	}

	// Getters, Setters

	public float getDx() {
		return dx;
	}

	public void setDx(float dx) {
		this.dx = dx;
	}

	public float getDy() {
		return dy;
	}

	public void setDy(float dy) {
		this.dy = dy;
	}
	
	public Direction getDir() {
		return dir;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

}
