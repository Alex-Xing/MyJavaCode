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

		// ������еĲ����������ļ�
		CreatPropFiles OutputConfigFile = new CreatPropFiles();
		OutputConfigFile.NewConn();
		OutputConfigFile.LoadAllExampleToFile();

		ADBRobot ADBControl = new ADBRobot();
		ADBControl.InitADB(VMIP);
		WaitTime(20000);// �ȴ�12�룬�Ա�ȷ��ADB����
		ADBControl.ConnectVM(VMIP);
		// ͬ��_system_build.prop�ļ�
		String BaseCFile = SystemParams.DefaultConfigPath
				+ GetAndroidVersion(Base_VM_Name) + "/_system_build.prop";
		String ExampCFile = SystemParams.ExampleConfigPath
				+ GetExamplePath(Base_VM_Name) + "_system_build.prop";
		String CnfOutputFile = GetRandomTempFilePath(SystemParams.TempPath);
		OutputConfigFile.MergeProperties_system_build_prop(BaseCFile,
				ExampCFile, CnfOutputFile);
		ADBControl.pushFile(CnfOutputFile, SystemParams.VM_system_build_prop);
		DelTempFile(CnfOutputFile);

		// ͬ��GetTelephonyManagerParma.class.Properties
		ExampCFile = SystemParams.ExampleConfigPath
				+ GetExamplePath(Base_VM_Name)
				+ "GetTelephonyManagerParma.class.Properties";
		CnfOutputFile = GetRandomTempFilePath(SystemParams.TempPath);
		OutputConfigFile.CreatGetTelephonyManagerParma(VM_Name, ExampCFile,
				CnfOutputFile);
		ADBControl.pushFile(CnfOutputFile,
				SystemParams.VM_GetTelephonyManagerParma_class_Properties);
		DelTempFile(CnfOutputFile);

		// ͬ��WifiInfo.class.Properties
		ExampCFile = SystemParams.ExampleConfigPath
				+ GetExamplePath(Base_VM_Name) + "WifiInfo.class.Properties";
		ADBControl.pushFile(ExampCFile,
				SystemParams.VM_GetWifiInfo_class_Properties);

		// ͬ��Romer ROM����ͳ�Ƴ���AppStats.apk
//		ExampCFile = SystemParams.LibPath + "RomerLib/AppStats.apk";
//		ADBControl.pushFile(ExampCFile, SystemParams.VM_AppStatsPath);
//		ExampCFile = SystemParams.LibPath + "RomerLib/libLeMeng.so";
//		ADBControl.pushFile(ExampCFile, SystemParams.VM_AppStatsLibPath);

		// ͬ��ROM ROM����ͳ�Ƴ���AppStats.apk
		ExampCFile = SystemParams.LibPath + "ROMLib/yueapp_1.1.apk";
		ADBControl.pushFile(ExampCFile, SystemParams.VM_AppStatsPath);

		switch (GetAndroidVersion(Base_VM_Name)) {
		case "4.3.1":
			// ͬ��ARM Lib
			ExampCFile = SystemParams.LibPath + "system.zip";
			ADBControl.pushFile(ExampCFile, SystemParams.VM_TempPath);
			// ADBControl.Shell("tar -xvzf /sdcard/system.tar.gz -C /");//��ѹ�ļ���
			ADBControl.Shell("yes |unzip /sdcard/system.zip -d /");// ��ѹ�ļ���
			break;
		}
		WaitTime(10000);// �ȴ�10�룬�Ա�VM�����ļ�������VM�п�������ʧ�ܻ��ļ���ʧ
		ADBControl.KillServer();// �������÷��񣬷���ϵͳ�����޷������˳�
		System.out.println("Run "+RobotName+" Complete");
		return true;
	}

	// ����Base_VM_Name��ȡ��ӦAndroid���������ļ��Ĵ��·��
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

	// ����Base_VM_Name��ȡ��Ӧ���������ļ��Ĵ��·��
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

	// ��ȡ�����ʱ�ļ�·��
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

	// ɾ����ʱ�ļ�
	public boolean DelTempFile(String BasePath) {
		boolean flag = false;
		File file = new File(BasePath);
		// ·��Ϊ�ļ��Ҳ�Ϊ�������ɾ��
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
