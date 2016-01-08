package RunAppRobot;

import java.awt.event.InputEvent;

import FindPictureLocation.FindPictureLocation;
import FindPictureLocation.captureDesktop;
import KeyBoardMouseControl.VK;

public class ZhongHuaWanNianLi extends BaseRobot {
	public FindPictureLocation MouseLocation = new FindPictureLocation();

	public ZhongHuaWanNianLi() {
		RobotName = "ZhongHuaWanNianLi";// �л�������
	}

	public Boolean Run() {
		// ����VMΪ��ǰ����
		WaitPictureAndMoveMouse(DataPath + "S0001");
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);

		// ����������
		WaitPictureAndMoveMouse(DataPath + "HomeKEY");
		WaitTime(1000);
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		WaitTime(1000);
		// ����Ӧ�ý���
		WaitPictureAndMoveMouse(DataPath + "ALLKEY");
		WaitTime(1000);
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		// ����APPͼ��
		SearchAPPIconAndMoveMouse(DataPath + "APPIcon", 6, 'L');
		WaitTime(1000);
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		WaitTime(1000);
		// ����APP
		System.out.println(OperationAPP());
		// while (true) {
		// if (OperationAPP()) {
		// break;
		// }
		// }

		// ����������
		WaitPictureAndMoveMouse(DataPath + "HomeKEY");
		WaitTime(1000);
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		System.out.println("Run " + RobotName + " Complete");
		return true;
	}

	public boolean OperationAPP() {
		// ���������
		if (MyRobot.FindPicture(DataPath + "S0002")) {
			WaitPictureAndMoveMouse(DataPath + "S0002");
			WaitTime(1000);
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		}
		// ����д���
		if (MyRobot.FindPicture(DataPath + "Unfortunately")) {
			MyRobot.KeyClick(VK.VK_ENTER);
			WaitTime(1000);
			MyRobot.KeyClick(VK.VK_ENTER);
			WaitPictureAndMoveMouse(DataPath + "HomeKEY");
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			return false;
		}
		// ¼��ע���û���
		if (MyRobot.FindPicture(DataPath + "RegisterUserName")) {
			RegisterUser();
		}
		// ����д���
		if (MyRobot.FindPicture(DataPath + "Unfortunately")) {
			MyRobot.KeyClick(VK.VK_ENTER);
			WaitTime(1000);
			MyRobot.KeyClick(VK.VK_ENTER);
			WaitPictureAndMoveMouse(DataPath + "HomeKEY");
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			return false;
		}
		// ȥ����ʾ��Ϣ
		if (MyRobot.FindPicture(DataPath + "Tips1")) {
			WaitPictureAndMoveMouse(DataPath + "Back");
			WaitTime(1000);
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		}
		// ����д���
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
		// ����д���
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

	// ��ÿ����Ϣ
	public void OpenDayMessage() {
		if (MyRobot.FindPicture(DataPath + "Day")) {
			WaitPictureAndMoveMouse(DataPath + "Day");
			WaitTime(1000);
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			int S = GetLimitIntRandom(5000, 10000);// �ȴ�ҳ�����
			WaitTime(S);
			WaitPictureAndMoveMouse(DataPath + "Back");
			WaitTime(1000);
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		}

	}

	// �����Ѳ���
	public void OpenReMind() {
		if (MyRobot.FindPicture(DataPath + "ReMind")) {
			WaitPictureAndMoveMouse(DataPath + "ReMind");
			WaitTime(1000);
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			// ȥ����ʾ��Ϣ
			if (MyRobot.FindPicture(DataPath + "Tips1")) {
				WaitPictureAndMoveMouse(DataPath + "Back");
				WaitTime(1000);
				MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			}
			int S = GetLimitIntRandom(10000, 15000);// �ȴ�ҳ�����
			WaitTime(S);
			WaitPictureAndMoveMouse(DataPath + "Back");
			WaitTime(1000);
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		}

	}

	// ע���û�����
	public void RegisterUser() {
		boolean Flag = true;
		while (Flag) {
			WaitPictureAndMoveMouse(DataPath + "RegisterUserName");
			WaitTime(1000);
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			WaitTime(1000);
			// ������������ַ
			String MailAddress = GetRandomMailAddress();
			MyRobot.KeyClick(MailAddress);
			WaitTime(1000);
			if (MyRobot.FindPicture(DataPath + "RegisterPassword")) {
				// ¼��ע������
				WaitPictureAndMoveMouse(DataPath + "RegisterPassword");
				WaitTime(1000);
				MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
				WaitTime(1000);
				// �������7λ����
				String Password = GetRandomStr(7);
				MyRobot.KeyClick(Password);
				WaitTime(1000);
			}

			// �������ע�ᰴť
			WaitPictureAndMoveMouse(DataPath + "RegisterButton");
			WaitTime(1000);
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			WaitTime(2000);// �ȴ�������ʾ
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
