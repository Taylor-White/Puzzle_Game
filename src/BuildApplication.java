import javax.swing.JFrame;


@SuppressWarnings("serial")
public class BuildApplication extends JFrame {

	
	private GameProcessing gameProcessing;
	private GameView gameView;

	public BuildApplication(String s) {
		super(s);
		setupGameView(s);
		configure();
		setupGameProcessor();
		gameProcessing.run();
	}



	private void setupGameProcessor() {
		
		GameProcessing gp = new GameProcessing(gameView);
		this.gameProcessing = gp;
		return;
	}
	


	public void setupGameView(String s) {
		
		//Build ToolBar
		ToolBar toolbar = new ToolBar();
		this.setJMenuBar(toolbar.build());
		
		//Build Game View
		gameView = new GameView();
		this.add(gameView);
		
	    return;
	}
	
	private void configure(){
		//Configure JFrame
		this.setSize(gameView.getViewSizeX(),gameView.getViewSizeY());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.setResizable(false);
		this.setVisible(true);
		//this.pack();
	}
}