import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Level_Builder implements Runnable {


	private int tiles_in_row;
	private int tiles_in_col;
	private int level_number;
	
	private Level_Details this_level;
	
	private boolean lock = false;
	
	//ImageList
	private ImageList imgList;

		
	public Level_Builder(int tiles_in_row, int tiles_in_col, ImageList il, int level_number){
		//Setup Variables
		this.tiles_in_row = tiles_in_row;
		this.tiles_in_col = tiles_in_col;
		this_level = new Level_Details(null, 0, 0);
		this.imgList = il;
		this.level_number = level_number;

	}
	
	public void setLevelNumber(int i){
		level_number = i;
	}

	public Level_Details getLevelDetails(){
		return this_level;
	}
	
	
	//Convert File Data to GameObject 2D Array
	private GameObjectsGrid parseLevel(String from_file) {
		
		
		setDimensionsOfGrid(from_file);
		System.out.println("Height of file is: " + this.tiles_in_col);
		System.out.println("Length of file is: " + this.tiles_in_row);
		boolean h_error = false;
		boolean w_error = false;

		GameObjectsGrid level_object = new GameObjectsGrid(tiles_in_row, tiles_in_col);
		//System.out.println("from_file string: \n" + from_file);
		String[] parts = from_file.split("\n");
		GridCell[][] level_array = new GridCell[tiles_in_col][tiles_in_row];
		int p_x = 0;
		int p_y = 0;
		Player p = null;
		int cc = 0;
		//System.out.println("tiles_in_row: " + tiles_in_row + " and tiles_in_col: " + tiles_in_col);

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
						char ch;
						try{
							ch = object_char.charAt(0);
						}catch(Exception e){
							ch = ' ';
						}
						switch (ch) {
				            case 'b':  
				            	object_char = object_char.substring(1);
								img_set = imgList.getBlock();
				            	new_cell.add(new Block(img_set, Integer.parseInt(object_char)));
				            break;
				            case 'i':  
				            	object_char = object_char.substring(1);
				            	img_set = imgList.getIndestructible();
				            	new_cell.add(new Indestructable(img_set, Integer.parseInt(object_char)));
		            		break;
				            case 'c':
				            	object_char = object_char.substring(1);
								img_set = imgList.getCoin();
				            	new_cell.add(new Coin(img_set, Integer.parseInt(object_char)));
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
						//System.out.println("x: " + x + " and y: " + y);
						level_array[x][y] = new_cell;
					} catch (IllegalArgumentException e){
						System.out.println("Invalid Format");
					}
				}
			}
		}
		level_object.setGameObjectGrid(level_array);
		this_level.setPlayer(p);
		this_level.setPlayer_x(p_x);
		this_level.setPlayer_y(p_y);
		this_level.setCoinCount(cc);
		this_level.setLevelGrid(level_object);
	
		//System.out.println("Level_array: " + level_array);
		System.out.println("done parsing level " + level_number + "...");
		return level_object;
		
	}
			
	private void setDimensionsOfGrid(String from_file) {
		int len = 0;
		int tmp_len = 0;
		String[] parts = from_file.split("\n");
		this.tiles_in_col = parts.length;
		for(int y=0; y<parts.length; y++){
			String[] cell;
			try{
				cell = parts[y].split(",");
			}catch(Exception e){
				cell = new String[]{"_"};
			}
			tmp_len = parts[y].length() - cell.length;
			if(tmp_len > len)
				len = tmp_len;
			tmp_len= 0;
		}
		this.tiles_in_row = len;
	}

	//Gets GameObject[][] from a level number
	public synchronized void setLevel(int level){
		String from_file = getStringFromFile(level);
		if(from_file != null){
			GameObjectsGrid level_array = parseLevel(from_file);
			//System.out.println("IN LEVEL BUILDER");
			//level_array.printGrid();
			this_level.setLevelGrid(level_array);
		}
		return;
	}
	
	//Get String Array From File
	private String getStringFromFile(int l){
		File inFile;
		try{
			inFile = new File ("./resources/levels/level_" + l + ".txt");
		}catch(Exception e){
			System.out.println(e);
			return null;
		}
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
		      //System.out.println (line);
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
		setLevel(level_number);
		lock = false;
	}

	public boolean getLock() {
		return lock;
	}
	
}
