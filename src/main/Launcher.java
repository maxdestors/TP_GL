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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import commons.RapportTest;

import plug.creatures.CreatureFactory;
import plug.creatures.MovementPluginFactory;
import plug.creatures.PluginMenuItemBuilderMovement;
import plug.creatures.PluginMenuItemBuilderWorld;
import plug.creatures.WorldPluginFactory;
import worlds.IWorld;
import creatures.IColorStrategy;
import creatures.ICreature;
import creatures.movement.IMovement;
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

	private final CreatureFactory factory;	//TODO
	private final WorldPluginFactory worldfactory;
	private final MovementPluginFactory movefactory;
	
	private final CreatureInspector inspector;
	private final CreatureVisualizer visualizer;
	private final CreatureSimulator simulator;
	
	private PluginMenuItemBuilderWorld menuBuilderWorld;
	private PluginMenuItemBuilderMovement menuBuilderMovement;
	private JMenuBar mb = new JMenuBar();	
	private Constructor<? extends IWorld> currentConstructorWorld = null;
	private Constructor<? extends IMovement> currentConstructorMovement = null;

	protected IWorld worldStrategy;
	protected IMovement moveStrategy;
	
	private boolean aTestFail = false;   // pour différencier l'affichage
	private String infoTestFail = null;  // string complète des tests échoués
	  
	public Launcher() {

		final IColorStrategy colorStrat = new ColorCube(50);

		JOptionPane.showMessageDialog(null, "Choisir un monde et un déplacement svp.", "INFO", JOptionPane.INFORMATION_MESSAGE); 
		setTitle("SIMULATEUR"); 	
		
		factory = CreatureFactory.getInstance();		//TODO
		worldfactory = WorldPluginFactory.getInstance();
		movefactory = MovementPluginFactory.getInstance();

		setName("Creature Simulator Plugin Version");
		setLayout(new BorderLayout());
		
		JPanel buttons = new JPanel();
		JButton loader = new JButton("Load plugins");
		loader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				worldfactory.load();
				movefactory.load();
				buildPluginMenus();
			}
		});
		buttons.add(loader);

		JButton reloader = new JButton("Reload plugins");
		reloader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				worldfactory.reload();
				movefactory.reload();
				buildPluginMenus();
			}
		});
		buttons.add(reloader);

		JButton restart = new JButton("(Re-)start simulation");
		restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentConstructorWorld != null && currentConstructorMovement != null) {
					synchronized(simulator) {
						if (simulator.isRunning()) {
							simulator.stop();
						}
					}
					simulator.clearCreatures();
					worldStrategy = worldfactory.createWorld(simulator, currentConstructorWorld);
					Collection<? extends ICreature> creatures = factory.createCreatures(simulator, 10, colorStrat, worldStrategy, movefactory, currentConstructorMovement);
					simulator.addAllCreatures(creatures);
					simulator.start();
				}
			}
		});
		buttons.add(restart);
		
		JButton addCreatures = new JButton("Add Creature");
		addCreatures.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentConstructorWorld != null && currentConstructorMovement != null) {
					synchronized(simulator) {
						if (simulator.isRunning()) {
							simulator.stop();
						}
					}
					worldStrategy = worldfactory.createWorld(simulator, currentConstructorWorld);
					simulator.updateWorldStrategy(worldStrategy);
					Collection<? extends ICreature> creatures = factory.createCreatures(simulator, 1, colorStrat, worldStrategy, movefactory, currentConstructorMovement);
					simulator.addAllCreatures(creatures);
					simulator.start();
				}
			}
		});
		buttons.add(addCreatures);
		
		add(buttons, BorderLayout.SOUTH);
		

		simulator = new CreatureSimulator(new Dimension(640, 480));
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
				String name = ((JMenuItem) e.getSource()).getActionCommand();
				if(name.indexOf("World") != -1) {
					currentConstructorWorld = worldfactory.getConstructorMap().get(((JMenuItem) e.getSource()).getActionCommand());
				} else if(name.indexOf("Movement") != -1) {
					currentConstructorMovement = movefactory.getConstructorMap().get(((JMenuItem) e.getSource()).getActionCommand());
				} else {
					System.out.print("Plugin non reconnu pour action performed : "+name+"\n");
				}
			}
		};
		menuBuilderWorld = new PluginMenuItemBuilderWorld(worldfactory.getConstructorMap(), listener);
		menuBuilderWorld.setMenuTitle("Monde/Environement");
		menuBuilderWorld.buildMenu();
		mb.add(menuBuilderWorld.getMenu());
		menuBuilderMovement = new PluginMenuItemBuilderMovement(movefactory.getConstructorMap(), listener);
		menuBuilderMovement.setMenuTitle("Deplacement");
		menuBuilderMovement.buildMenu();
		mb.add(menuBuilderMovement.getMenu());
		
		JMenu menuTest = new JMenu("Tests");                 // menu pour les tests
		mb.add(menuTest);                                    // on l'ajoute à la barre des menus mb
		JMenuItem item1 = new JMenuItem("Rapport de test");  // sous menu de menuTest
		menuTest.add(item1);                                 // on l'ajoute
		item1.addActionListener(new ActionListener() {       // on écoute dessus...
			public void actionPerformed(ActionEvent arg0) {
				RapportTest rapport = new RapportTest(aTestFail, infoTestFail, worldfactory, movefactory);		// la fenetre se lance
				rapport.windowTest();
			}
		});
		
		setJMenuBar(mb);
	}
	
	public static void main(String args[]) {
	    Logger.getLogger("plug").setLevel(Level.INFO);
		double myMaxSpeed = 5;
		CreatureFactory.init(myMaxSpeed);
		WorldPluginFactory.init();
		MovementPluginFactory.init();
		Launcher launcher = new Launcher();
		launcher.setVisible(true);
	}
	
}
