package CreatNewAndroidSystem;

import java.util.List;

import DataOperation.DataConnect;
import SSHControl.BaseControl;
import VMControl.ControlRobot;

public class CreatNewAndroidSystem {
	//private SystemParams MyParams = new SystemParams();
	private DataConnect conn=new DataConnect();

	public void NewConn() {
		conn.GetConn(SystemParams.DatabaseHose, SystemParams.DatabasePort,
				SystemParams.DatabaseName, SystemParams.DatabaseUser,
				SystemParams.DatabasePassword);
	}

	public void Run() {
		ControlRobot NewControlRobot = new ControlRobot();
		NewControlRobot.StartVMByName(SystemParams.RouteVMName,1);
		BaseControl SSH = new BaseControl();
		List<String> ReturnStr=null;
		boolean Flag = true;
		while (Flag) {
			WaitTime(20000);
			// ÷¥––≤‚ ‘÷∏¡Ó
			ReturnStr = SSH.runRomoteScript(SystemParams.RouteHost, SystemParams.RouteUser,
					SystemParams.RoutePassword, "pwd");
			Flag=ReturnStr.isEmpty();
			//System.out.println(Flag);
		}
		
		NewConn();
		CreatNewSystemInfo CreatInfo = new CreatNewSystemInfo();
		CreatInfo.SetConn(conn);
		CreatInfo.CreatVMInfo();
			
		CreatAndroidVM CreatVM = new CreatAndroidVM();
		CreatVM.SetConn(conn);
		CreatVM.CreatNewVM();
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
		CreatNewAndroidSystem MyRobot=new CreatNewAndroidSystem();
		MyRobot.Run();

	}

}
