package creatures.movement;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;

import org.junit.Before;
import org.junit.Test;

import worlds.IWorld;
import worlds.WorldClosed;
import creatures.AbstractCreature;
import creatures.StandardCreature;
import creatures.visual.CreatureSimulator;

public class MovementBouncingTest {
	
	CreatureSimulator environment = mock(CreatureSimulator.class);
	final double w = 200;
	final double h = 100;
	AbstractCreature creature;
	IWorld worldStrategy = new WorldClosed(environment);
	IMovement movementStrat = new MovementBouncing(environment);
	
	@Before
	public void setup() {
		when(environment.getSize()).thenReturn(new Dimension((int)w, (int)h));
		creature = new StandardCreature(environment, new Point2D.Double(0, 0), 0, 5, Color.RED, worldStrategy, movementStrat);
	}

	@Test
	public void directionChanged() {
		double direction = creature.getDirection();
		for(int i = 0; i < 30; i++) {
			creature.act();
		}
		assertNotEquals(creature.getDirection(), direction, 0.001);;
	}
	
	@Test
	public void speedChanged() {
		double speed = creature.getSpeed();
		for(int i = 0; i < 30; i++) {
			creature.act();
		}
		assertNotEquals(creature.getSpeed(), speed, 0.001);
	}

}
