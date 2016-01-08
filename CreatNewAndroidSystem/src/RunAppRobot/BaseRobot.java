package RunAppRobot;

import java.awt.event.InputEvent;
import java.util.Random;

import CreatNewAndroidSystem.SystemParams;
import DataOperation.DataConnect;
import FindPictureLocation.FindPictureLocation;
import KeyBoardMouseControl.AutoRobot;

//InputEvent.BUTTON1_DOWN_MASK ���
//InputEvent.BUTTON2_DOWN_MASK �м�
//InputEvent.BUTTON3_DOWN_MASK �Ҽ�
public class BaseRobot {
	public SystemParams MyParams = new SystemParams();
	public String DataPath = SystemParams.RobotDataPath;
	public AutoRobot MyRobot = new AutoRobot();
	public String SourceVM_Name = "";
	public String RobotName = "";
	public DataConnect conn = new DataConnect();

	public void SetConn(DataConnect dataconn) {
		conn = dataconn;
	}

	public void NewConn() {
		conn.GetConn(SystemParams.DatabaseHose, SystemParams.DatabasePort,
				SystemParams.DatabaseName, SystemParams.DatabaseUser,
				SystemParams.DatabasePassword);
	}

	public void SetBaseDataPath(String SVM_Name) {
		SetBaseDataPath(SVM_Name, RobotName);
	}

	public void SetBaseDataPath(String SVM_Name, String RobotName) {
		SourceVM_Name = SVM_Name;
		DataPath = DataPath + SourceVM_Name + "/" + RobotName + "/";
		// System.out.println(DataPath);
	}

