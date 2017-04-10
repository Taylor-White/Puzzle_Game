import java.awt.List;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class GridCell {
	private ArrayList<GameObject> list = new ArrayList<GameObject>();
	private int i;
	private int x_coordinate;
	private int y_coordinate;
	
	
	public GridCell(){
		this.i = 0;
	}
	public GridCell(int x, int y) {
		this.x_coordinate = x;
		this.y_coordinate = y;
	}
	
	/*
	public GridCell(int j, int x, int y, BufferedImage[] img_set, boolean isTraversable, EnumConsts.Object_Name n, boolean isBreakable) {
		this.list.add(new GameObject(j,img_set, isTraversable, n, isBreakable));
		this.x_coordinate = x;
		this.y_coordinate = y;
	}*/
	public void add(GameObject go){
		list.add(go);
	}
	public GameObject getNext(){
		return list.get(i++);
	}
	public void resetCounter(){
		this.i = 0;
	}
	
	public GameObject remove(EnumConsts.Object_Name object_Name){
		GameObject ob = null;
		for(int i=0; i<list.size(); i++){
			if(list.get(i).getName() == object_Name){
				System.out.println("Found Player");
				ob = list.get(i);
				list.remove(i);
				if(list.isEmpty()){
					//list.add(new GameObject(0, null, true, null, false));//new GameObject(0, x, y, null, true, EnumConsts.Object_Name.Empty)
				}
				return ob;
			}
		}
		return ob;
	}
	public GameObject getGameObject() {
		// TODO Auto-generated method stub
		return null;
	}
	public ArrayList<GameObject> getList() {
		return list;
	}
	public int size() {
		
		return list.size();
	}
	public GameObject getAt(int i) {
		
		return list.get(i);
	}
	public GameObject removePlayer() {

		
		for(int i=0; i<list.size(); i++){
			
		}
		return null;
	}
	public void reset() {
		this.i=0;
	}
	public int getX(){
		return this.x_coordinate;
	}
	public int getY(){
		return this.y_coordinate;
	}
}
