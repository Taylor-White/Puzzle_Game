import java.awt.image.BufferedImage;



public class Coin extends GameObject {
	
	private int frame_y = 0;
	private int frame_x = 0;
	private int[] animation_lengths;
	private int idle_timer;
	private final static int TIME_TO_IDLE_ANIMATION = 12;
	private final static int FRAME_DELAY = 1;
	private int current_frame_delay_counter = FRAME_DELAY;
	
	private static int coin_count = 0;
	
	public Coin(BufferedImage imgList, int style){
		super(imgList, false, true, true, false, false, EnumConsts.Object_Name.Coin);
		animation_lengths = new int[2];
		animation_lengths[0] = 6;
		animation_lengths[1] = 6;
		this.frame_y = style;		
		System.out.println("imgList.getHeight(): " + imgList.getHeight());
		if(imgList.getHeight() < frame_y*TILE_SIZE + TILE_SIZE){
			this.frame_y = 0;
		}	
	}

	public void animate(){
		if(idle_timer < TIME_TO_IDLE_ANIMATION){
			idle_timer++;
		}else{
			setNextFrame();
			if(frame_x == 0){
				idle_timer = 0;
			}
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
	
	public boolean destroy(){
		setCoin_count(getCoin_count() - 1);
		return true;
	}

	public static int getCoin_count() {
		return coin_count;
	}

	public static void setCoin_count(int coin_count) {
		Coin.coin_count = coin_count;
	}
	

}
