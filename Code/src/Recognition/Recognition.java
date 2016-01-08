package Recognition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import Images.Filtering;

public class Recognition {

	private final String BinImagesDir = System.getProperty("user.dir")+"/BinImages";//��Ŵ����ͼƬ��·��

	public String[] doRecognitionCode(String PicFilename) {
		//�����Ŵ����ͼƬ��·�����棬���½�һ��
		try {
			if (!(new File(BinImagesDir).isDirectory())) {
				new File(BinImagesDir).mkdir();
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
		File tempFile =new File( PicFilename.trim());  
        String BinFileName = BinImagesDir+"/"+tempFile.getName();
		//String BinFileName=PicFilename.substring(PicFilename.lastIndexOf("\\")+1);  
        new Filtering().TotalClear(PicFilename, BinFileName);
        //���˱���ɫ���кڰ׶�ֵ������  
        //long startTime=System.currentTimeMillis(); 
        //new BinImage().releaseSound(PicFilename, BinFileName, new Filtering().GetBgGrayValue(PicFilename));
       // long endTime=System.currentTimeMillis(); //��ȡ����ʱ��  
		//System.out.println("����ģ��1����ʱ�䣺 "+(endTime-startTime)+"ms");   
		
      //OCRʶ��    
		//startTime=System.currentTimeMillis(); 
		
        Runtime run = Runtime.getRuntime();         
        try {
			Process pr1 = run.exec("cmd.exe /c d:\\Tools\\Tesseract-OCR\\tesseract "+BinFileName+" "+BinFileName+" -L eng myword -psm 7");
			pr1.waitFor();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        String line;
        //int i = 0;  
        String CodeNum="";
        //ע����������txt����Ҫʱ��ģ����н�����Ҫ�ȴ�ֱ�������ټ���ִ�У�����ͻ��Ҳ����ļ�  
        try {
			FileReader CodeChar = new FileReader(BinFileName+".txt");
			BufferedReader brNum = new BufferedReader(CodeChar);  
	        while ((line = brNum.readLine()) != null)  
	        {  
	        	CodeNum=CodeNum+ line;  
	        }
	        
	        brNum.close();
	        CodeChar.close();
	        
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        
        System.out.println(BinFileName+":OCRʶ������"+CodeNum+";");
        
        //endTime=System.currentTimeMillis(); //��ȡ����ʱ��  
		//System.out.println("����ģ��2����ʱ�䣺 "+(endTime-startTime)+"ms");   
		return null;
	}

	
	public void BatRecognitionCode(String filepath,boolean Recursive) {
		// get file list where the path has
				File file = new File(filepath);
				// get the folder list
				File[] array = file.listFiles();
				for (int i = 0; i < array.length; i++) {
					if (array[i].isFile()) {
						new Recognition().doRecognitionCode(array[i].getPath());  
						
					} else if (array[i].isDirectory()) {
						if (Recursive == true) {
							BatRecognitionCode(array[i].getPath(),Recursive);
						}
					}
				}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long startTime=System.currentTimeMillis(); 
		//new Recognition().doRecognitionCode("E:\\Code\\code\\S04_1_1.png");
		new Recognition().BatRecognitionCode("E:\\Code\\code\\S",false); 
		long endTime=System.currentTimeMillis(); //��ȡ����ʱ��  
		System.out.println("��������ʱ�䣺 "+(endTime-startTime)+"ms");   

	}

}
