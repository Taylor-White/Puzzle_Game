import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;


public class LevelBuilder {
	
	private ImageList imageList;
	private int x_tiles;
	private int y_tiles;
	private GameObject[][] current_level;
	
	public LevelBuilder(int x, int y, ImageList i){
		imageList = i;
		x_tiles = x;
		y_tiles = y;
	}
	
	//Gets GameObject[][] from a level number
	public GameObject[][] getLevel(int level){
		String from_file = getStringFromFile(level);
		GameObject[][] level_array = parseLevel(from_file);
		this.current_level = level_array;
		System.out.println("current level cols: " + this.current_level[0].length);
		return level_array;
	}
	
	//Gets unaltered current level
	public GameObject[][] getCurrentLevel(){
		return current_level;
	}
	
	
	//Convert File Data to GameObject 2D Array
	private GameObject[][] parseLevel(String from_file) {
		String[] parts = from_file.split("\n");
		GameObject[][] level_array = new GameObject[x_tiles][y_tiles];
		for(int i=0; i<parts.length; i++){
			String[] row = parts[i].split(",");
			for(int j=0; j<row[i].length(); j++){
				try{
					//Convert from char to image using Image List?
					
					//level_array[i][j] = new GameObject(0, i, j, imageList);
					//level_array[i][j] = parts[i].charAt(j);
				} catch (IllegalArgumentException e){
					System.out.println("Invalid Format");
				}
			}
		}
		
		
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
