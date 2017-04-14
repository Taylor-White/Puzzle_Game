import java.awt.image.BufferedImage;



public class Dynamite extends GameObject{

	private final static int IMAGE_ROW_LENGTH = 7;
	private boolean isIgnited;

	//Dynamite Type
	private int frame_x = 0;
	private int frame_y = 0;
	private int dynamite_number;
	
	//Animation Speed
	private final static int FRAME_DELAY = 1;
	private int current_frame_delay_counter = FRAME_DELAY;
	
	//Time to Detonate
	private final static int DETONATION_TIME = 3*IMAGE_ROW_LENGTH*FRAME_DELAY;
	private int time_until_detonate;
	
	//Manage Falling
	private boolean isFalling = false;
	private int timer = 0;
	private final static int ACTION_TIME = 8;
	private final static int OFFSET_INTERVAL = TILE_SIZE / ACTION_TIME;
	private int offset_y = 0;
	
	

	public Dynamite(BufferedImage imgList, int style, boolean ignited){
		super(imgList, false, true, true, true, EnumConsts.Object_Name.Dynamite);
		this.frame_y = style;
		dynamite_number = style;
		isIgnited = ignited;
		time_until_detonate = DETONATION_TIME;

	}
	
	public void animate(){
    	timer--;
    	//Done Falling
    	if(timer == 0){
    		isFalling = false;
    		offset_y = 0;
    	}else{
    		offset_y += OFFSET_INTERVAL;
    	}	
		if(isIgnited){
				setNextFrame();
				time_until_detonate--;
		}
		return;
	}
	
	public boolean halfWayThere(){ 
		if(timer == ACTION_TIME / 2){
			offset_y = - TILE_SIZE / 2;
			return true;
		}
		return false;
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
	}
	
	public void ignite(){
		isIgnited = true;
	}

	public BufferedImage getCurrentImage(){
		return image_frames.getSubimage(frame_x * TILE_SIZE, frame_y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
	}
	
	public boolean isItem() {
		if(isIgnited){
			return false;
		}
		return isItem;
	}
	public int getType(){
		return this.frame_y;
	}

	public boolean destroy(){
		if(time_until_detonate <= 0 || !isIgnited){
			return true;
		}
		return false;
	}
	
	public int getItemNumber() {
		return dynamite_number;
	}
}
