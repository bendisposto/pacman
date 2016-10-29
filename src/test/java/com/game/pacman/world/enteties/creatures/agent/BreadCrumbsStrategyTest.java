package com.game.pacman.world.enteties.creatures.agent;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BreadCrumbsStrategyTest {

	int[][] tiles = {
		{1,1,1,1},
		{1,1,0,1}, // start in (1,1)
		{1,1,1,1},
		{0,1,0,1},
		{1,1,0,1} // goal in (3,4)
	};
	BreadCrumbsStrategy strategy;

	@Before
	public void setUp() throws Exception {
		strategy = new BreadCrumbsStrategy(tiles);
	}

	@After
	public void tearDown() throws Exception {
		strategy = null;
	}

	@Test
	public final void testGetXDir() {
		strategy.findPath(0,0,3,4); // from 3,4
		strategy.findPath(0,0,3,3); // to 3,3
		strategy.findPath(0,0,3,2); // to 2,2
		assertThat(strategy.getXDir(2, 2), is(1)); // should move right at this position
		assertThat(strategy.getXDir(3, 1), is(0)); // should move to down, not right or left
	}

	@Test
	public final void testGetYDir() {
		strategy.findPath(0,0,3,4); // from 3,4
		strategy.findPath(0,0,3,3); // to 3,3
		assertThat(strategy.getYDir(3, 2), is(1)); // should move down at this position

		strategy.findPath(0,0,2,2); // to 2,2
		assertThat(strategy.getYDir(1, 2), is(0)); // should move to right, not up or down

		strategy.findPath(0,0,1,2); // to 1,2
		strategy.findPath(0,0,0,2); // to 0,2
		assertThat(strategy.getYDir(1, 3), is(-1)); // should move to right, not up or down
	}

}
