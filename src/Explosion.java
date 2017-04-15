import java.awt.image.BufferedImage;


public class Explosion extends GameObject{

	private int frame_y = 0;
	private int frame_x = 0;
	private int animation_length;
	private final static int FRAME_DELAY = 1;
	private int current_frame_delay_counter = FRAME_DELAY;
	
	public Explosion(int style, BufferedImage imgList){
		super(imgList, false, true, true, false, false, EnumConsts.Object_Name.Explosion);
		animation_length = 3;
		frame_y = style;
	}
	
	public void animate(){
		if(frame_x < animation_length){
			setNextFrame();
		}
		return;
	}
	
	private void setNextFrame() {
		if(current_frame_delay_counter == 0){
			if(frame_x < animation_length){
				frame_x++;
			}else{
				frame_x = 0;
			}
			current_frame_delay_counter = FRAME_DELAY;
		}else{
			current_frame_delay_counter--;
		}
	}
	
	public BufferedImage getCurrentImage(){
		//System.out.println("GETS THE CHILD");
		System.out.println(frame_x + " " + frame_y);
		return image_frames.getSubimage(frame_x * TILE_SIZE, frame_y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
	}
	
	public boolean destroy(){
		if( frame_x == animation_length ){
			return true;
		}
		return false;
	}
	
	public int getType(){
		return frame_y;
	}
}
