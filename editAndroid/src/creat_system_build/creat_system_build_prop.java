package creat_system_build;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

public class creat_system_build_prop {

	static Properties props = new Properties();

	/**
	 * 根据key读取value 在工程中取得相对路径的方法
	 * ：this.getClass().getResource("工程路径").getPath();
	 * 
	 * @param filePath
	 * @param key
	 * @return
	 */
	public static String readValue(String filePath, String key) {
		Properties props = new Properties();
		FileInputStream in = null;
		try {
			in = new FileInputStream(filePath);
			props.load(in);
			String value = props.getProperty(key);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 读取properties的全部信息
	 * 
	 * @param filePath
	 *            文件的路径
	 */
	public static void readProperties(String filePath, String New_filePath) {

		InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(filePath));
			props.load(in);
			Enumeration en = props.propertyNames();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				String property = "";
				if (key.equals("ro.build.version.sdk")
						|| key.equals("ro.build.version.release")) {
					property = props.getProperty(key);
				} else {
					property = readValue(New_filePath, key);
					if (property == null) {
						property = props.getProperty(key);
					}

				}
				props.setProperty(key, property);
				System.out.println(key + " : " + property);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void writeProperties(String filePath) {
		// Properties props = new Properties();
		OutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);
			// props.setProperty(parameterName, parameterValue);
			// 以适合使用load方法加载到Properties表中的格式，将此Properties表中的属性列表（键和元素对）写入输出流

			// 加载额外的内容
			// String otherContent="";
			props.store(fos, "");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		readProperties("D:/android-x86/default/build.prop",
				"D:/android-x86/samsumg Gt-S6358/_system_build.prop");
		writeProperties("D:/android-x86/test.prop");
	}

}
