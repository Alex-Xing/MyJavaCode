package VMControl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Random;

import CreatNewAndroidSystem.SystemParams;
import DataOperation.DataConnect;

import com.mysql.jdbc.Statement;

public class CreatPropFiles {

	private Properties props = new Properties();
	// private SystemParams MyParams = new SystemParams();
	private DataConnect conn = new DataConnect();

	public void SetConn(DataConnect dataconn) {
		conn = dataconn;
	}

	public void NewConn() {
		conn.GetConn(SystemParams.DatabaseHose, SystemParams.DatabasePort,
				SystemParams.DatabaseName, SystemParams.DatabaseUser,
				SystemParams.DatabasePassword);
	}

	public void LoadAllExampleToFile() {
		try {
			Statement statement = (Statement) conn.GetConn().createStatement();
			String sql = "SELECT * FROM `system_prop`";
			// System.out.println(sql);
			ResultSet rs = statement.executeQuery(sql);
			// System.out.println(rs);

			while (rs.next()) {
				String Brand = rs.getString("Brand");
				CreatPath(SystemParams.ExampleConfigPath + Brand);
				String MODEL = rs.getString("MODEL");
				CreatPath(SystemParams.ExampleConfigPath + Brand + "/" + MODEL);

				String ConfigFile, TmpStr;

				TmpStr = rs.getString("/default.prop");
				if (TmpStr.length() > 0) {
					ConfigFile = SystemParams.ExampleConfigPath + Brand + "/"
							+ MODEL + "/_default.prop";
					CreatConfigFile(ConfigFile, TmpStr);
				}

				TmpStr = rs.getString("/system/build.prop");
				if (TmpStr.length() > 0) {
					ConfigFile = SystemParams.ExampleConfigPath + Brand + "/"
							+ MODEL + "/_system_build.prop";
					CreatConfigFile(ConfigFile, TmpStr);
				}

				TmpStr = rs.getString("/system/default.prop");
				if (TmpStr.length() > 0) {
					ConfigFile = SystemParams.ExampleConfigPath + Brand + "/"
							+ MODEL + "/_system_default.prop";
					CreatConfigFile(ConfigFile, TmpStr);
				}

				TmpStr = rs.getString("/data/local.prop");
				if (TmpStr.length() > 0) {
					ConfigFile = SystemParams.ExampleConfigPath + Brand + "/"
							+ MODEL + "/_data_local.prop";
					CreatConfigFile(ConfigFile, TmpStr);
				}

				TmpStr = rs.getString("/x86.prop");
				if (TmpStr.length() > 0) {
					ConfigFile = SystemParams.ExampleConfigPath + Brand + "/"
							+ MODEL + "/_x86.prop";
					CreatConfigFile(ConfigFile, TmpStr);
				}

				TmpStr = rs.getString("GetTelephonyManagerParma");
				if (TmpStr.length() > 0) {
					ConfigFile = SystemParams.ExampleConfigPath + Brand + "/"
							+ MODEL
							+ "/GetTelephonyManagerParma.class.Properties";
					CreatConfigFile(ConfigFile, TmpStr);
				}
				TmpStr = "getMacAddress=" + GetRamdonMac();
				ConfigFile = SystemParams.ExampleConfigPath + Brand + "/"
						+ MODEL + "/WifiInfo.class.Properties";
				CreatConfigFile(ConfigFile, TmpStr);
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 获取随机MAC地址
	public static String GetRamdonMac() {
		Random rnd1 = new Random();
		String Mac = Integer.toHexString(rnd1.nextInt(16));
		// MAC 地址第二位必需为偶数
		int I;
		int T_Number = 1;
		while ((T_Number % 2) != 0) {
			T_Number = rnd1.nextInt(16);
		}
		Mac = Mac + Integer.toHexString(T_Number) + ":";
		for (I = 0; I < 10; I++) {
			Mac = Mac + Integer.toHexString(rnd1.nextInt(16));
			if ((I % 2) != 0 && I != 9) {
				Mac = Mac + ":";
			}
		}

		// 08:D4:2B:EA:D3:D4
		return Mac;
	}

	public void CreatConfigFile(String Path, String Text) {
		File f = new File(Path);
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				System.out.println(e);
			}
		}

		byte[] bytes = null;
		try {
			bytes = Text.getBytes("UTF-8");// 一定由string得到bytes才能写入到文件里
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		OutputStream os;
		try {
			os = new FileOutputStream(Path);
			os.write(bytes);
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}// 将字节一个个写入文件
	}

	public void CreatPath(String Path) {
		File dir = new File(Path);
		if (dir.exists() && dir.isDirectory()) {
			// 目录存在
			return;
		} else {
			// 不存在
			dir.mkdir();
		}
	}

	/**
	 * 根据key读取value 在工程中取得相对路径的方法
	 * ：this.getClass().getResource("工程路径").getPath();
	 * 
	 * @param filePath
	 * @param key
	 * @return
	 */
	public String readValue(String filePath, String key) {
		Properties props = new Properties();
		InputStreamReader in = null;
		try {
			in = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
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
	public void MergeProperties_system_build_prop(String filePath,
			String New_filePath, String OutputFile) {
		props.clear();
		InputStreamReader in = null;
		try {
			in = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
			props.load(in);
			Enumeration<?> en = props.propertyNames();
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
				// System.out.println(key + " : " + property);
			}
			writeProperties(OutputFile);
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

	public void CreatGetTelephonyManagerParma(String VM_Name,
			String InputFilePath, String OutputFilePath) {
		props.clear();
		InputStreamReader in = null;
		try {
			in = new InputStreamReader(new FileInputStream(InputFilePath),
					"UTF-8");
			props.load(in);
			Enumeration<?> en = props.propertyNames();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				String property = "";
				property = props.getProperty(key);
				if (key.equals("getDeviceId")) {
					property = VM_Name;
				}
				if (key.equals("getSimOperator")) {
					property = GetRandomSimOperator();
				}
				
				if (key.equals("getSimSerialNumber")) {
					property = GetRandomNumberStr(20);
				}
				if (key.equals("getSubscriberId")) {
					property = props.getProperty("getSimOperator")
							+ GetRandomNumberStr(10);
				}

				props.setProperty(key, property);
				// System.out.println(key + " : " + property);
			}
			writeProperties(OutputFilePath);
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

	public void writeProperties(String filePath) {
		// Properties props = new Properties();

		OutputStreamWriter fos = null;
		try {
			fos = new OutputStreamWriter(new FileOutputStream(filePath),
					"UTF-8");
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

	public String GetRandomSimOperator() {
		String[] SimOperator = { "46000", "46001", "46002", "46003", "46007" };
		Random random = new Random();
		int I = random.nextInt(SimOperator.length);
		return SimOperator[I];
	}

	// 随机获取一个指定长度的数字字符串
	public String GetRandomNumberStr(int Len) {
		Random random = new Random();
		int i, j;
		String Tmp = "";
		for (i = 0; i < Len; i++) {
			j = random.nextInt(10);
			Tmp = Tmp + String.valueOf(j).trim();
			// System.out.println(j);
		}
		return Tmp;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		CreatPropFiles test = new CreatPropFiles();

		// test.readProperties("D:/android-x86/default/build.prop","D:/android-x86/samsumg Gt-S6358/_system_build.prop");
		// test.writeProperties("D:/android-x86/test.prop");

		test.NewConn();
		test.LoadAllExampleToFile();

	}

}
