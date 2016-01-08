package RunAppRobot;

import java.awt.event.InputEvent;

import FindPictureLocation.FindPictureLocation;
import FindPictureLocation.captureDesktop;
import KeyBoardMouseControl.VK;

public class MaxthonRobotV2 extends BaseRobot {
	public FindPictureLocation MouseLocation = new FindPictureLocation();

	public MaxthonRobotV2() {
		RobotName = "MaxthonRobot";
	}

	public Boolean Run() {
		// 设置VM为当前窗口
		WaitPictureAndMoveMouse(DataPath + "S0001");
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		
		// 返加主界面
		WaitPictureAndMoveMouse(DataPath + "HomeKEY");
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		// 进入应用界面
		WaitPictureAndMoveMouse(DataPath + "ALLKEY");
		WaitTime(1000);
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);

		SearchAPPIconAndMoveMouse(DataPath + "APPIcon.png", 6, 'R');
		WaitTime(1000);
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		WaitTime(1000);

		int Random = GetLimitIntRandom(0, 4);
		switch (Random) {
		case 0:
			InputWebAddress();
			break;
		case 1:
			LoadLableTab();
			break;
		case 2:
			InputWebAddress();
			LoadLableTab();
			break;
		case 3:
			LoadLableTab();
			InputWebAddress();
			break;
		}

