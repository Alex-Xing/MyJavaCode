package RunAppRobot;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import ADBControl.ADBRobot;
import CreatNewAndroidSystem.SystemParams;
import VMControl.CreatPropFiles;

import com.mysql.jdbc.Statement;

public class SynchronizeFiles extends BaseRobot {
	public SynchronizeFiles() {
		RobotName = "SynchronizeFiles";
		NewConn();
	}

	public Boolean Run(String IP, String B_VM_Name, String Name) {
		String VMIP = IP;
		String Base_VM_Name = B_VM_Name;
		String VM_Name = Name;

		// 输出所有的参数样本到文件
		CreatPropFiles OutputConfigFile = new CreatPropFiles();
		OutputConfigFile.NewConn();
		OutputConfigFile.LoadAllExampleToFile();

		ADBRobot ADBControl = new ADBRobot();
		ADBControl.InitADB(VMIP);
		WaitTime(20000);// 等待12秒，以便确保ADB正常
		ADBControl.ConnectVM(VMIP);
		// 同步_system_build.prop文件
		String BaseCFile = SystemParams.DefaultConfigPath
				+ GetAndroidVersion(Base_VM_Name) + "/_system_build.prop";
		String ExampCFile = SystemParams.ExampleConfigPath
				+ GetExamplePath(Base_VM_Name) + "_system_build.prop";
		String CnfOutputFile = GetRandomTempFilePath(SystemParams.TempPath);
		OutputConfigFile.MergeProperties_system_build_prop(BaseCFile,
				ExampCFile, CnfOutputFile);
		ADBControl.pushFile(CnfOutputFile, SystemParams.VM_system_build_prop);
		DelTempFile(CnfOutputFile);

		// 同步GetTelephonyManagerParma.class.Properties
		ExampCFile = SystemParams.ExampleConfigPath
				+ GetExamplePath(Base_VM_Name)
				+ "GetTelephonyManagerParma.class.Properties";
		CnfOutputFile = GetRandomTempFilePath(SystemParams.TempPath);
		OutputConfigFile.CreatGetTelephonyManagerParma(VM_Name, ExampCFile,
				CnfOutputFile);
		ADBControl.pushFile(CnfOutputFile,
				SystemParams.VM_GetTelephonyManagerParma_class_Properties);
		DelTempFile(CnfOutputFile);

		// 同步WifiInfo.class.Properties
		ExampCFile = SystemParams.ExampleConfigPath
				+ GetExamplePath(Base_VM_Name) + "WifiInfo.class.Properties";
		ADBControl.pushFile(ExampCFile,
				SystemParams.VM_GetWifiInfo_class_Properties);

		// 同步Romer ROM内置统计程序AppStats.apk
//		ExampCFile = SystemParams.LibPath + "RomerLib/AppStats.apk";
//		ADBControl.pushFile(ExampCFile, SystemParams.VM_AppStatsPath);
//		ExampCFile = SystemParams.LibPath + "RomerLib/libLeMeng.so";
//		ADBControl.pushFile(ExampCFile, SystemParams.VM_AppStatsLibPath);

		// 同步ROM ROM内置统计程序AppStats.apk
		ExampCFile = SystemParams.LibPath + "ROMLib/yueapp_1.1.apk";
		ADBControl.pushFile(ExampCFile, SystemParams.VM_AppStatsPath);

		switch (GetAndroidVersion(Base_VM_Name)) {
		case "4.3.1":
			// 同步ARM Lib
			ExampCFile = SystemParams.LibPath + "system.zip";
			ADBControl.pushFile(ExampCFile, SystemParams.VM_TempPath);
			// ADBControl.Shell("tar -xvzf /sdcard/system.tar.gz -C /");//解压文件包
			ADBControl.Shell("yes |unzip /sdcard/system.zip -d /");// 解压文件包
			break;
		}
		WaitTime(10000);// 等待10秒，以便VM保存文件，否则VM有可能重启失败或文件丢失
		ADBControl.KillServer();// 必需重置服务，否则系统可能无法正常退出
		System.out.println("Run "+RobotName+" Complete");
		return true;
	}

	// 根据Base_VM_Name获取对应Android参数样本文件的存放路径
	public String GetAndroidVersion(String Base_VM_Name) {
		String Path = "";
		try {
			Statement statement = (Statement) conn.GetConn().createStatement();
			String sql = "SELECT * FROM `system_prop` where `Signature`=\""
					+ Base_VM_Name.trim() + "\"";
			// System.out.println(sql);
			ResultSet rs = statement.executeQuery(sql);
			// System.out.println(rs);

			while (rs.next()) {
				Path = rs.getString("AndroidVersion");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Path;
	}

	// 根据Base_VM_Name获取对应参数样本文件的存放路径
	public String GetExamplePath(String Base_VM_Name) {
		String Path = "";
		try {
			Statement statement = (Statement) conn.GetConn().createStatement();
			String sql = "SELECT * FROM `system_prop` where `Signature`=\""
					+ Base_VM_Name.trim() + "\"";
			// System.out.println(sql);
			ResultSet rs = statement.executeQuery(sql);
			// System.out.println(rs);

			while (rs.next()) {
				Path = rs.getString("Brand") + "/" + rs.getString("MODEL")
						+ "/";
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Path;
	}

	// 获取随机临时文件路径
	public String GetRandomTempFilePath(String BasePath) {
		Random rnd1 = new Random();
		String Path = BasePath;
		while (true) {
			Path = Path + "/" + String.valueOf(rnd1.nextInt());
			File f = new File(Path);
			if (!f.exists()) {
				return Path;
			}
		}
	}

	// 删除临时文件
	public boolean DelTempFile(String BasePath) {
		boolean flag = false;
		File file = new File(BasePath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
