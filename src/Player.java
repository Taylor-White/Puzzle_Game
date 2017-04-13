import java.awt.image.BufferedImage;




public class Player extends GameObject {
	
	private int frame_y = 0;
	private int frame_x = 0;
	private int[] animation_lengths;
	private EnumConsts.Direction direction = EnumConsts.Direction.Right;
	private final static int ACTION_TIME = 8;
	private int timer = 0;
	private boolean lock = false;
	private	enum Player_State { Stand, Walk, Fall, Fire, Climb, Climb_Idle, Climb_Fire };
	private Player_State player_state = Player_State.Stand;
	private final static int OFFSET_INTERVAL = TILE_SIZE / ACTION_TIME;
	private int offset_x = 0;
	private int offset_y = 0;
	private final static int FRAME_DELAY = 1;
	private int current_frame_delay_counter = FRAME_DELAY;
	
	public Player(BufferedImage imgList){
		super(imgList, false, true, true, false, EnumConsts.Object_Name.Player);
		animation_lengths = new int[15];
		animation_lengths[0] = 0;
		animation_lengths[1] = 0;
		animation_lengths[2] = 3;
		animation_lengths[3] = 3;
		animation_lengths[4] = 6;
		animation_lengths[5] = 6;
		animation_lengths[6] = 6;
		animation_lengths[7] = 6;
		animation_lengths[8] = 6;
		animation_lengths[9] = 6;
		animation_lengths[10] = 1;
		animation_lengths[11] = 0;
		animation_lengths[12] = 3;
		animation_lengths[13] = 3;
		animation_lengths[14] = 3;
	}
	
