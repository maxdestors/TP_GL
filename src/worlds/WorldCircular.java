package worlds;

import static java.lang.Math.PI;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import creatures.AbstractCreature;
import creatures.IEnvironment;

public class WorldCircular implements IWorld {
	
	IEnvironment env;
	
	public WorldCircular(IEnvironment env) {
		this.env = env;
	}

	public Point2D applyBounds(AbstractCreature creature) {
		Point2D p = creature.getPosition();
		double x = p.getX();
		double y = p.getY();
		double direction = creature.getDirection();
		Dimension s = env.getSize();
		
		if (x > s.getWidth() / 2) {
			x -= s.getWidth();
		} else if (x < -s.getWidth() / 2) {
			x += s.getWidth();
		}

		if (y > s.getHeight() / 2) {
			y = s.getHeight() - y;
			direction = setDirectionBounceY(direction);
		} else if (y < -s.getHeight() / 2) {
			y = - s.getHeight() - y;
			direction = setDirectionBounceY(direction);
		}
		
		creature.setDirection(direction);
		//creature.setPosition(x, y); // on tourne en rond
		return new Point2D.Double(x,y);
	}
	

	private double setDirectionBounceY(double direction) {
		return PI * 2 - direction;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return  getClass().getName();
	}

	
	
}
