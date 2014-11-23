package worlds;

import static java.lang.Math.toRadians;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;

import org.junit.Before;
import org.junit.Test;

import creatures.StandardCreature;
import creatures.movement.IMovement;
import creatures.visual.CreatureSimulator;

public class WorldClosedTest {
	CreatureSimulator environment = mock(CreatureSimulator.class);
	final double w = 200;
	final double h = 100;
	IMovement movementStrategy = mock(IMovement.class);
	IWorld worldStrategy = new WorldClosed(environment);

	@Before
	public void setup() {
		when(environment.getSize()).thenReturn(new Dimension((int)w, (int)h));
	}


	@Test
	public void testDirectLeftUp() throws Exception {
		StandardCreature creature = new StandardCreature(environment, new Point2D.Double(0, 0), toRadians(150), 10, Color.RED, worldStrategy, movementStrategy);
		creature.setPosition(new Point2D.Double(-w/2-6, 0));
		
		assertEquals(toRadians(30), creature.getDirection(), 0.01);
		assertEquals(-w/2+6, creature.getPosition().getX(), 2);
		assertEquals(0, creature.getPosition().getY(), 2);
    }	
	
	@Test
	public void testDirectLeftDown() throws Exception {
		StandardCreature creature = new StandardCreature(environment, new Point2D.Double(0, 0), toRadians(210), 10, Color.RED, worldStrategy, movementStrategy);
		creature.setPosition(new Point2D.Double(-w/2-6, 0));
		
		assertEquals(toRadians(330), creature.getDirection(), 0.01);
		assertEquals(-w/2+6, creature.getPosition().getX(), 2);
		assertEquals(0, creature.getPosition().getY(), 2);
    }	
	
	
	@Test 
	public void testDirectRightUp() throws Exception {
		StandardCreature creature = new StandardCreature(environment, new Point2D.Double(0, 0), toRadians(30), 10, Color.RED, worldStrategy, movementStrategy);
		creature.setPosition(new Point2D.Double(w/2+6, 0));
		
		assertEquals(toRadians(150), creature.getDirection(), 0.01);
		assertEquals(w/2-6, creature.getPosition().getX(), 2);
		assertEquals(0, creature.getPosition().getY(), 2);
    }	
	
	@Test 
	public void testDirectRightDown() throws Exception {
		StandardCreature creature = new StandardCreature(environment, new Point2D.Double(0, 0), toRadians(330), 10, Color.RED, worldStrategy, movementStrategy);
		creature.setPosition(new Point2D.Double(w/2+6, 0));
		
		assertEquals(toRadians(210), creature.getDirection(), 0.01);
		assertEquals(w/2-6, creature.getPosition().getX(), 2);
		assertEquals(0, creature.getPosition().getY(), 2);
    }	
	
	
	@Test
	public void testDirectUpRight() throws Exception {
		StandardCreature creature = new StandardCreature(environment, new Point2D.Double(0, 0), toRadians(30), 10, Color.RED, worldStrategy, movementStrategy);
		creature.setPosition(new Point2D.Double(0, -h/2-6));
		
		assertEquals(toRadians(330), creature.getDirection(), 0.01);
		assertEquals(0, creature.getPosition().getX(), 2);
		assertEquals(-h/2+6, creature.getPosition().getY(), 2);
    }	
	
	@Test
	public void testDirectUpLeft() throws Exception {
		StandardCreature creature = new StandardCreature(environment, new Point2D.Double(0, 0), toRadians(150), 10, Color.RED, worldStrategy, movementStrategy);
		creature.setPosition(new Point2D.Double(0, -h/2-6));
		
		assertEquals(toRadians(210), creature.getDirection(), 0.01);
		assertEquals(0, creature.getPosition().getX(), 2);
		assertEquals(-h/2+6, creature.getPosition().getY(), 2);
    }
	
	@Test
	public void testDirectDownRight() throws Exception {
		StandardCreature creature = new StandardCreature(environment, new Point2D.Double(0, 0), toRadians(330), 10, Color.RED, worldStrategy, movementStrategy);
		creature.setPosition(new Point2D.Double(0, h/2+6));
		
		assertEquals(toRadians(30), creature.getDirection(), 0.01);
		assertEquals(0, creature.getPosition().getX(), 2);
		assertEquals(h/2-6, creature.getPosition().getY(), 2);
    }	
	
	@Test
	public void testDirectDownLeft() throws Exception {
		StandardCreature creature = new StandardCreature(environment, new Point2D.Double(0, 0), toRadians(210), 10, Color.RED, worldStrategy, movementStrategy);
		creature.setPosition(new Point2D.Double(0, h/2+6));
		
		assertEquals(toRadians(150), creature.getDirection(), 0.01);
		assertEquals(0, creature.getPosition().getX(), 2);
		assertEquals(h/2-6, creature.getPosition().getY(), 2);
    }
	
	
	@Test
	public void testUpperRightCorner45() throws Exception {
		StandardCreature creature = new StandardCreature(environment, new Point2D.Double(0, 0), toRadians(45), 1, Color.RED, worldStrategy, movementStrategy);
		creature.setPosition(new Point2D.Double(w/2+3, -h/2-3));
		
		assertEquals(toRadians(225), creature.getDirection(), 0.01);
		assertEquals(w/2-3, creature.getPosition().getX(), 1);
		assertEquals(-h/2+3, creature.getPosition().getY(), 1);
    }	
	
	@Test
	public void testUpperRightCorner30() throws Exception {
		StandardCreature creature = new StandardCreature(environment, new Point2D.Double(0, 0), toRadians(30), 1, Color.RED, worldStrategy, movementStrategy);
		creature.setPosition(new Point2D.Double(w/2+3, -h/2-3));
		
		assertEquals(toRadians(210), creature.getDirection(), 0.01);
		assertEquals(w/2-3, creature.getPosition().getX(), 1);
		assertEquals(-h/2+3, creature.getPosition().getY(), 1);
    }	
	
	@Test
	public void testDirectBottom() throws Exception {
		StandardCreature creature = new StandardCreature(environment, new Point2D.Double(0, 0), toRadians(270), 1, Color.RED, worldStrategy, movementStrategy);
		creature.setPosition(new Point2D.Double(0, h/2+3));

		assertEquals(toRadians(90), creature.getDirection(), 0.01);
		assertEquals(0, creature.getPosition().getX(), 1);
		assertEquals(h/2-3, creature.getPosition().getY(), 1);
		
	}

}
