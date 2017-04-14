import java.util.ArrayList;





public class GameProcessing{
	private GameObjectsGrid current_level;
	
	//Player Details
	private int player_x = 0;
	private int player_y = 0;
	private Player player;
	private boolean input_blocked = false;
	EnumConsts.Player_Action next_action = null;
	
	private ArrayList<Movable_Object> movable_objects = new ArrayList<Movable_Object>();
	
	//Item List
	private int[] items;
	
	//ImageList
	private ImageList imgList;
	
	//Application Objects
	private GameView gameView;
	
	//Default Values
	private final int tiles_in_row;
	private final int tiles_in_col;

	private KeyActionManager keyActionManager;
	
	//Level Stuff
	private Level_Builder level_builder;
	private int level_int = 1;
	private boolean restart = false;

	public GameProcessing(int x_tiles, int y_tiles, GameView gv){
		//Setup Variables
		this.tiles_in_row = x_tiles;
		this.tiles_in_col = y_tiles;
		
		//Set Game View
		this.gameView = gv;
		
		//Action Manager
		this.keyActionManager = gv.getActionManager();
		
		//Setup Graphics
		this.imgList = new ImageList();
		this.imgList.initialize();
		System.out.println(imgList.toString());
		
		//Initialize current level to '1'
		level_int = 1;
		
		//Blank Level
		this.current_level = new GameObjectsGrid();
		
		//Build Level
		level_builder = new Level_Builder(x_tiles, y_tiles, imgList);
		
		//Set Inventory to Empty
		items = new int[]{0,0,0,0};
	}
	public void run(){

		while(true){
			level_builder.setLevel(level_int);
			new Thread(level_builder).start();
			while(level_builder.getLock()){
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			movable_objects.clear();
			//Need a function for setting these
			this.current_level = level_builder.getCurrentLevel();
			this.player = level_builder.getCurPlayer();
			this.player_x = level_builder.getCurPlayerX();
			this.player_y = level_builder.getCurPlayerY();
			System.out.println("gameView: " + gameView.toString());
			gameView.drawing(current_level);
			
			while(true){
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				input_blocked = player.getLock();
				adjust_positions();
				//Fix for Idle Climbing Animation ~~~
				if(!current_level.isGround(player_x, player_y) && player.is_idle_climb()){
					player.stand();
				}if(current_level.isGround(player_x, player_y) && player.is_standing()){
					player.idle_climb();
				}
				
				checkPlayerCollisions(); 
				checkPlayerFalling();
				checkItemsFalling();
				
				checkPlayerCollisions();
				
				//checkPlayerDeath();
				//if player dies, break loop and restart level
				if(restart){
					restart = false;
					reset_items();
					break;
				}
				current_level.increment_animations();
				gameView.drawing(current_level);
				//End Loop
			}
			System.out.println("THIS IS DONE");
			level_builder.setLevel(1);
		}
	}
	

	private void checkItemsFalling() {
		for(int i=0; i<movable_objects.size(); i++){
			Movable_Object obj = movable_objects.get(i);
			int obj_x = obj.getX();
			int obj_y = obj.getY();
			if(!obj.getObj().isFalling() && shouldFall(obj_x, obj_y)){
				start_fall(obj_x, obj_y, obj.getObj());	
			}			
		}
	}
	private void checkPlayerFalling() {
		if(!input_blocked && shouldFall(player_x, player_y)){
			start_fall(player_x, player_y, player);	
		}else if(!input_blocked){
			next_action = keyActionManager.getNext();
			if(next_action != null){
				perform_action(next_action);
				next_action = null;
			}
		}
	}
	private void reset_items() {
		for(int i = 0; i<4; i++){
			gameView.setItemNumber(i,0);
			items[i] = 0;
		}
	}
	private void checkPlayerCollisions() {
		int item_number;
		GameObject item = current_level.isItem(player_x,  player_y);
		if(item != null){
			item_number = item.getItemNumber();
			item_update(item_number, true);
		}
		current_level.checkPlayerCollisions(player_x,player_y);
		checkPlayerDeath();
	}
	private void adjust_positions() {
		//for(objects in grid that can move)
		for(int i=0; i<movable_objects.size(); i++){
			GameObject obj = movable_objects.get(i).getObj();
			if(obj.halfWayThere()){
				System.out.println("THIS IS REACHED");
				move_object_in_grid(obj, null);
			}
		}
		if(player.halfWayThere()){
			EnumConsts.Direction direction = player.getDirection();
			//move_from_fall(player_x, player_y, EnumConsts.Object_Name.Player);
			move_object_in_grid(player, direction);
			//Fix for animation after climbing
		}
	}
	private void move_object_in_grid(GameObject obj, EnumConsts.Direction d) {
		if(obj.getName() == EnumConsts.Object_Name.Player){
			switch (d) {
			
			case Left:
				current_level.moveObjectTo(player,player_x, player_y, player_x-1, player_y);
				player_x = player_x - 1;
				break;
			case Right:
				current_level.moveObjectTo(player,player_x, player_y, player_x+1, player_y);
				player_x = player_x + 1;
				break;
			case Up:
				current_level.moveObjectTo(player,player_x, player_y, player_x, player_y-1);
				player_y = player_y - 1;
				break;
			case Down:
				current_level.moveObjectTo(player,player_x, player_y, player_x, player_y+1);
				player_y = player_y + 1;
				break;
			default:
				break;
			}
		}else if(obj.getName() == EnumConsts.Object_Name.Dynamite){
			int tmp_x = -1;
			int tmp_y = -1;
			for(int i=0; i<movable_objects.size(); i++){
				Movable_Object m_obj = movable_objects.get(i);
				if(obj == m_obj.getObj()){
					tmp_x = m_obj.getX();
					tmp_y = m_obj.getY();
					current_level.moveObjectTo(m_obj.getObj(), tmp_x, tmp_y, tmp_x, tmp_y+1);
					m_obj.setY(tmp_y+1);
					break;
				}
			}
			
		}else{
			System.out.println("Error");
		}
			
		
	}
	private void checkPlayerDeath() {
		if(!current_level.isTraversable(player_x, player_y)){
			//PlayerDeath!!!
			System.out.println("Player Dead");
			this.restart = true;
		}
		
	}
	private void start_fall(int x, int y, GameObject obj) {
		obj.startFalling();
	}
	private boolean shouldFall(int x, int y) {
		return !(current_level.isGround(x, (y+1)) || current_level.isGround(x, (y)));
	}
	
	public void perform_action(EnumConsts.Player_Action action){
		System.out.println("Action: " + action);
		switch (action) {
			case Move_Left:
				Move_Left(action);
				break;
			case Move_Right:
				Move_Right(action);
				break;
			case Move_Up:
				Move_Up(action);
				break;	
			case Move_Down:
				Move_Down(action);
				break;
			case Fire_Left:
				Fire_Left(action);
				break;
			case Fire_Right:
				Fire_Right(action);
				break;
			case Self_Destruct:
				Self_Destruct(action);
				break;
			case Drop_Item_0:
				Drop_Item(action, 0);
				break;
			case Drop_Item_1:
				Drop_Item(action, 1);
				break;
			case Drop_Item_2:
				Drop_Item(action, 2);
				break;
			case Drop_Item_3:
				Drop_Item(action, 3);
				break;
			default:
				System.out.println("Invalid action");
				break;	
		}
	}
	
	private void Drop_Item(EnumConsts.Player_Action action, int item_num) {
		// Use player action to determine which type of dynamite to drop
		System.out.println("Dropping item");
		if(items[item_num] > 0){
			item_update(item_num, false);
			Dynamite d = new Dynamite(imgList.getDynamite(), item_num, true);
			boolean already_dropped = false;
			//Drop item in direction player is facing.  If player is facing up/down or the spot for the item to land is impassible, drop item on player
			if(player.getDirection() == EnumConsts.Direction.Left){
				if(current_level.isTraversable(player_x-1, player_y)){
					current_level.add(player_x-1, player_y, d);
					Movable_Object mv = new Movable_Object(player_x-1, player_y, d);
					movable_objects.add(mv);
					already_dropped = true;
				}else{
					already_dropped = false;
				}
			}else if(player.getDirection() == EnumConsts.Direction.Right){
				if(current_level.isTraversable(player_x+1, player_y)){
					current_level.add(player_x+1, player_y, d);
					Movable_Object mv = new Movable_Object(player_x+1, player_y, d);
					movable_objects.add(mv);
					already_dropped = true;
				}else{
					already_dropped = false;
				}
			}
			if(!already_dropped){
				current_level.add(player_x, player_y, d);
				Movable_Object mv = new Movable_Object(player_x, player_y, d);
				movable_objects.add(mv);
			}
			
			
		}
	}
	private void item_update(int i, boolean inc){
		System.out.println("Current num of items at: " + i + " is " + items[i]);
		if(inc){
			items[i]++;
		}else{
			items[i]--;
		}
		gameView.setItemNumber(i,items[i]);
	}
	
	//THIS METHOD IS CURRENTLY BEING USED FOR TESTING ~
	private void Self_Destruct(EnumConsts.Player_Action action) {
		// CURRENTLY PRINTS OUT NEXT LEVEL
		current_level.printGrid();
		
		System.out.println("Player Explode");
		
		//this.restart = true;
		
	}
	public void paintThisFrame(){
		gameView.drawing(current_level);
	}
	
    private void Move_Left(EnumConsts.Player_Action action) {
    	System.out.println("MoveAction Called");
    	System.out.println("isGround: " + current_level.isGround(player_x-1, player_y));
    	
    	if(current_level.isTraversable(player_x-1, player_y)){
    		player.move(EnumConsts.Direction.Left);
    		System.out.println("Moving Player To...");
    	}else{
    		System.out.println("Can't move " + action);
    	}
    }
    
    private void Move_Right(EnumConsts.Player_Action action) {
    	System.out.println("MoveAction Called");
    	System.out.println("isGround: " + current_level.isGround(player_x+1, player_y));
    	
    	if(current_level.isTraversable(player_x+1, player_y)){
    		player.move(EnumConsts.Direction.Right);
    		System.out.println("Moving Player To...");
    	}else{
    		System.out.println("Can't move " + action);
    	}
    }
    
    private void Move_Up(EnumConsts.Player_Action action) {
    	System.out.println("MoveAction Called");
    	System.out.println("isGround: " + current_level.isGround(player_x-1, player_y));
    	
    	if(current_level.isGround(player_x, player_y) && current_level.isTraversable(player_x, player_y-1)){
    		player.move(EnumConsts.Direction.Up);
    		System.out.println("Moving Player To...");
    	}else{
    		System.out.println("Can't move " + action);
    	}
    }
    
    private void Move_Down(EnumConsts.Player_Action action) {
    	System.out.println("MoveAction Called");
    	System.out.println("isGround: " + current_level.isGround(player_x-1, player_y));
    	
    	if(current_level.isTraversable(player_x, player_y+1)){
    		player.move(EnumConsts.Direction.Down);
    	}else{
    		System.out.println("Can't move " + action);
    	}
    }
    
    private void Fire_Left(EnumConsts.Player_Action action){
    	System.out.println("FireAction Called");
    	System.out.println("isBreakable: " + current_level.isBreakable(player_x-1, player_y+1));
    	
    	if(current_level.isBreakable(player_x-1, player_y+1) && current_level.isTraversable(player_x-1, player_y)){
    		current_level.destroy(player_x-1, player_y+1, EnumConsts.Object_Name.Block);
    		player.fireStanding(EnumConsts.Direction.Left);
    	}else{
    		player.fireStanding(EnumConsts.Direction.Left);
    		System.out.println("Can't fire " + action);
    	}
    }
    
    private void Fire_Right(EnumConsts.Player_Action action){
    	System.out.println("FireAction Called");
    	System.out.println("isBreakable: " + current_level.isBreakable(player_x+1, player_y+1));
    	
    	if(current_level.isBreakable(player_x+1, player_y+1) && current_level.isTraversable(player_x+1, player_y)){
    		current_level.destroy(player_x+1, player_y+1, EnumConsts.Object_Name.Block);
    		player.fireStanding(EnumConsts.Direction.Right);
    	}else{
    		player.fireStanding(EnumConsts.Direction.Right);
    		System.out.println("Can't fire " + action);
    	}
    }


}

	

