package creatures.movement;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.awt.geom.Point2D;
import java.lang.Math;

import creatures.AbstractCreature;
import creatures.IEnvironment;

public class MovementBouncing implements IMovement {
	
	public MovementBouncing(IEnvironment env) {
		currCycle = (int)(Math.random() * 100) % NUMBER_OF_CYCLES_PER_CHANGE;
	}
	
	private static final double MIN_SPEED = 3;
	private static final double MAX_SPEED = 10;
	
	private static final int NUMBER_OF_CYCLES_PER_CHANGE = 30;
	
	protected int currCycle;
	
	public void applyNoise(AbstractCreature creature) {
		currCycle++;
		currCycle %= NUMBER_OF_CYCLES_PER_CHANGE;

		// every NUMBER_OF_CYCLES_PER_CHANGE we do the change
		if (currCycle == 0) {
			creature.setSpeed(creature.getSpeed() + ((Math.random() * 2) - 1));

			// maintain the speed within some boundaries
			if (creature.getSpeed() < MIN_SPEED) {
				creature.setSpeed(MIN_SPEED);
			} else if (creature.getSpeed() > MAX_SPEED) {
				creature.setSpeed(MAX_SPEED);
			}

			creature.setDirection(creature.getDirection() + ((Math.random() * Math.PI / 2) - (Math.PI / 4)));
		}
	}

	public String getName() {
		// TODO Auto-generated method stub
		return  getClass().getName();
	}

	public Point2D move(AbstractCreature creature) {
		// TODO Auto-generated method stub
		applyNoise(creature);
		double newX = creature.getPosition().getX() + creature.getSpeed() * cos(creature.getDirection());
		double newY = creature.getPosition().getY() - creature.getSpeed() * sin(creature.getDirection());
		return new Point2D.Double(newX, newY);
	}

}
