package creatures.movement;

import static java.lang.Math.toRadians;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import worlds.IWorld;
import worlds.WorldClosed;
import creatures.AbstractCreature;
import creatures.ICreature;
import creatures.StandardCreature;
import creatures.visual.CreatureSimulator;

public class MovementFlockTest {
	
	CreatureSimulator environment = mock(CreatureSimulator.class);
	final double w = 200;
	final double h = 100;
	AbstractCreature creature;
	IWorld worldStrategy = new WorldClosed(environment);
	IMovement movementStrat = new MovementFlock(environment);
	
	@Before
	public void setup() {
		when(environment.getSize()).thenReturn(new Dimension((int)w, (int)h));
		creature = new StandardCreature(environment, new Point2D.Double(0, 0), 0, 5, Color.RED, worldStrategy, movementStrat);
	}
	
	@Test
	public void testEmerginBehavior() throws Exception {
		StandardCreature main = new StandardCreature(environment, new Point2D.Double(0, 0), toRadians(0), 5, Color.RED, worldStrategy, movementStrat);

		ICreature other = mock(ICreature.class);
		when(other.getDirection()).thenReturn(toRadians(270));
		when(other.getSpeed()).thenReturn(10.0);
		when(other.getPosition()).thenReturn(new Point2D.Double(1,0));
		when(other.distanceFromAPoint(eq(main.getPosition()))).thenReturn(1.0);
		when(other.directionFormAPoint(eq(main.getPosition()), eq(main.getDirection()))).thenReturn(0.0);

		ArrayList<ICreature> creaturesAround = new ArrayList<ICreature>();
		creaturesAround.add(other);
		
		when(environment.getCreatures()).thenReturn(creaturesAround);
		
		main.act();
		
		assertEquals(toRadians((270+0)/2), main.getDirection(), .01);
		assertEquals((10.0+5.0)/2, main.getSpeed(), .01);
		
		verify(other).getPosition();
		verify(other).getDirection();
		verify(other).getSpeed();
		verify(other).directionFormAPoint(eq(main.getPosition()),eq(0.0));
		verify(other).distanceFromAPoint(eq(main.getPosition()));
		verifyNoMoreInteractions(other);
	}	

}
