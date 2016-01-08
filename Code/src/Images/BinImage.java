package Images;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BinImage {

	public void releaseSound(String filepath, String destpath, int Threshold) {
		// 过滤背景色进行黑白二值化处理
		try {
			BufferedImage bi = ImageIO.read(new File(filepath));
			int width = bi.getWidth();
			int height = bi.getHeight();
			BufferedImage bi2 = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_ARGB);
			Raster raster = bi.getRaster();
			WritableRaster wr = bi2.getRaster();
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					int[] a = new int[4];
					raster.getPixel(i, j, a);
					// System.out.println("("+a[0]+", "+a[1]+", "+a[2]+", "+a[3]+")");
					if ((a[0] + a[1] + a[2]) / 3 > Threshold) {
						a[0] = 255;
						a[1] = 255;
						a[2] = 255;
						a[3] = 255;
						wr.setPixel(i, j, a);
					} else {
						a[0] = 0;
						a[1] = 0;
						a[2] = 0;
						a[3] = 255;
						wr.setPixel(i, j, a);
					}
				}

			}
			ImageIO.write(bi2, "PNG", new File(destpath));
			// ImageIO.write(bi, "PNG", new File(destpath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void BatchReleaseSound(String filepath, String destpath,
			int Threshold, boolean Recursive) {
		// get file list where the path has
		File file = new File(filepath);
		// get the folder list
		File[] array = file.listFiles();
		for (int i = 0; i < array.length; i++) {
			if (array[i].isFile()) {
				// only take file name
				//System.out.println("^^^^^" + array[i].getName());
				// take file path and name
				//System.out.println("#####" + array[i]);
				// take file path and name
				//System.out.println("*****" + array[i].getPath());
				new BinImage().releaseSound(array[i].getPath(),destpath+"/"+array[i].getName(),Threshold);
				
			} else if (array[i].isDirectory()) {
				if (Recursive == true) {
					BatchReleaseSound(array[i].getPath(), destpath, Threshold,
							Recursive);
				}
			}
		}
	}

	public static void main(String[] args) {
		 new BinImage().releaseSound("E:\\Code\\code\\S04.png","E:\\Code\\code\\S04_1.png", 150);
		//new BinImage().BatchReleaseSound("E:\\Code\\code\\S\\12306code",
		//		"E:\\Code\\code\\result", 150, false);
		System.out.println("complete;");
	}

}
