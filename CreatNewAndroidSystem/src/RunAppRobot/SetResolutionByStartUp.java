package RunAppRobot;

import java.awt.event.InputEvent;

import FindPictureLocation.FindPictureLocation;
import FindPictureLocation.captureDesktop;
import KeyBoardMouseControl.VK;

//InputEvent.BUTTON1_DOWN_MASK 左键
//InputEvent.BUTTON2_DOWN_MASK 中键
//InputEvent.BUTTON3_DOWN_MASK 右键
//启动VM时修改分辨率
public class SetResolutionByStartUp extends BaseRobot {
	public FindPictureLocation MouseLocation = new FindPictureLocation();

	public SetResolutionByStartUp() {
		RobotName = "SetResolutionByStartUp";
	}

	public Boolean Run() {
		
		// 设置VM为当前窗口
		WaitPictureAndMoveMouse(DataPath + "S0001");
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);

		// 等待Android启动窗口
		WaitPictureAndMoveMouse(DataPath + "S0003.png");
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);

		MyRobot.KeyClick("ee vga=ask");
		MyRobot.KeyClick(VK.VK_ENTER);
		MyRobot.KeyClick("b");

		// 等待选择分辨率设置窗口
		WaitPicture(DataPath + "S0004.png");
		MyRobot.KeyClick(VK.VK_ENTER);
		WaitTime(1000);
		MyRobot.KeyClick("360");
		MyRobot.KeyClick(VK.VK_ENTER);

		// 等待解锁窗口
		boolean Flag = true;
		while (Flag) {
			if (MyRobot.FindPicture(DataPath + "Lock")) {
				
				// 解锁操作
				String TmpName = MyRobot.FindPictureFolder(DataPath + "Lock");
				MyRobot.MoveMouseToPicture(DataPath + "Lock");
				System.out.println("定位成功：" + DataPath + "Lock");
				int Width=MouseLocation.GetPictureWidth(TmpName) * 2;
				WaitTime(100);
				MyRobot.MousePresss(InputEvent.BUTTON1_DOWN_MASK);
				int[] Tmp = new int[2];
				Tmp = MyRobot.GetMouseLocation();
				MyRobot.MoveMouse(
						Tmp[0] + Width,
						Tmp[1]);
				WaitTime(100);
				MyRobot.MouseRelease(InputEvent.BUTTON1_DOWN_MASK);
				Flag = false;
			} else {
				WaitTime(1000);
				System.out.println("无法定位：" + DataPath + "Lock");
				if (MyRobot.MoveMouseToPicture(DataPath + "Init-OK")) {
					Flag = false;
					System.out.println("无解锁窗口!");
					// 点击OK按钮
					WaitPictureAndMoveMouse(DataPath + "Init-OK");
					WaitTime(1000);
					MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
				}
			}
		}

		boolean falg = false;
		while (!falg) {
			falg = CheckInitOK();
		}
		WaitTime(1000);
		// 返回桌面
		WaitPictureAndMoveMouse(DataPath + "HomeKEY");
		while (Flag) {
			if (MyRobot.MoveMouseToPicture(DataPath + "HomeKEY")) {
				System.out.println("定位成功：" + DataPath + "HomeKEY");
				Flag = false;
			} else {
				WaitTime(1000);
				System.out.println("无法定位：" + DataPath + "HomeKEY");
			}
		}
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		WaitTime(1000);
		//重启网卡
		MyRobot.KeyPress(VK.VK_ALT);
		MyRobot.KeyClick(VK.VK_F1);
		MyRobot.KeyRelease(VK.VK_ALT);
		WaitTime(1000);
		MyRobot.KeyClick("ifconfig eth0 down");
		MyRobot.KeyClick(VK.VK_ENTER);
		WaitTime(1000);
		MyRobot.KeyClick("ifconfig eth0 up");
		MyRobot.KeyClick(VK.VK_ENTER);
		WaitTime(1000);
		MyRobot.KeyPress(VK.VK_ALT);
		MyRobot.KeyClick(VK.VK_F7);
		MyRobot.KeyRelease(VK.VK_ALT);
		System.out.println("Run SetResolutionByStartUp Complete");
		return true;

	}

	public boolean CheckInitOK() {
		// 等待加载桌面
		WaitTime(2000);

		// 如果有引导OK按钮则按下，没有就跳过
		if (MyRobot.MoveMouseToPicture(DataPath + "Init-OK")) {
			WaitTime(1000);
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			System.out.println("定位成功：" + DataPath + "Init-OK");
		}
		// 如果有错误
		if (MyRobot.FindPicture(DataPath + "Unfortunately")) {
			MyRobot.KeyClick(VK.VK_ENTER);
			WaitTime(1000);
			MyRobot.KeyClick(VK.VK_ENTER);
			WaitTime(1000);
			WaitPictureAndMoveMouse(DataPath + "HomeKEY");
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			return false;
		}
		WaitTime(2000);
		// 如果有第二个引导OK按钮则按下，没有就跳过
		if (MyRobot.MoveMouseToPicture(DataPath + "Init-OK")) {
			WaitTime(1000);
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			System.out.println("定位成功：" + DataPath + "Init-OK");
		}
		// 如果有错误
		if (MyRobot.FindPicture(DataPath + "Unfortunately")) {
			MyRobot.KeyClick(VK.VK_ENTER);
			WaitTime(1000);
			MyRobot.KeyClick(VK.VK_ENTER);
			WaitTime(1000);
			WaitPictureAndMoveMouse(DataPath + "HomeKEY");
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			return false;
		}

		// 进入应用界面，检查是否有引导OK按钮
		WaitPictureAndMoveMouse(DataPath + "ALLKEY");
		WaitTime(1000);
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);

		// 如果有错误
		if (MyRobot.FindPicture(DataPath + "Unfortunately")) {
			MyRobot.KeyClick(VK.VK_ENTER);
			WaitTime(1000);
			MyRobot.KeyClick(VK.VK_ENTER);
			WaitTime(1000);
			WaitPictureAndMoveMouse(DataPath + "HomeKEY");
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			return false;
		}
		// 等待加载应用界面
		WaitTime(3000);
		// 如果有引导OK按钮则按下，没有就跳过
		if (MyRobot.FindPicture(DataPath + "Unfortunately")) {
			WaitTime(1000);
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			System.out.println("定位成功：" + DataPath + "Init-OK");
		}

		// 如果有错误
		if (MyRobot.FindPicture(DataPath + "Unfortunately")) {
			MyRobot.KeyClick(VK.VK_ENTER);
			WaitTime(1000);
			MyRobot.KeyClick(VK.VK_ENTER);
			WaitTime(1000);
			WaitPictureAndMoveMouse(DataPath + "HomeKEY");
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
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
			SetResolutionByStartUp test = new SetResolutionByStartUp();
			test.SetBaseDataPath("Huawei - HUAWEI MT1-U06");
			test.WaitTime(5000);
			test.Run();
		} else {
			captureDesktop Tools = new captureDesktop();
			Tools.SaveDesktopToPNG("D:/android-x86/RobotData/Huawei - HUAWEI MT1-U06/Desktop.png");
		}

	}

}
