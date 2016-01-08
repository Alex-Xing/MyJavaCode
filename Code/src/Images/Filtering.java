package Images;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Filtering {
	
	public void BatTotalClear(String filepath, String destpath,boolean Recursive) {
		// get file list where the path has
				File file = new File(filepath);
				// get the folder list
				File[] array = file.listFiles();
				for (int i = 0; i < array.length; i++) {
					if (array[i].isFile()) {
						new Filtering().TotalClear(array[i].getPath(),destpath+"\\"+array[i].getName());  
						
					} else if (array[i].isDirectory()) {
						if (Recursive == true) {
							BatTotalClear(array[i].getPath(),destpath,Recursive);
						}
					}
				}
	}
	
	public void TotalClear(String filepath, String destpath) {
		File tempFile = new File(filepath.trim());
		String Out1 = tempFile.getParent() + "/T_" + tempFile.getName();
		String Out2=tempFile.getParent()+"/T2_"+ tempFile.getName();
		new Filtering().ClearBG(filepath, Out1,
				new Filtering().GetBgGrayValue(filepath));
		//new Filtering().ClearPoint_XY(Out1,Out2,3);
		new Filtering().ClearColorPoint2(Out1, Out2, 0.1);
		new BinImage().releaseSound(Out2, destpath, 150);

	}

	// 中值滤波去噪
	public void ClearPoint_XY(String filepath, String destpath, int XY) {
		try {
			BufferedImage bi = ImageIO.read(new File(filepath));
			int width = bi.getWidth();
			int height = bi.getHeight();
			BufferedImage bi2 = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_ARGB);
			Raster raster = bi.getRaster();
			WritableRaster wr = bi2.getRaster();
			int X, Y, J, K, M, N;

			for (J = XY / 2; J < width - XY / 2; J++) {
				for (K = XY / 2; K < height - XY / 2; K++) {
					int[] a = new int[4];
					int[][] TempPoint = new int[XY * XY][4];
					int I = 0;
					for (M = J - XY / 2; M < J + XY / 2 + 1; M++) {
						for (N = K - XY / 2; N < K + XY / 2 + 1; N++) {
							raster.getPixel(M, N, a);
							TempPoint[I] = a.clone();
							I++;
						}
					}

					int[] TP = new int[4];
					for (X = 0; X < (XY * XY / 2 + 1); X++) {
						for (Y = X + 1; Y < (XY * XY); Y++) {
							if (TempPoint[X][1] > TempPoint[Y][1]) {
								TP = TempPoint[X];
								TempPoint[X] = TempPoint[Y];
								TempPoint[Y] = TP;
							}
						}
					}
					wr.setPixel(J, K, TempPoint[4]);
				}
			}

			ImageIO.write(bi2, "PNG", new File(destpath));
			// ImageIO.write(bi, "PNG", new File(destpath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 去色噪
	public void ClearColorPoint(String filepath, String destpath, double Arg) {
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
				for (int j = 0; j < height / 2; j++) {
					int[] a = new int[4];
					raster.getPixel(i, j, a);
					wr.setPixel(i, j, a);
				}
			}
			for (int i = 0; i < width; i++) {
				for (int j = height / 2; j < height; j++) {

					int[] a = new int[4];
					raster.getPixel(i, j, a); 
					boolean clearB = true;

					for (int k = 0; k < width; k++) {
						for (int l = 0; l < height / 2; l++) {
							int[] b = new int[4];
							raster.getPixel(k, l, b);
							// if(a[0]==b[0] && a[1]==b[1] && a[2]==b[2]
							// &&a[3]==b[3]){
							if ((a[0] >= b[0] * (1 - Arg) && a[0] <= b[0]
									* (1 + Arg))
									&& (a[1] >= b[1] * (1 - Arg) && a[1] <= b[1]
											* (1 + Arg))
									&& (a[2] >= b[2] * (1 - Arg) && a[2] <= b[2]
											* (1 + Arg))
									&& (a[3] >= b[3] * (1 - Arg) && a[3] <= b[3]
											* (1 + Arg))) {

								clearB = false;
								break;
							}
							if (clearB == false) {
								break;
							}
						}
					}
					if (clearB == false) {
						wr.setPixel(i, j, a);
					} else {
						a[0] = a[1] = a[2] = a[3] = 255;
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

	// 去色噪
	public void ClearColorPoint2(String filepath, String destpath, double Arg) {
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
				for (int j = 0; j < height / 2; j++) {
					int[] a = new int[4];
					raster.getPixel(i, j, a);
					wr.setPixel(i, j, a);
				}
			}
			for (int i = 0; i < width; i++) {
				for (int j = height / 2; j < height; j++) {

					int[] a = new int[4];
					raster.getPixel(i, j, a);
					boolean clearB = true;
					int Star=0,End=width;
					if (i<=2){
						Star=0;
					}else
					{
						Star=i-2;
					}
					if ((Star+3)>width){
						End=width;
					}else
					{
						End=Star+3;
					}
					for (int k = Star; k < End; k++) {

						for (int l = 0; l < height / 2; l++) {
							int[] b = new int[4];
							raster.getPixel(k, l, b);
							/*
							 if(a[0]==b[0] && a[1]==b[1] && a[2]==b[2]
							 &&a[3]==b[3]){
							*/
							if ((a[0] >= b[0] * (1 - Arg) && a[0] <= b[0]
									* (1 + Arg))
									&& (a[1] >= b[1] * (1 - Arg) && a[1] <= b[1]
											* (1 + Arg))
									&& (a[2] >= b[2] * (1 - Arg) && a[2] <= b[2]
											* (1 + Arg))
									&& (a[3] >= b[3] * (1 - Arg) && a[3] <= b[3]
											* (1 + Arg))) {
								
								clearB = false;
								break;
							}
							if (clearB == false) {
								break;
							}
						}
					}
					if (clearB == false) {
						wr.setPixel(i, j, a);
					} else {
						a[0] = a[1] = a[2] = a[3] = 255;
						wr.setPixel(i, j, a);
					}

				}
			}
			ImageIO.write(bi2, "PNG", new File(destpath));
			//ImageIO.write(bi2, "PNG", new File(destpath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 背景阀值
	public int GetBgGrayValue(String filepath) {
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(new File(filepath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (bi == null) {
			return 0;
		}

		int width = bi.getWidth();
		int height = bi.getHeight();
		Raster raster = bi.getRaster();

		int[] pixelNum = new int[256]; // 图象直方图，共256个点
		int n, n1, n2;
		int total; // total为总和，累计值
		double m1, m2, sum, csum, fmax, sb; // sb为类间方差，fmax存储最大方差值
		int k, t, q;
		int threshValue = 1; // 阈值
		int step = 1;
		// 生成直方图
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int[] a = new int[4];
				raster.getPixel(i, j, a);
				// 返回各个点的颜色，以RGB表示
				pixelNum[a[0]]++; // 相应的直方图加1
			}
		}
		// 直方图平滑化
		for (k = 0; k <= 255; k++) {
			total = 0;
			for (t = -2; t <= 2; t++) // 与附近2个灰度做平滑化，t值应取较小的值
			{
				q = k + t;
				if (q < 0) // 越界处理
					q = 0;
				if (q > 255)
					q = 255;
				total = total + pixelNum[q]; // total为总和，累计值
			}
			pixelNum[k] = (int) ((float) total / 5.0 + 0.5); // 平滑化，左边2个+中间1个+右边2个灰度，共5个，所以总和除以5，后面加0.5是用修正值
		}
		// 求阈值
		sum = csum = 0.0;
		n = 0;
		// 计算总的图象的点数和质量矩，为后面的计算做准备
		for (k = 0; k <= 255; k++) {
			sum += (double) k * (double) pixelNum[k]; // x*f(x)质量矩，也就是每个灰度的值乘以其点数（归一化后为概率），sum为其总和
			n += pixelNum[k]; // n为图象总的点数，归一化后就是累积概率
		}

		fmax = -1.0; // 类间方差sb不可能为负，所以fmax初始值为-1不影响计算的进行
		n1 = 0;
		for (k = 0; k < 256; k++) // 对每个灰度（从0到255）计算一次分割后的类间方差sb
		{
			n1 += pixelNum[k]; // n1为在当前阈值遍前景图象的点数
			if (n1 == 0) {
				continue;
			} // 没有分出前景后景
			n2 = n - n1; // n2为背景图象的点数
			if (n2 == 0) {
				break;
			} // n2为0表示全部都是后景图象，与n1=0情况类似，之后的遍历不可能使前景点数增加，所以此时可以退出循环
			csum += (double) k * pixelNum[k]; // 前景的“灰度的值*其点数”的总和
			m1 = csum / n1; // m1为前景的平均灰度
			m2 = (sum - csum) / n2; // m2为背景的平均灰度
			sb = (double) n1 * (double) n2 * (m1 - m2) * (m1 - m2); // sb为类间方差
			if (sb > fmax) // 如果算出的类间方差大于前一次算出的类间方差
			{
				fmax = sb; // fmax始终为最大类间方差（otsu）
				threshValue = k; // 取最大类间方差时对应的灰度的k就是最佳阈值
			}
		}
		return threshValue;
	}

	public void ClearBG(String filepath, String destpath, int Threshold) {
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		 * String SFilename="E:\\Code\\code\\S02.png"; String
		 * Out1="E:\\Code\\code\\S02_1.png"; String
		 * Out2="E:\\Code\\code\\S02_1_1.png"; new
		 * Filtering().ClearBG(SFilename,Out1,new
		 * Filtering().GetBgGrayValue(SFilename)); new
		 * Filtering().ClearColorPoint(Out1,Out2);
		 */
		//new Filtering().TotalClear("E:\\Code\\code\\S05.png","E:\\Code\\code\\S05_1.png");
		new Filtering().BatTotalClear("E:\\Code\\code\\S\\12306code", "E:\\Code\\code\\S", false);
		 System.out.println("complete");
		// Filtering().GetBgGrayValue("E:\\Code\\code\\S04.png"));
	}

}
