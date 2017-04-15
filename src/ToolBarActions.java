import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * Events for menu actions
 * 
 */

public class ToolBarActions extends ToolBar implements ActionListener{

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		System.out.println("Selected: " + action);
		switch (action) {
		case "Restart Level":
            this.restart();
            break;
        case "High Scores":
            this.viewHighScores();
            break;
        case "Exit":
            this.exit();
            break;
        default:
            throw new IllegalArgumentException("Invalid Action: " + action);
    }
		
		
		
	}
	
	private void viewHighScores() {
		// ** file io **
	}

	public ActionListener restart() {
		// TODO Auto-generated method stub
		return null;
	}

	public ActionListener exit() {
		//BuildApplication.shutDown();
		System.exit(0);
		return null;
	}



}
