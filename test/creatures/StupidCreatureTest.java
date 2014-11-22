package creatures;

import static java.lang.Math.toRadians;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;

import org.junit.Before;
import org.junit.Test;

import worlds.IWorld;
import creatures.visual.CreatureSimulator;

public class StupidCreatureTest {
	CreatureSimulator environment = mock(CreatureSimulator.class);
	final double w = 200;
	final double h = 100;
	IWorld worldStrategy = mock(IWorld.class);
	
	@Before
	public void setup() {
		when(environment.getSize()).thenReturn(new Dimension((int)w, (int)h));
		//when(worldStrategy.applyBounds(null)).thenReturn(new Dimension((int)w, (int)h));
	}

	
	@Test
	public void testLeft() throws Exception {
		StupidCreature creature = new StupidCreature(environment, new Point2D.Double(-w/2+1, 0), toRadians(180), 10, Color.RED, worldStrategy);
		creature.act();
		
		assertEquals(toRadians(180), creature.getDirection(), 0.001);
		assertEquals(w/2, creature.getPosition().getX(), 2);
		assertEquals(0, creature.getPosition().getY(), 2);
    }
	
	@Test
	public void testRight() throws Exception {
		StupidCreature creature = new StupidCreature(environment, new Point2D.Double(w/2-1, 0), toRadians(0), 10, Color.RED, worldStrategy);
		creature.act();
		
		assertEquals(toRadians(0), creature.getDirection(), 0.001);
		assertEquals(-w/2, creature.getPosition().getX(), 2);
		assertEquals(0, creature.getPosition().getY(), 2);
    }	
	
	@Test
	public void testUp() throws Exception {
		StupidCreature creature = new StupidCreature(environment, new Point2D.Double(0, h/2-1), toRadians(90), 10, Color.RED, worldStrategy);
		creature.act();
		
		assertEquals(toRadians(90), creature.getDirection(), 0.001);
		assertEquals(0, creature.getPosition().getX(), 2);
		assertEquals(-h/2, creature.getPosition().getY(), 2);
    }	
	
	@Test
	public void testBottom() throws Exception {
		StupidCreature creature = new StupidCreature(environment, new Point2D.Double(0, -h/2+1), toRadians(270), 10, Color.RED, worldStrategy);
		creature.act();
		
		assertEquals(toRadians(270), creature.getDirection(), 0.001);
		assertEquals(0, creature.getPosition().getX(), 2);
		assertEquals(h/2, creature.getPosition().getY(), 2);
    }	
	
}
