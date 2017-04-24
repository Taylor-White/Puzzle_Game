import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;







public class GameObjectsGrid {

	private GridCell[][] gameObjectGrid;
	
	private int tiles_in_row;
	private int tiles_in_col;
	private int current_x = 0;
	private int current_y = 0;

	public Player player;
	

	/*
	 * Constructor
	 */
	public GameObjectsGrid(int tiles_in_row, int tiles_in_col) {
		this.tiles_in_row = tiles_in_row;
		this.tiles_in_col = tiles_in_col;
	}

	/*
	 * Sets the GameObjectGrid
	 */
	public void setGameObjectGrid(GridCell[][] gridCells) {
		this.gameObjectGrid = gridCells;
	}

	/*
	 * Gets the GridCell object at x,y
	 */
	public GridCell getAt(int x, int y){
		return gameObjectGrid[x][y];
	}
	
	/*
	 * Cycles through the grid each time getNext is called.  Creates a GameObject which it returns.
	 * 
	 * Must call reset() after
	 */
	public GridCell getNext(){
		GridCell go;
		if(current_x < tiles_in_col && current_y < tiles_in_col){
			go = gameObjectGrid[current_x][current_y];
		}else{
			go = null;
		}
		current_x++;
		if(current_x == tiles_in_col){
			current_x = 0;
			current_y++;
			
		}
		return go;
	}
	public void reset(){
		this.current_x = 0;
		this.current_y = 0;
	}

	/*
	 * Checks if a cell is ground.  Returns true if any GameObjects have isGround = true
	 */
	public boolean isGround(int x, int y) {
		if(!isValid(x,y)){
			return true;
		}
		if(gameObjectGrid[x][y] == null){return false;}
		List<GameObject> list = gameObjectGrid[x][y].getList();
		
		for(int i=0; i<list.size(); i++){
			if(list.get(i).isGround()){
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Checks if there is an item in the cell.  Returns true if there are any
	 */
	public GameObject isItem(int x, int y) {
		if(gameObjectGrid[x][y] == null){return null;}
		List<GameObject> list = gameObjectGrid[x][y].getList();
		
		for(int i=0; i<list.size(); i++){
			if(list.get(i).isItem()){
				return list.get(i);
			}
		}
		return null;
	}
	
	/*
	 * Checks if a cell is traversable.  True if there are no GameObjects with isTraversable = false in the cell
	 */
	public boolean isTraversable(int x, int y) {
    	if(x < 0 || x > tiles_in_row || y < 0 || y > tiles_in_col){
	        System.out.println("Out of Bounds!");
	        return false;
	    }else if(gameObjectGrid[x][y] == null){
	    	return false;
	    }
    	
		List<GameObject> list = gameObjectGrid[x][y].getList();
		for(int i=0; i<list.size(); i++){
			if(!list.get(i).isTraversable()){
				return false;
			}
		}
		return true;
	}	

	/*
	 * Check if object in a grid cell can be destroyed.  Returns true unless there is an unbreakable object
	 */
	public boolean isBreakable(int x, int y) {
		if(gameObjectGrid[x][y] == null){return false;}
		List<GameObject> list = gameObjectGrid[x][y].getList();

		for(int i=0; i<list.size(); i++){
			//System.out.println(list.get(i).toString());
			if(!list.get(i).isBreakable()){
				return false;
			}
		}
		return true;
	}
	
	/*
	 * Check if a player should die (Player shares a tile with a game object where isDeath=true;
	 */
	public boolean isDeath(int x, int y) {
		if(gameObjectGrid[x][y] == null){return false;}
		List<GameObject> list = gameObjectGrid[x][y].getList();
		
		for(int i=0; i<list.size(); i++){
			if(list.get(i).isDeath()){
				return true;
			}
		}
		return false;
	}
	
	public void moveObjectTo(GameObject obj, int from_x, int from_y, int to_x, int to_y) {
		gameObjectGrid[from_x][from_y].remove(obj);
		gameObjectGrid[to_x][to_y].add(obj);
		//ADD PLAYER_X AND PLAYER_Y CHANGES TO THIS
	}
	


	/*
	 * Prints the grid
	 */
	public void printGrid(){
		System.out.println("Printing Grid...");
		for(int i=0; i<gameObjectGrid.length; i++){
			for(int j=0; j<gameObjectGrid[i].length; j++){
				if(gameObjectGrid[i][j] != null){
					List<GameObject> list = gameObjectGrid[i][j].getList();
					System.out.println("Tile: " + i + " " + j);
					for(int k=0; k<list.size(); k++){
						if(list.get(k) != null){
							System.out.println("Object: " + list.get(k).getName());
						}
					}
				}
			}	
		}
	}


	public void destroy(int x, int y, EnumConsts.Object_Name object) {
		List<GameObject> list = gameObjectGrid[x][y].getList();
		
		for(int i=0; i<list.size(); i++){
			//System.out.println(list.get(i).toString());
			if(list.get(i).getName() == object){
				boolean remove_object = list.get(i).destroy();
				/*if(remove_object)
					list.remove(i);*/
			}
		}
		return;		
	}
	

	
	/*
	 * Call animate() for each GameObject in the grid
	 */
	public void increment_animations() {
		for(int i=0; i<gameObjectGrid.length; i++){
			for(int j=0; j<gameObjectGrid[i].length; j++){
				if(gameObjectGrid[i][j] != null){
					List<GameObject> list = gameObjectGrid[i][j].getList();
					for(int k=0; k<list.size(); k++){
						if(list.get(k) != null){
							list.get(k).animate();
						}
					}
				}
			}	
		}		
		
	}

	/*
	 * Check if a game object should interact with other objects
	 */
	public void checkCollision(GameObject obj_1, int x, int y) {
		if(gameObjectGrid[x][y] == null){return;}
		List<GameObject> list = gameObjectGrid[x][y].getList();
		boolean isBreakable = isBreakable(x,y);
		for(int i=0; i<list.size(); i++){
			GameObject obj_2 = list.get(i);
			//Player interacting with...
			if(obj_1.getName() == EnumConsts.Object_Name.Player){
				//Coin
				if(obj_2.getName() == EnumConsts.Object_Name.Coin){
					boolean isRemove = list.get(i).destroy();
					if(isRemove){
						list.remove(i);
					}
				//Dynamite
				}else if(obj_2.getName() == EnumConsts.Object_Name.Dynamite  && obj_2.isItem()){
					boolean isRemove = list.get(i).destroy();
					if(isRemove){
						list.remove(i);
					}
				}
			//Explosion effect interacting with...
			}else if(obj_1.getName() == EnumConsts.Object_Name.Explosion && isBreakable){
				//Block
				if(obj_2.getName() == EnumConsts.Object_Name.Block){
					boolean isRemove = list.get(i).destroy();
					if(isRemove){
						//list.remove(i);
					}
				}
			}
		}
		return;
	}
	
	/*
	 * Add a game object to the grid
	 */
	public void add(int x, int y, GameObject obj) {
		gameObjectGrid[x][y].add(obj);
	}
	
	/*
	 * Removes a given GameObject from the grid
	 */
	public void remove(int tmp_x, int tmp_y, GameObject obj) {
		List<GameObject> list = gameObjectGrid[tmp_x][tmp_y].getList();
		for(int i=0; i<list.size(); i++){
			if(list.get(i) == obj){
				list.remove(i);
			}
		}
		return;	
	}

	/*
	 * Check if a coordinate is out of bounds
	 */
    private boolean isValid(int x, int y){
    	if(x < 0 || x > tiles_in_row || y < 0 || y > tiles_in_col){
    		return false;
    	}
    	return true;
    }




}