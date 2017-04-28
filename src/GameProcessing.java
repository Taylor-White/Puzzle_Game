import java.util.ArrayList;
import java.util.List;





public class GameProcessing{
	private GameObjectsGrid current_level;
	
	//Player Details
	private int player_x = 0;
	private int player_y = 0;
	private Player player;
	private boolean input_blocked = false;
	EnumConsts.Player_Action next_action = null;
	
	private ArrayList<Movable_Object> active_objects = new ArrayList<Movable_Object>();
	
	//Item List
	private int[] items;
	
	//ImageList
	private ImageList imgList;
	
	//Application Objects
	private GameView gameView;
	
	//Default Values
	private int tiles_in_row;
	private int tiles_in_col;

	private KeyActionManager keyActionManager;
	
	//private ToolBarActions toolBarActions;
	
	//Level Stuff
	private Level_Builder level_builder;
	private int level_int;
	private boolean restart = false;
	List<Level_Details> level_list;

	public GameProcessing(GameView gv){
		//Set Game View
		this.gameView = gv;
		
		//Action Manager
		this.keyActionManager = gv.getActionManager();
		
		//Setup Graphics
		this.imgList = new ImageList();
		this.imgList.initialize();
		System.out.println(imgList.toString());
		
		//Initialize current level to '1'
		level_int = 0;
		
		//Blank Level
		this.current_level = new GameObjectsGrid(tiles_in_row, tiles_in_col);
		
		//Build Level
		level_builder = new Level_Builder(tiles_in_row, tiles_in_col, imgList, level_int);
		
		//Storage for future levels
		level_list = new ArrayList<Level_Details>();
		
		//Set Inventory to Empty
		items = new int[]{0,0,0,0};
	}
	public void run(){

		while(true){
			
			
			//Fix Loading Levels
			System.out.println("SET LEVEL: " + level_int);
			level_builder.setLevel(level_int);
			new Thread(level_builder).start();
			while(level_builder.getLock()){
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			level_list.add(level_builder.getLevelDetails());
			
			
			initializeLevelVariables();
			gameView.drawing(current_level);
			current_level.printBooleanGrid();
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
				
				checkCollisions(); 
				checkPlayerFalling();
				checkItemsFalling();
				checkRemovingObjects();
				checkCollisions();
				checkLevelWin();
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
		}
	}
	


	private void checkRemovingObjects() {
		for(int i=0; i<active_objects.size(); i++){
			Movable_Object obj = active_objects.get(i);
			if(obj.getObj().getName() == EnumConsts.Object_Name.Dynamite && obj.getObj().destroy()){
				if(obj.getObj().getType() == 0){ //Vertical
					int tmp_x = obj.getX();
					int tmp_y = obj.getY();
					current_level.remove(tmp_x, tmp_y, obj.getObj());
					active_objects.remove(i);
					build_dynamite_v(obj, obj.getX(), obj.getY(), i);

				}else if(obj.getObj().getType() == 1){ //Horizontal
					int tmp_x = obj.getX();
					int tmp_y = obj.getY();
					current_level.remove(tmp_x, tmp_y, obj.getObj());
					active_objects.remove(i);
					build_dynamite_h(obj, obj.getX(), obj.getY(), i);

				}else if(obj.getObj().getType() == 2){ //Cross
					int tmp_x = obj.getX();
					int tmp_y = obj.getY();
					current_level.remove(tmp_x, tmp_y, obj.getObj());
					active_objects.remove(i);
					build_dynamite_c(obj, obj.getX(), obj.getY(), i);
					
				}else if(obj.getObj().getType() == 3){ //Circle

					int tmp_x = obj.getX();
					int tmp_y = obj.getY();
					current_level.remove(tmp_x, tmp_y, obj.getObj());
					active_objects.remove(i);
					build_dynamite_o(obj, obj.getX(), obj.getY(), i);
				}
			}else if(obj.getObj().getName() == EnumConsts.Object_Name.Explosion && obj.getObj().destroy()){
				int tmp_x = obj.getX();
				int tmp_y = obj.getY();
				current_level.remove(tmp_x, tmp_y, obj.getObj());
				active_objects.remove(i);
			}
		}
	}
	

	private void checkItemsFalling() {
		for(int i=0; i<active_objects.size(); i++){
			Movable_Object obj = active_objects.get(i);
			int obj_x = obj.getX();
			int obj_y = obj.getY();
			if(!obj.getObj().isFalling() && itemShouldFall(obj_x, obj_y)){
				start_fall(obj_x, obj_y, obj.getObj());	
			}			
		}
	}
	private boolean itemShouldFall(int obj_x, int obj_y) {
		return !(current_level.isGround(obj_x, (obj_y+1)) || obj_y > tiles_in_col-2);
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
	private void checkCollisions() {
		int item_number;
		GameObject item = current_level.isItem(player_x,  player_y);
		if(item != null){
			item_number = item.getItemNumber();
			item_update(item_number, true);
		}
		current_level.checkCollision(player, player_x,player_y);
		for(int i=0; i<active_objects.size(); i++){
			Movable_Object mo = active_objects.get(i);
			current_level.checkCollision(mo.getObj(), mo.getX(), mo.getY());
		}
		
		checkPlayerDeath();
	}
	
	private void checkLevelWin() {
		
		if(current_level.isExit(player_x, player_y)){
			//Win Level!!!
			System.out.println("Level Won");
			level_int++;
			this.restart = true;
		}
		
	}
	private void adjust_positions() {
		//for(objects in grid that can move)
		for(int i=0; i<active_objects.size(); i++){
			GameObject obj = active_objects.get(i).getObj();
			if(obj.halfWayThere()){
				System.out.println("THIS IS REACHED");
				move_object_in_grid(obj, null);
			}
		}
		if(player.halfWayThere()){
			EnumConsts.Direction direction = player.getDirection();
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
			for(int i=0; i<active_objects.size(); i++){
				Movable_Object m_obj = active_objects.get(i);
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
		if(current_level.isDeath(player_x, player_y)){
			//PlayerDeath!!!
			System.out.println("Player Dead");
			this.restart = true;
		}
		
	}
	private void start_fall(int x, int y, GameObject obj) {
		obj.startFalling();
	}
	private boolean shouldFall(int x, int y) {
		return !(current_level.isGround(x, (y+1)) || current_level.isGround(x, (y)) || y > tiles_in_col-2 /* out of bounds */);
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
			System.out.println("player direction: " + player.getDirection());
			//Drop item in direction player is facing.  If player is facing up/down or the spot for the item to land is impassible, drop item on player
			if(player.getDirection() == EnumConsts.Direction.Left){
				if(current_level.isTraversable(player_x-1, player_y)){
					current_level.add(player_x-1, player_y, d);
					Movable_Object mv = new Movable_Object(player_x-1, player_y, d);
					active_objects.add(mv);
					already_dropped = true;
				}else{
					already_dropped = false;
				}
				
			}else if(player.getDirection() == EnumConsts.Direction.Right){
				if(current_level.isTraversable(player_x+1, player_y)){
					current_level.add(player_x+1, player_y, d);
					Movable_Object mv = new Movable_Object(player_x+1, player_y, d);
					active_objects.add(mv);
					already_dropped = true;
				}else{
					already_dropped = false;
				}
			}
			if(!already_dropped){
				current_level.add(player_x, player_y, d);
				Movable_Object mv = new Movable_Object(player_x, player_y, d);
				active_objects.add(mv);
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
		//current_level.printGrid();
		
		System.out.println("Player Explode");
		level_int++;
		this.restart = true;
		
	}
	
	/*
	 * Building Dynamite Explosion Animation
	 */
	private void build_dynamite_v(Movable_Object obj, int x, int y, int i) {
		//FIX BUT WITH GOING OUT OF BOUNDS
		Movable_Object mv;
		
		//Middle sprite
		if(isValid(x,y)){
			mv = new Movable_Object(x, y, new Explosion(2, imgList.getExplosion()));
			current_level.add(x, y, mv.getObj());
			active_objects.add(mv);
		}	
		y = y-1;	
		System.out.println("isValid: " + isValid(x,y));
		if(isValid(x,y)){
			//vertical sprite up one
			mv = new Movable_Object(x, y, new Explosion(1, imgList.getExplosion()));
			current_level.add(x, y, mv.getObj());
			active_objects.add(mv);
		}	
		y = y-1;
		if(isValid(x,y)){
			//vertical sprite up two
			mv = new Movable_Object(x, y, new Explosion(0, imgList.getExplosion()));
			current_level.add(x, y, mv.getObj());
			active_objects.add(mv);
		}
		y = y+3;
		if(isValid(x,y)){
			//vertical sprite down one
			mv = new Movable_Object(x, y, new Explosion(3, imgList.getExplosion()));
			current_level.add(x, y, mv.getObj());
			active_objects.add(mv);
		}	
		y = y+1;
		if(isValid(x,y)){
			//vertical sprite down two
			mv = new Movable_Object(x, y, new Explosion(4, imgList.getExplosion()));
			current_level.add(x, y, mv.getObj());
			active_objects.add(mv);
		}
	}
	
	private void build_dynamite_h(Movable_Object obj, int x, int y, int i) {
		//FIX BUT WITH GOING OUT OF BOUNDS
		Movable_Object mv;
		if(isValid(x,y)){
			//Middle sprite
			mv = new Movable_Object(x, y, new Explosion(7, imgList.getExplosion()));
			current_level.add(x, y, mv.getObj());
			active_objects.add(mv);
		}
		//horizontal sprite up one
		x = x-1;
		if(isValid(x,y)){
			mv = new Movable_Object(x, y, new Explosion(6, imgList.getExplosion()));
			current_level.add(x, y, mv.getObj());
			active_objects.add(mv);
		}
		//horizontal sprite up one
		x = x-1;
		if(isValid(x,y)){
			mv = new Movable_Object(x, y, new Explosion(5, imgList.getExplosion()));
			current_level.add(x, y, mv.getObj());
			active_objects.add(mv);
		}
		//horizontal sprite down one
		x = x+3;
		if(isValid(x,y)){

			mv = new Movable_Object(x, y, new Explosion(8, imgList.getExplosion()));
			current_level.add(x, y, mv.getObj());
			
			active_objects.add(mv);
		}	
		
		//horizontal sprite down two
		x = x+1;
		if(isValid(x,y)){

			mv = new Movable_Object(x, y, new Explosion(9, imgList.getExplosion()));
			current_level.add(x, y, mv.getObj());
			active_objects.add(mv);
		}	
	}	
		
		
	
	private void build_dynamite_c(Movable_Object obj, int x, int y, int i) {
		//FIX BUT WITH GOING OUT OF BOUNDS
		//Middle sprite
		Movable_Object mv;
		if(isValid(x,y)){

			mv = new Movable_Object(x, y, new Explosion(10, imgList.getExplosion()));
			current_level.add(x, y, mv.getObj());
			active_objects.add(mv);
		}	
		//vertical sprite up one
		y = y-1;	
		if(isValid(x,y)){

			mv = new Movable_Object(x, y, new Explosion(1, imgList.getExplosion()));
			current_level.add(x, y, mv.getObj());
			active_objects.add(mv);
		}
		
		//vertical sprite up two
		y = y-1;
		if(isValid(x,y)){
	
			mv = new Movable_Object(x, y, new Explosion(0, imgList.getExplosion()));
			current_level.add(x, y, mv.getObj());
			active_objects.add(mv);
		}	
		
		//vertical sprite down one
		y = y+3;
		if(isValid(x,y)){
	
			mv = new Movable_Object(x, y, new Explosion(3, imgList.getExplosion()));
			current_level.add(x, y, mv.getObj());
			
			active_objects.add(mv);
		}	
		
		//vertical sprite down two
		y = y+1;
		if(isValid(x,y)){

			mv = new Movable_Object(x, y, new Explosion(4, imgList.getExplosion()));
			current_level.add(x, y, mv.getObj());
			active_objects.add(mv);
		}	

		y = y-2;
		
		//horizontal sprite up one
		x = x-1;
		if(isValid(x,y)){

			mv = new Movable_Object(x, y, new Explosion(6, imgList.getExplosion()));
			current_level.add(x, y, mv.getObj());
			active_objects.add(mv);
		}	
		
		//horizontal sprite up one
		x = x-1;
		if(isValid(x,y)){
	
			mv = new Movable_Object(x, y, new Explosion(5, imgList.getExplosion()));
			current_level.add(x, y, mv.getObj());
			active_objects.add(mv);
		}	
		
		//horizontal sprite down one
		x = x+3;
		if(isValid(x,y)){

			mv = new Movable_Object(x, y, new Explosion(8, imgList.getExplosion()));
			current_level.add(x, y, mv.getObj());
			
			active_objects.add(mv);
		}	
		
		//horizontal sprite down two
		x = x+1;
		if(isValid(x,y)){

			mv = new Movable_Object(x, y, new Explosion(9, imgList.getExplosion()));
			current_level.add(x, y, mv.getObj());
			active_objects.add(mv);
		}	
		
	}
	
	private void build_dynamite_o(Movable_Object obj, int x, int y, int i) {
		//FIX BUT WITH GOING OUT OF BOUNDS
		//sprite up one and left one
		Movable_Object mv;
		y=y-1;
		x=x-1;
		if(isValid(x,y)){

			mv = new Movable_Object(x, y, new Explosion(11, imgList.getExplosion()));
			current_level.add(x, y, mv.getObj());
			active_objects.add(mv);
		}	
		//sprite left one
		y = y+1;	
		if(isValid(x,y)){

			mv = new Movable_Object(x, y, new Explosion(12, imgList.getExplosion()));
			current_level.add(x, y, mv.getObj());
			active_objects.add(mv);
		}
		
		//sprite left one down one
		y = y+1;
		if(isValid(x,y)){
	
			mv = new Movable_Object(x, y, new Explosion(13, imgList.getExplosion()));
			current_level.add(x, y, mv.getObj());
			active_objects.add(mv);
		}	
		
		//sprite up one
		x=x+1;
		y = y-2;
		if(isValid(x,y)){
	
			mv = new Movable_Object(x, y, new Explosion(14, imgList.getExplosion()));
			current_level.add(x, y, mv.getObj());
			
			active_objects.add(mv);
		}	
		
		//middle
		y = y+1;
		if(isValid(x,y)){

			mv = new Movable_Object(x, y, new Explosion(15, imgList.getExplosion()));
			current_level.add(x, y, mv.getObj());
			active_objects.add(mv);
		}	
		
		//sprite down one
		y = y+1;
		if(isValid(x,y)){

			mv = new Movable_Object(x, y, new Explosion(16, imgList.getExplosion()));
			current_level.add(x, y, mv.getObj());
			active_objects.add(mv);
		}
		
		y=y-2;
		x=x+1;
		
		//sprite up one right one
		if(isValid(x,y)){

			mv = new Movable_Object(x, y, new Explosion(17, imgList.getExplosion()));
			current_level.add(x, y, mv.getObj());
			active_objects.add(mv);
		}	
		//sprite left one
		y = y+1;	
		if(isValid(x,y)){

			mv = new Movable_Object(x, y, new Explosion(18, imgList.getExplosion()));
			current_level.add(x, y, mv.getObj());
			active_objects.add(mv);
		}
		
		//sprite left one down one
		y = y+1;
		if(isValid(x,y)){
	
			mv = new Movable_Object(x, y, new Explosion(19, imgList.getExplosion()));
			current_level.add(x, y, mv.getObj());
			active_objects.add(mv);
		}	
		
	}
	//End Dynamite Animation Building
	
	public void paintThisFrame(){
		gameView.drawing(current_level);
	}
	
	/*
	 * Player Action Methods
	 */
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


    private boolean isValid(int x, int y){
    	System.out.println("x: " + x + " y: " + y + " tiles_row: " + tiles_in_row + " tiles_col: " + tiles_in_col);
    	if(x < 0 || x > tiles_in_row || y < 0 || y > tiles_in_col){
    		return false;
    	}
    	return true;
    }
    
    /*
     * Initialize variables before the level starts such as player location and number of coins
     */
	private void initializeLevelVariables() {
		active_objects.clear();
		
		this.current_level = level_list.get(level_int).getLevelGrid();
		this.player = level_list.get(level_int).getPlayer();
		this.player_x = level_list.get(level_int).getPlayer_x();
		this.player_y = level_list.get(level_int).getPlayer_y();
		Coin.setCoin_count(level_list.get(level_int).getCoinCount());
		this.tiles_in_col = this.current_level.getTilesInCol();
		this.tiles_in_row = this.current_level.getTilesInRow();
	}
}

	

