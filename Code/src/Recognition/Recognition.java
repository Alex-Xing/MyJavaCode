package Recognition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import Images.Filtering;

public class Recognition {

	private final String BinImagesDir = System.getProperty("user.dir")+"/BinImages";//存放处理后图片的路径

	public String[] doRecognitionCode(String PicFilename) {
		//如果存放处理后图片的路径不存，则新建一个
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
        //过滤背景色进行黑白二值化处理  
        //long startTime=System.currentTimeMillis(); 
        //new BinImage().releaseSound(PicFilename, BinFileName, new Filtering().GetBgGrayValue(PicFilename));
       // long endTime=System.currentTimeMillis(); //获取结束时间  
		//System.out.println("程序模块1运行时间： "+(endTime-startTime)+"ms");   
		
      //OCR识别    
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
        //注意这里生成txt是需要时间的，所有进程需要等待直到返回再继续执行，否则就会找不到文件  
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
        
        System.out.println(BinFileName+":OCR识别结果："+CodeNum+";");
        
        //endTime=System.currentTimeMillis(); //获取结束时间  
		//System.out.println("程序模块2运行时间： "+(endTime-startTime)+"ms");   
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
		long endTime=System.currentTimeMillis(); //获取结束时间  
		System.out.println("程序运行时间： "+(endTime-startTime)+"ms");   

	}

}
