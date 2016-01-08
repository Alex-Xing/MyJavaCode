package FindPictureLocation;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class FindPictureLocation {

	/**
	 * @param args
	 */
	public List<int[]> PictureLocation(String File1, String File2) {
		List<int[]> list = new ArrayList<int[]>();
		try {
			BufferedImage bi = ImageIO.read(new File(File1));
			BufferedImage bi2 = ImageIO.read(new File(File2));

			list.clear();
			list = PictureLocation(bi, bi2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.print(list.size());
		return list;
	}

	public List<int[]> PictureLocation(BufferedImage bi, String FilePath) {
		List<int[]> list = new ArrayList<int[]>();
		list.clear();
		try {
			BufferedImage bi2 = ImageIO.read(new File(FilePath));
			list = PictureLocation(bi, bi2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public List<int[]> PictureLocation(BufferedImage bi, BufferedImage bi2) {
		List<int[]> list = new ArrayList<int[]>();
		boolean Tmp = false;
		int width = bi.getWidth();
		int height = bi.getHeight();
		list.clear();
		for (int i = 0; i < (width - bi2.getWidth()); i++) {
			for (int j = 0; j < (height - bi2.getHeight()); j++) {
				Tmp = ComparePicture(bi, bi2, i, j);
				if (Tmp) {
					//System.out.print(Tmp + ":" + i + ":" + j + "\r\n");
					int[] Location = new int[2];
					Location[0] = i;
					Location[1] = j;
					list.add(Location);
				}
			}
		}
		return list;
	}

	private boolean ComparePicture(BufferedImage bi, BufferedImage bi2,
			int offsetX, int offsetY) {
		int width = bi2.getWidth();
		int height = bi2.getHeight();
		Raster raster = bi.getRaster();
		Raster raster2 = bi2.getRaster();
		for (int i = offsetX; i < width + offsetX; i++) {
			for (int j = offsetY; j < height + offsetY; j++) {
				int[] a = new int[4];
				int[] b = new int[4];
				raster.getPixel(i, j, a);
				raster2.getPixel(i - offsetX, j - offsetY, b);
				if (a[0] != b[0] || a[1] != b[1] || a[2] != b[2]
						|| a[3] != b[3]) {
					return false;
				}

			}
		}
		return true;
	}

	public int GetPictureWidth(String FilePath) {

		try {
			BufferedImage bi = ImageIO.read(new File(FilePath));
			return bi.getWidth();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public int GetPictureHeight(String FilePath) {

		try {
			BufferedImage bi = ImageIO.read(new File(FilePath));
			return bi.getHeight();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FindPictureLocation test = new FindPictureLocation();
		test.PictureLocation("e:/test.png", "e:/s2.png");
	}

}
