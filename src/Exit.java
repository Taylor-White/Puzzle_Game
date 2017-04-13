import java.awt.image.BufferedImage;




public class Exit extends GameObject {
	
	private int frame_y = 0;
	private int frame_x = 0;
	private int[] animation_lengths;
	private int current_frame_delay_counter = FRAME_DELAY;
	private final static int FRAME_DELAY = 1;
	private boolean isOpen = false;
	
	public Exit(BufferedImage imgList){
		super(imgList, false, true, false, false, EnumConsts.Object_Name.Coin);
		animation_lengths = new int[3];
		animation_lengths[0] = 0;
		animation_lengths[1] = 7;
		animation_lengths[2] = 1;
	}
	//coin.getSubimage(xGrid * TILE_SIZE, yGrid * TILE_SIZE, TILE_SIZE, TILE_SIZE);
	
	public void animate(){
    	setNextFrame();
    	//Opens door when coins are collected.  Need a way to count coins that makes sense.
		/*if(GameProcessing.coin_count() == 0 && isOpen == false){
			System.out.println("DOOR OPENING");
			frame_x = 0;
			frame_y = 1;
			isOpen = true;
		}*/
		if(frame_x == 7 && frame_y == 1){
			frame_x = 0;
			frame_y = 2;
		}
		return;
	}
	
	private void setNextFrame() {
		if(current_frame_delay_counter == 0){
			if(frame_x < animation_lengths[frame_y]){
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
		return image_frames.getSubimage(frame_x * TILE_SIZE, frame_y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
	}
	

}
