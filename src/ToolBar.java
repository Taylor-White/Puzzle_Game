import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class ToolBar {

	/*protected JFrame frame;
	
	public ToolBar(JFrame f){
		this.frame = f;
	}*/
	
	public JMenuBar build() {
		ToolBarActions action = new ToolBarActions();
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenu view = new JMenu("View");
		JMenu levels = new JMenu("Levels");
		
		JMenuItem jmiSmall = new JMenuItem("Small (400, 250)");
		JMenuItem jmiMedium = new JMenuItem("Small (600, 400)");
		JMenuItem jmiLarge = new JMenuItem("Small (1200, 800)");
		
		JMenuItem jmiRestart = new JMenuItem("Restart Level");
		JMenuItem jmiExit = new JMenuItem("Exit");

		JMenuItem jmihighScores = new JMenuItem("High Scores");
		JMenuItem jmiLevel1 = new JMenuItem("Level 1");
		JMenuItem jmiLevel2 = new JMenuItem("Level 2");
		JMenuItem jmiLevel3 = new JMenuItem("Level 3");
		JMenuItem jmiLevel4 = new JMenuItem("Level 4");
		
		jmiRestart.addActionListener(new ToolBarActions());
		jmiExit.addActionListener(new ToolBarActions());
		jmihighScores.addActionListener(new ToolBarActions());
		//jmiRestart.addActionListener(action.restart());
		//jmiRestart.addActionListener(action.restart());
		
		
		file.add(jmiRestart);
		file.add(jmiExit);
		
		view.add(jmiSmall);
		view.add(jmiMedium);
		view.add(jmiLarge);
		
		levels.add(jmihighScores);
		levels.addSeparator();
		levels.add(jmiLevel1);
		levels.add(jmiLevel2);
		levels.add(jmiLevel3);
		levels.add(jmiLevel4);
		
		menuBar.add(file);
		menuBar.add(view);
		menuBar.add(levels);
		return menuBar;
	}
	
}
