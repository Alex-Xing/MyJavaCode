package SquidControl;

import java.io.File;

import OperationAndroidSystem.SystemParams;
import SSHControl.BaseControl;

public class SetupSquid {

	public void Setup() {
		BaseControl SSH = new BaseControl();

		CreatSquidConfig SConf = new CreatSquidConfig();
		String TmpFile = SConf.GetRandomTempFilePath(SystemParams.TempPath);
		// ���ɱ�����ʱ�����ļ�
		SConf.NewConfigFile(TmpFile);
		boolean Flag = false;
		while (!Flag) {
			WaitTime(20000);
			// �ϴ������ļ�
			Flag = SSH.PutFileToRemote(SystemParams.RouteHost,
					SystemParams.RouteUser, SystemParams.RoutePassword,
					TmpFile, SystemParams.SquitConfigPath);
			System.out.println(Flag);
		}
		// ɾ��������ʱ�����ļ�
		SConf.DelTempFile(TmpFile);
		File config = new File(TmpFile);
		TmpFile = config.getName();
		// �������ļ�����Ϊ/etc/squid/squid.conf
		SSH.runRomoteScript(SystemParams.RouteHost, SystemParams.RouteUser,
				SystemParams.RoutePassword, "mv "
						+ SystemParams.SquitConfigPath + "/" + TmpFile + " "
						+ SystemParams.SquitConfigPath + "/squid.conf");
		// ����Squid����
		SSH.runRomoteScript(SystemParams.RouteHost, SystemParams.RouteUser,
				SystemParams.RoutePassword, "service squid restart");

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
		SetupSquid test = new SetupSquid();
		test.Setup();
	}

}
