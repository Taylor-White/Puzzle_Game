
public class Movable_Object {

	private int x;
	private int y;
	private GameObject obj;
	
	public Movable_Object(int x, int y, GameObject obj){
		this.setX(x);
		this.setY(y);
		this.setObj(obj);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public GameObject getObj() {
		return obj;
	}

	public void setObj(GameObject obj) {
		this.obj = obj;
	}
	
}
