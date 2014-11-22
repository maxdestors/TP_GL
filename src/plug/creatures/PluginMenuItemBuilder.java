package plug.creatures;

import java.lang.reflect.*;
import java.util.Map;
import java.util.logging.Logger;
import java.awt.event.*;

import javax.swing.*;

import worlds.IWorld;
import creatures.ICreature;


public class PluginMenuItemBuilder {

  private JMenu menu;

  private Map<String,Constructor<? extends ICreature>> constructors;
  private Map<String,Constructor<? extends IWorld>> constructorsWorld;

  private ActionListener listener;
  
  private static Logger logger = Logger.getLogger("plug.Menu");

  public PluginMenuItemBuilder(Map<String,Constructor<? extends ICreature>> mc, ActionListener listener) {
    menu = new JMenu();
    this.constructors = mc;
    this.constructorsWorld = null;
    this.listener = listener;
  }

  public PluginMenuItemBuilder(ActionListener listener, Map<String, Constructor<? extends IWorld>> mcw) {
	    menu = new JMenu();
	    this.constructors = null;
	    this.constructorsWorld = mcw;
	    this.listener = listener;
}

public void setMenuTitle(String title) {
    menu.setText(title);
  }

  public void buildMenu() {
    logger.info("Building plugin menu");
    menu.removeAll();
    if(constructors != null) {
      for (String name : constructors.keySet()) {
        JMenuItem mi = new JMenuItem(name);
        // ActionCommand contains the name of the plugin = key in the map
        mi.setActionCommand(name);
        mi.addActionListener(listener);
        menu.add(mi);
      }
    } else if(constructorsWorld != null) {
      for (String name : constructorsWorld.keySet()) {
        JMenuItem mi = new JMenuItem(name);
        // ActionCommand contains the name of the plugin = key in the map
        mi.setActionCommand(name);
        mi.addActionListener(listener);
        menu.add(mi);
      }
    }
  }

  public JMenu getMenu() {
    return menu;
  }

}
