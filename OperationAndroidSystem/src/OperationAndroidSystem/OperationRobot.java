package OperationAndroidSystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import DataOperation.DataConnect;
import RunAppRobot.MaxthonRobot;
import RunAppRobot.SetResolutionByStartUp;
import SquidControl.SetupSquid;
import VMControl.ControlRobot;

import com.mysql.jdbc.Statement;

public class OperationRobot {

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

	public void Run() {
		// 启动路由VM
		ControlRobot NewControlRobot = new ControlRobot();
		NewControlRobot.StartVMByName(SystemParams.RouteVMName, 1);
		// 配置代理服务器地址
		SetupSquid Proxy = new SetupSquid();
		// Proxy.Setup();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
		String WorkingDate = df.format(new Date());
		try {
			Statement statement = (Statement) conn.GetConn().createStatement();
			Statement statement2 = (Statement) conn.GetConn().createStatement();
			String sql = "SELECT * FROM `vm_system_info` where `Status`=\"Waiting\" "
					+ "and (`LastOperationTime` Not like \""
					+ WorkingDate
					+ "%\" "
					+ "Or isnull(`LastOperationTime`)) Order by `LastOperationTime` ASC";
			// System.out.println(sql);
			ResultSet rs = statement.executeQuery(sql);
			String VM_Name = null;
			String Base_VM_Name = null;
			String[] App_List = null;
			int UpdateProxyFlag = GetChangProxyFlag();
			System.out.println("UpdateProxyFlag:" + UpdateProxyFlag);
			while (rs.next()) {
				UpdateProxyFlag = UpdateProxyFlag - 1;
				// 是否需要更换代理服务器
				if (UpdateProxyFlag == 0) {
					Proxy.Setup();
					UpdateProxyFlag = GetChangProxyFlag();
					System.out.println("UpdateProxyFlag：" + UpdateProxyFlag);
				}
				VM_Name = rs.getString("VM_Name");
				Base_VM_Name = rs.getString("Base_VM_Name");
				App_List = rs.getString("App_List").split(";");
				sql = "UPDATE `vm_system_info` SET `LastOperationTime` = Now() ,"
						+ "`Status`=\"Processing\" "
						+ "WHERE vm_name =\""
						+ VM_Name + "\"";
				statement2.executeUpdate(sql);

				RunVM(VM_Name, Base_VM_Name, App_List);

				sql = "UPDATE `vm_system_info` SET `Status`=\"Waiting\" "
						+ "WHERE vm_name =\"" + VM_Name + "\"";
				statement2.executeUpdate(sql);

			}
			rs.close();
			statement.close();
			statement2.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// rs.close();
		}
	}

	// 获取更换代理服务器的标志
	public int GetChangProxyFlag() {
		int I = GetLimitIntRandom(1, 10);
		int ReturnInt = 0;
		// 调整命中概率
		switch (I) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
			ReturnInt = 1;
			break;
		// case 10:
		// ReturnInt=2;
		// break;

		}
		return ReturnInt;
	}

	public void RunVM(String VM_Name) {
		String Base_VM_Name = SourceVMName(GetBase_VM_Name(VM_Name));
		String[] App_List = GetAPPList(VM_Name);
		RunVM(VM_Name, Base_VM_Name, App_List);
	}

	public void RunVM(String VM_Name, String Base_VM_Name, String[] App_List) {
		ControlRobot NewControlRobot = new ControlRobot();
		Base_VM_Name = SourceVMName(Base_VM_Name);
		// String[] App_List = GetAPPList(VM_Name);
		if (NewControlRobot.StartVMByName(VM_Name)) {
			// 初始化VM
			SetResolutionByStartUp Init = new SetResolutionByStartUp();
			Init.SetBaseDataPath(Base_VM_Name);
			Init.Run();
			int I;
			for (I = 0; I < App_List.length; I++) {
				// 处理各APP的操作
				RunAppRobot(App_List[I], VM_Name, Base_VM_Name);
				WaitTime(GetLimitIntRandom(1000, 4000));
			}

			// 关闭VM
			NewControlRobot.PowerOffVMByName(VM_Name);
		}

	}

	// 运行APP Robot
	public void RunAppRobot(String AppSignature, String VM_Name,
			String Base_VM_Name) {
		boolean Flag = true;
		switch (AppSignature) {
		case "aa9218e6a7a6336a388b70303eb33c03":
			// 傲游云浏览器.apk (Rom)
			System.out.println("傲游云浏览器");
			MaxthonRobot Robot = new MaxthonRobot();
			Robot.SetBaseDataPath(Base_VM_Name);
			Flag = Robot.Run();
			break;
		}
		if (Flag == false) {
			DeleteAPPRecord(AppSignature, VM_Name);
		}
	}

	public void DeleteAPPRecord(String AppSignature, String VM_Name) {
		try {
			Statement statement = (Statement) conn.GetConn().createStatement();
			String sql = "SELECT * FROM `vm_system_info` where `VM_Name`=\""
					+ VM_Name + "\"";
			ResultSet rs = statement.executeQuery(sql);
			String[] App_List = null;
			while (rs.next()) {
				App_List = rs.getString("App_List").split(";");
			}
			rs.close();
			int I;
			String TmpStr = "";
			for (I = 0; I < App_List.length; I++) {
				if (!App_List[I].equals(AppSignature)) {
					TmpStr = TmpStr + App_List[I] + ";";
				}
			}
			TmpStr = TmpStr.substring(0, TmpStr.length() - 1);

			sql = "UPDATE `vm_system_info` SET `App_List` =\"" + TmpStr + "\" "
					+ "WHERE vm_name =\"" + VM_Name + "\"";
			// System.out.println(sql);
			statement.executeUpdate(sql);

			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// rs.close();
		}
	}

	// 在上下限之间产生一个随机数
	public int GetLimitIntRandom(int Lower_limit, int Upper_limit) {
		Random random = new Random();
		int Total = random.nextInt(Upper_limit)
				% (Upper_limit - Lower_limit + 1) + Lower_limit;
		return Total;
	}

	// 根据VM_Name获取真实的源Base_VM名称
	public String GetBase_VM_Name(String VM_Name) {
		String Base_VM_Name = "";
		try {
			Statement statement = (Statement) conn.GetConn().createStatement();
			String sql = "SELECT * FROM `vm_system_info` where `VM_Name`=\""
					+ VM_Name.trim() + "\"";
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				Base_VM_Name = rs.getString("Base_VM_Name");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Base_VM_Name;
	}

	// 根据Base_VM_Name获取真实的源VM名称
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

	// 根据VM_Name获取对应样的APPList
	public String[] GetAPPList(String VM_Name) {
		String APPList = "";
		try {
			Statement statement = (Statement) conn.GetConn().createStatement();
			String sql = "SELECT * FROM `vm_system_info` where `VM_Name`=\""
					+ VM_Name.trim() + "\"";
			// System.out.println(sql);
			ResultSet rs = statement.executeQuery(sql);
			// System.out.println(rs);

			while (rs.next()) {
				APPList = rs.getString("App_List");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] TmpStr = APPList.split(";");

		return TmpStr;
	}

	public void WaitTime(int S) {
		try {
			Thread.sleep(S);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		OperationRobot test = new OperationRobot();
		test.NewConn();
		// test.RunVM("866587919379935");
		test.Run();
		// test.DeleteAPPRecord("99a205a3cfc58d40221720e5f1aae33e","838193907471692");
	}

}
