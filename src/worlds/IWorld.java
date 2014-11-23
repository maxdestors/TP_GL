package worlds;

import java.awt.geom.Point2D;

import plug.IPlugin;
import creatures.AbstractCreature;

public interface IWorld extends IPlugin {
	
	public Point2D applyBounds(AbstractCreature creature);
	
}
