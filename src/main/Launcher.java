package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import plug.creatures.CreatureFactory;
import plug.creatures.MovementPluginFactory;
import plug.creatures.PluginMenuItemBuilderMovement;
import plug.creatures.PluginMenuItemBuilderWorld;
import plug.creatures.WorldPluginFactory;
import worlds.IWorld;
import creatures.ICreature;
import creatures.IMovement;
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
	
	private boolean aTestFail = false;   //---------------------------- pour différencier l'affichage
	private String infoTestFail = null;  //---------------------------- string complète des tests échoués
	  
	public Launcher() {
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
					moveStrategy = movefactory.createMovement(simulator, currentConstructorMovement);
					// TODO a refaire
					Collection<? extends ICreature> creatures = factory.createCreatures(simulator, 10, new ColorCube(50), worldStrategy, moveStrategy);
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
					moveStrategy = movefactory.createMovement(simulator, currentConstructorMovement);
					Collection<? extends ICreature> creatures = factory.createCreatures(simulator, 1, new ColorCube(50), worldStrategy, moveStrategy);
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
			@Override
			public void actionPerformed(ActionEvent arg0) {
				windowTest();								 // la fenetre se lance
			}
		});
		
		setJMenuBar(mb);
	}
	
	/**
	 *  Afficher la fenêtre de rapport de test
	 *  Appel la fonction getInfoTest
	 */
	private void windowTest() {
		JFrame jf = new JFrame();
		jf.setTitle("Rapports test");
		jf.setSize(400, 1000);
		jf.setLocationRelativeTo(null);
		jf.setResizable(false);
		jf.setContentPane(getInfoTest());
		jf.setVisible(true);
	}
	
	/**
	 * Fonction qui permet d'obtenir et lister les informations
	 * relatives aux classes de tests
	 * @return JPanel
	 */
	private JPanel getInfoTest() 
	{
		// panel 
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		// label de titre de fenêtre
		JLabel title = new JLabel("<html><h2>Les rapports de test</h2><br></html>");
		panel.add(title);
		// bouton d'info supplémentaire
		JButton infoBut;
		
		infoFromTest(worldfactory.getMapTest(), panel);
		infoFromTest(movefactory.getMapTest(), panel);
				
		// info bouton
		infoBut = new JButton("More info on failure");
		infoBut.addActionListener((new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!aTestFail) {
					JOptionPane.showMessageDialog(null, "Pas d'échecs, les plugins sont correctement chargés.", "Failure", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(null, infoTestFail, "Failure", JOptionPane.INFORMATION_MESSAGE);
				}
			}	
		}));
		
		panel.add(infoBut);
		
		return panel;
	}
	
	/**
	 * Parcours la Map pour afficher les rapports d'erreur
	 * @param map
	 * @param panel
	 */
	private void infoFromTest(Map<String, Result> map, JPanel panel) {
		Set<?> entrees = map.entrySet();
		Iterator<?> iter = entrees.iterator();
		// itération sur notre map pour l'affichage des informations
		while(iter.hasNext()) 
		{
			Map.Entry entree = (Map.Entry)iter.next();
			Result t = (Result) entree.getValue();		// récupération des results
			String s = (String) entree.getKey();		// récupération du nom de classe
		
			List<Failure> failures = t.getFailures();
			int nbFailures = failures.size();
			int nbTest = t.getRunCount();

			if(nbFailures > 0) 
			{
				panel.add(new JLabel("<html><font color='red'><u>Rapport de test de la classe</u> : <b>" + s + "</b><br><br>Nombre test(s) lancés: " + nbTest + "<br>Nombre d'échec(s): " + nbFailures + "<br>--> Le plugin n'a pas été chargé.<br><br><br></font></html>"));
				aTestFail = true;
				infoTestFail += t.getFailures();
			} 
			else {
				panel.add(new JLabel("<html><u>Rapport de test de la classe</u> : <b><font color='green'>" + s + "</font></b><br><br>Nombre test(s) lancés: " + nbTest + "<br>Nombre d'échec(s): " + nbFailures + "<br>--> Le plugin a bien été chargé.<br><br><br></html>"));
			}
		}
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
