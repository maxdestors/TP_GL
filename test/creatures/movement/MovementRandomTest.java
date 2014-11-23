package creatures.movement;

import static java.lang.Math.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import worlds.IWorld;
import worlds.WorldClosed;
import creatures.AbstractCreature;
import creatures.StandardCreature;
import creatures.visual.CreatureSimulator;

public class MovementRandomTest {
	
	CreatureSimulator environment = mock(CreatureSimulator.class);
	final double w = 200;
	final double h = 100;
	AbstractCreature creature;
	IWorld worldStrategy = new WorldClosed(environment);
	IMovement movementStrat = new MovementRandom(environment);
	
	@Before
	public void setup() {
		when(environment.getSize()).thenReturn(new Dimension((int)w, (int)h));
		creature = new StandardCreature(environment, new Point2D.Double(0, 0), 0, 5, Color.RED, worldStrategy, movementStrat);
	}

	@Test
	public void move() {
		creature.act();
		
		Assert.assertEquals(0, creature.getDirection(), 0.001);
		Assert.assertEquals(creature.getSpeed() * cos(creature.getDirection()), creature.getPosition().getX(), 0.01);
		Assert.assertEquals(creature.getSpeed() * sin(creature.getDirection()), creature.getPosition().getY(), 0.01);
	}

}
