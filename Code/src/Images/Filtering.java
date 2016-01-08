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

	// ��ֵ�˲�ȥ��
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

	// ȥɫ��
	public void ClearColorPoint(String filepath, String destpath, double Arg) {
		// ���˱���ɫ���кڰ׶�ֵ������
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

	// ȥɫ��
	public void ClearColorPoint2(String filepath, String destpath, double Arg) {
		// ���˱���ɫ���кڰ׶�ֵ������
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

	// ������ֵ
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

		int[] pixelNum = new int[256]; // ͼ��ֱ��ͼ����256����
		int n, n1, n2;
		int total; // totalΪ�ܺͣ��ۼ�ֵ
		double m1, m2, sum, csum, fmax, sb; // sbΪ��䷽�fmax�洢��󷽲�ֵ
		int k, t, q;
		int threshValue = 1; // ��ֵ
		int step = 1;
		// ����ֱ��ͼ
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int[] a = new int[4];
				raster.getPixel(i, j, a);
				// ���ظ��������ɫ����RGB��ʾ
				pixelNum[a[0]]++; // ��Ӧ��ֱ��ͼ��1
			}
		}
		// ֱ��ͼƽ����
		for (k = 0; k <= 255; k++) {
			total = 0;
			for (t = -2; t <= 2; t++) // �븽��2���Ҷ���ƽ������tֵӦȡ��С��ֵ
			{
				q = k + t;
				if (q < 0) // Խ�紦��
					q = 0;
				if (q > 255)
					q = 255;
				total = total + pixelNum[q]; // totalΪ�ܺͣ��ۼ�ֵ
			}
			pixelNum[k] = (int) ((float) total / 5.0 + 0.5); // ƽ���������2��+�м�1��+�ұ�2���Ҷȣ���5���������ܺͳ���5�������0.5��������ֵ
		}
		// ����ֵ
		sum = csum = 0.0;
		n = 0;
		// �����ܵ�ͼ��ĵ����������أ�Ϊ����ļ�����׼��
		for (k = 0; k <= 255; k++) {
			sum += (double) k * (double) pixelNum[k]; // x*f(x)�����أ�Ҳ����ÿ���Ҷȵ�ֵ�������������һ����Ϊ���ʣ���sumΪ���ܺ�
			n += pixelNum[k]; // nΪͼ���ܵĵ�������һ��������ۻ�����
		}

		fmax = -1.0; // ��䷽��sb������Ϊ��������fmax��ʼֵΪ-1��Ӱ�����Ľ���
		n1 = 0;
		for (k = 0; k < 256; k++) // ��ÿ���Ҷȣ���0��255������һ�ηָ�����䷽��sb
		{
			n1 += pixelNum[k]; // n1Ϊ�ڵ�ǰ��ֵ��ǰ��ͼ��ĵ���
			if (n1 == 0) {
				continue;
			} // û�зֳ�ǰ����
			n2 = n - n1; // n2Ϊ����ͼ��ĵ���
			if (n2 == 0) {
				break;
			} // n2Ϊ0��ʾȫ�����Ǻ�ͼ����n1=0������ƣ�֮��ı���������ʹǰ���������ӣ����Դ�ʱ�����˳�ѭ��
			csum += (double) k * pixelNum[k]; // ǰ���ġ��Ҷȵ�ֵ*����������ܺ�
			m1 = csum / n1; // m1Ϊǰ����ƽ���Ҷ�
			m2 = (sum - csum) / n2; // m2Ϊ������ƽ���Ҷ�
			sb = (double) n1 * (double) n2 * (m1 - m2) * (m1 - m2); // sbΪ��䷽��
			if (sb > fmax) // ����������䷽�����ǰһ���������䷽��
			{
				fmax = sb; // fmaxʼ��Ϊ�����䷽�otsu��
				threshValue = k; // ȡ�����䷽��ʱ��Ӧ�ĻҶȵ�k���������ֵ
			}
		}
		return threshValue;
	}

	public void ClearBG(String filepath, String destpath, int Threshold) {
		// ���˱���ɫ���кڰ׶�ֵ������
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