	public void WaitTime(int S) {
		try {
			Thread.sleep(S);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	public void WaitPicture(String PicName) {
		boolean Flag = true;
		while (Flag) {
			if (MyRobot.FindPicture(PicName)) {
				System.out.println("��λ�ɹ���" + PicName);
				Flag = false;
			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				System.out.println("�޷���λ��" + PicName);
			}
		}
	}

	public void WaitPictureAndMoveMouse(String PicName) {
		boolean Flag = true;
		while (Flag) {
			if (MyRobot.MoveMouseToPicture(PicName)) {
				System.out.println("��λ�ɹ���" + PicName);
				Flag = false;
			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				System.out.println("�޷���λ��" + PicName);
			}
		}
	}

	// ��������֮�����һ�������
	public int GetLimitIntRandom(int Lower_limit, int Upper_limit) {
		Random random = new Random();
		int Total = random.nextInt(Upper_limit)
				% (Upper_limit - Lower_limit + 1) + Lower_limit;
		return Total;
	}

	// ��������֮�����һ��ָ����С��������飬Duplicates���Ƿ�����ظ��ı�־
	public int[] GetLimitIntRandom(int Lower_limit, int Upper_limit,
			int ArrayCount, boolean Duplicates) {
		int[] TmpIndex = new int[ArrayCount];

		TmpIndex[0] = GetLimitIntRandom(Lower_limit, Upper_limit);
		for (int i = 0; i < ArrayCount; i++) {
			int ReadIndex = GetLimitIntRandom(Lower_limit, Upper_limit);// �������Ҫ�����ķ���������
			if (!Duplicates) {
				for (int j = 0; j < i; j++) {
					if (TmpIndex[j] == ReadIndex) {// ����������Ѿ��ù�������������
						j = -1;
						ReadIndex = GetLimitIntRandom(Lower_limit, Upper_limit);
					}
				}
			}
			TmpIndex[i] = ReadIndex;
			// System.out.println(i + ":" + ReadIndex);
		}
		return TmpIndex;
	}

	public boolean SearchAPPIconAndMoveMouse(String PicName, int PagerNumber,
			char way) {
		// ȷ���ص�Ӧ�ý����һҳ
		MovePageToFirst();
		int i = 0;
		for (i = 0; i < PagerNumber; i++) {
			if (MyRobot.MoveMouseToPicture(PicName)) {
				System.out.println("��λ�ɹ���" + PicName);
				return true;
			} else {
				switch (way) {
				case 'L':
					PageLeft();
					WaitTime(1);
					break;
				case 'R':
					PageRight();
					WaitTime(1);
					break;
				}
			}
		}
		return false;
	}

	public void MovePageToFirst() {
		int i;
		for (i = 0; i < 5; i++) {
			PageLeft();
		}
	}

	public void PageRight() {
		FindPictureLocation MouseLocation = new FindPictureLocation();

		MyRobot.MoveMouseToPicture(DataPath + "Function");
		String TmpName = MyRobot.FindPictureFolder(DataPath + "Function");
		int[] Tmp = new int[2];
		Tmp = MyRobot.GetMouseLocation();
		MyRobot.MoveMouse(Tmp[0],
				Tmp[1] - (MouseLocation.GetPictureHeight(TmpName) * 3));
		MyRobot.MousePresss(InputEvent.BUTTON1_DOWN_MASK);
		WaitTime(50);// ����Ҫ�ȴ�����VMû��ӳ
		MyRobot.MoveMouse(
				Tmp[0] - (MouseLocation.GetPictureWidth(TmpName) * 5), Tmp[1]
						- (MouseLocation.GetPictureHeight(TmpName) * 3));
		MyRobot.MouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}

	public void PageLeft() {

		FindPictureLocation MouseLocation = new FindPictureLocation();
		String TmpName = MyRobot.FindPictureFolder(DataPath + "Back");
		MyRobot.MoveMouseToPicture(DataPath + "Back");

		int[] Tmp = new int[2];
		Tmp = MyRobot.GetMouseLocation();
		MyRobot.MoveMouse(Tmp[0],
				Tmp[1] - (MouseLocation.GetPictureHeight(TmpName) * 3));
		MyRobot.MousePresss(InputEvent.BUTTON1_DOWN_MASK);
		WaitTime(50);// ����Ҫ�ȴ�����VMû��ӳ
		MyRobot.MoveMouse(
				Tmp[0] + (MouseLocation.GetPictureWidth(TmpName) * 5), Tmp[1]
						- (MouseLocation.GetPictureHeight(TmpName) * 3));
		MyRobot.MouseRelease(InputEvent.BUTTON1_DOWN_MASK);

	}

	// ����ָ�����ȵ����ȫ�ַ����ַ���
	public String GetRandomStr(int Len) {
		String NotIncludeStr = null;
		return GetRandomStr(Len, NotIncludeStr);
	}

	// ����ָ�����ȵ����ȫ�����ַ���
	public String GetRandomNumberStr(int Len) {
		String NotIncludeStr = "`~!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?"
				+ "abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		return GetRandomStr(Len, NotIncludeStr);
	}

	// ����ָ�����ȵ����ȫ��ĸ�ַ���
	public String GetRandomCharStr(int Len) {
		String NotIncludeStr = "`~!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?"
				+ "1234567890";
		return GetRandomStr(Len, NotIncludeStr);
	}

	// ����ָ�����ȵ����ȫ��д��ĸ�ַ���
	public String GetRandomUpperCaseCharStr(int Len) {
		String NotIncludeStr = "`~!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?"
				+ "abcdefghijklmnopqrstuvwxyz" + "1234567890";
		return GetRandomStr(Len, NotIncludeStr);
	}

	// ����ָ�����ȵ����ȫСд��ĸ�ַ���
	public String GetRandomLowerCaseCharStr(int Len) {
		String NotIncludeStr = "`~!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?"
				+ "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "1234567890";
		return GetRandomStr(Len, NotIncludeStr);
	}

	// ����ָ�����ȵ����ȫ�����ַ���
	public String GetRandomSymbolStr(int Len) {
		String NotIncludeStr = "abcdefghijklmnopqrstuvwxyz"
				+ "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "1234567890";
		return GetRandomStr(Len, NotIncludeStr);
	}

	// ����ָ�����ȵ�����ַ���
	public String GetRandomStr(int Len, String NotInclude) {
		String SString = "`~!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?"
				+ "abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
				+ "1234567890";
		String DString = "";
		String TmpStr = "";
		for (int I = 0; I < Len; I++) {
			while (true) {
				int Index = GetLimitIntRandom(0, SString.length());
				TmpStr = SString.substring(Index, Index + 1);
				if (NotInclude == null || NotInclude == ""
						|| NotInclude.indexOf(TmpStr) < 0) {
					DString = DString + TmpStr;
					break;
				}
			}
		}
		return DString;
	}

	// �����������
	public String GetRandomMailAddress() {
		int Index = GetLimitIntRandom(7, 15);
		String Name = GetRandomStr(Index, "`~!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?");
		Index = GetLimitIntRandom(4, 8);
		String HostName = GetRandomStr(Index,
				"`~!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?");
		String TailName = GetRandomStr(3, "`~!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?"
				+ "1234567890");
		return Name + "@" + HostName + "." + TailName;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
