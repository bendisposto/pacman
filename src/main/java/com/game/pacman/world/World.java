package com.game.pacman.world;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.game.pacman.world.enteties.EntityManager;
import com.game.pacman.world.enteties.creatures.Monster;
import com.game.pacman.world.enteties.creatures.Player;
import com.game.pacman.world.observer.Observable;
import com.game.pacman.world.tiles.BlockTile;
import com.game.pacman.world.tiles.EmptyTile;
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
	private int width, height; // width and height of level in tiles
	public static Tile[] TILE_TYPES = new Tile[100]; // 100 different types
	private static Tile EMPTY = new EmptyTile(0);
	private static Tile BLOCK = new BlockTile(1);
	private static Tile POINT = new EmptyTile(2); // TODO: make point

	public World(String path) {
		addTile(EMPTY);
		addTile(BLOCK);
		addTile(POINT);
		loadWorld(path);

		entityMngr = new EntityManager(); 
		// Player
		Player player = new Player(startX, startY, this);
		entityMngr.setPlayer(player);
		// hard coded test
		Monster monster = new Monster(16, 18, this, player);
		entityMngr.addCreature(monster);
	}
	
	public void tick() {
		entityMngr.tick();
		if(entityMngr.getPlayer().getHealth() == 0) {
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
			Tile tile = TILE_TYPES[tiles[x][y]];
			if(tile == null)
				return EMPTY;
			return tile;
		}
		else { // if player is outside of scope
			return EMPTY;
		}
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
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int type = parseInt(tokens[(x + (y * width)) + 4]);
				tiles[x][y] = type;
				// TODO:
//				if(type == 3)
//					createMonster at  x, y
//					entityManager.addCreature(monster)
//					erase 3 replace with 0
//				if(type == 1)
//					createPoint at x y
//					entityManager.addEntity(point)
//					erase 2 replace with 0
			}
		}
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
