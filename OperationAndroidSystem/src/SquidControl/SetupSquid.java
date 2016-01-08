package SquidControl;

import java.io.File;

import OperationAndroidSystem.SystemParams;
import SSHControl.BaseControl;

public class SetupSquid {

	public void Setup() {
		BaseControl SSH = new BaseControl();

		CreatSquidConfig SConf = new CreatSquidConfig();
		String TmpFile = SConf.GetRandomTempFilePath(SystemParams.TempPath);
		// 生成本地临时配置文件
		SConf.NewConfigFile(TmpFile);
		boolean Flag = false;
		while (!Flag) {
			WaitTime(20000);
			// 上传配置文件
			Flag = SSH.PutFileToRemote(SystemParams.RouteHost,
					SystemParams.RouteUser, SystemParams.RoutePassword,
					TmpFile, SystemParams.SquitConfigPath);
			System.out.println(Flag);
		}
		// 删除本地临时配置文件
		SConf.DelTempFile(TmpFile);
		File config = new File(TmpFile);
		TmpFile = config.getName();
		// 将配置文件更名为/etc/squid/squid.conf
		SSH.runRomoteScript(SystemParams.RouteHost, SystemParams.RouteUser,
				SystemParams.RoutePassword, "mv "
						+ SystemParams.SquitConfigPath + "/" + TmpFile + " "
						+ SystemParams.SquitConfigPath + "/squid.conf");
		// 重启Squid服务
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
