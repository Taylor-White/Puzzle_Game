import java.awt.image.BufferedImage;


public class Indestructable extends GameObject {

	public Indestructable(BufferedImage imgList){
		super(imgList, true, false, false, false, EnumConsts.Object_Name.Indestructable);
	}
}
