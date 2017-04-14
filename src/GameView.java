import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;



@SuppressWarnings("serial")
public class GameView extends JPanel{

	private Graphics2D graphics;
	
	private final int ITEM_WINDOW = 2;
	
	//Inventory Labels
	ArrayList<JLabel> item_label;
	
	
	//Canvas Constants
	private final int tile_size_x = 32;
	private final int tile_size_y = 32;
	private final int tiles_in_row;
	private final int tiles_in_col;
	
	//Game Graphics
	private BufferedImage image_block;
	private BufferedImage bi;

	//Build Game Objects Container
	private GameObjectsGrid gameObjectsGrid;
	
	//Key Action Manager
	private KeyActionManager keyActionManager = new KeyActionManager();    
	
	//Key Bindings
		InputMap inputMap;
	    ActionMap actionMap;
	
	public GameView(int tiles_x, int tiles_y){
			//Set Default Values
			this.tiles_in_row = tiles_x;
			this.tiles_in_col = tiles_y;
		
			//Set item labels
			item_label = new ArrayList<JLabel>();
			
			this.setLayout(new BorderLayout());
			
			setupInventoryWindow("Inventory");

		   //Set up JPanel
		   this.setSize(getViewSizeX(), getViewSizeY());
		   this.setOpaque(true);
		   		   
	       //Load Graphics
	       try {                
	           image_block = ImageIO.read(new File("./resources/sprites/block.png"));
	        } catch (IOException ex) {
	             System.out.println("Exception: " + ex);
	        }
	       
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
	       
	       inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_U,0), "drop_item");
	       
	       //Set KeyAction Managers
	       actionMap = this.getActionMap();
	       actionMap.put("move_left", keyActionManager.add(EnumConsts.Player_Action.Move_Left));  
	       actionMap.put("move_right", keyActionManager.add(EnumConsts.Player_Action.Move_Right));
	       actionMap.put("move_up", keyActionManager.add(EnumConsts.Player_Action.Move_Up));
	       actionMap.put("move_down", keyActionManager.add(EnumConsts.Player_Action.Move_Down));
	       
	       actionMap.put("fire_left", keyActionManager.add(EnumConsts.Player_Action.Fire_Left));  
	       actionMap.put("fire_right", keyActionManager.add(EnumConsts.Player_Action.Fire_Right));
	       
	       actionMap.put("self_destruct", keyActionManager.add(EnumConsts.Player_Action.Self_Destruct));
	
	       actionMap.put("drop_item", keyActionManager.add(EnumConsts.Player_Action.Drop_Item));
	}   
	public void drawing(GameObjectsGrid gog){
		this.gameObjectsGrid = gog;
	      try {
	    	  image_block = ImageIO.read(new File("./resources/sprites/block.png"));
	      } catch(IOException e) {
	         System.out.println("failed");
	      }
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
	            		int[] offset = go.getOffset(tile_size_x, tile_size_y);
	        		//System.out.println("gride_cell.getAt" + i + "): " + go.getDefaultImage().toString());
	            	g.drawImage(go.getCurrentImage(), tile_size_x*grid_cell.getX() + offset[0], tile_size_y*grid_cell.getY() + offset[1], null);
	
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
        return new Dimension(tile_size_y * tiles_in_col, tile_size_x * tiles_in_row);
    }

	public int getViewSizeX() {
		return tiles_in_row * tile_size_x;
	}
	public int getViewSizeY() {
		return tiles_in_col * tile_size_y;
	}
	public KeyActionManager getActionManager() {
		System.out.println("GameView's view of keyactionmanager: " + keyActionManager.getList().toString());

		return this.keyActionManager;
	}
	
	private void setupInventoryWindow(String s) {
		JPanel inventory_panel = new JPanel();
		inventory_panel.setBackground(Color.RED);
		inventory_panel.setPreferredSize(new Dimension(ITEM_WINDOW*32, ITEM_WINDOW*32));
		
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
		
		ImageIcon explosive_h = new ImageIcon("./resources/sprites/inventory/explosive_h.png");
		ImageIcon explosive_v = new ImageIcon("./resources/sprites/inventory/explosive_v.png");
		ImageIcon explosive_o = new ImageIcon("./resources/sprites/inventory/explosive_o.png");
		ImageIcon explosive_c = new ImageIcon("./resources/sprites/inventory/explosive_c.png");
		
		item_1.setIcon(explosive_h);
		item_2.setIcon(explosive_v);
		item_3.setIcon(explosive_o);
		item_4.setIcon(explosive_c);
		
		
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
