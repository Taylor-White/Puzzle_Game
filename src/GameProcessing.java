import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.AbstractAction;

public class GameProcessing{
	private GameObjectsGrid current_level;
	
	//Player Details
	private int player_x = 0;
	private int player_y = 0;
	private Player player;
	private boolean input_blocked = false;
	
	//ImageList
	private ImageList imgList;
	
	//Application Objects
	private GameView gameView;
	
	//Default Values
	private final int tiles_in_row;
	private final int tiles_in_col;

	private KeyActionManager keyActionManager;
    
	public GameProcessing(int x_tiles, int y_tiles, GameView gv){
		//Setup Variables
		this.tiles_in_row = x_tiles;
		this.tiles_in_col = y_tiles;
		
		//Set Game View
		this.gameView = gv;
		
		this.keyActionManager = gv.getActionManager();
		System.out.println("GameProcessing view of keyactionmanager: " + keyActionManager.getList().toString());
		
		//Setup Graphics
		this.imgList = new ImageList();
		this.imgList.initialize();
		System.out.println(imgList.toString());
		
		//Initialize current level to '1'
		this.current_level = new GameObjectsGrid();
		this.current_level.setGameObjectGrid(getLevel(1));
		

       
	}
	public void run(){
		
		//System.out.println("current level: " + current_level.toString());
		System.out.println("gameView: " + gameView.toString());
		gameView.drawing(current_level);
		//current_level.printGrid();
		EnumConsts.Player_Action next_action;
		boolean playerMoved = false;
		boolean playerFalling = false;
		while(true){
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			input_blocked = player.getLock();
			adjust_positions();
			//Fix for Idle Climbing Animation ~~~
			if(!current_level.isGround(player_x, player_y) && player.is_idle_climb()){
				player.stand();
			}if(current_level.isGround(player_x, player_y) && player.is_standing()){
				player.idle_climb();
			}
			
			current_level.checkPlayerCollisions(player_x,player_y);
			
			if(!input_blocked && shouldFall(player_x, player_y)){
				start_fall(player_x, player_y, EnumConsts.Object_Name.Player);	
			}else if(!input_blocked){
				next_action = keyActionManager.getNext();
				if(next_action != null){
					perform_action(next_action);
					next_action = null;
					playerMoved = true;
				}
			}
			current_level.checkPlayerCollisions(player_x,player_y);
			//checkPlayerDeath();
			current_level.increment_animations();
			gameView.drawing(current_level);
			//End Loop
		}
	}
	

	private void adjust_positions() {
		//for(objects in grid that can move)
		if(player.halfWayThere()){
			EnumConsts.Direction direction = player.getDirection();
			//move_from_fall(player_x, player_y, EnumConsts.Object_Name.Player);
			move_object_in_grid(player, direction);
			//Fix for animation after climbing
		}
		
	}
	private void move_object_in_grid(Player p, EnumConsts.Direction d) {
		//int x = p.getX();
		//int y = p.getY();

		switch (d) {
		case Left:
			current_level.movePlayerTo(player_x, player_y, player_x-1, player_y);
			player_x = player_x - 1;
			break;
		case Right:
			current_level.movePlayerTo(player_x, player_y, player_x+1, player_y);
			player_x = player_x + 1;
			break;
		case Up:
			current_level.movePlayerTo(player_x, player_y, player_x, player_y-1);
			player_y = player_y - 1;
			break;
		case Down:
			current_level.movePlayerTo(player_x, player_y, player_x, player_y+1);
			player_y = player_y + 1;
			break;
		default:
			break;
		}
			
		
	}
	private void checkPlayerDeath() {
		if(!current_level.isTraversable(player_x, player_y)){
			//PlayerDeath!!!
			System.out.println("Player Dead");
		}
		
	}
	private void start_fall(int x, int y, EnumConsts.Object_Name p) {
    	player.falling();
	}
	private void move_from_fall(int x, int y, EnumConsts.Object_Name p) {
    	current_level.movePlayerTo(player_x, player_y, player_x, player_y+1);
    	player_y = player_y+1;
	}
	private boolean shouldFall(int x, int y) {
		//System.out.println("SHOULD FALL?? " + current_level.isGround(player_x, player_y+1));
		return !(current_level.isGround(x, (y+1)) || current_level.isGround(x, (y)));
		
	}
	public void perform_action(EnumConsts.Player_Action action){
		System.out.println("Action: " + action);
		switch (action) {
			case Move_Left:
				Move_Left(action);
				break;
			case Move_Right:
				Move_Right(action);
				break;
			case Move_Up:
				Move_Up(action);
				break;	
			case Move_Down:
				Move_Down(action);
				break;
			case Fire_Left:
				Fire_Left(action);
				break;
			case Fire_Right:
				Fire_Right(action);
				break;
			case Self_Destruct:
				Self_Destruct(action);
				break;
			default:
				System.out.println("Invalid action");
				break;
					
		}
		//current_level.printGrid();
		//paintThisFrame();
	}
	
