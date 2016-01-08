package CreatNewAndroidSystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import DataOperation.DataConnect;
import RunAppRobot.GetAndroidIP;
import RunAppRobot.InstallAppRobot;
import RunAppRobot.SetResolutionByStartUp;
import RunAppRobot.SynchronizeFiles;
import VMControl.ControlRobot;

import com.mysql.jdbc.Statement;

public class CreatAndroidVM {
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

	public void CreatNewVM() {
		String VM_Name = "";
		String Base_VM_Name = "";

		// String App_List = "";
		// String Creat_Time = "";
		try {
			Statement statement = (Statement) conn.GetConn().createStatement();

			String sql = "";
			// 更新是否已创建VM的标志
			sql = "UPDATE `new_vm_system_info` SET `Created_Flag` = true WHERE vm_name in(Select VM_name from vm_system_info)";
			// System.out.println(sql);
			statement.executeUpdate(sql);
			// 除掉重复项VM信息
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String NowDate = df.format(new Date());
			sql = "Delete FROM `new_vm_system_info` where LOCATE(\'"
					+ NowDate.trim()
					+ "\', Creat_Time)<1 and `Created_Flag` = true";
			statement.executeUpdate(sql);

			// 除掉重复项VM信息
			sql = "SELECT * FROM `new_vm_system_info` where `Created_Flag`=false and `vm_name` Not In(Select VM_name from vm_system_info)";
			// System.out.println(sql);
			ResultSet rs = statement.executeQuery(sql);
			// System.out.println(rs);
			Statement statement2 = (Statement) conn.GetConn().createStatement();
			while (rs.next()) {
				VM_Name = rs.getString("VM_Name");
				Base_VM_Name = rs.getString("Base_VM_Name");
				// App_List = rs.getString("App_List");
				CreatNewVMObj(VM_Name, Base_VM_Name);// , App_List);

				String sql2 = "UPDATE `new_vm_system_info` SET `Created_Flag` = true WHERE vm_name =\""
						+ VM_Name.trim() + "\"";
				// System.out.println(sql);
				statement2.executeUpdate(sql2);
				sql2 = "INSERT INTO `vm_system_info` ";
				sql2 = sql2
						+ "(`VM_Name`, `Base_VM_Name`, `App_List`, `Creat_Time`, `LastOperationTime`, `Status`) ";
				sql2 = sql2
						+ "select `VM_Name`, `Base_VM_Name`, `App_List`, `Creat_Time`,NULL, \"Waiting\" as `Status` ";
				sql2 = sql2 + "from `new_vm_system_info` ";
				sql2 = sql2 + "WHERE vm_name =\"" + VM_Name.trim() + "\"";
				// System.out.println(sql);
				statement2.executeUpdate(sql2);
			}
			rs.close();
			statement.close();
			statement2.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void CreatNewVMObj(String VM_Name, String Base_VM_Name) {
		// ,String App_List) {
		ControlRobot NewControlRobot = new ControlRobot();
		// Clone新的VM
		if (NewControlRobot.InitVMS(SourceAndridVersion(Base_VM_Name), VM_Name)) {

			// 设置定VM的分辨率为参数样本中的分辨率
			NewControlRobot.SetCustomVideoMode(VM_Name,
					GetResolution(Base_VM_Name));

			// 启动VM
			NewControlRobot.StartVMByName(VM_Name);
			// 初始化VM
			SetResolutionByStartUp Init = new SetResolutionByStartUp();
			Init.SetBaseDataPath(SourceVMName(Base_VM_Name));
			Init.Run();

			// 获取VM的IP 地址
			String VMIP = null;
			GetAndroidIP GetVMIP = new GetAndroidIP();
			GetVMIP.SetBaseDataPath(SourceVMName(Base_VM_Name));
			GetVMIP.Run();
			VMIP = GetVMIP.GetVMIP();
			
			// 同步APP文件
			InstallAppRobot InsAPP = new InstallAppRobot();
			InsAPP.SetBaseDataPath(SourceVMName(Base_VM_Name));
			// InsAPP.NewConn();
			InsAPP.Run(VMIP, VM_Name);
			//同步配置文件
			SynchronizeFiles SynchronizeMyBaseFiles = new SynchronizeFiles();
			// SynchronizeMyBaseFiles.NewConn();
			SynchronizeMyBaseFiles.Run(VMIP, Base_VM_Name, VM_Name);

			// 重启VM
//			NewControlRobot.ResetVMByName(VM_Name);

//			// 初始化VM
//			Init = new SetResolutionByStartUp();
//			Init.SetBaseDataPath(SourceVMName(Base_VM_Name));
//			Init.Run();

			// 关闭VM
			NewControlRobot.PowerOffVMByName(VM_Name);
			// 设置VM网卡为内部网络模式
			// NewControlRobot.SetVM_InternalNetworkForName(VM_Name);
		}
	}

	// 根据Base_VM_Name获取对应样本中的分辨率
	public String GetResolution(String Base_VM_Name) {
		String Resolution = "";
		try {
			Statement statement = (Statement) conn.GetConn().createStatement();
			String sql = "SELECT * FROM `system_prop` where `Signature`=\""
					+ Base_VM_Name.trim() + "\"";
			// System.out.println(sql);
			ResultSet rs = statement.executeQuery(sql);
			// System.out.println(rs);

			while (rs.next()) {
				Resolution = rs.getString("resolution");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String Width = "width=";
		String TmpStr = Resolution.substring(Resolution.indexOf(Width)
				+ Width.length());
		Width = TmpStr.substring(0, TmpStr.indexOf(","));

		String Height = "height=";
		TmpStr = Resolution.substring(Resolution.indexOf(Height)
				+ Height.length());
		Height = TmpStr.substring(0, TmpStr.indexOf(","));

		Resolution = Width + "x" + Height;
		return Resolution;
	}

	// 根据Base_VM_Name获取真实的Android版本
	public String SourceAndridVersion(String Base_VM_Name) {
		String SoueVMName = "";
		try {
			Statement statement = (Statement) conn.GetConn().createStatement();
			String sql = "SELECT * FROM `system_prop` where `Signature`=\""
					+ Base_VM_Name.trim() + "\"";
			// System.out.println(sql);
			ResultSet rs = statement.executeQuery(sql);
			// System.out.println(rs);

			while (rs.next()) {
				SoueVMName = rs.getString("AndroidVersion");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SoueVMName;
	}

	// 根据Base_VM_Name获取真实的系统名称
	public String SourceVMName(String Base_VM_Name) {
		String SoueVMName = "";
		try {
			Statement statement = (Statement) conn.GetConn().createStatement();
			String sql = "SELECT * FROM `system_prop` where `Signature`=\""
					+ Base_VM_Name.trim() + "\"";
			// System.out.println(sql);
			ResultSet rs = statement.executeQuery(sql);
			// System.out.println(rs);

			while (rs.next()) {
				SoueVMName = rs.getString("Brand") + " - "
						+ rs.getString("MODEL");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SoueVMName;
	}

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CreatAndroidVM test = new CreatAndroidVM();
		test.NewConn();
		// test.GetResolution("1cc3e020cde0bef53a52255647af5c59");
		test.CreatNewVM();
		// test.InstalAppToVM("192.168.11.25", test.GetAPPList("5-1281195362"));
		// ControlRobot NewControlRobot = new ControlRobot();
		// NewControlRobot.StartVMByName("-1622450054");

	}

}
