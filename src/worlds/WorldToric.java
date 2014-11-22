package worlds;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import creatures.AbstractCreature;
import creatures.IEnvironment;

public class WorldToric implements IWorld {
	
	IEnvironment env;
	
	public WorldToric(IEnvironment env) {
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
			y -= s.getHeight();
		} else if (y < -s.getHeight() / 2) {
			y += s.getHeight();
		}
		
		creature.setDirection(direction);
		return new Point2D.Double(x,y);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return  getClass().getName();
	}
}
