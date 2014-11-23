package plug.creatures;

import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.junit.runner.Result;

import plug.IPlugin;
import plug.PluginLoader;
import creatures.IEnvironment;
import creatures.IMovement;

public class MovementPluginFactory implements IPluginFactory {
	
	/**
	 * singleton for the abstract factory
	 */
	protected static MovementPluginFactory _singleton;
		
	protected PluginLoader pluginLoader;
	
	private final String pluginDir = "myplugins/repository"; // /Movements
	
	protected Map<String,Constructor<? extends IMovement>> constructorMap; 

	/**
	 * logger facilities to trace plugin loading...
	 */
	private static Logger logger = Logger.getLogger("plug.MovementPluginFactory");
	
	
    public static void init() {
        if (_singleton != null) {
            throw new RuntimeException("MovementFactory already created by " 
				  + _singleton.getClass().getName());
        } else {
             _singleton = new MovementPluginFactory();
        }
     }

    public static MovementPluginFactory getInstance() {
    	return _singleton;
    }

    private MovementPluginFactory() {
    	try {
    		pluginLoader = new PluginLoader(pluginDir,IMovement.class);
    	}
    	catch (MalformedURLException ex) {
    	}
		constructorMap = new HashMap<String,Constructor<? extends IMovement>>();
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
			Constructor<? extends IMovement> c = null;
			try {				
				c = (Constructor<? extends IMovement>) p.getDeclaredConstructor(IEnvironment.class);
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
	
	public Map<String, Constructor<? extends IMovement>> getConstructorMap() {
		return constructorMap;
	}


	// -----------------------------------
	public IMovement createMovement(IEnvironment env, Constructor<? extends IMovement> constructorMovement) {
		IMovement Movement = null;
		try {
			Movement = constructorMovement.newInstance(env);
		} catch (Exception e) {
			logger.info("calling constructor " + constructorMovement + " failed with exception " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return Movement;
	}
	
	/**
	 * Getter HashMap for tests
	 * @return
	 */
	public Map<String, Result> getMapTest(){
		return pluginLoader.getMapTest();
	}

}
