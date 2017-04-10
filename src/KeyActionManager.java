import java.awt.Desktop.Action;
import java.awt.event.ActionEvent;
import java.awt.List;
import java.util.ArrayList;

import javax.swing.AbstractAction;


public class KeyActionManager {
	private ArrayList<Action> keylist = new ArrayList<Action>();
	private EnumConsts.Player_Action next_action;
	
	public KeyActionManager(){
		
	}
	public Action add(EnumConsts.Player_Action a){
		Action action = new Action(a);
		keylist.add(action);
		System.out.println("Adding action: " + action.toString());
		System.out.println("Adding action to list: " + keylist.toString());
		return action;
	}
	public Action getAt(int i){
		return keylist.get(i);
	}
	
	public EnumConsts.Player_Action getNext(){
		EnumConsts.Player_Action return_action = next_action;
		this.next_action = null;
		return return_action;
	}
	//System.out.println("Next Action: " + next_action);
	public Object getList() {
		// TODO Auto-generated method stub
		return keylist;
	}
	
    private class Action extends AbstractAction {

        private EnumConsts.Player_Action action;
        
        Action(EnumConsts.Player_Action action) {
            this.action = action;
        }

		@Override
        public void actionPerformed(ActionEvent e) {
        	switch (action){
        		case Move_Left:
        			//System.out.println("Move Left");
        			KeyActionManager.this.next_action = action;
        			break;
        		case Move_Right:
        			//System.out.println("Move Right");
        			KeyActionManager.this.next_action = action;
        			break;
        		case Move_Up:
        			//System.out.println("Move Left");
        			KeyActionManager.this.next_action = action;
        			break;
        		case Move_Down:
        			//System.out.println("Move Left");
        			KeyActionManager.this.next_action = action;
        			break;	
        		case Fire_Left:
        			//System.out.println("Fire Left");
        			KeyActionManager.this.next_action = action;
        			break;
        		case Fire_Right:
        			//System.out.println("Fire Right");
        			KeyActionManager.this.next_action = action;
        			break;
        		case Self_Destruct:
        			//System.out.println("Fire Right");
        			KeyActionManager.this.next_action = action;
        			break;
        		default:
        			//System.out.println("Invalid Input");
        			break;
        	}
        	System.out.println("Next Action: " + KeyActionManager.this.next_action);
        }
    }


	


}
