import java.awt.image.BufferedImage;
import java.util.ArrayList;







public class GameObjectsGrid {

	private GridCell[][] gameObjectGrid;
	
	private int tiles_in_row = 32;
	private int tiles_in_col = 24;
	private int current_x = 0;
	private int current_y = 0;
	

	
	public Player player;
	

	
	public void setGameObjectGrid(GridCell[][] gridCells) {
		this.gameObjectGrid = gridCells;
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
	public GameObject isItem(int x, int y) {
		if(gameObjectGrid[x][y] == null){return null;}
		ArrayList<GameObject> list = gameObjectGrid[x][y].getList();
		
		for(int i=0; i<list.size(); i++){
			//System.out.println(list.get(i).toString());
			if(list.get(i).isItem()){
				return list.get(i);
			}
		}
		return null;
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
	
	public void moveObjectTo(GameObject obj, int from_x, int from_y, int to_x, int to_y) {
		//System.out.println("Remove player from...");
		gameObjectGrid[from_x][from_y].remove(obj);
		gameObjectGrid[to_x][to_y].add(obj);
		//ADD PLAYER_X AND PLAYER_Y CHANGES TO THIS
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
	
	public void remove(int tmp_x, int tmp_y, GameObject obj) {
		ArrayList<GameObject> list = gameObjectGrid[tmp_x][tmp_y].getList();
		System.out.println("In remove in gameobjectgrid");
		System.out.println("OBJ: " + obj);
		for(int i=0; i<list.size(); i++){
			System.out.println(list.get(i).toString());
			if(list.get(i) == obj){
				System.out.println("made it here" + list.get(i).getName());
				list.remove(i);
				
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
			GameObject obj = list.get(i);
			if(obj.getName() == EnumConsts.Object_Name.Coin){
				boolean isRemove = list.get(i).destroy();
				if(isRemove){
					list.remove(i);
				}
			}else if(obj.getName() == EnumConsts.Object_Name.Dynamite  && obj.isItem()){
				boolean isRemove = list.get(i).destroy();
				if(isRemove){
					list.remove(i);
				}
			}
		}
		return;
		
	}
	public void add(int x, int y, GameObject obj) {
		gameObjectGrid[x][y].add(obj);
	}


	public boolean isDeath(int x, int y) {
		if(gameObjectGrid[x][y] == null){return false;}
		ArrayList<GameObject> list = gameObjectGrid[x][y].getList();
		
		for(int i=0; i<list.size(); i++){
			//System.out.println(list.get(i).toString());
			if(list.get(i).isDeath()){
				return true;
			}
		}
		return false;
	}









}