import java.awt.image.BufferedImage;


public class Ladder extends GameObject {
	public Ladder(BufferedImage imgList){
		super(imgList, true, true, false, false, false, EnumConsts.Object_Name.Ladder);
	}
}
