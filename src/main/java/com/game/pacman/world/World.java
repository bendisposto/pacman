package com.game.pacman.world;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.game.pacman.world.enteties.EntityManager;
import com.game.pacman.world.enteties.creatures.Player;
import com.game.pacman.world.enteties.creatures.monster.Monster;
import com.game.pacman.world.enteties.creatures.monster.MonsterFactory;
import com.game.pacman.world.observer.Observable;
import com.game.pacman.world.tiles.BlockTile;
import com.game.pacman.world.tiles.EmptyTile;
import com.game.pacman.world.tiles.PointTile;
import com.game.pacman.world.tiles.Tile;

/**
 * Represents the world and the static tiles
 * @author patriknygren
 *
 */
public class World extends Observable {
	
	private int startX, startY; // start position for player

	// Entities
	private EntityManager entityMngr;

	private int[][] tiles; 
	private Points points;

	private int width, height; // width and height of level in tiles

	public static Tile[] TILE_TYPES = new Tile[3]; // 3 different types
	public static Tile EMPTY = new EmptyTile(0);
	public static Tile BLOCK = new BlockTile(1);
	public static Tile POINT = new PointTile(2);

	public World(String path) {
		addTile(EMPTY);
		addTile(BLOCK);
		addTile(POINT);
		loadWorld(path);
	}
	
	public void tick() {
		entityMngr.tick();
		Player player = entityMngr.getPlayer();
		if(player.hasWon() || player.hasLost()) {
			this.notifyObserver();
		}
	}

	/**
	 * Renders the world and entities
	 * @param g
	 */
	public void render(Graphics g) {
		// Render world
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				getTile(x, y).render(g, x*Tile.TILESIZE, y*Tile.TILESIZE);
			}
		}
		// Render entities after world
		entityMngr.render(g);
	}
	
	/**
	 * Adds new type of tile to available tiles in the world
	 * @param t Tile to add
	 */
	public void addTile(Tile t) {
		if(TILE_TYPES[t.getId()] == null)
			TILE_TYPES[t.getId()] = t;
	}
	
	public int[][] getTiles() {
		return tiles;
	}
	
	/**
	 * Takes tile coordinates and return the tile at that position
	 * @param x 
	 * @param y
	 * @return
	 */
	public Tile getTile(int x, int y) {
		if(x >= 0 && x < width && y >= 0 && y < height) {
			Tile tile = TILE_TYPES[tiles[y][x]];
			if(tile == null)
				return EMPTY;
			return tile;
		}
		else { // if player is outside of scope
			return EMPTY;
		}
	}

	public boolean removePoint(int x, int y) {
		if(points.isPoint(x, y)) {
			points.removePoint(x, y); // remove logical point
			tiles[y][x] = EMPTY.getId(); // remove graphical point
			return true;
		}
		return false;
	}

	public int[][] getInvertedTiles() {
		int[][] invertedMatrix = new int[tiles.length][tiles[0].length];
		for (int y = 0; y < tiles.length; y++) {
			for (int x = 0; x < tiles[y].length; x++) {
				invertedMatrix[y][x] = (tiles[y][x] ^ 1); // invert
			}
		}
		return invertedMatrix;
	}

	public float getWidth() {
		return tiles[0].length * Tile.TILESIZE;
	}

	public float getHeigth() {
		return tiles.length * Tile.TILESIZE;
	}
	
	public EntityManager getEntityManager() {
		return this.entityMngr;
	}

	private void loadWorld(String path) {
		String file = readStringFromFile(path);
		String[] tokens = file.split("\\s+");
		width = parseInt(tokens[0]);
		height = parseInt(tokens[1]);
		startX = parseInt(tokens[2]);
		startY = parseInt(tokens[3]);

		tiles = new int[width][height];
		points = new Points(width, height);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int type = parseInt(tokens[(x + (y * width)) + 4]);
				if(type == POINT.getId()) {
					points.addPoint(x, y);
				}
				tiles[y][x] = type;
			}
		}

		entityMngr = new EntityManager(); 

		// Player
		Player player = new Player(startX, startY, this, points.getGoal());
		entityMngr.setPlayer(player);

		// Monsters
		MonsterFactory monsterFactory = new MonsterFactory(this, player);
		Monster redMonster 	  = monsterFactory.getMonster(Monster.Type.RED, 1,1);      // upper left
		Monster yellowMonster = monsterFactory.getMonster(Monster.Type.YELLOW, 21, 1); // upper right
		Monster purpleMonster = monsterFactory.getMonster(Monster.Type.PURPLE, 1, 21); // lower left
		Monster azureMonster  = monsterFactory.getMonster(Monster.Type.AZURE, 21, 21); // lower right
		entityMngr.addCreature(redMonster);
		entityMngr.addCreature(yellowMonster);
		entityMngr.addCreature(purpleMonster);
		entityMngr.addCreature(azureMonster);
	}
	
	private static String readStringFromFile(String path) {
		StringBuilder builder = new StringBuilder();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(path));
			String line;
			while((line = br.readLine()) != null) {
				builder.append(line + "\n");
			}
			br.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}

	private static int parseInt(String number) {
		try {
			return Integer.parseInt(number);
		}
		catch(NumberFormatException e) {
			e.printStackTrace();
			return 0;
		}
	}

}
