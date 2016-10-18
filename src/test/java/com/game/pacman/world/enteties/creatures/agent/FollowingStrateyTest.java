package com.game.pacman.world.enteties.creatures.agent;

import static org.junit.Assert.*;

import java.util.Stack;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import org.mockito.Mockito;

import com.game.pacman.world.enteties.creatures.agent.FollowingStrategy;
import com.game.pacman.world.enteties.creatures.agent.pathfinder.Astar;

public class FollowingStrateyTest {
	
	int[][] tiles = {
		{0,0,0,0},
		{0,0,1,0}, // start in (1,1)
		{0,0,0,0},
		{1,0,1,0},
		{0,0,1,0} // goal in (3,4)
	};
	FollowingStrategy strategy;

	@Before
	public void setUp() throws Exception {
		Astar mockAstar = Mockito.mock(Astar.class);
		Stack<Integer> path = new Stack<>();
		path.add(6);
		path.add(10);
		path.add(11);
		path.add(12);
		path.add(16);
		path.add(20);
		Mockito.when(mockAstar.calculatePath(1, 1, 3, 4)).thenReturn(path);
		Mockito.when(mockAstar.getWidth()).thenReturn(4);
		strategy = new FollowingStrategy(mockAstar);
	}

	@After
	public void tearDown() throws Exception {
		strategy = null;
	}

	@Test
	public void testFindPath() {
		strategy.findPath(1, 1, 3, 4);
		// no movement since monster is in first cell already (1,1)
		assertThat(strategy.getXDir(1), is(0));
		assertThat(strategy.getYDir(1), is(0));

		strategy.findPath(1, 1, 3, 4);
		// no movement since monster just calculated path and is now getting the first cell of path (where monster is)
		assertThat(strategy.getXDir(1), is(0));
		assertThat(strategy.getYDir(1), is(0));

		strategy.findPath(1, 1, 3, 4);
		// move down since next step is now below monster (1,2)
		assertThat(strategy.getXDir(1), is(0));
//		assertThat(strategy.getYDir(1), is(1));
		int ydir = strategy.getYDir(1);
		assertThat(ydir, is(1));

		strategy.findPath(1, 2, 3, 4);
		// move right (2,2)
		assertThat(strategy.getXDir(1), is(0));
		assertThat(strategy.getYDir(2), is(0));
		
		// remove
		strategy.findPath(1, 2, 3, 4);
		// move right (2,2)
		assertThat(strategy.getXDir(1), is(1));
		assertThat(strategy.getYDir(2), is(0));

		strategy.findPath(2, 2, 3, 4);
		// move right (3,2)
		assertThat(strategy.getXDir(2), is(1));
		assertThat(strategy.getYDir(2), is(0));

		strategy.findPath(3, 2, 3, 4);
		// move down (3,3)
		assertThat(strategy.getXDir(3), is(0));
		assertThat(strategy.getYDir(2), is(1));

		strategy.findPath(3, 3, 3, 4);
		// move down (3,4)
		assertThat(strategy.getXDir(3), is(0));
		assertThat(strategy.getYDir(4), is(1));
	}

	@Test
	public void testGetXDir() {
		assertThat(strategy.getXDir(1), is(-1)); // press left
		assertThat(strategy.getXDir(-1), is(1)); // press right
		assertThat(strategy.getXDir(0), is(0)); // no press
	}
	
	@Test
	public void testGetXCoord() {
		assertThat(strategy.getXCoord(12), is(3));
	}

	@Test
	public void testGetYDir() {
		assertThat(strategy.getYDir(1), is(-1)); // press left
		assertThat(strategy.getYDir(-1), is(1)); // press right
		assertThat(strategy.getYDir(0), is(0)); // no press
	}

	@Test
	public void testGetYCoord() {
		assertThat(strategy.getYCoord(12), is(2));
	}
}
