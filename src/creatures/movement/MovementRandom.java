package creatures.movement;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.awt.geom.Point2D;

import creatures.AbstractCreature;
import creatures.IEnvironment;

public class MovementRandom implements IMovement {
	
	public MovementRandom(IEnvironment env) {}

	public Point2D move(AbstractCreature creature) {
		// TODO Auto-generated method stub
		double newX = creature.getPosition().getX() + creature.getSpeed() * cos(creature.getDirection());
		double newY = creature.getPosition().getY() - creature.getSpeed() * sin(creature.getDirection());
		return new Point2D.Double(newX, newY);
	}

	public String getName() {
		// TODO Auto-generated method stub
		return  getClass().getName();
	}

}
