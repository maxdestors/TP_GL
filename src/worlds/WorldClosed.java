package worlds;

import static java.lang.Math.PI;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import creatures.AbstractCreature;
import creatures.IEnvironment;

public class WorldClosed implements IWorld {
	
	IEnvironment env;
	
	public WorldClosed(IEnvironment env) {
		this.env = env;
	}

	public Point2D applyBounds(AbstractCreature creature) {
		Point2D p = creature.getPosition();
		double x = p.getX();
		double y = p.getY();
		double direction = creature.getDirection();
		Dimension s = env.getSize();
		
		if (x > s.getWidth() / 2) {
			x = s.getWidth() - x;
			direction = setDirectionBounceX(direction);
		} else if (x < -s.getWidth() / 2) {
			x = - s.getWidth() - x;
			direction = setDirectionBounceX(direction);
		}

		if (y > s.getHeight() / 2) {
			y = s.getHeight() - y;
			direction = setDirectionBounceY(direction);
		} else if (y < -s.getHeight() / 2) {
			y = - s.getHeight() - y;
			direction = setDirectionBounceY(direction);
		}
		
		creature.setDirection(direction);
		return new Point2D.Double(x,y);
	}
	
	private double setDirectionBounceX(double direction) {
		if (direction >= PI)
			return 3*PI - direction;
		else
			return PI - direction;
	}

	private double setDirectionBounceY(double direction) {
		return PI * 2 - direction;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return  getClass().getName();
	}
	
}
