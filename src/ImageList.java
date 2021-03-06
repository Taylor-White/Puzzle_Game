import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

//holds images for animations

public class ImageList {

	private static BufferedImage block;
	private static BufferedImage indestructible;
	private static BufferedImage coin;
	private static BufferedImage player;
	private static BufferedImage ladder;
	private static BufferedImage exit;
	private static BufferedImage dynamite;
	private static BufferedImage explosion;
	
	public ImageList(){
		
	}
	
	public void initialize(){
		BufferedImage block_img = null;
		BufferedImage coin_img = null;
		BufferedImage player_img = null;
		BufferedImage indestructible_img = null;
		BufferedImage ladder_img = null;
		BufferedImage exit_img = null;
		BufferedImage dynamite_img = null;
		BufferedImage explosion_img = null;

		
		//Initialize Block Image
		try {
			block_img = ImageIO.read(new File("./resources/sprites/blocks/block_sprite.png"));
	      } catch(IOException e) {
	         System.out.println("Error: " + e);
	      }
		ImageList.block = block_img;
		
		//Initialize Indestructible Image
		try {
			indestructible_img = ImageIO.read(new File("./resources/sprites/indestructible.png"));
	      } catch(IOException e) {
	         System.out.println("Error: " + e);
	      }
		ImageList.indestructible = indestructible_img;
		
		//Initialize Coin Image
		try {
			coin_img = ImageIO.read(new File("./resources/sprites/coin.png"));
	      } catch(IOException e) {
	         System.out.println("Error: " + e);
	      }
		ImageList.coin = coin_img;
		
		//Initialize Player Image
		try {
			player_img = ImageIO.read(new File("./resources/sprites/player.png"));
	      } catch(IOException e) {
	         System.out.println("Error: " + e);
	      }
		ImageList.player = player_img;
		
		//Initialize Ladder Image
		try {
			ladder_img = ImageIO.read(new File("./resources/sprites/ladder.png"));
	      } catch(IOException e) {
	         System.out.println("Error: " + e);
	      }
		ImageList.ladder = ladder_img;
		
		//Initialize Dynamite Image
		try {
			dynamite_img = ImageIO.read(new File("./resources/sprites/dynamite.png"));
	      } catch(IOException e) {
	         System.out.println("Error: " + e);
	      }
		ImageList.dynamite = dynamite_img;
		
		//Initialize Exit Image
			try {
				exit_img = ImageIO.read(new File("./resources/sprites/exit.png"));
		      } catch(IOException e) {
		         System.out.println("Error: " + e);
		      }
			ImageList.exit = exit_img;
				
		//Initialize Explosion Image
		try {
			explosion_img = ImageIO.read(new File("./resources/sprites/explosions.png"));
	      } catch(IOException e) {
	         System.out.println("Error: " + e);
	      }
		ImageList.explosion = explosion_img;		
		return;
	}
	
	public BufferedImage getBlock(){
		return block;
	}
	public BufferedImage getIndestructible(){
		return indestructible;
	}
	public BufferedImage getCoin(){
		return coin;
	}
	public BufferedImage getPlayer(){
		return player;
	}
	public BufferedImage getLadder() {
		return ladder;
	}
	public BufferedImage getDynamite() {
		return dynamite;
	}
	
	public BufferedImage getExplosion() {
		return explosion;
	}

	public BufferedImage getExit() {
		// TODO Auto-generated method stub
		return exit;
	}



}
