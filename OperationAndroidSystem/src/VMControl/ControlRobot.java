//负责VM管理
package VMControl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import OperationAndroidSystem.SystemParams;

public class ControlRobot {
	//private SystemParams MyParams = new SystemParams();
	private String VBMCmdPath = SystemParams.VBMCmdPath;
	private String Bridged_Interface = SystemParams.Bridged_Interface;
	private String Host_Only_Interface = SystemParams.Host_Only_Interface;

	public Boolean SetCustomVideoMode(String VM_Name, String resolution) {
		String CMDStr = VBMCmdPath + "  setextradata \"" + VM_Name.trim()
				+ "\" \"CustomVideoMode1\" \"" + resolution + "x16\"";
		// System.out.print(CMDStr + "\r\n");
		String ReturnStr;
		ReturnStr = ExecuteCMD(CMDStr).trim();

		if (ReturnStr.equals("")) {
			return true;
		} else {
			return false;
		}

	}

	// 按名称重启指定的VM
	public Boolean ResetVMByName(String Name) {
		String CMDStr = VBMCmdPath + " controlvm \"" + Name + "\" reset";
		// System.out.print(CMDStr + "\r\n");
		String ReturnStr;
		ReturnStr = ExecuteCMD(CMDStr).trim();

		if (ReturnStr.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	// 按名称关闭指定的VM
	public Boolean PowerOffVMByName(String Name) {
		String CMDStr = VBMCmdPath + " controlvm \"" + Name + "\" poweroff";
		// System.out.print(CMDStr + "\r\n");
		String ReturnStr;
		ReturnStr = ExecuteCMD(CMDStr).trim();

		if (ReturnStr
				.equals("0%...10%...20%...30%...40%...50%...60%...70%...80%...90%...100%")) {
			return true;
		} else {
			return false;
		}
	}

	// 按名称启动指定的VM
	public Boolean StartVMByName(String Name) {
		return StartVMByName(Name,0);

	}
	// 按名称启动指定的VM
		public Boolean StartVMByName(String Name,int RunType) {
			String CMDStr = VBMCmdPath + " startvm \"" + Name + "\"";
			switch (RunType){
			case 0:
				CMDStr = CMDStr +" --type gui";
				break;
			case 1:
				CMDStr = CMDStr +" --type headless";
				break;
			case 2:
				CMDStr = CMDStr +" --type sdl";
				break;
				
			}
			//System.out.print(CMDStr + "\r\n");
			String ReturnStr;
			ReturnStr = ExecuteCMD(CMDStr).trim();

			if (ReturnStr.indexOf("successfully")>1) {
				return true;
			} else {
				return false;
			}
		}

	public Boolean SetVM_InternalNetworkForName(String VM_Name) {
		return SetVM_InternalNetworkForName(VM_Name, 1, GetRamdonMac());
	}

	// 设置新VM的网络为内部网格模式并指定一个随机MAC地址
	public Boolean SetVM_InternalNetworkForName(String Name, int AdapterNumber,
			String Mac) {
		String CMDStr = VBMCmdPath + " modifyvm \"" + Name + "\" --nic"
				+ String.valueOf(AdapterNumber).trim() + " intnet --intnet"
				+ String.valueOf(AdapterNumber).trim() + " \"intnet\""
				+ " --macaddress" + String.valueOf(AdapterNumber).trim() + " "
				+ Mac;
		// System.out.print(CMDStr + "\r\n");
		String ReturnStr;
		ReturnStr = ExecuteCMD(CMDStr).trim();

		if (ReturnStr.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	// 设置新VM的网络为HostOnly网格模式并指定一个随机MAC地址
	public Boolean SetVM_HostOnlyNetworkForName(String Name, int AdapterNumber,
			String Mac) {
		String CMDStr = VBMCmdPath + " modifyvm \"" + Name + "\" --nic"
				+ String.valueOf(AdapterNumber).trim()
				+ " hostonly --hostonlyadapter"
				+ String.valueOf(AdapterNumber).trim() + " "
				+ Host_Only_Interface + " --macaddress"
				+ String.valueOf(AdapterNumber).trim() + " " + Mac;
		//System.out.print(CMDStr + "\r\n");
		String ReturnStr;
		ReturnStr = ExecuteCMD(CMDStr).trim();

		if (ReturnStr.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	// 设置新VM的网络为桥接模式并指定一个随机MAC地址
	public Boolean SetVM_NetBridgedNetworkForName(String Name,
			int AdapterNumber, String Mac) {
		String CMDStr = VBMCmdPath + " modifyvm \"" + Name + "\" --nic"
				+ String.valueOf(AdapterNumber).trim()
				+ " bridged --bridgeadapter"
				+ String.valueOf(AdapterNumber).trim() + " "
				+ Bridged_Interface + " --macaddress"
				+ String.valueOf(AdapterNumber).trim() + " " + Mac;
		// System.out.print(CMDStr + "\r\n");
		String ReturnStr;
		ReturnStr = ExecuteCMD(CMDStr).trim();

		if (ReturnStr.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	// 克隆新的VM并设置好网络
	public Boolean InitVMS(String VM_Name, String New_VM_Name) {
		if (!CloneVMSforName(VM_Name, New_VM_Name)) {
			return false;
		}
		if (!SetVM_HostOnlyNetworkForName(New_VM_Name, 1, GetRamdonMac())) {
			return false;
		}
		return true;
	}

	// 克隆新的VM
	public Boolean CloneVMSforName(String VM_Name, String New_VM_Name) {
		String CMDStr = VBMCmdPath + " clonevm \"" + VM_Name + "\" --name \""
				+ New_VM_Name + "\" --register";
		//System.out.println(CMDStr);
		String ReturnStr;
		ReturnStr = ExecuteCMD(CMDStr).trim();
		String TmpStr;
		TmpStr = "Machine has been successfully cloned as \"" + New_VM_Name
				+ "\"";
		if (ReturnStr.equals(TmpStr)) {
			return true;
		} else {
			return false;
		}
	}

	// 获取随机MAC地址
	public static String GetRamdonMac() {
		Random rnd1 = new Random();
		String Mac = Integer.toHexString(rnd1.nextInt(16));
		// MAC 地址第二位必需为偶数
		int I;
		int T_Number = 1;
		while ((T_Number % 2) != 0) {
			T_Number = rnd1.nextInt(16);
		}
		Mac = Mac + Integer.toHexString(T_Number);
		for (I = 0; I < 10; I++) {
			Mac = Mac + Integer.toHexString(rnd1.nextInt(16));
		}

		return Mac;
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
		/*
		 * int i; for (i = 0; i < 10; i++) System.out.print(GetRamdonMac() +
		 * "\r\n");
		 */
		ControlRobot Tmp = new ControlRobot();
		 System.out.print(Tmp.StartVMByName("Debian Route Server"));
		// System.out.print(Tmp.CloneVMSforName("S_Android", "new_test"));
		//System.out.print(Tmp.SetVM_HostOnlyNetworkForName("aa", 1,GetRamdonMac()));
		// System.out.print(Tmp.InitVMS("S_Android", "new_test"));
		// System.out.print(Tmp.ResetVMByName("5-1281195362"));
	}

}
