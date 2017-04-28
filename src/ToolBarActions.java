import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * Events for menu actions
 * 
 */

public class ToolBarActions extends ToolBar implements ActionListener{
	private int level_to_skip_to = 0;
	private EnumConsts.Menu_Action next = EnumConsts.Menu_Action.None;
	
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		System.out.println("Selected: " + action);
		switch (action) {
		case "Restart Level":
            this.next = EnumConsts.Menu_Action.Restart;
            break;
		case "Level 0":
			level_to_skip_to = 0;
			this.next = EnumConsts.Menu_Action.Skip_To_Level;
            break;
		case "Level 1":
			level_to_skip_to = 1;
			this.next = EnumConsts.Menu_Action.Skip_To_Level;
            break;
		case "Level 2":
			level_to_skip_to = 2;
			this.next = EnumConsts.Menu_Action.Skip_To_Level;
            break;
		case "Level 3":
			level_to_skip_to = 3;
			this.next = EnumConsts.Menu_Action.Skip_To_Level;
            break;
		case "Level 4":
			level_to_skip_to = 4;
			this.next = EnumConsts.Menu_Action.Skip_To_Level;
            break;
		case "Level 5":
			level_to_skip_to = 5;
			this.next = EnumConsts.Menu_Action.Skip_To_Level;
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
		// NEED TO BUILD
		// ** file io **
	}

	public ActionListener exit() {
		//BuildApplication.shutDown();
		System.exit(0);
		return null;
	}

	public int getLevel() {
		return level_to_skip_to;
	}

	public EnumConsts.Menu_Action getNextAction() {
		EnumConsts.Menu_Action tmp = next;
		next = EnumConsts.Menu_Action.None;
		return tmp;
	}



}