	public void animate(){
		switch (player_state) {
        case Stand: 
    		if(direction == EnumConsts.Direction.Left){
    			frame_x = 0;
    			frame_y = 1;
    		}else{
    			frame_x = 0;
    			frame_y = 0;
    		}
            break;
        case Walk:
        	if(EnumConsts.Direction.Left == direction){
        		setNextFrame();
            	timer--;
            	//Done Falling
            	if(timer == 0){
            		lock = false;
            		player_state = Player_State.Stand;
            		offset_x = 0;
            	}else{
            		offset_x -= OFFSET_INTERVAL;
            	}	
        	}else{
        		setNextFrame();
            	timer--;
            	//Done Falling
            	if(timer == 0){
            		lock = false;
            		player_state = Player_State.Stand;
            		offset_x = 0;
            	}else{
            		offset_x += OFFSET_INTERVAL;
            	}	
        	}
        	
        	break;
        case Fall:
        	setNextFrame();
        	timer--;
        	//Done Falling
        	if(timer == 0){
        		lock = false;
        		player_state = Player_State.Stand;
        		offset_y = 0;
        	}else{
        		offset_y += OFFSET_INTERVAL;
        	}	
        	break;
        case Fire: 
    		setNextFrame();
    		if(frame_x == 0 && current_frame_delay_counter == FRAME_DELAY){
    			player_state = Player_State.Stand;
    			lock = false;
    			offset_y = 0;
    		}
            break;
        case Climb_Fire: 
    		setNextFrame();
    		if(frame_x == 0 && current_frame_delay_counter == FRAME_DELAY){
    			player_state = Player_State.Climb_Idle;
    			lock = false;
    			offset_y = 0;
    		}
            break;
        case Climb:
        	if(EnumConsts.Direction.Up == direction){
        		setNextFrame();
            	timer--;
            	//Done Falling
            	if(timer == 0){
            		lock = false;
            		player_state = Player_State.Climb_Idle;
            		offset_y = 0;
            		frame_y = 11;
                	frame_x = 0;
            	}else{
            		offset_y -= OFFSET_INTERVAL;
            	}	
        	}else if(EnumConsts.Direction.Down == direction){
        		setNextFrame();
            	timer--;
            	//Done Falling
            	if(timer == 0){
            		lock = false;
            		player_state = Player_State.Climb_Idle;
            		offset_y = 0;
            		frame_y = 11;
                	frame_x = 0;
            	}else{
            		offset_y += OFFSET_INTERVAL;
            	}	
        	}else if(EnumConsts.Direction.Right == direction){
        		setNextFrame();
            	timer--;
            	//Done Falling
            	if(timer == 0){
            		lock = false;
            		player_state = Player_State.Climb_Idle;
            		offset_x = 0;
            		frame_y = 11;
                	frame_x = 0;
            	}else{
            		offset_x += OFFSET_INTERVAL;
            	}
        	}else if(EnumConsts.Direction.Left == direction){
        		setNextFrame();
            	timer--;
            	//Done Falling
            	if(timer == 0){
            		lock = false;
            		player_state = Player_State.Climb_Idle;
            		offset_x = 0;
            		frame_y = 11;
                	frame_x = 0;
            	}else{
            		offset_x -= OFFSET_INTERVAL;
            	}
        	}	
            break;
        case Climb_Idle:
        	frame_y = 11;
        	frame_x = 0;
        	break;
        default: 
            break;
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
	
	public void faceLeft(){
		direction = EnumConsts.Direction.Left;
	}
	
	public void faceRight(){
		direction = EnumConsts.Direction.Right;
	}
	
	public void fireStanding(EnumConsts.Direction direction){
		current_frame_delay_counter = FRAME_DELAY;
		if(player_state == Player_State.Climb_Idle){
			if(direction == EnumConsts.Direction.Left){
				frame_y = 7;
				frame_x = 0;
			}else{
				frame_y = 6;
				frame_x = 0;
			}
			this.direction = direction;
			player_state = Player_State.Climb_Fire;
			lock = true;			
		}else{
			if(direction == EnumConsts.Direction.Left){
				frame_y = 5;
				frame_x = 0;
			}else{
				frame_y = 4;
				frame_x = 0;
			}
			this.direction = direction;
			player_state = Player_State.Fire;
			lock = true;
		}	
	}
	public void falling(){
		if(player_state != Player_State.Fall){
			frame_y = 14;
			frame_x = 0;
			player_state = Player_State.Fall;
			direction = EnumConsts.Direction.Down;
		}	
		timer = ACTION_TIME;
		lock = true;
	}
	
	/*public void walk(EnumConsts.Direction direction){
		if(direction == EnumConsts.Direction.Left){
			
		}
	}*/
	
	public boolean getLock(){
		return lock;
	}
	
	public int[] getOffset(int tile_size_x, int tile_size_y){
		//System.out.println("Offset_y: " + offset_y);
		return new int[]{offset_x,offset_y};
	}
	public boolean halfWayThere(){ //Living on a prayer
		if((timer == ACTION_TIME / 2) && offset_y < 0){
			offset_y = TILE_SIZE / 2;
			return true;
		}else if((timer == ACTION_TIME / 2) && offset_y > 0){
			offset_y = - TILE_SIZE / 2;
			return true;
		}else if(timer == ACTION_TIME / 2 && offset_x > 0){
			offset_x = - TILE_SIZE / 2;
			return true;
		}else if(timer == ACTION_TIME / 2 && offset_x < 0){
			offset_x = TILE_SIZE / 2;
			return true;
		}
		return false;
	}

	public EnumConsts.Direction getDirection() {
		return direction;
	}

	public void move(EnumConsts.Direction d) {
		direction = d;
		//if(player_state != Player_State.Walk){
		if (direction == EnumConsts.Direction.Up){
			frame_y = 10;
			frame_x = 0;
			player_state = Player_State.Climb;
		}else if (direction == EnumConsts.Direction.Down){
			frame_y = 10;
			frame_x = 0;
			player_state = Player_State.Climb;
		}else if (direction == EnumConsts.Direction.Left){
			if(player_state == Player_State.Climb_Idle){
				frame_y = 10;
				frame_x = 0;
				player_state = Player_State.Climb;
			}else{
				frame_y = 3;
				frame_x = 0;
				player_state = Player_State.Walk;
			}
		}else if(direction == EnumConsts.Direction.Right){
			if(player_state == Player_State.Climb_Idle){
				frame_y = 10;
				frame_x = 0;
				player_state = Player_State.Climb;
			}else{
				frame_y = 2;
				frame_x = 0;
				player_state = Player_State.Walk;
			}
			
			
		}
		//}	
		timer = ACTION_TIME;
		lock = true;
		
	}

	//Fix for finishing climbing up ladders
	public void stand() {
		player_state = Player_State.Stand;
		direction = EnumConsts.Direction.Right; //Bug making player face right.  Easiest solution would be creating a neutral animation where he faces forward.
	}

	public boolean is_idle_climb() {
		if(player_state == Player_State.Climb_Idle){
			return true;
		}
		return false;
	}

	public boolean is_standing() {
		if(player_state == Player_State.Stand){
			return true;
		}
		return false;
	}

	public void idle_climb() {
		frame_y = 10;
		frame_x = 0;
		player_state = Player_State.Climb_Idle;
		direction = EnumConsts.Direction.Right;
		
	}
}
