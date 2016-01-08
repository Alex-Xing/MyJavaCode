package FindPictureLocation;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class captureDesktop {

	/**
	 * @param args
	 */
	public BufferedImage LoadDesktop(){
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle rect = new Rectangle(d);
        BufferedImage desktopImg = new BufferedImage((int)d.getWidth(),(int)d.getHeight(),BufferedImage.TYPE_4BYTE_ABGR);
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = environment.getDefaultScreenDevice();
        Robot robot;
		try {
			robot = new Robot(device);
			desktopImg = robot.createScreenCapture(rect);
			
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return desktopImg;
	}
	
	public void SaveDesktopToPNG(String FilePath){
		try {
			ImageIO.write(LoadDesktop(), "PNG", new File(FilePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		captureDesktop test=new captureDesktop();
		test.SaveDesktopToPNG("e:/test.png");	        
	}

}
