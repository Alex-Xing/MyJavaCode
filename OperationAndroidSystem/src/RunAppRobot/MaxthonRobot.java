package RunAppRobot;

import java.awt.event.InputEvent;

import FindPictureLocation.FindPictureLocation;
import FindPictureLocation.captureDesktop;
import KeyBoardMouseControl.VK;

public class MaxthonRobot extends BaseRobot {
	public FindPictureLocation MouseLocation = new FindPictureLocation();

	public MaxthonRobot() {
		RobotName = "MaxthonRobot";
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
		boolean Flag=SearchAPPIconAndMoveMouse(DataPath + "APPIcon", 6, 'R');
		if (Flag) {
			WaitTime(1000);
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			WaitTime(1000);
			// 操作APP
			OperationAPP();
		}else
		{
			System.out.println("Error:没找到APP图标!!将退出Robot");
		}
		// 返加主界面
		WaitPictureAndMoveMouse(DataPath + "HomeKEY");
		WaitTime(1000);
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		System.out.println("Run " + RobotName + " Complete");
		return Flag;
	}

	// 操作APP
	public void OperationAPP() {

		if (MyRobot.MoveMouseToPicture(DataPath + "S0003")) {
			System.out.println("定位成功：" + DataPath + "S0003");
			WaitTime(1000);
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		} else {
			WaitTime(1000);
			System.out.println("无法定位：" + DataPath + "S0003");
			if (MyRobot.MoveMouseToPicture(DataPath + "S0005")) {
				System.out.println("定位成功：" + DataPath + "S0005");
				WaitTime(1000);
				MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			} else if (MyRobot.MoveMouseToPicture(DataPath + "HomePapge")) {
				System.out.println("定位成功：" + DataPath + "HomePapge");
				WaitTime(1000);
				MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			}

		}

		int Random = GetLimitIntRandom(0, 4);
		switch (Random) {
		case 0:
			InputWebAddress();
			break;
		case 1:
			LoadTypeReading();
			break;
		case 2:
			InputWebAddress();
			LoadTypeReading();
			break;
		case 3:
			LoadTypeReading();
			InputWebAddress();
			break;
		}
	}

