package creatures.movement;

import static commons.Utils.filter;
import static java.lang.Math.abs;

import java.awt.geom.Point2D;

import commons.Utils.Predicate;
import creatures.AbstractCreature;
import creatures.ICreature;
import creatures.IEnvironment;

public class MovementFlock implements IMovement {
	
	IEnvironment env;
	
	public MovementFlock(IEnvironment env) {
		this.env = env;
	}

	static class CreaturesAroundCreature implements Predicate<ICreature> {
		private final AbstractCreature observer;

		public CreaturesAroundCreature(AbstractCreature observer) {
			this.observer = observer;
		}

		public boolean apply(ICreature input) {
			if (input == observer) {
				return false;
			}
			double dirAngle = input.directionFormAPoint(observer.getPosition(),
					observer.getDirection());

			return abs(dirAngle) < (observer.getFieldOfView() / 2)
					&& observer.distanceFromAPoint(input.getPosition()) <= observer
							.getLengthOfView();

		}
	}
	
	/** Minimal distance between this creature and the ones around. */
	private final static double MIN_DIST = 10d;

	/** Minimal speed in pixels per loop. */
	private final static double MIN_SPEED = 3d;

	public Point2D move(AbstractCreature creature) {
		// speed - will be used to compute the average speed of the nearby
		// creatures including this instance
		double avgSpeed = creature.getSpeed();
		// direction - will be used to compute the average direction of the
		// nearby creatures including this instance
		double avgDir = creature.getDirection();
		// distance - used to find the closest nearby creature
		double minDist = Double.MAX_VALUE;

		// iterate over all nearby creatures
		Iterable<ICreature> creatures = filter(env.getCreatures(), new CreaturesAroundCreature(creature));
		int count = 0;
		for (ICreature c : creatures) {
			avgSpeed += c.getSpeed();
			avgDir += c.getDirection();
			minDist = Math.min(minDist, c.distanceFromAPoint(creature.getPosition()));
			count++;
		}

		// average
		avgSpeed = avgSpeed / (count + 1);
		// min speed check
		if (avgSpeed < MIN_SPEED) {
			avgSpeed = MIN_SPEED;
		}
		// average
		avgDir = avgDir / (count + 1);

		// apply - change this creature state
		creature.setDirection(avgDir);
		creature.setSpeed(avgSpeed);

		// if we are not too close move closer
		if (minDist > MIN_DIST) {
			// we move always the maximum
			double incX = creature.getSpeed() * Math.cos(avgDir);
			double incY = -creature.getSpeed() * Math.sin(avgDir);

			// we should not moved closer than a dist - MIN_DIST
			return new Point2D.Double(creature.getPosition().getX() + incX, creature.getPosition().getY() + incY);
		}
		return creature.getPosition();
	}

	public String getName() {
		// TODO Auto-generated method stub
		return  getClass().getName();
	}
}
