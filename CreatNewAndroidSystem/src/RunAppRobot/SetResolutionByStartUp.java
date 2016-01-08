package RunAppRobot;

import java.awt.event.InputEvent;

import FindPictureLocation.FindPictureLocation;
import FindPictureLocation.captureDesktop;
import KeyBoardMouseControl.VK;

//InputEvent.BUTTON1_DOWN_MASK ���
//InputEvent.BUTTON2_DOWN_MASK �м�
//InputEvent.BUTTON3_DOWN_MASK �Ҽ�
//����VMʱ�޸ķֱ���
public class SetResolutionByStartUp extends BaseRobot {
	public FindPictureLocation MouseLocation = new FindPictureLocation();

	public SetResolutionByStartUp() {
		RobotName = "SetResolutionByStartUp";
	}

	public Boolean Run() {
		
		// ����VMΪ��ǰ����
		WaitPictureAndMoveMouse(DataPath + "S0001");
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);

		// �ȴ�Android��������
		WaitPictureAndMoveMouse(DataPath + "S0003.png");
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);

		MyRobot.KeyClick("ee vga=ask");
		MyRobot.KeyClick(VK.VK_ENTER);
		MyRobot.KeyClick("b");

		// �ȴ�ѡ��ֱ������ô���
		WaitPicture(DataPath + "S0004.png");
		MyRobot.KeyClick(VK.VK_ENTER);
		WaitTime(1000);
		MyRobot.KeyClick("360");
		MyRobot.KeyClick(VK.VK_ENTER);

		// �ȴ���������
		boolean Flag = true;
		while (Flag) {
			if (MyRobot.FindPicture(DataPath + "Lock")) {
				
				// ��������
				String TmpName = MyRobot.FindPictureFolder(DataPath + "Lock");
				MyRobot.MoveMouseToPicture(DataPath + "Lock");
				System.out.println("��λ�ɹ���" + DataPath + "Lock");
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
				System.out.println("�޷���λ��" + DataPath + "Lock");
				if (MyRobot.MoveMouseToPicture(DataPath + "Init-OK")) {
					Flag = false;
					System.out.println("�޽�������!");
					// ���OK��ť
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
		// ��������
		WaitPictureAndMoveMouse(DataPath + "HomeKEY");
		while (Flag) {
			if (MyRobot.MoveMouseToPicture(DataPath + "HomeKEY")) {
				System.out.println("��λ�ɹ���" + DataPath + "HomeKEY");
				Flag = false;
			} else {
				WaitTime(1000);
				System.out.println("�޷���λ��" + DataPath + "HomeKEY");
			}
		}
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
		WaitTime(1000);
		//��������
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
		// �ȴ���������
		WaitTime(2000);

		// ���������OK��ť���£�û�о�����
		if (MyRobot.MoveMouseToPicture(DataPath + "Init-OK")) {
			WaitTime(1000);
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			System.out.println("��λ�ɹ���" + DataPath + "Init-OK");
		}
		// ����д���
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
		// ����еڶ�������OK��ť���£�û�о�����
		if (MyRobot.MoveMouseToPicture(DataPath + "Init-OK")) {
			WaitTime(1000);
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			System.out.println("��λ�ɹ���" + DataPath + "Init-OK");
		}
		// ����д���
		if (MyRobot.FindPicture(DataPath + "Unfortunately")) {
			MyRobot.KeyClick(VK.VK_ENTER);
			WaitTime(1000);
			MyRobot.KeyClick(VK.VK_ENTER);
			WaitTime(1000);
			WaitPictureAndMoveMouse(DataPath + "HomeKEY");
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			return false;
		}

		// ����Ӧ�ý��棬����Ƿ�������OK��ť
		WaitPictureAndMoveMouse(DataPath + "ALLKEY");
		WaitTime(1000);
		MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);

		// ����д���
		if (MyRobot.FindPicture(DataPath + "Unfortunately")) {
			MyRobot.KeyClick(VK.VK_ENTER);
			WaitTime(1000);
			MyRobot.KeyClick(VK.VK_ENTER);
			WaitTime(1000);
			WaitPictureAndMoveMouse(DataPath + "HomeKEY");
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			return false;
		}
		// �ȴ�����Ӧ�ý���
		WaitTime(3000);
		// ���������OK��ť���£�û�о�����
		if (MyRobot.FindPicture(DataPath + "Unfortunately")) {
			WaitTime(1000);
			MyRobot.Mouseclick(InputEvent.BUTTON1_DOWN_MASK);
			System.out.println("��λ�ɹ���" + DataPath + "Init-OK");
		}

		// ����д���
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
