import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;



@SuppressWarnings("serial")
public class GameView extends JPanel{

	private Graphics2D graphics;
	
	
	private final int ITEM_WINDOW = 2;
	
	//Inventory Labels
	ArrayList<JLabel> item_label;
	
	JPanel inventory_panel;
	
	//Canvas Constants
	private final int PIXELS_IN_TILE_X = 32;
	private final int PIXELS_IN_TILE_Y = 32;
	
	private final int HEIGHT = PIXELS_IN_TILE_X * 24;
	private final int WIDTH = PIXELS_IN_TILE_Y * 32;

	//Build Game Objects Container
	private GameObjectsGrid gameObjectsGrid;
	
	//Key Action Manager
	private KeyActionManager keyActionManager = new KeyActionManager();    
	
	//Key Bindings
		InputMap inputMap;
	    ActionMap actionMap;
	
	public GameView(){
		
			//Set item labels
			item_label = new ArrayList<JLabel>();
			
			this.setLayout(new BorderLayout());
			
			setupInventoryWindow("Inventory");

		   //Set up JPanel
		   this.setSize(getViewSizeX(), getViewSizeY());
		   this.setOpaque(true);
		   this.setBackground(new Color(220, 220, 220));
	       
	       //Set Key Bindings
	       inputMap = this.getInputMap();
	       actionMap = this.getActionMap();
		       
	       inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W,0), "move_left");
	       inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R,0), "move_right");
	       inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E,0), "move_up");
	       inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D,0), "move_down");
	
	       inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S,0), "fire_left");
	       inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F,0), "fire_right");
	       
	       inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X,0), "self_destruct");
	       
	       inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_U,0), "drop_item_0");
	       inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_I,0), "drop_item_1");
	       inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_O,0), "drop_item_2");
	       inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_P,0), "drop_item_3");
	       
	       //Set KeyAction Managers
	       actionMap = this.getActionMap();
	       actionMap.put("move_left", keyActionManager.add(EnumConsts.Player_Action.Move_Left));  
	       actionMap.put("move_right", keyActionManager.add(EnumConsts.Player_Action.Move_Right));
	       actionMap.put("move_up", keyActionManager.add(EnumConsts.Player_Action.Move_Up));
	       actionMap.put("move_down", keyActionManager.add(EnumConsts.Player_Action.Move_Down));
	       
	       actionMap.put("fire_left", keyActionManager.add(EnumConsts.Player_Action.Fire_Left));  
	       actionMap.put("fire_right", keyActionManager.add(EnumConsts.Player_Action.Fire_Right));
	       
	       actionMap.put("self_destruct", keyActionManager.add(EnumConsts.Player_Action.Self_Destruct));
	
	       actionMap.put("drop_item_0", keyActionManager.add(EnumConsts.Player_Action.Drop_Item_0));
	       actionMap.put("drop_item_1", keyActionManager.add(EnumConsts.Player_Action.Drop_Item_1));
	       actionMap.put("drop_item_2", keyActionManager.add(EnumConsts.Player_Action.Drop_Item_2));
	       actionMap.put("drop_item_3", keyActionManager.add(EnumConsts.Player_Action.Drop_Item_3));
	}   
	public void drawing(GameObjectsGrid gog){
		this.gameObjectsGrid = gog;

	      repaint();
	   }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if(this.gameObjectsGrid == null){
        	return;
        }
        //System.out.println("before paint loop...");
        GridCell grid_cell = gameObjectsGrid.getNext();
        while(grid_cell != null){
        	for(int i=0; i<grid_cell.size(); i++){
        		Graphics2D g2d = (Graphics2D) g.create();
        		GameObject go = grid_cell.getAt(i);
        		if(go != null){
	            	if(go.getDefaultImage() != null){
	            		int[] offset = go.getOffset(PIXELS_IN_TILE_X, PIXELS_IN_TILE_Y);
	        		//System.out.println("gride_cell.getAt" + i + "): " + go.getDefaultImage().toString());
	            	try{
	            		g.drawImage(go.getCurrentImage(), PIXELS_IN_TILE_X*grid_cell.getX() + offset[0], PIXELS_IN_TILE_Y*grid_cell.getY() + offset[1], null);
	            	}catch(Exception e){
	            		System.out.println(e);
	            	}
	            	}else{
	            		//System.out.println("null at: " + go.getX());
	            	}
        		}	
            	g2d.dispose();
            	
        	}
        	grid_cell.reset();
        	grid_cell = gameObjectsGrid.getNext();
        }
        gameObjectsGrid.reset();
    }
    
	@Override
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet()) {
            return super.getPreferredSize();
        }
        return new Dimension(WIDTH, HEIGHT);
    }

	public int getViewSizeX() {
		return WIDTH;
	}
	public int getViewSizeY() {
		return HEIGHT;
	}
	public KeyActionManager getActionManager() {
		System.out.println("GameView's view of keyactionmanager: " + keyActionManager.getList().toString());

		return this.keyActionManager;
	}
	
	private void setupInventoryWindow(String s) {
		TitledBorder title =  BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.darkGray),"Inventory");
		title.setTitle("Inventory");
		title.setTitleJustification(TitledBorder.CENTER);
		//(TitledBorder.RIGHT);
		title.setTitleColor(Color.BLACK);
		inventory_panel = new JPanel();
		inventory_panel.setBackground(new Color(165, 165, 165));
		inventory_panel.setPreferredSize(new Dimension(ITEM_WINDOW*32, ITEM_WINDOW*32));
		inventory_panel.setBorder(title);
		
		JLabel item_1 = new JLabel();
		JLabel item_2 = new JLabel();
		JLabel item_3 = new JLabel();
		JLabel item_4 = new JLabel();
		
		JLabel number_left_1;
		JLabel number_left_2;
		JLabel number_left_3;
		JLabel number_left_4;
		
		number_left_1 = new JLabel("0");
		number_left_2 = new JLabel("0");
		number_left_3 = new JLabel("0");
		number_left_4 = new JLabel("0");
		
		item_label.add(number_left_1);
		item_label.add(number_left_2);
		item_label.add(number_left_3);
		item_label.add(number_left_4);
		
		number_left_1.setFont (number_left_1.getFont ().deriveFont (16.0f));
		number_left_2.setFont (number_left_2.getFont ().deriveFont (16.0f));
		number_left_3.setFont (number_left_3.getFont ().deriveFont (16.0f));
		number_left_4.setFont (number_left_4.getFont ().deriveFont (16.0f));
		
		ImageIcon explosive_h = new ImageIcon("./resources/sprites/inventory/explosive_v.png");
		ImageIcon explosive_v = new ImageIcon("./resources/sprites/inventory/explosive_h.png");
		ImageIcon explosive_c = new ImageIcon("./resources/sprites/inventory/explosive_c.png");
		ImageIcon explosive_o = new ImageIcon("./resources/sprites/inventory/explosive_o.png");
		
		item_1.setIcon(explosive_h);
		item_2.setIcon(explosive_v);
		item_3.setIcon(explosive_c);
		item_4.setIcon(explosive_o);
		
		
		inventory_panel.add(new JLabel(s + ":"));
		inventory_panel.add(item_1);
		inventory_panel.add(number_left_1);
		inventory_panel.add(item_2);
		inventory_panel.add(number_left_2);
		inventory_panel.add(item_3);
		inventory_panel.add(number_left_3);
		inventory_panel.add(item_4);
		inventory_panel.add(number_left_4);
		
		this.add(inventory_panel, BorderLayout.SOUTH);
		
	}
	
	public void setItemNumber(int i, int x){
		JLabel label = item_label.get(i);
		label.setText(String.valueOf(x));
		item_label.set(i, label);
		return;
	}

}
