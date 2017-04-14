import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class Level_Builder implements Runnable {

	private GameObjectsGrid current_level_unaltered;
	private Player cur_player;
	private int cur_player_x;
	private int cur_player_y;
	private int cur_coin_count;
	private ArrayList<GameObject> current_level_moving_objects = new ArrayList<GameObject>();
	private GameObjectsGrid next_level;
	private Player next_player;
	private int next_player_x;
	private int next_player_y;
	private int nxt_coin_count;
	private ArrayList<GameObject> next_level_moving_objects = new ArrayList<GameObject>();
	private final int tiles_in_row;
	private final int tiles_in_col;
	private int level_number;
	
	private Level_Details this_level;
	//// ADD THIS LATER
	
	private boolean lock = false;
	
	//ImageList
	private ImageList imgList;

		
	public Level_Builder(int x_tiles, int y_tiles, ImageList il){
		//Setup Variables
		this.tiles_in_row = x_tiles;
		this.tiles_in_col = y_tiles;
		this.imgList = il;
		this.level_number = 1;
		cur_player = null;
		cur_player_x = 0;
		cur_player_y = 0;
		getLevel(level_number, true);
	}
	
	public void setLevel(int i){
		level_number = i;
	}

	public GameObjectsGrid getCurrentLevel(){
		return current_level_unaltered;
	}
	public int getCurPlayerX(){
		return cur_player_x;
	}
	public int getCurPlayerY(){
		return cur_player_y;
	}
	public Player getCurPlayer(){
		return cur_player;
	}
	public int getCurCoinCount(){
		return cur_coin_count;
	}
	public int getNextPlayerX(){
		return next_player_x;
	}
	public int getNextPlayerY(){
		return next_player_y;
	}
	public Player getNextPlayer(){
		return next_player;
	}
	public int getNextCoinCount(){
		return nxt_coin_count;
	}
	public GameObjectsGrid getNextLevel(){
		return next_level;
	}
	//Convert File Data to GameObject 2D Array
	private GameObjectsGrid parseLevel(String from_file, boolean isCurr) {
		GameObjectsGrid level_object = new GameObjectsGrid();
		System.out.println("from_file string: \n" + from_file);
		String[] parts = from_file.split("\n");
		GridCell[][] level_array = new GridCell[tiles_in_row][tiles_in_col];
		int p_x = 0;
		int p_y = 0;
		Player p = null;
		int cc = 0;
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
						//System.out.println("ch: " + (int)ch);
						
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
				            	cc++; //Increment coin count

		                    break;
				            case 'p':  
				            	img_set = imgList.getPlayer();
				            	isGround = true;
				            	p_x = x;
				            	p_y = y;
				            	name = EnumConsts.Object_Name.Player;
				            	System.out.println("player coordinates: " + x + " " + y);
				            	p = new Player(img_set);
				            	new_cell.add(p);
				            break; 
				            case 'e':  
				            	img_set = imgList.getExit();
				            	name = EnumConsts.Object_Name.Exit;
				            	new_cell.add(new Exit(img_set));
				            break; 
				            case 'l':  
				            	img_set = imgList.getLadder();
				            	new_cell.add(new Ladder(img_set));
				            break; 
				            case 'd':  
				            	object_char = object_char.substring(1);
				            	img_set = imgList.getDynamite();
				            	new_cell.add(new Dynamite(img_set, Integer.parseInt(object_char), false));
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
		level_object.setGameObjectGrid(level_array);
		if(isCurr){
			this.cur_player = p;
			this.cur_player_x = p_x;
			this.cur_player_y = p_y;
			this.cur_coin_count = cc;
			current_level_unaltered = level_object;
		}else{
			this.next_player = p;
			this.next_player_x = p_x;
			this.next_player_y = p_y;
			this.nxt_coin_count = cc;
			next_level = level_object;
		}
		
		System.out.println("done parsing level...");
		return level_object;
		
	}
			
	//Gets GameObject[][] from a level number
	public GameObjectsGrid getLevel(int level, boolean isCurr){
		String from_file = getStringFromFile(level);
		GameObjectsGrid level_array = parseLevel(from_file, isCurr);
		System.out.println("IN LEVEL BUILDER");
		level_array.printGrid();
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

	@Override
	public void run() {
		lock = true;
		current_level_unaltered = getLevel(level_number, true);
		next_level = getLevel(level_number+1, false);
		lock = false;
	}

	public boolean getLock() {
		return lock;
	}
	
}
