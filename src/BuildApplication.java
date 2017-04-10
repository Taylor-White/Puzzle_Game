import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class BuildApplication extends JFrame {

	private GameProcessing gameProcessing;
	private GameView gameView;
	private final int tiles_in_row = 32;
	private final int tiles_in_col = 24;

	public BuildApplication(String s) {
		super(s);
		setupGameView(s);
		setupGameProcessor();
		gameProcessing.run();
		
		//Make Test Calls Here
		//gameProcessing.setCurrentLevel();
	}

	private void setupGameProcessor() {
		
		GameProcessing gp = new GameProcessing(tiles_in_row, tiles_in_col, gameView);
		this.gameProcessing = gp;
		return;
	}
	


	public void setupGameView(String s) {
		
		//Build ToolBar
		ToolBar toolbar = new ToolBar();
		this.setJMenuBar(toolbar.build());
		
		
		//Build Game View
		gameView = new GameView(tiles_in_row, tiles_in_col);
		this.add(gameView);
		
		
		//Configure JFrame
		this.setSize(gameView.getViewSizeX(),gameView.getViewSizeY());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	    return;
	}
	

	
	public void shutDown(){
		
		return;
	}

	public void run() {
		// TODO Auto-generated method stub
	    return;
	}
}