	// 手工录入网页地址
	public void InputWebAddress() {

		String[] LinkArray = GetRandomLink();
		for (int i = 0; i < LinkArray.length; i++) {
			String TmpName = MyRobot.FindPictureFolder(DataPath + "Address");
			WaitPictureAndMoveMouse(DataPath + "Address");
			int[] Tmp = new int[2];
			Tmp = MyRobot.GetMouseLocation();
			Tmp[0] = Tmp[0] + (MouseLocation.GetPictureWidth(TmpName) * 5);
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
			if (MyRobot.MoveMouseToPicture(DataPath + "Cancel")) {
				System.out.println("定位成功：" + DataPath + "Cancel");
				MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
				WaitTime(1000);
			}

		}
		WaitPictureAndMoveMouse(DataPath + "HomePapge");
		// WaitTime(100);
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

	public void LoadTypeReading() {

		int RandomCount = GetLimitIntRandom(1, 5);// 随机生成要阅读多少个分类的总数
		int[] TmpIndex = GetLimitIntRandom(0, 5, RandomCount, false);
		for (int i = 0; i < TmpIndex.length; i++) {
			System.out.println(TmpIndex[i]);
			LoadTypeReading(TmpIndex[i]);
		}

	}

	public void LoadTypeReading(int index) {
		// 定位到聚合阅读界面
		while (true) {
			if (MyRobot.MoveMouseToPicture(DataPath + "S0003")) {
				System.out.println("定位成功：" + DataPath + "S0003");
				break;
			} else {
				WaitTime(1000);
				System.out.println("无法定位：" + DataPath + "S0003");
				if (MyRobot.MoveMouseToPicture(DataPath + "S0005")) {
					System.out.println("定位成功：" + DataPath + "S0005");
					WaitTime(1000);
					MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
				} else if (MyRobot.MoveMouseToPicture(DataPath + "HomePapge")) {
					System.out.println("定位成功：" + DataPath + "HomePapge");
					WaitTime(1000);
					MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
				}

			}
		}
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		WaitTime(1000);

		int[] NewLocation = new int[2];
		int[] Tmp = new int[2];
		// Tmp = MyRobot.GetMouseLocation();
		// 确认X座标
		switch (index) {
		case 0:
		case 2:
		case 4:
			MyRobot.MoveMouseToPicture(DataPath + "S0005");
			Tmp = MyRobot.GetMouseLocation();
			NewLocation[0] = Tmp[0];
			// System.out.println(NewLocation[0]);
			break;
		case 1:
		case 3:
		case 5:
			MyRobot.MoveMouseToPicture(DataPath + "S0003");
			Tmp = MyRobot.GetMouseLocation();
			NewLocation[0] = Tmp[0];
			// System.out.println(NewLocation[0]);
			break;
		}
		// 确认Y座标
		String TmpName = MyRobot.FindPictureFolder(DataPath + "Function");
		switch (index) {
		case 0:
		case 1:

			NewLocation[1] = Tmp[1] + MouseLocation.GetPictureHeight(TmpName)
					* 5;
			break;
		case 2:
		case 3:
			NewLocation[1] = Tmp[1] + MouseLocation.GetPictureHeight(TmpName)
					* 10;
			break;
		case 4:
		case 5:
			NewLocation[1] = Tmp[1] + MouseLocation.GetPictureHeight(TmpName)
					* 13;
			break;
		}
		// System.out.println(NewLocation[0] + ":" + NewLocation[1]);
		MyRobot.MoveMouse(NewLocation[0], NewLocation[1]);
		WaitTime(100);
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		int S = GetLimitIntRandom(10000, 20000);// 等待页面加载
		// System.out.println("S："+S);
		WaitTime(S);

		int RandomCount = GetLimitIntRandom(1, 6);// 随机生成要阅读多少个分类的总数
		int[] TmpIndex = GetLimitIntRandom(1, 6, RandomCount, false);
		for (int i = 0; i < TmpIndex.length; i++) {
			// System.out.println(TmpIndex[i]);
			LoadDetailReading(TmpIndex[i]);
		}

		// 返回到APP首页
		WaitTime(1000);
		MyRobot.MoveMouseToPicture(DataPath + "HomePapge");
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		S = GetLimitIntRandom(5000, 10000);
		WaitTime(S);
	}

	// 加载详细页面，Offset是下移量
	public void LoadDetailReading(int Offset) {
		if (Offset == 0) {
			Offset = 1;
		}
		for (int i = 0; i < Offset; i++) {
			MyRobot.KeyClick(VK.VK_DOWN);
			WaitTime(1000);
		}
		MyRobot.KeyClick(VK.VK_ENTER);
		int S = GetLimitIntRandom(10000, 20000);// 等待页面加载
		WaitTime(S);
		WaitPictureAndMoveMouse(DataPath + "Back");
		WaitTime(100);
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		WaitTime(100);

		// MyRobot.KeyClick(VK.VK_UP);
		// WaitTime(1000);

		// for (int i = 0; i < Offset; i++) {
		// MyRobot.KeyClick(VK.VK_UP);
		// WaitTime(1000);
		// }

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean flag = true;
		//flag = false;
		if (flag) {
			MaxthonRobot test = new MaxthonRobot();
			test.SetBaseDataPath("samsung - GT-S6358");
			test.WaitTime(5000);
			// test.PageRight();
			// test.PageLeft();
			test.Run();
			// test.LoadTypeReading();
			// test.LoadTypeReading(4);
			// test.LoadDetailReading(0);

		} else {
			captureDesktop Tools = new captureDesktop();
			Tools.SaveDesktopToPNG("D:/android-x86/RobotData/samsung - GT-S6358/Desktop.png");
		}
	}

}
