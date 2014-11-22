package worlds;

import java.awt.geom.Point2D;

import creatures.AbstractCreature;
import plug.IPlugin;

public interface IWorld extends IPlugin {
	
	public Point2D applyBounds(AbstractCreature creature);
	
}
