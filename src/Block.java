import java.awt.image.BufferedImage;


public class Block extends GameObject {
	
	private final static int RESPAWN_TIME = 96;
	private int respawn_counter = 0;
	private int graphics_style_x;
	private int graphics_style_y;
	private final static int IMAGE_ROW_LENGTH = 10;

	public Block(BufferedImage imgList, int style){
		super(imgList, true, false, true, EnumConsts.Object_Name.Block);
		System.out.println("Style: " + style);
		this.graphics_style_y = style / IMAGE_ROW_LENGTH;
		this.graphics_style_x = style % IMAGE_ROW_LENGTH;
		System.out.println("Graphics_style_x: " + graphics_style_x);
		System.out.println("Graphics_style_y: " + graphics_style_y);

	}
	
	public boolean destroy(){
		System.out.println("Destroying Block...");
		respawn_counter = RESPAWN_TIME;
		isTraversable = true;
		isGround = false;
		return false;
	}
	
	public void animate(){
		if(respawn_counter > 0){
			respawn_counter--;
		}else if(isTraversable == true || isGround == false){
			isTraversable = false;
			isGround = true;
		}
		
		return;
	}
	
	public BufferedImage getCurrentImage(){
		//System.out.println("GETS THE CHILD");
		if(respawn_counter == 0){
			return image_frames.getSubimage(graphics_style_x * TILE_SIZE, graphics_style_y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
		}	
		return null;
	}
}
