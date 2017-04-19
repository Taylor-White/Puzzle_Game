import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Level_Builder implements Runnable {

	private GameObjectsGrid current_level_unaltered;
	private Player cur_player;
	private int cur_player_x;
	private int cur_player_y;
	private int cur_coin_count;
	private List<GameObject> current_level_moving_objects = new ArrayList<GameObject>();
	private GameObjectsGrid next_level;
	private Player next_player;
	private int next_player_x;
	private int next_player_y;
	private int nxt_coin_count;
	private List<GameObject> next_level_moving_objects = new ArrayList<GameObject>();
	private final int tiles_in_row;
	private final int tiles_in_col;
	private int level_number;
	
	private Level_Details this_level;
	//// ADD THIS LATER ~ Will be an object containing the information about a level so the Game processing can request that object and parse it itself.
	
	private boolean lock = false;
	
	//ImageList
	private ImageList imgList;

		
	public Level_Builder(int tiles_in_row, int tiles_in_col, ImageList il){
		//Setup Variables
		this.tiles_in_row = tiles_in_row;
		this.tiles_in_col = tiles_in_col;
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

	//All these get methonds will not be necessary when I add the Level Details object
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
		boolean h_error = false;
		boolean w_error = false;

		GameObjectsGrid level_object = new GameObjectsGrid(tiles_in_row, tiles_in_col);
		System.out.println("from_file string: \n" + from_file);
		String[] parts = from_file.split("\n");
		GridCell[][] level_array = new GridCell[tiles_in_col][tiles_in_row];
		int p_x = 0;
		int p_y = 0;
		Player p = null;
		int cc = 0;
		System.out.println("tiles_in_row: " + tiles_in_row + " and tiles_in_col: " + tiles_in_col);

		for(int y=0; y<tiles_in_row; y++){ // parts.length //tiles_in_row
			String[] cell;
			try{
				cell = parts[y].split(",");
			}catch(Exception e){
				if(!h_error){
					System.out.println("Warning, undefined tiles in level while parsing (height).");
					h_error = true;
				}	
				cell = new String[]{"_"};
			}
			
			for(int x=0; x<tiles_in_col; x++){ // cell.length //tiles_in_col
				String[] depth;
				try{
					depth = cell[x].split(":");
				}catch(Exception e){
					if(!w_error){
						System.out.println("Warning, undefined tiles in level while parsing (width).");
						w_error = true;
					}
					depth = new String[]{"_"};
				}
				GridCell new_cell = new GridCell(x, y);
				for(int c = 0; c<depth.length; c++){
					try{
						//Convert from char to image using Image List?
						BufferedImage img_set = null;
						String object_char = depth[c];
						char ch = object_char.charAt(0);
						switch (ch) {
				            case 'b':  
				            	object_char = object_char.substring(1);
								img_set = imgList.getBlock();
				            	new_cell.add(new Block(img_set, Integer.parseInt(object_char)));
				            break;
				            case 'i':  
				            	img_set = imgList.getIndestructible();
				            	new_cell.add(new Indestructable(img_set));
		            		break;
				            case 'c':
				            	//Change this to match with block
				            	if(object_char.charAt(1) == '1'){
				            		img_set = imgList.getCoin();
					            	new_cell.add(new Coin(img_set));
				            	}else if(object_char.charAt(1) == '2'){
				            		img_set = imgList.getCoinAlternate();
					            	new_cell.add(new Coin(img_set));
				            	}else{
				            		System.out.println("error::::::");
				            	}
				            	cc++; //Increment coin count

		                    break;
				            case 'p':  
				            	img_set = imgList.getPlayer();
				            	p_x = x;
				            	p_y = y;
				            	System.out.println("player coordinates: " + x + " " + y);
				            	p = new Player(img_set);
				            	new_cell.add(p);
				            break; 
				            case 'e':  
				            	img_set = imgList.getExit();
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
						System.out.println("x: " + x + " and y: " + y);
						level_array[x][y] = new_cell;
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
		System.out.println("Level_array: " + level_array);
		System.out.println("done parsing level...");
		return level_object;
		
	}
			
	//Gets GameObject[][] from a level number
	public synchronized GameObjectsGrid getLevel(int level, boolean isCurr){
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
