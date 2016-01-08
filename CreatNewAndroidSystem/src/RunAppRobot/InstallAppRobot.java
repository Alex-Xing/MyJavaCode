package RunAppRobot;

import java.awt.event.InputEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import ADBControl.ADBRobot;
import CreatNewAndroidSystem.SystemParams;
import FindPictureLocation.captureDesktop;
import KeyBoardMouseControl.VK;

import com.mysql.jdbc.Statement;

public class InstallAppRobot extends BaseRobot {

	public InstallAppRobot() {
		RobotName = "InstallAppRobot";
		NewConn();
	}

	public boolean InatallApp(String IP, String[] AppList) {
		ADBRobot ADBControl = new ADBRobot();
		ADBControl.InitADB(IP);
		WaitTime(20000);// 等待12秒，以便确保ADB正常
		ADBControl.ConnectVM(IP);
		int I;
		boolean Flag = false;
		for (I = 0; I < AppList.length; I++) {
			Flag = false;
			while (!Flag) {
				Flag = ADBControl
						.InstallAPP(SystemParams.RootPath + AppList[I].trim());
				System.out.println("install APP:"+Flag+" "+AppList[I].trim());
				WaitTime(2000);
				if (MyRobot.FindPicture(DataPath + "Unfortunately")) {
					MyRobot.KeyClick(VK.VK_ENTER);
					MyRobot.KeyClick(VK.VK_ENTER);
					WaitPictureAndMoveMouse(DataPath + "HomeKEY");
					WaitTime(1000);
					MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
					Flag = false;
				} else {
					Flag = true;
				}
			}
		}
		//WaitTime(10000);// 等待10秒，以便VM保存文件，否则VM有可能重启失败或文件丢失
		ADBControl.KillServer();
		return true;
	}

	// 根据VM_Name获取对应样的APPList
	public String[] GetAPPList(String VM_Name) {
		String APPList = "";
		try {
			Statement statement = (Statement) conn.GetConn().createStatement();
			String sql = "SELECT * FROM `new_vm_system_info` where `VM_Name`=\""
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
		return GetAppPath(APPList);
	}
	
	//AppList获取对应的APP存放路径数组
	public String[] GetAppPath(String AppList){
		String[] TmpStr = AppList.split(";");
		int i;
		for (i = 0; i < TmpStr.length; i++)
			try {
				Statement statement = (Statement) conn.GetConn()
						.createStatement();
				String sql = "SELECT * FROM `app_info` where `Signature`=\""
						+ TmpStr[i].trim() + "\"";
				// System.out.println(sql);
				ResultSet rs = statement.executeQuery(sql);
				// System.out.println(rs);
				while (rs.next()) {
					TmpStr[i] = rs.getString("Path");
					System.out.println(rs.getString("Name") + ":"
							+ rs.getString("Type"));
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return TmpStr;
	}
	public Boolean Run(String IP, String VM_Name) {
		// 设置VM为当前窗口
		WaitPictureAndMoveMouse(DataPath + "S0001");
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		boolean Flag = true;
		while (Flag) {
			Flag = !InatallApp(IP, GetAPPList(VM_Name));
			// WaitTime(5000);
		}
		System.out.println("Run InstallAppRobot Complete");
		return true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean flag = true;
		// flag = false;
		if (flag) {
			InstallAppRobot test = new InstallAppRobot();
			test.SetBaseDataPath("samsung - GT-S6358");
			// test.NewConn();
			test.WaitTime(5000);
			String tt = ";f36a854b41c640b9b2afca199579d9de;7a9a00b0a8da7605ecafdff85b5ba4a9;32ee0fb2af567c7205cd1aaaf7496525;aa9218e6a7a6336a388b70303eb33c03;f8a62eaf8c72f5dee087d34b6132ea8e;67e377b72f5cca3b625207ea19af125c;d2b30cb8f913360492f3f5e62778a3a1;b25a7e870a00eadd3678ae431562904c;850c041c26dd41e3256029d2d73aa6de;87e7b9fb0da889f53501d543eafa20e6;217605981af961475f9de2d634d51b1f;215370d8e64d14ae3481ef98ab8d6432;bdeaede798835a3ab79d41195e3f9cb0;37e67d679c9dc2e4e9d6137a13afe3a7;e988ac8f65459d8f0dde423330e8f84d;575f5c1af5ea3ac0ba98a7eb15e2ee21;99a205a3cfc58d40221720e5f1aae33e";
			tt=";d2b30cb8f913360492f3f5e62778a3a1;f36a854b41c640b9b2afca199579d9de;1a74a84e6cb8cc53d7e454ee5d31e112;48ef38130453c5199e190e8de7ec6872;f8a62eaf8c72f5dee087d34b6132ea8e;575f5c1af5ea3ac0ba98a7eb15e2ee21;51ca14a79239317db04d432efca2d04d;217605981af961475f9de2d634d51b1f;bdeaede798835a3ab79d41195e3f9cb0";
			test.InatallApp("10.10.0.173", test.GetAppPath(tt));
			// test.Run("192.168.11.39", "3-1041921130");

		} else {
			captureDesktop Tools = new captureDesktop();
			Tools.SaveDesktopToPNG("D:/android-x86/RobotData/samsung - GT-S6358/Desktop.png");
		}

	}

}
