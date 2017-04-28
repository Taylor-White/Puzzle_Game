import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class ToolBar {
	private ToolBarActions actionManager;
	public JMenuBar build() {
		actionManager = new ToolBarActions();
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
		JMenuItem jmiLevel0 = new JMenuItem("Level 0");
		JMenuItem jmiLevel1 = new JMenuItem("Level 1");
		JMenuItem jmiLevel2 = new JMenuItem("Level 2");
		JMenuItem jmiLevel3 = new JMenuItem("Level 3");
		JMenuItem jmiLevel4 = new JMenuItem("Level 4");
		JMenuItem jmiLevel5 = new JMenuItem("Level 5");
		
		jmiRestart.addActionListener(actionManager);
		jmiExit.addActionListener(actionManager);
		jmihighScores.addActionListener(actionManager);
		jmiLevel0.addActionListener(actionManager);
		jmiLevel1.addActionListener(actionManager);
		jmiLevel2.addActionListener(actionManager);
		jmiLevel3.addActionListener(actionManager);
		jmiLevel4.addActionListener(actionManager);
		jmiLevel5.addActionListener(actionManager);
		//jmiRestart.addActionListener(action.restart());
		//jmiRestart.addActionListener(action.restart());
		
		//Disable any menu items that have not been built yet
		jmihighScores.setEnabled(false);
		jmiSmall.setEnabled(false);
		jmiMedium.setEnabled(false);
		jmiLarge.setEnabled(false);
		
		file.add(jmiRestart);
		file.add(jmiExit);
		
		view.add(jmiSmall);
		view.add(jmiMedium);
		view.add(jmiLarge);
		
		levels.add(jmihighScores);
		levels.addSeparator();
		levels.add(jmiLevel0);
		levels.add(jmiLevel1);
		levels.add(jmiLevel2);
		levels.add(jmiLevel3);
		levels.add(jmiLevel4);
		levels.add(jmiLevel5);
		
		menuBar.add(file);
		menuBar.add(view);
		menuBar.add(levels);
		return menuBar;
	}
	
	public ToolBarActions getToolbarActionManager(){
		return actionManager;
	}
	
}
