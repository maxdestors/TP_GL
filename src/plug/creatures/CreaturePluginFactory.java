package plug.creatures;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.logging.Logger;

import worlds.IWorld;
import creatures.IColorStrategy;
import creatures.ICreature;
import creatures.IEnvironment;
import creatures.IMovement;
import creatures.StandardCreature;

public class CreaturePluginFactory {
	
	/**
	 * singleton for the abstract factory
	 */
	protected static CreaturePluginFactory _singleton;
	
	private double maxSpeed;
	
	/**
	   * logger facilities to trace plugin loading...
	   */
	private static Logger logger = Logger.getLogger("plug.CreaturePluginFactory");
	
	
    public static void init(double inMaxSpeed) {
        if (_singleton != null) {
            throw new RuntimeException("CreatureFactory already created by " + _singleton.getClass().getName());
        } else {
             _singleton = new CreaturePluginFactory(inMaxSpeed);
        }
     }

    public static CreaturePluginFactory getInstance() {
    	return _singleton;
    }

    private CreaturePluginFactory(double inMaxSpeed) {
		maxSpeed = inMaxSpeed;
    }
	
	private final Random rand = new Random();

	public Collection<ICreature> createCreatures(IEnvironment env, int count, IColorStrategy colorStrategy, IWorld worldStrategy, IMovement moveStrategy) {
		Collection<ICreature> creatures = new ArrayList<ICreature>();		
		Dimension s = env.getSize();		
		for (int i=0; i<count; i++) {	
			// X coordinate
			double x = (rand.nextDouble() * s.getWidth()) - s.getWidth() / 2;
			// Y coordinate
			double y = (rand.nextDouble() * s.getHeight()) - s.getHeight() / 2;
			// direction
			double direction = (rand.nextDouble() * 2 * Math.PI);
			// speed why int ????
			int speed = (int) (rand.nextDouble() * maxSpeed);
			StandardCreature creature = new StandardCreature(env, new Point2D.Double(x,y), speed, direction, colorStrategy.getColor(), worldStrategy, moveStrategy);
			creatures.add(creature);
		}		
		return creatures;
	}

}
