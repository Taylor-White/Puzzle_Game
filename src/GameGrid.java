import java.util.ArrayList;


public class GameGrid {

	private ArrayList<GridCell> gameObjectGrid;
	
	private int tiles_in_row = 32;
	private int tiles_in_col = 24;
	private int current_x = 0;
	private int current_y = 0;
	
	
	public void setGameGrid(ArrayList<GridCell> g) {
		this.gameObjectGrid = g;
	}
	
	public ArrayList<GridCell> getGameGrid(){
		return gameObjectGrid;
	}

	public GridCell getAt(int x){
		return gameObjectGrid.get(x);
	}
	/*public GameObject getNext(){
		GameObject go = gameObjectGrid[current_x][current_y];
		current_x++;
		if(gameObjectGrid[current_x][current_y] == null){
			current_x = 0;
			current_y++;
		}
		return go;
	}*/
	/*public GameObject getAt(int i){
		int counter = 0;
		for(int x=0; x<tiles_in_row-1; x++){
			for(int y=0; y<tiles_in_col-1; y++){
				if(counter >= i){
					System.out.println("getting at: " + y + " and " + x);
					System.out.println("Object is: " + gameObjectGrid[y][x].toString());
					return gameObjectGrid[x][y];
				}
				counter++;
			}
		}
		System.out.println("null...");
		return null;
	}*/

}
