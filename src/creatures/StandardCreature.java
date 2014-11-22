package creatures;

import java.awt.Color;
import java.awt.geom.Point2D;

import worlds.IWorld;


/**
 * Standard creature
 */
public class StandardCreature extends AbstractCreature {

	public StandardCreature(IEnvironment environment, Point2D position, double direction, double speed, Color color, IWorld worldStrategy, IMovement movementStrat) {
		super(environment, position, worldStrategy, movementStrat);
		
		this.direction = direction;
		this.speed = speed;
		this.color = color;
	}

	public void act() {
		move();
	}
}
