
public class Level_Details {

	//Will eventually store information for one level from Level_Builder
	//This way game processing can get this object and parse it instead of making many calls to Level_Builder
	
	private GameObjectsGrid level_grid;
	private Player player;
	private int player_x;
	private int player_y;
	private int coin_count;
	
	public Level_Details(Player p, int x, int y){
		player = p;
		player_x = x;
		player_y = y;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getPlayer_x() {
		return player_x;
	}

	public void setPlayer_x(int player_x) {
		this.player_x = player_x;
	}

	public int getPlayer_y() {
		return player_y;
	}

	public void setPlayer_y(int player_y) {
		this.player_y = player_y;
	}

	public int getCoinCount() {
		return coin_count;
	}

	public void setCoinCount(int coin_count) {
		this.coin_count = coin_count;
	}

	public void setLevelGrid(GameObjectsGrid level_grid) {
		this.level_grid = level_grid;
	}
	
	public GameObjectsGrid getLevelGrid() {
		return level_grid;
		
	}

	public int getTilesInCol() {
		// TODO Auto-generated method stub
		return 0;
	}
}
