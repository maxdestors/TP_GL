package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import plug.creatures.CreaturePluginFactory;
import plug.creatures.PluginMenuItemBuilder;
import plug.creatures.WorldPluginFactory;
import worlds.IWorld;
import creatures.ICreature;
import creatures.visual.ColorCube;
import creatures.visual.CreatureInspector;
import creatures.visual.CreatureSimulator;
import creatures.visual.CreatureVisualizer;

/**
 * Just a simple test of the simulator.
 * 
 */
@SuppressWarnings("serial")
public class Launcher extends JFrame {

	private final CreaturePluginFactory factory;
	private final WorldPluginFactory worldfactory;
	
	private final CreatureInspector inspector;
	private final CreatureVisualizer visualizer;
	private final CreatureSimulator simulator;
	
	private PluginMenuItemBuilder menuBuilder;
	private PluginMenuItemBuilder menuBuilderWorld;
	private JMenuBar mb = new JMenuBar();	
	private Constructor<? extends ICreature> currentConstructor = null;
	private Constructor<? extends IWorld> currentConstructorWorld = null;//-------------------

	protected IWorld worldStrategy;
	  
	public Launcher() {
		factory = CreaturePluginFactory.getInstance();
		worldfactory = WorldPluginFactory.getInstance();

		setName("Creature Simulator Plugin Version");
		setLayout(new BorderLayout());
		
		JPanel buttons = new JPanel();
		JButton loader = new JButton("Load plugins");
		loader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				factory.load();
				worldfactory.load();
				buildPluginMenus();
			}
		});
		buttons.add(loader);

		JButton reloader = new JButton("Reload plugins");
		reloader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				factory.reload();
				worldfactory.reload();
				buildPluginMenus();
			}
		});
		buttons.add(reloader);

		JButton restart = new JButton("(Re-)start simulation");
		restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentConstructor != null && currentConstructorWorld != null) {//-------------------
					//System.out.print("currentConstructor : "+currentConstructor+"\n");//-------------------
					//System.out.print("currentConstructorWorld : "+currentConstructorWorld+"\n"); //-------------------
					synchronized(simulator) {
						if (simulator.isRunning()) {
							simulator.stop();
						}
					}
					simulator.clearCreatures();
					worldStrategy = worldfactory.createWorld(simulator, currentConstructorWorld); //-------------------
					Collection<? extends ICreature> creatures = factory.createCreatures(simulator, 10, new ColorCube(50), worldStrategy, currentConstructor);
					simulator.addAllCreatures(creatures);
					simulator.start();
				}
			}
		});
		buttons.add(restart);
		
		add(buttons, BorderLayout.SOUTH);
		
		/*-------------------------------*/
		simulator = new CreatureSimulator(new Dimension(640, 480));
		
		//worldStrategy = new WorldClosed(simulator); //-------------------
		/*-------------------------------*/

		inspector = new CreatureInspector();
		inspector.setFocusableWindowState(false);
		visualizer = new CreatureVisualizer(simulator);
		visualizer.setDebug(false);
		visualizer.setPreferredSize(simulator.getSize());
		
		add(visualizer, BorderLayout.CENTER);
	
	    buildPluginMenus();

	    pack();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				exit(evt);
			}
		});
		
	}
	
	private void exit(WindowEvent evt) {
		System.exit(0);
	}

	public void buildPluginMenus() {	
		mb.removeAll();
		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// the name of the plugin is in the ActionCommand
				//System.out.print(((JMenuItem) e.getSource()).getActionCommand()+"\n"); //----------------------------------
				String name = ((JMenuItem) e.getSource()).getActionCommand(); //----------------------------------
				
				if (name.indexOf("Creature") != -1) { //----------------------------------
					currentConstructor = factory.getConstructorMap().get(((JMenuItem) e.getSource()).getActionCommand());
				} else if(name.indexOf("World") != -1) { //----------------------------------
					currentConstructorWorld = worldfactory.getConstructorMap().get(((JMenuItem) e.getSource()).getActionCommand()); //----------------------------------
				} else { //----------------------------------
					System.out.print("Plugin non reconnu pour action performed : "+name+"\n"); //----------------------------------
				} //----------------------------------
			}
		};
		menuBuilder = new PluginMenuItemBuilder(factory.getConstructorMap(),listener);
		menuBuilder.setMenuTitle("Creatures");
		menuBuilder.buildMenu();
		mb.add(menuBuilder.getMenu());
		//System.out.print(" listener :"+ listener +" worldfactory :"+ worldfactory+"\n");
		menuBuilderWorld = new PluginMenuItemBuilder(listener, worldfactory.getConstructorMap());
		menuBuilderWorld.setMenuTitle("Monde/Environement");
		menuBuilderWorld.buildMenu();
		mb.add(menuBuilderWorld.getMenu());
		setJMenuBar(mb);
	}
	
	
	public static void main(String args[]) {
	    Logger.getLogger("plug").setLevel(Level.INFO);
		double myMaxSpeed = 5;
		CreaturePluginFactory.init(myMaxSpeed);
		WorldPluginFactory.init();
		Launcher launcher = new Launcher();
		launcher.setVisible(true);
	}
	
}


