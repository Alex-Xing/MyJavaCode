package ADBControl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import CreatNewAndroidSystem.SystemParams;

public class ADBRobot {
	// private SystemParams MyParams = new SystemParams();
	private String ADBCmdPath = SystemParams.ADBPath;

	public String Shell(String CMD) {

		String CMDStr = ADBCmdPath + " shell " + CMD.trim();
		String ReturnStr = ExecuteCMD(CMDStr).trim();
		return ReturnStr;
	}

	public String GetDevicesList() {
		String CMDStr = ADBCmdPath + " devices";
		String ReturnStr;
		ReturnStr = ExecuteCMD(CMDStr).trim();
		return ReturnStr;
	}

	public boolean CheckDevicce(String IP) {
		String TmpStr = GetDevicesList();
		if (TmpStr.indexOf(IP.trim()) < 1) {
			return false;
		}
		return true;
	}

	public boolean KillServer() {
		String CMDStr = ADBCmdPath + " kill-server";
		// String CMDStr = "taskkill /im adb.exe /f";
		String ReturnStr;
		ReturnStr = ExecuteCMD(CMDStr).trim();
		if (ReturnStr.equals("")
				|| ReturnStr.indexOf("成功: 已终止进程 \"adb.exe\"") > -1) {
			return true;
		} else {
			return false;

		}
	}

	public boolean ReStartServer() {
		if (!KillServer()) {
			return false;
		}

		String CMDStr = ADBCmdPath + " start-server";
		String ReturnStr = ExecuteCMD(CMDStr).trim();
		String TmpStr = "* daemon not running. starting it now on port 5037 *\n";
		TmpStr = TmpStr + "* daemon started successfully *";
		//
		if (ReturnStr.equals("") || ReturnStr.equals(TmpStr)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean ConnectVM(String IP) {
		String CMDStr = ADBCmdPath + " connect " + IP.trim();
		String ReturnStr, TmpStr;
		ReturnStr = ExecuteCMD(CMDStr).trim();
		TmpStr = "* daemon not running. starting it now on port 5037 *\n";
		TmpStr = TmpStr + "* daemon started successfully *\n";
		TmpStr = TmpStr + "connected to " + IP.trim() + ":5555";
		if (ReturnStr.equals("connected to " + IP.trim() + ":5555")
				|| ReturnStr.equals(TmpStr)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean ChangToRoot() {
		String CMDStr = ADBCmdPath + " root";
		String ReturnStr;
		ReturnStr = ExecuteCMD(CMDStr).trim();
		if (ReturnStr.equals("restarting adbd as root")
				|| ReturnStr.equals("adbd is already running as root")) {
			return true;
		} else {
			return false;
		}
	}

	// 获取模拟器中的文件
	public boolean PullFile(String remotePath, String localFile) {
		String CMDStr = ADBCmdPath + " pull \"" + remotePath.trim() + "\" \""
				+ localFile.trim() + "\"";
		String ReturnStr;
		ReturnStr = ExecuteCMD(CMDStr).trim();
		if (ReturnStr.equals("remote object \'" + remotePath.trim()
				+ "\' does not exist")) {
			return false;
		} else {
			return true;
		}

	}

	// 向模拟器中写文件
	public boolean pushFile(String localFile, String remotePath) {
		String CMDStr = ADBCmdPath + " push \"" + localFile.trim() + "\" \""
				+ remotePath.trim() + "\"";
		String ReturnStr;
		ReturnStr = ExecuteCMD(CMDStr).trim();
		if (ReturnStr.equals("cannot stat \'" + localFile.trim()
				+ "\': No such file or directory")) {
			return false;
		} else {
			return true;
		}

	}

	// 安装应用程序
	public boolean InstallAPP(String LocalAppPath) {
		String CMDStr = ADBCmdPath + " install -r \"" + LocalAppPath.trim()
				+ "\"";
		String ReturnStr;
		ReturnStr = ExecuteCMD(CMDStr).trim();
		if (ReturnStr.indexOf("can't find ") > 0
				|| ReturnStr.indexOf("Failure") > 0) {
			return false;
		} else {
			return true;
		}

	}

	public boolean InitADB(String IP) {
		if (!ReStartServer()) {
			return false;
		}

		if (!ConnectVM(IP)) {
			return false;
		}

		if (!ChangToRoot()) {
			return false;
		}
		if (!ConnectVM(IP)) {
			return false;
		}
		return true;
	}

	// 执行命令
	public String ExecuteCMD(String CMD) {
		String output = null;
		String error = null;
		StringBuffer outputBuffer = new StringBuffer();
		StringBuffer errorBuffer = new StringBuffer();
		try {
			Process process = Runtime.getRuntime().exec(CMD);
			errorBuffer = GetOutput(process.getErrorStream());
			outputBuffer = GetOutput(process.getInputStream());
			process.waitFor();
			output = outputBuffer.toString();
			error = errorBuffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (output.length() > 0) {
			return output;
		} else {// if (error.length() > 0) {
			return error;
		}
	}

	// 获取命令行输出
	public StringBuffer GetOutput(InputStream ins) {
		InputStream is;
		StringBuffer buffer = new StringBuffer();
		is = ins;
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				buffer.append(line);
				buffer.append("\n");
			}
			br.close();

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return buffer;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ADBRobot test = new ADBRobot();
		test.InitADB("192.168.11.144");
		test.Shell("tar -xvzf /sdcard/system.tar.gz -C /");

	}

}
