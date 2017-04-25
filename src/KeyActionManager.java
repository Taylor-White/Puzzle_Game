import java.awt.event.ActionEvent;
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

	public Object getList() {
		return keylist;
	}
	
    private class Action extends AbstractAction {

        private EnumConsts.Player_Action action;
        
        Action(EnumConsts.Player_Action action) {
            this.action = action;
        }

		@Override
        public void actionPerformed(ActionEvent e) {
			KeyActionManager.this.next_action = action;
        	//System.out.println("Next Action: " + KeyActionManager.this.next_action);
        }
    }


	


}
