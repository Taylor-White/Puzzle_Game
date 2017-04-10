import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;

/* Unused */
public class KeyAction extends AbstractAction {
	
	private String cmd;

    public KeyAction(String cmd) {
        this.cmd = cmd;
        System.out.println("Set key: " + cmd);
    }
	@Override
	public void actionPerformed(ActionEvent e) {
		if (cmd.equalsIgnoreCase("RightArrow")) {
            System.out.println("The RightArrow was pressed!");
        }
		System.out.println("The RightArrow was pressed!22");
	}

}
