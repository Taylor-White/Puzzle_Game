import java.awt.image.BufferedImage;


public class Empty extends GameObject  {
	public Empty(BufferedImage imgList){
		super(imgList, false, true, false, false, false,EnumConsts.Object_Name.Empty);
	}
}