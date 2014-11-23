package creatures;

import java.awt.geom.Point2D;

import plug.IPlugin;
import simulator.IActionable;
import visual.IDrawable;
import worlds.IWorld;

public interface ICreature extends IDrawable, IActionable, IPlugin {

	public IEnvironment getEnvironment();

	public double getSpeed();

	public double getDirection();

	public Point2D getPosition();

	public abstract double distanceFromAPoint(Point2D p);

	public abstract double directionFormAPoint(Point2D p, double axis);
	
	public void setWorldStrategy(IWorld newWorldStrategy);


}