	private void Self_Destruct(EnumConsts.Player_Action action) {
		System.out.println("Player Explode");
		
	}
	public void paintThisFrame(){
		gameView.drawing(current_level);
	}
	
	
	
	/*
	 * Level Builder
	 */
	
	//Gets GameObject[][] from a level number
		public GridCell[][] getLevel(int level){
			String from_file = getStringFromFile(level);
			GridCell[][] level_array = parseLevel(from_file);
			return level_array;
		}
		
		//Convert File Data to GameObject 2D Array
		private GridCell[][] parseLevel(String from_file) {
			System.out.println("from_file string: \n" + from_file);
			String[] parts = from_file.split("\n");
			GridCell[][] level_array = new GridCell[tiles_in_row][tiles_in_col];
			for(int y=0; y<parts.length; y++){
				String[] cell = parts[y].split(",");
				//System.out.println("row length: " + cell.length);
				for(int x=0; x<cell.length; x++){
					String[] depth = cell[x].split(":");
					//System.out.println("cell[" + x + "].split: " + cell[x]);
					GridCell new_cell = new GridCell(x, y);
					for(int c = 0; c<depth.length; c++){
						//System.out.println("depth: " + depth.length);
						try{
							//Convert from char to image using Image List?
							BufferedImage img_set = null;
							boolean isGround = true;
							boolean isBreakable = false;
							EnumConsts.Object_Name name = null;
							String object_char = depth[c];
							char ch = object_char.charAt(0);
							System.out.println("ch: " + (int)ch);
							
							///////////////////////////
							switch (ch) {
					            case 'b':  
					            	object_char = object_char.substring(1);
									img_set = imgList.getBlock();
					            	isGround = false;
					            	name = EnumConsts.Object_Name.Block;
					            	isBreakable = true;
					            	new_cell.add(new Block(img_set, Integer.parseInt(object_char)));
					            break;
					            case 'i':  
					            	img_set = imgList.getIndestructible();
					            	isGround = false;
					            	name = EnumConsts.Object_Name.Indestructable;
					            	System.out.println("Getting I");
					            	new_cell.add(new Indestructable(img_set));
			            		break;
					            case 'c':
					            	if(object_char.charAt(1) == '1'){
					            		img_set = imgList.getCoin();
						            	isGround = true;
						            	name = EnumConsts.Object_Name.Coin;
						            	new_cell.add(new Coin(img_set));
					            	}else if(object_char.charAt(1) == '2'){
					            		img_set = imgList.getCoin();
						            	isGround = true;
						            	name = EnumConsts.Object_Name.Coin;
						            	new_cell.add(new Coin(img_set));
					            	}else{
					            		System.out.println("error::::::");
					            	}

			                    break;
					            case 'p':  
					            	img_set = imgList.getPlayer();
					            	isGround = true;
					            	player_x = x;
					            	player_y = y;
					            	name = EnumConsts.Object_Name.Player;
					            	System.out.println("player coordinates: " + x + " " + y);
					            	player = new Player(img_set);
					            	new_cell.add(player);
					            break; 
					            case 'd':  
					            	img_set = imgList.getExit();
					            	name = EnumConsts.Object_Name.Exit;
					            	new_cell.add(new Exit(img_set));
					            break; 
					            case 'l':  
					            	img_set = imgList.getLadder();
					            	new_cell.add(new Ladder(img_set));
					            break; 
					            default: 
					            	img_set = null;
					            break;
					        }
							//System.out.println("depth[c]: " + depth[c]);
							
							level_array[x][y] = new_cell;
							//System.out.println("x: " + x + "\ny: " + y + "   image: " + level_array[x][y].getAt(0).getDefaultImage());
						} catch (IllegalArgumentException e){
							System.out.println("Invalid Format");
						}
					}
				}
			}
			System.out.println("done parsing level...");
			return level_array;
			
		}

