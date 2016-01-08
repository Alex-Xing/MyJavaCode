package RunAppRobot;

import java.awt.event.InputEvent;

import CreatNewAndroidSystem.SystemParams;
import FindPictureLocation.FindPictureLocation;
import FindPictureLocation.captureDesktop;
import KeyBoardMouseControl.VK;
import Tools.SampleTCPServer;

public class GetAndroidIP extends BaseRobot {
	public FindPictureLocation MouseLocation = new FindPictureLocation();
	//private SystemParams MyParams = new SystemParams();
	private String VMIP = null;

	public GetAndroidIP() {
		RobotName = "GetAndroidIP";
	}

	public String GetVMIP() {
		return VMIP;
	}

	public Boolean Run() {
		boolean Flag = true;

		while (Flag) {
			Flag = !GetIPAdd();
		}

		System.out.println("Run GetAndroidIP Complete");
		return true;
	}

	public boolean GetIPAdd() {
		boolean Flag = true;
		// 设置VM为当前窗口
		while (Flag) {
			if (MyRobot.MoveMouseToPicture(DataPath + "S0001")) {
				System.out.println("定位成功：" + DataPath + "S0001");
				Flag = false;
			} else {
				WaitTime(1000);
				System.out.println("无法定位：" + DataPath + "S0001");
			}
		}
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);

		// 启动本地测试端口，获取VM的IP地址
		SampleTCPServer TestServer = new SampleTCPServer();
		Thread T1 = new Thread(TestServer);
		// TestServer.run();
		T1.start();
		// 启动浏览器访问本地测试端口
		WaitPictureAndMoveMouse(DataPath + "HomeKEY");
		WaitTime(1000);
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);

		Flag = true;
		while (Flag) {
			if (MyRobot.MoveMouseToPicture(DataPath + "ALLKEY")) {
				System.out.println("定位成功：" + DataPath + "ALLKEY");
				Flag = false;
			} else {
				WaitTime(1000);
				System.out.println("无法定位：" + DataPath + "ALLKEY");
				// 如果有错误
				if (MyRobot.FindPicture(DataPath + "Unfortunately")) {
					MyRobot.KeyClick(VK.VK_ENTER);
					MyRobot.KeyClick(VK.VK_ENTER);
					WaitPictureAndMoveMouse(DataPath + "HomeKEY");
					MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
				}
			}
		}
		WaitTime(1000);
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		WaitTime(2000);
		// WaitPictureAndMoveMouse(DataPath + "Browser.png");

		Flag = true;
		while (Flag) {
			if (MyRobot.MoveMouseToPicture(DataPath + "Browser")) {
				System.out.println("定位成功：" + DataPath + "Browser");
				Flag = false;
			} else {
				WaitTime(1000);
				System.out.println("无法定位：" + DataPath + "Browser");
				// 如果有引导OK按钮则按下，没有就跳过
				if (MyRobot.MoveMouseToPicture(DataPath + "Init-OK")) {
					WaitTime(1000);
					MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
					System.out.println("定位成功：" + DataPath + "Init-OK");
				}
			}
		}
		WaitTime(1000);
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		WaitTime(3000);
		// WaitPictureAndMoveMouse(DataPath + "S0003.png");
		Flag = true;
		String TmpName =null;
		while (Flag) {
			if (MyRobot.MoveMouseToPicture(DataPath + "S0003")) {
				System.out.println("定位成功：" + DataPath + "S0003");
				Flag = false;
			} else {
				WaitTime(1000);
				System.out.println("无法定位：" + DataPath + "S0003");
				MyRobot.MoveMouseToPicture(DataPath + "HomeKEY");
				//尝试下拉窗口调出地址栏
				TmpName = MyRobot.FindPictureFolder(DataPath + "HomeKEY");
				int[] Tmp = new int[2];
				Tmp = MyRobot.GetMouseLocation();
				MyRobot.MoveMouse(
						Tmp[0],
						Tmp[1]
								- (MouseLocation.GetPictureHeight(TmpName) * 5));
				MyRobot.MousePresss(InputEvent.BUTTON1_DOWN_MASK);
				MyRobot.MoveMouse(
						Tmp[0],
						Tmp[1]
								- (MouseLocation.GetPictureHeight(TmpName) * 2));
				MyRobot.MouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			}
		}
		TmpName = MyRobot.FindPictureFolder(DataPath + "S0003");
		int[] Location = new int[2];
		Location = MyRobot.GetMouseLocation();
		MyRobot.MoveMouse(
				Location[0]
						- (MouseLocation
								.GetPictureWidth(TmpName) * 3),
				Location[1]);
		WaitTime(1000);
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);

		WaitTime(1000);
		MyRobot.KeyClick(VK.VK_BACK_SPACE);
		MyRobot.KeyClick(SystemParams.LocalHostTestIP.trim());
		MyRobot.KeyPress(VK.VK_SHIFT);
		MyRobot.KeyClick(VK.VK_SEMICOLON);
		MyRobot.KeyRelease(VK.VK_SHIFT);
		MyRobot.KeyClick(SystemParams.LocalHostTestPort.trim());
		MyRobot.KeyClick(VK.VK_ENTER);

		WaitTime(2000);
		VMIP = TestServer.GetClientIP();

		TestServer.Stop();
		T1.interrupt();

		System.out.println(TestServer.GetClientIP());
		TestServer = null;
		T1 = null;
		System.gc();
		if (VMIP == null||VMIP.equals("127.0.0.1")) {
			return false;
		}

		return true;

	}

	/**
	 * @param args
	 */

	public static void main(String[] args) {

		boolean flag = true;
		// flag = false;
		if (flag) {
			GetAndroidIP test = new GetAndroidIP();
			test.SetBaseDataPath("samsung - GT-S6358");
			test.WaitTime(5000);
			test.Run();
			// test.test();

		} else {
			captureDesktop Tools = new captureDesktop();
			Tools.SaveDesktopToPNG("D:/android-x86/RobotData/samsung - GT-S6358/Desktop.png");
		}
	}

}
