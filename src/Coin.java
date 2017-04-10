import java.awt.image.BufferedImage;



public class Coin extends GameObject {
	
	private int frame_y = 0;
	private int frame_x = 0;
	private int[] animation_lengths;
	private int idle_timer;
	private final static int TIME_TO_IDLE_ANIMATION = 12;
	public static int coin_count = 0;
	
	public Coin(BufferedImage imgList){
		super(imgList, false, true, true, EnumConsts.Object_Name.Coin);
		coin_count++;
		animation_lengths = new int[2];
		animation_lengths[0] = 6;
		animation_lengths[1] = 3;
	}
	//coin.getSubimage(xGrid * TILE_SIZE, yGrid * TILE_SIZE, TILE_SIZE, TILE_SIZE);
	
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
		if(frame_x < animation_lengths[frame_y]){
			frame_x++;
		}else{
			frame_x = 0;
		}	
		//System.out.println("frame_x: " + frame_x);
	}
	
	public BufferedImage getCurrentImage(){
		//System.out.println("GETS THE CHILD");
		return image_frames.getSubimage(frame_x * TILE_SIZE, frame_y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
	}
	
	public boolean destroy(){
		System.out.println("COIN DESTROY!!!!!!!!!!!");
		coin_count--;
		if(coin_count == 0){System.out.println("COINS ALL GONE!!!!");}
		return true;
	}
	
	public static int getCoinCount(){
		return coin_count;
	}
}
