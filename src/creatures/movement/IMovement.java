package creatures.movement;

import java.awt.geom.Point2D;

import creatures.AbstractCreature;
import plug.IPlugin;

public interface IMovement extends IPlugin {
	
	public Point2D move(AbstractCreature creature);

}