		//Get String Array From File
		private String getStringFromFile(int l){
			File inFile = new File ("./resources/levels/level_" + l + ".txt");

		    Scanner sc;
			try {
				sc = new Scanner (inFile);
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}
			try{
				StringBuilder sb = new StringBuilder();
				String line;
			    while (sc.hasNextLine())
			    {
			      line = sc.nextLine();
			      System.out.println (line);
			      sb.append(line);
			      sb.append("\n");
			    }
			    return sb.toString();
			}
			finally{
				sc.close();
			}
		}
    private void Move_Left(EnumConsts.Player_Action action) {
    	boolean success = false;
    	System.out.println("MoveAction Called");
    	System.out.println("isGround: " + current_level.isGround(player_x-1, player_y));
    	
    	
    	if(current_level.isTraversable(player_x-1, player_y)){
    		player.move(EnumConsts.Direction.Left);
    		System.out.println("Moving Player To...");
    		//current_level.movePlayerTo(player_x, player_y, (player_x-1), player_y);
    		//player_x = player_x-1;
    		//player.faceLeft();
    	}else{
    		System.out.println("Can't move " + action);
    	}
    }
    
    private void Move_Right(EnumConsts.Player_Action action) {
    	boolean success = false;
    	System.out.println("MoveAction Called");
    	System.out.println("isGround: " + current_level.isGround(player_x+1, player_y));
    	
    	
    	if(current_level.isTraversable(player_x+1, player_y)){
    		player.move(EnumConsts.Direction.Right);

    		System.out.println("Moving Player To...");
    		//current_level.movePlayerTo(player_x, player_y, (player_x+1), player_y);
    		//player_x = player_x+1;
    		//player.faceRight();
    	}else{
    		System.out.println("Can't move " + action);
    	}
    }
    
    private void Move_Up(EnumConsts.Player_Action action) {
    	boolean success = false;
    	System.out.println("MoveAction Called");
    	System.out.println("isGround: " + current_level.isGround(player_x-1, player_y));
    	
    	
    	if(current_level.isGround(player_x, player_y) && current_level.isTraversable(player_x, player_y-1)){
    		player.move(EnumConsts.Direction.Up);
    		System.out.println("Moving Player To...");
    		//current_level.movePlayerTo(player_x, player_y, (player_x), player_y-1);
    		//player_y = player_y-1;
    	}else{
    		System.out.println("Can't move " + action);
    	}
    }
    
    private void Move_Down(EnumConsts.Player_Action action) {
    	boolean success = false;
    	System.out.println("MoveAction Called");
    	System.out.println("isGround: " + current_level.isGround(player_x-1, player_y));
    	
    	if(current_level.isTraversable(player_x, player_y+1)){
    		player.move(EnumConsts.Direction.Down);

    		//System.out.println("Moving Player To...");
    		//current_level.movePlayerTo(player_x, player_y, (player_x), player_y+1);
    		//player_y = player_y+1;
    	}else{
    		System.out.println("Can't move " + action);
    	}
    }
    
    private void Fire_Left(EnumConsts.Player_Action action){
    	boolean success = false;
    	System.out.println("FireAction Called");
    	System.out.println("isBreakable: " + current_level.isBreakable(player_x-1, player_y+1));
    	
    	if(current_level.isBreakable(player_x-1, player_y+1) && current_level.isTraversable(player_x-1, player_y)){
    		current_level.destroy(player_x-1, player_y+1, EnumConsts.Object_Name.Block);
    		player.fireStanding(EnumConsts.Direction.Left);
    	}else{
    		player.fireStanding(EnumConsts.Direction.Left);
    		System.out.println("Can't fire " + action);
    	}
    	
    }
    
    private void Fire_Right(EnumConsts.Player_Action action){
    	boolean success = false;
    	System.out.println("FireAction Called");
    	System.out.println("isBreakable: " + current_level.isBreakable(player_x+1, player_y+1));
    	
    	if(current_level.isBreakable(player_x+1, player_y+1) && current_level.isTraversable(player_x+1, player_y)){
    		current_level.destroy(player_x+1, player_y+1, EnumConsts.Object_Name.Block);
    		player.fireStanding(EnumConsts.Direction.Right);
    	}else{
    		player.fireStanding(EnumConsts.Direction.Right);
    		System.out.println("Can't fire " + action);
    	}
    	
    }
    
    private void Fall(){
    	boolean success = false;
    	System.out.println("FireAction Called\n\n\n\n\n\n\n----------");
    	System.out.println("isGround: " + current_level.isGround(player_x, player_y+1));
    	
    	
    	if(!current_level.isGround(player_x, player_y+1) && !current_level.isGround(player_x, player_y)){
    		System.out.println("Moving Player To...");
    		current_level.movePlayerTo(player_x, player_y, player_x, player_y+1);
    		player_y = player_y+1;
    		//player.falling();
    	}
    }
    
    
    
}

	

