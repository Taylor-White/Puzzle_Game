import java.awt.image.BufferedImage;


public class Dynamite extends GameObject{

	private final static int IMAGE_ROW_LENGTH = 7;
	private boolean isIgnited;
	private int frame_x = 0;
	private int frame_y = 0;
	private int dynamite_number;
	private final static int FRAME_DELAY = 1;
	private int current_frame_delay_counter = FRAME_DELAY;
	private final static int DETONATION_TIME = 3*IMAGE_ROW_LENGTH*FRAME_DELAY;
	
	

	public Dynamite(BufferedImage imgList, int style, boolean ignited){
		super(imgList, false, true, true, true, EnumConsts.Object_Name.Dynamite);
		this.frame_y = style;
		dynamite_number = style;
		isIgnited = ignited;
		//isIgnited = true;

	}
	
	public void animate(){
		if(isIgnited){
				setNextFrame();
			
		}
		return;
	}
	
	public int getType(){
		return this.frame_y;
	}
	

	
	private void setNextFrame() {
		if(current_frame_delay_counter == 0){
			if(frame_x < IMAGE_ROW_LENGTH){
				frame_x++;
			}else{
				frame_x = 0;
			}	
			current_frame_delay_counter = FRAME_DELAY;

		}else{
			current_frame_delay_counter--;
		}	
		//System.out.println("frame_x: " + frame_x);
	}
	
	public void ignite(){
		isIgnited = true;
	}

	public BufferedImage getCurrentImage(){
		//System.out.println("GETS THE CHILD");
		return image_frames.getSubimage(frame_x * TILE_SIZE, frame_y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
	}
	
	public boolean isItem() {
		if(isIgnited){
			return false;
		}
		return isItem;
	}
	

	public boolean destroy(){
		return true;
	}
	
	public int getItemNumber() {
		return dynamite_number;
	}
}
