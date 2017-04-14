

public class GameProcessing{
	private GameObjectsGrid current_level;
	
	//Player Details
	private int player_x = 0;
	private int player_y = 0;
	private Player player;
	private boolean input_blocked = false;
	
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
		
		this.keyActionManager = gv.getActionManager();
		System.out.println("GameProcessing view of keyactionmanager: " + keyActionManager.getList().toString());
		
		//Setup Graphics
		this.imgList = new ImageList();
		this.imgList.initialize();
		System.out.println(imgList.toString());
		
		//Initialize current level to '1'
		level_int = 1;
		this.current_level = new GameObjectsGrid();
		
		level_builder = new Level_Builder(x_tiles, y_tiles, imgList);
		items = new int[]{0,0,0,0};
	}
	public void run(){
		EnumConsts.Player_Action next_action;
		while(true){
			level_builder.setLevel(level_int);
			new Thread(level_builder).start();
			while(level_builder.getLock()){
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
				
				if(!input_blocked && shouldFall(player_x, player_y)){
					start_fall(player_x, player_y, EnumConsts.Object_Name.Player);	
				}else if(!input_blocked){
					next_action = keyActionManager.getNext();
					if(next_action != null){
						perform_action(next_action);
						next_action = null;
					}
				}
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
			System.out.println("tell me the item isn'tnull");
			item_number = item.getItemNumber();
			item_update(item_number, true);
		}
		current_level.checkPlayerCollisions(player_x,player_y);
		checkPlayerDeath();
	}
	private void adjust_positions() {
		//for(objects in grid that can move)
		if(player.halfWayThere()){
			EnumConsts.Direction direction = player.getDirection();
			//move_from_fall(player_x, player_y, EnumConsts.Object_Name.Player);
			move_object_in_grid(player, EnumConsts.Object_Name.Player, direction);
			//Fix for animation after climbing
		}
	}
	private void move_object_in_grid(Player p, EnumConsts.Object_Name o, EnumConsts.Direction d) {
		switch (d) {
		case Left:
			current_level.movePlayerTo(player_x, player_y, player_x-1, player_y);
			player_x = player_x - 1;
			break;
		case Right:
			current_level.movePlayerTo(player_x, player_y, player_x+1, player_y);
			player_x = player_x + 1;
			break;
		case Up:
			current_level.movePlayerTo(player_x, player_y, player_x, player_y-1);
			player_y = player_y - 1;
			break;
		case Down:
			current_level.movePlayerTo(player_x, player_y, player_x, player_y+1);
			player_y = player_y + 1;
			break;
		default:
			break;
		}
			
		
	}
	private void checkPlayerDeath() {
		if(!current_level.isTraversable(player_x, player_y)){
			//PlayerDeath!!!
			System.out.println("Player Dead");
			this.restart = true;
		}
		
	}
	private void start_fall(int x, int y, EnumConsts.Object_Name p) {
    	player.falling();
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
			default:
				System.out.println("Invalid action");
				break;
					
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
	
	private void Self_Destruct(EnumConsts.Player_Action action) {
		// CURRENTLY PRINTS OUT NEXT LEVEL
		//level_builder.getNextLevel().printGrid();
		
		//item_update(0, true);
		System.out.println("Player Explode");
		
		this.restart = true;
		
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

	

