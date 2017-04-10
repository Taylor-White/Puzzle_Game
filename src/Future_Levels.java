import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Future_Levels {

	private GameObjectsGrid current_level_unaltered;
	private GameObjectsGrid next_level;
	private final int tiles_in_row;
	private final int tiles_in_col;
	
	//ImageList
	private ImageList imgList;
		
	public Future_Levels(int x_tiles, int y_tiles, ImageList il){
		//Setup Variables
		this.tiles_in_row = x_tiles;
		this.tiles_in_col = y_tiles;
		this.imgList = il;
	}
	public void setLevel(int i){
		
	}
	public GameObjectsGrid getCurrentLevel(){
		return current_level_unaltered;
	}
	public GameObjectsGrid getNextLevel(){
		return next_level;
	}
	//Convert File Data to GameObject 2D Array
	private GridCell[][] parseLevel(String from_file) {
		GameObjectsGrid level_object = null;
		System.out.println("from_file string: \n" + from_file);
		String[] parts = from_file.split("\n");
		GridCell[][] level_array = new GridCell[tiles_in_row][tiles_in_col];
		int p_x = 0;
		int p_y = 0;
		Player p = null;
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
				            	p_x = x;
				            	p_y = y;
				            	name = EnumConsts.Object_Name.Player;
				            	System.out.println("player coordinates: " + x + " " + y);
				            	p = new Player(img_set);
				            	new_cell.add(p);
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
		level_object.setGameObjectGrid(level_array, p_x, p_y, p);
		System.out.println("done parsing level...");
		return level_array;
		
	}
			
	//Gets GameObject[][] from a level number
	public GridCell[][] getLevel(int level){
		String from_file = getStringFromFile(level);
		GridCell[][] level_array = parseLevel(from_file);
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
	
}
