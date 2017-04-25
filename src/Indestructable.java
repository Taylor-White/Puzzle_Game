import java.awt.image.BufferedImage;


public class Indestructable extends GameObject {

	private int graphics_style_x;
	
	public Indestructable(BufferedImage imgList, int style){
		super(imgList, true, false, false, false, false, EnumConsts.Object_Name.Indestructable);
		
		//If style number is out of bounds, change style type to default
		if(imgList.getWidth() < style*TILE_SIZE){
			this.graphics_style_x = 0;
		}else{
			this.graphics_style_x = style;
		}	
	}
	public BufferedImage getCurrentImage(){

		return image_frames.getSubimage(graphics_style_x * TILE_SIZE, 0 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
	}
}
