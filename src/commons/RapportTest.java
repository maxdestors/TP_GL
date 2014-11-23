package commons;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import plug.creatures.MovementPluginFactory;
import plug.creatures.WorldPluginFactory;


/**
 * 
 * Classe de g�n�ration de rapport de test
 *
 */

public class RapportTest {
	
	private boolean aTestFail;    // est ce qu'un test a �chou�
	private String infoTestFail;  // r�cup�re les infos relatives au test dans une string
	private final WorldPluginFactory worldfactory;     // les factory concern�es world et movement
	private final MovementPluginFactory movefactory;
	
	
	/**
	 * Constructeur
	 * @param aTestFail
	 * @param infoTestFail
	 * @param worldfactory
	 * @param movefactory
	 */
	public RapportTest(boolean aTestFail, String infoTestFail, WorldPluginFactory wdfac, MovementPluginFactory mvfac) {
		this.aTestFail = false;
		this.infoTestFail = null;
		this.worldfactory = wdfac;
		this.movefactory = mvfac;
	}
	
	/**
	 *  Afficher la fen�tre de rapport de test
	 *  Appel la fonction getInfoTest
	 */
	public void windowTest() {
		JFrame jf = new JFrame();
		jf.setTitle("Rapports test");
		jf.setSize(430, 840);
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
		// label de titre de fen�tre
		JLabel title = new JLabel("<html><h2>Les rapports de test</h2><br></html>");
		panel.add(title);
		// bouton d'info suppl�mentaire
		JButton infoBut;
		// r�cup�ration des info
		infoFromTest(worldfactory.getMapTest(), panel);
		infoFromTest(movefactory.getMapTest(), panel);
		// bouton d'information
		infoBut = new JButton("More info on failure");
		infoBut.addActionListener((new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!aTestFail) {
					JOptionPane.showMessageDialog(null, "Pas d'�checs, les plugins sont correctement charg�s.", "Failure", JOptionPane.INFORMATION_MESSAGE);
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
		// it�ration sur notre map pour l'affichage des informations
		while(iter.hasNext()) 
		{
			Map.Entry entree = (Map.Entry)iter.next();
			Result t = (Result) entree.getValue();		// r�cup�ration des results
			String s = (String) entree.getKey();		// r�cup�ration du nom de classe
		
			List<Failure> failures = t.getFailures();
			int nbFailures = failures.size();
			int nbTest = t.getRunCount();

			if(nbFailures > 0) 
			{
				panel.add(new JLabel("<html><font color='red'><u>Rapport de test de la classe</u> : <b>" + s + "</b><br><br>Nombre test(s) lanc�s: " + nbTest + "<br>Nombre d'�chec(s): " + nbFailures + "<br>--> Le plugin n'a pas �t� charg�.<br><br><br></font></html>"));
				aTestFail = true;
				infoTestFail += t.getFailures();
			} 
			else {
				panel.add(new JLabel("<html><u>Rapport de test de la classe</u> : <b><font color='green'>" + s + "</font></b><br><br>Nombre test(s) lanc�s: " + nbTest + "<br>Nombre d'�chec(s): " + nbFailures + "<br>--> Le plugin a bien �t� charg�.<br><br><br></html>"));
			}
		}
	}
	

}
