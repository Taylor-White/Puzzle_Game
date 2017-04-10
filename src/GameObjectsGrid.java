import java.awt.image.BufferedImage;
import java.util.ArrayList;





public class GameObjectsGrid {

	private GridCell[][] gameObjectGrid;
	
	private int tiles_in_row = 32;
	private int tiles_in_col = 24;
	private int current_x = 0;
	private int current_y = 0;
	
	public int player_x = 0;
	public int player_y = 0;
	
	public Player player;
	

	
	public void setGameObjectGrid(GridCell[][] gridCells) {
		this.gameObjectGrid = gridCells;
	}
	public void setGameObjectGrid(GridCell[][] level_array, int p_x, int p_y, Player p) {
		this.player_x = p_x;
		this.player_y = p_y;
		this.player = p;
		
	}
	
	public GridCell[][] getGameObjectGrid(){
		return gameObjectGrid;
	}
	public ArrayList<GameObject> getObjectList(){
		ArrayList<GameObject> list = new ArrayList<GameObject>();
		for(int i=0; i<gameObjectGrid.length; i++){
			for(int k=0; k<gameObjectGrid[i].length; k++){	
				list = gameObjectGrid[i][k].getList();
			}
		}
		return list;
	}
	public GridCell getAt(int x, int y){
		return gameObjectGrid[x][y];
	}
	public GridCell getNext(){
		GridCell go = gameObjectGrid[current_x][current_y];
		current_x++;
		if(gameObjectGrid[current_x][current_y] == null){
			current_x = 0;
			current_y++;
		}
		return go;
	}
	public Object playerCell() {
		// TODO Auto-generated method stub
		return null;
	}
	public Object playerCell(EnumConsts.Player_Action action) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isGround(int x, int y) {
		if(gameObjectGrid[x][y] == null){return false;}
		ArrayList<GameObject> list = gameObjectGrid[x][y].getList();
		
		for(int i=0; i<list.size(); i++){
			//System.out.println(list.get(i).toString());
			if(list.get(i).isGround()){
				return true;
			}
		}
		return false;
	}
	public boolean isTraversable(int x, int y) {
		if(gameObjectGrid[x][y] == null){return false;}
		ArrayList<GameObject> list = gameObjectGrid[x][y].getList();
		
		for(int i=0; i<list.size(); i++){
			//System.out.println(list.get(i).toString());
			if(!list.get(i).isTraversable()){
				return false;
			}
		}
		return true;
	}	

	public boolean isBreakable(int x, int y) {
		if(gameObjectGrid[x][y] == null){return false;}
		ArrayList<GameObject> list = gameObjectGrid[x][y].getList();
		System.out.println("Trying to break block at: ");
		System.out.println("X: " + x);
		System.out.println("Y: " + y);

		for(int i=0; i<list.size(); i++){
			//System.out.println(list.get(i).toString());
			if(!list.get(i).isBreakable()){
				return false;
			}
		}
		return true;
	}

	public void movePlayerTo(int from_x, int from_y, int to_x, int to_y) {
		EnumConsts.Object_Name n = EnumConsts.Object_Name.Player;
		//System.out.println("Remove player from...");
		GameObject player = gameObjectGrid[from_x][from_y].remove(n);
		gameObjectGrid[to_x][to_y].add(player);
		this.player_x = to_x;
		this.player_y = to_y;
	}


	public void setPlayerCoordinates(int x, int y){
		this.player_x = x;
		this.player_y = y;
	}
	public void printGrid(){
		System.out.println("Printing Grid...");
		for(int i=0; i<gameObjectGrid.length; i++){
			for(int j=0; j<gameObjectGrid[i].length; j++){
				if(gameObjectGrid[i][j] != null){
					ArrayList<GameObject> list = gameObjectGrid[i][j].getList();
					//if(list != null){
						System.out.println("Tile: " + i + " " + j);
						for(int k=0; k<list.size(); k++){
							if(list.get(k) != null){
								System.out.println("Object: " + list.get(k).getName());
							}
						}
					//}	
				}
			}	
		}
	}
	public void reset(){
		this.current_x = 0;
		this.current_y = 0;
	}

	public void destroy(int x, int y, EnumConsts.Object_Name object) {
		ArrayList<GameObject> list = gameObjectGrid[x][y].getList();
		
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

	public void increment_animations() {
		//System.out.println("Printing Grid...");
		for(int i=0; i<gameObjectGrid.length; i++){
			for(int j=0; j<gameObjectGrid[i].length; j++){
				if(gameObjectGrid[i][j] != null){
					ArrayList<GameObject> list = gameObjectGrid[i][j].getList();
					//if(list != null){
						//System.out.println("Tile: " + i + " " + j);
						for(int k=0; k<list.size(); k++){
							if(list.get(k) != null){
								list.get(k).animate();
							}
						}
					//}	
				}
			}	
		}		
		
	}

	public void checkPlayerCollisions(int x, int y) {
		if(gameObjectGrid[x][y] == null){return;}
		ArrayList<GameObject> list = gameObjectGrid[x][y].getList();

		for(int i=0; i<list.size(); i++){
			//System.out.println(list.get(i).toString());
			if(list.get(i).getName() == EnumConsts.Object_Name.Coin){
				boolean isRemove = list.get(i).destroy();
				if(isRemove){
					list.remove(i);
				}
			}
		}
		return;
		
	}




}