		// 返加主界面
		WaitPictureAndMoveMouse(DataPath + "HomeKEY");
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		return true;
	}

	// 加载云标签
	public void LoadLableTab() {
		WaitPictureAndMoveMouse(DataPath + "S0006.png");
		WaitTime(500);
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		WaitTime(500);
		int RandomCount = GetLimitIntRandom(1, 11);
		int[] TmpIndex = GetLimitIntRandom(0, 11, RandomCount, false);
		for (int I = 0; I < TmpIndex.length; I++) {
			System.out.println(TmpIndex[I]);
			LoadLableDetail(TmpIndex[I]);
		}
		// LoadLableDetail(9);
	}

	public void LoadLableDetail(int Index) {
		while (true) {
			if (MyRobot.MoveMouseToPicture(DataPath + "S0003.png")) {
				System.out.println("定位成功：" + DataPath + "S0003.png ");
				break;
			} else {
				WaitTime(1000);
				System.out.println("无法定位：" + DataPath + "S0003.png ");
				// 如果有错误
				if (MyRobot.FindPicture(DataPath + "Unfortunately.png")
						|| MyRobot.FindPicture(DataPath
								+ "Unfortunately-中文.png")) {
					MyRobot.KeyClick(VK.VK_ENTER);
					MyRobot.KeyClick(VK.VK_ENTER);
					WaitPictureAndMoveMouse(DataPath + "HomeKEY.png");
					MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
				}
			}
		}
		WaitPictureAndMoveMouse(DataPath + "S0003.png");
		int[] Tmp = new int[2];
		Tmp = MyRobot.GetMouseLocation();
		// 确认X座标
		switch (Index) {
		case 0:
		case 6:
			Tmp[0] = Tmp[0]
					+ (MouseLocation.GetPictureWidth(DataPath + "S0003.png"));
			break;
		case 1:
		case 7:
			Tmp[0] = Tmp[0]
					+ (MouseLocation.GetPictureWidth(DataPath + "S0003.png") * 2);
			break;
		case 2:
		case 8:
			Tmp[0] = Tmp[0]
					+ (MouseLocation.GetPictureWidth(DataPath + "S0003.png") * 4);
			break;
		case 3:
		case 9:
			Tmp[0] = Tmp[0]
					+ (MouseLocation.GetPictureWidth(DataPath + "S0003.png") * 7);
			break;
		case 4:
		case 10:
			Tmp[0] = Tmp[0]
					+ (MouseLocation.GetPictureWidth(DataPath + "S0003.png") * 8);
			break;
		case 5:
			Tmp[0] = Tmp[0]
					+ (MouseLocation.GetPictureWidth(DataPath + "S0003.png") * 10);
			break;
		}
		// 确认Y座标
		switch (Index) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
			Tmp[1] = Tmp[1]
					+ (MouseLocation.GetPictureHeight(DataPath + "S0003.png") * 3);

			break;
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
			Tmp[1] = Tmp[1]
					+ (MouseLocation.GetPictureHeight(DataPath + "S0003.png") * 5);
			break;
		}
		MyRobot.MoveMouse(Tmp[0], Tmp[1]);
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		int S = GetLimitIntRandom(20000, 30000);// 等待页面加载
		WaitTime(S);
		WaitPictureAndMoveMouse(DataPath + "Back.png");
		WaitTime(1000);
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		WaitTime(1000);
	}

	// 手工录入网页地址
	public void InputWebAddress() {

		String[] LinkArray = GetRandomLink();
		for (int i = 0; i < LinkArray.length; i++) {
			WaitPictureAndMoveMouse(DataPath + "S0003.png");
			int[] Tmp = new int[2];
			Tmp = MyRobot.GetMouseLocation();
			Tmp[0] = Tmp[0]
					+ (MouseLocation.GetPictureWidth(DataPath + "S0003.png") * 5);
			MyRobot.MoveMouse(Tmp[0], Tmp[1]);
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			WaitTime(1000);
			MyRobot.KeyPress(VK.VK_CONTROL);
			MyRobot.KeyClick(VK.VK_A);
			MyRobot.KeyRelease(VK.VK_CONTROL);

			System.out.println(LinkArray[i]);
			MyRobot.KeyClick(LinkArray[i]);
			MyRobot.KeyClick(VK.VK_ENTER);
			int S = GetLimitIntRandom(20000, 30000);// 等待页面加载
			WaitTime(S);
			if (MyRobot.MoveMouseToPicture(DataPath + "S0005.png")) {
				System.out.println("定位成功：" + DataPath + "S0005.png ");
				MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
				WaitTime(1000);
			}

		}
		WaitPictureAndMoveMouse(DataPath + "Back.png");
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);

	}

	// 生成随机网页地址
	public String[] GetRandomLink() {

		String[] LinkArray = { "http://www.21cn.com", "http://www.sohu.com",
							   "http://www.sina.com.cn/", "http://www.daqi.com/",
							   "http://tieba.baidu.com", "http://www.tom.com" };
		
		int RandomCount = GetLimitIntRandom(1, LinkArray.length);
		int[] TmpIndex = GetLimitIntRandom(0, 6, RandomCount, false);
		String[] ReturnStr = new String[TmpIndex.length];
		for (int I = 0; I < TmpIndex.length; I++) {
			ReturnStr[I] = LinkArray[TmpIndex[I]];
		}
		return ReturnStr;
	}

	// 查找APP的ICON
	public boolean SearchAPPIconAndMoveMouse(String PicName, int PagerNumber,
			char way) {
		// 确保回到应用界面第一页
		MovePageToFirst();
		int i = 0;
		for (i = 0; i < PagerNumber; i++) {
			if (MyRobot.MoveMouseToPicture(PicName)) {
				System.out.println("定位成功：" + PicName);
				return true;
			} else {
				switch (way) {
				case 'L':
					PageLeft();
					WaitTime(1000);
					break;
				case 'R':
					PageRight();
					WaitTime(1000);
					break;
				}
			}
		}
		return false;
	}

	public void MovePageToFirst() {
		int i;
		for (i = 0; i < 7; i++) {
			PageLeft();
			WaitTime(1000);
		}
	}

	public void PageRight() {
		MyRobot.MoveMouseToPicture(DataPath + "Function.png");
		int[] Tmp = new int[2];
		Tmp = MyRobot.GetMouseLocation();
		MyRobot.MoveMouse(
				Tmp[0],
				Tmp[1]
						- (MouseLocation.GetPictureHeight(DataPath
								+ "Function.png") * 3));
		MyRobot.MousePresss(InputEvent.BUTTON1_DOWN_MASK);
		WaitTime(50);// 必须要等待否则VM没反映
		MyRobot.MoveMouse(
				Tmp[0]
						- (MouseLocation.GetPictureWidth(DataPath
								+ "Function.png") * 5),
				Tmp[1]
						- (MouseLocation.GetPictureHeight(DataPath
								+ "Function.png") * 3));
		MyRobot.MouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}

	public void PageLeft() {
		MyRobot.MoveMouseToPicture(DataPath + "Back.png");
		int[] Tmp = new int[2];
		Tmp = MyRobot.GetMouseLocation();
		MyRobot.MoveMouse(
				Tmp[0],
				Tmp[1]
						- (MouseLocation
								.GetPictureHeight(DataPath + "Back.png") * 3));
		MyRobot.MousePresss(InputEvent.BUTTON1_DOWN_MASK);
		WaitTime(50);// 必须要等待否则VM没反映
		MyRobot.MoveMouse(
				Tmp[0]
						+ (MouseLocation.GetPictureWidth(DataPath + "Back.png") * 5),
				Tmp[1]
						- (MouseLocation
								.GetPictureHeight(DataPath + "Back.png") * 3));
		MyRobot.MouseRelease(InputEvent.BUTTON1_DOWN_MASK);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean flag = true;
		// flag = false;
		if (flag) {
			MaxthonRobotV2 test = new MaxthonRobotV2();
			test.SetBaseDataPath("samsung - GT-S6358");
			test.WaitTime(5000);
			test.Run();
			// test.InputWebAddress();
			// test.LoadTypeReading(0);http://tieba.baidu.com
			// test.LoadLableTab();
			// test.LoadDetailReading(0);

		} else {
			captureDesktop Tools = new captureDesktop();
			Tools.SaveDesktopToPNG("D:/android-x86/RobotData/samsung - GT-S6358/Desktop.png");
		}
	}

}
