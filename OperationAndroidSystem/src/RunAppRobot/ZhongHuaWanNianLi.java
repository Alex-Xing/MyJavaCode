package RunAppRobot;

import java.awt.event.InputEvent;

import FindPictureLocation.FindPictureLocation;
import FindPictureLocation.captureDesktop;
import KeyBoardMouseControl.VK;

public class ZhongHuaWanNianLi extends BaseRobot {
	public FindPictureLocation MouseLocation = new FindPictureLocation();

	public ZhongHuaWanNianLi() {
		RobotName = "ZhongHuaWanNianLi";// 中华万年历
	}

	public Boolean Run() {
		// 设置VM为当前窗口
		WaitPictureAndMoveMouse(DataPath + "S0001");
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);

		// 返加主界面
		WaitPictureAndMoveMouse(DataPath + "HomeKEY");
		WaitTime(1000);
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		WaitTime(1000);
		// 进入应用界面
		WaitPictureAndMoveMouse(DataPath + "ALLKEY");
		WaitTime(1000);
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		// 查找APP图标
		SearchAPPIconAndMoveMouse(DataPath + "APPIcon", 6, 'L');
		WaitTime(1000);
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		WaitTime(1000);
		// 操作APP
		System.out.println(OperationAPP());
		// while (true) {
		// if (OperationAPP()) {
		// break;
		// }
		// }

		// 返加主界面
		WaitPictureAndMoveMouse(DataPath + "HomeKEY");
		WaitTime(1000);
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		System.out.println("Run " + RobotName + " Complete");
		return true;
	}

	public boolean OperationAPP() {
		// 点击启界面
		if (MyRobot.FindPicture(DataPath + "S0002")) {
			WaitPictureAndMoveMouse(DataPath + "S0002");
			WaitTime(1000);
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		}
		// 如果有错误
		if (MyRobot.FindPicture(DataPath + "Unfortunately")) {
			MyRobot.KeyClick(VK.VK_ENTER);
			WaitTime(1000);
			MyRobot.KeyClick(VK.VK_ENTER);
			WaitPictureAndMoveMouse(DataPath + "HomeKEY");
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			return false;
		}
		// 录入注册用户名
		if (MyRobot.FindPicture(DataPath + "RegisterUserName")) {
			RegisterUser();
		}
		// 如果有错误
		if (MyRobot.FindPicture(DataPath + "Unfortunately")) {
			MyRobot.KeyClick(VK.VK_ENTER);
			WaitTime(1000);
			MyRobot.KeyClick(VK.VK_ENTER);
			WaitPictureAndMoveMouse(DataPath + "HomeKEY");
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			return false;
		}
		// 去除提示信息
		if (MyRobot.FindPicture(DataPath + "Tips1")) {
			WaitPictureAndMoveMouse(DataPath + "Back");
			WaitTime(1000);
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		}
		// 如果有错误
		if (MyRobot.FindPicture(DataPath + "Unfortunately")) {
			MyRobot.KeyClick(VK.VK_ENTER);
			WaitTime(1000);
			MyRobot.KeyClick(VK.VK_ENTER);
			WaitPictureAndMoveMouse(DataPath + "HomeKEY");
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			return false;
		}
		int Random = GetLimitIntRandom(0, 4);
		switch (Random) {
		case 0:
			OpenDayMessage();
			break;
		case 1:
			OpenReMind();
			break;
		case 2:
			OpenDayMessage();
			OpenReMind();
			break;
		case 3:
			OpenReMind();
			OpenDayMessage();
			break;
		}
		// 如果有错误
		if (MyRobot.FindPicture(DataPath + "Unfortunately")) {
			MyRobot.KeyClick(VK.VK_ENTER);
			WaitTime(1000);
			MyRobot.KeyClick(VK.VK_ENTER);
			WaitPictureAndMoveMouse(DataPath + "HomeKEY");
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			return false;
		}
		return true;
	}

	// 打开每日信息
	public void OpenDayMessage() {
		if (MyRobot.FindPicture(DataPath + "Day")) {
			WaitPictureAndMoveMouse(DataPath + "Day");
			WaitTime(1000);
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			int S = GetLimitIntRandom(5000, 10000);// 等待页面加载
			WaitTime(S);
			WaitPictureAndMoveMouse(DataPath + "Back");
			WaitTime(1000);
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		}

	}

	// 打开提醒操作
	public void OpenReMind() {
		if (MyRobot.FindPicture(DataPath + "ReMind")) {
			WaitPictureAndMoveMouse(DataPath + "ReMind");
			WaitTime(1000);
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			// 去除提示信息
			if (MyRobot.FindPicture(DataPath + "Tips1")) {
				WaitPictureAndMoveMouse(DataPath + "Back");
				WaitTime(1000);
				MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			}
			int S = GetLimitIntRandom(10000, 15000);// 等待页面加载
			WaitTime(S);
			WaitPictureAndMoveMouse(DataPath + "Back");
			WaitTime(1000);
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		}

	}

	// 注册用户操作
	public void RegisterUser() {
		boolean Flag = true;
		while (Flag) {
			WaitPictureAndMoveMouse(DataPath + "RegisterUserName");
			WaitTime(1000);
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			WaitTime(1000);
			// 随机生产邮箱地址
			String MailAddress = GetRandomMailAddress();
			MyRobot.KeyClick(MailAddress);
			WaitTime(1000);
			if (MyRobot.FindPicture(DataPath + "RegisterPassword")) {
				// 录入注册密码
				WaitPictureAndMoveMouse(DataPath + "RegisterPassword");
				WaitTime(1000);
				MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
				WaitTime(1000);
				// 随机生产7位密码
				String Password = GetRandomStr(7);
				MyRobot.KeyClick(Password);
				WaitTime(1000);
			}

			// 点击快速注册按钮
			WaitPictureAndMoveMouse(DataPath + "RegisterButton");
			WaitTime(1000);
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			WaitTime(2000);// 等待错误提示
			Flag = false;
			if (MyRobot.FindPicture(DataPath + "EmailError")) {
				String TmpName = MyRobot.FindPictureFolder(DataPath
						+ "EmailError");
				WaitPictureAndMoveMouse(DataPath + "EmailError");

				// MyRobot.MousePresss(InputEvent.BUTTON1_DOWN_MASK);
				int[] Tmp = new int[2];
				Tmp = MyRobot.GetMouseLocation();
				MyRobot.MoveMouse(
						Tmp[0] - (MouseLocation.GetPictureWidth(TmpName) * 2),
						Tmp[1]);
				WaitTime(1000);
				MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
				WaitTime(1000);
				MyRobot.KeyPress(VK.VK_CONTROL);
				MyRobot.KeyClick(VK.VK_A);
				MyRobot.KeyRelease(VK.VK_CONTROL);
				MyRobot.KeyClick(VK.VK_BACK_SPACE);
				Flag = true;
			}
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-genGetRandomStrerated method stub
		boolean flag = true;
		// flag = false;
		if (flag) {
			ZhongHuaWanNianLi test = new ZhongHuaWanNianLi();
			test.SetBaseDataPath("samsung - GT-S6358");
			test.WaitTime(5000);
			test.Run();

			// test.OpenReMind();
			// test.LoadTypeReading(4);
			// test.LoadDetailReading(0);

		} else {
			captureDesktop Tools = new captureDesktop();
			Tools.SaveDesktopToPNG("D:/android-x86/RobotData/samsung - GT-S6358/Desktop.png");
		}
	}

}
