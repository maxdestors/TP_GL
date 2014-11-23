package plug.creatures;

import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.junit.runner.Result;

import plug.IPlugin;
import plug.PluginLoader;
import worlds.IWorld;
import creatures.IEnvironment;

public class WorldPluginFactory implements IPluginFactory {
	
	/**
	 * singleton for the abstract factory
	 */
	protected static WorldPluginFactory _singleton;
		
	protected PluginLoader pluginLoader;
	
	private final String pluginDir = "myplugins/repository";// /worlds
	
	protected Map<String,Constructor<? extends IWorld>> constructorMap; 

	/**
	 * logger facilities to trace plugin loading...
	 */
	private static Logger logger = Logger.getLogger("plug.WorldPluginFactory");
	
	
    public static void init() {
        if (_singleton != null) {
            throw new RuntimeException("WorldFactory already created by " 
				  + _singleton.getClass().getName());
        } else {
             _singleton = new WorldPluginFactory();
        }
     }

    public static WorldPluginFactory getInstance() {
    	return _singleton;
    }

    private WorldPluginFactory() {
    	try {
    		pluginLoader = new PluginLoader(pluginDir,IWorld.class);
    	}
    	catch (MalformedURLException ex) {
    	}
		constructorMap = new HashMap<String,Constructor<? extends IWorld>>();
    	load();
    }
	
    public void load() {
    	pluginLoader.loadPlugins();
    	buildConstructorMap();
    }
    
    public void reload() {
    	pluginLoader.reloadPlugins();
    	constructorMap.clear();
    	buildConstructorMap();
    }
    
	@SuppressWarnings("unchecked")
	private void buildConstructorMap() {
		for (Class<? extends IPlugin> p : pluginLoader.getPluginClasses()) {
			Constructor<? extends IWorld> c = null;
			try {				
				c = (Constructor<? extends IWorld>) p.getDeclaredConstructor(IEnvironment.class);
				c.setAccessible(true);
			} catch (SecurityException e) {
				logger.info("Cannot access (security) constructor for plugin" + p.getName());
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				logger.info("No constructor in plugin " + p.getName() + " with the correct signature");
				e.printStackTrace();
			}
			if (c != null)
				constructorMap.put(p.getName(),c);
		}
	}
	
	public Map<String, Constructor<? extends IWorld>> getConstructorMap() {
		return constructorMap;
	}


	// -----------------------------------
	public IWorld createWorld(IEnvironment env, Constructor<? extends IWorld> constructorWorld) {
		IWorld world = null;
		try {
			world = constructorWorld.newInstance(env);
		} catch (Exception e) {
			logger.info("calling constructor " + constructorWorld + " failed with exception " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return world;
	}
	
	/**
	 * Getter HashMap for tests
	 * @return
	 */
	public Map<String, Result> getMapTest(){
		return pluginLoader.getMapTest();
	}

}
