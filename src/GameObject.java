import java.awt.image.BufferedImage;




public abstract class GameObject {
	protected final static int TILE_SIZE = 32;
	
	EnumConsts.Object_Name name;
	BufferedImage image_frames;
	int default_image_index;
	
	boolean isGround;
	boolean isBreakable;
	boolean isTraversable;
	boolean isItem;
	
	public GameObject(BufferedImage imgList, boolean isGround, boolean isTraversable, boolean isBreakable, boolean isItem, EnumConsts.Object_Name n){
		this.name = n;
		this.image_frames = imgList;
		this.isGround = isGround;
		this.isBreakable = isBreakable;
		this.isTraversable = isTraversable;
		this.isItem = isItem;
		return;
	}
	
	public BufferedImage getDefaultImage(){
		if(image_frames == null){
			return null;
		}
		//return image_frames[default_image_index];
		return image_frames.getSubimage(0, 0, TILE_SIZE, TILE_SIZE);
	}
	
	public BufferedImage getCurrentImage(){
		return getDefaultImage();
	}

	public boolean isGround() {
		return this.isGround;
	}

	public EnumConsts.Object_Name getName() {
		return name;
	}

	public boolean isBreakable() {
		return this.isBreakable;
	}
	public boolean isTraversable() {
		return this.isTraversable;
	}
	public boolean destroy(){
		return false;
	}
	
	public void animate(){
		return;
	}
	public int[] getOffset(int tile_size_x, int tile_size_y){
		return new int[]{0,0};
	}

	public boolean isItem() {
		return isItem;
	}

	public int getItemNumber() {
		// TODO Auto-generated method stub
		return -1;
	}

	public boolean halfWayThere() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isFalling() {
		return false;
	}
	
	public void startFalling(){
		
	}

}
