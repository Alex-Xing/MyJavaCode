package SSHControl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.SCPClient;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;

public class BaseControl {
	//public static final String SUCCESS = "SUCCESS";

	public List<String> runRomoteScript(String host, String username,
			String password, String cmd) {
		List<String> result = new ArrayList<String>();
		Connection conn = new Connection(host);
		try {
			conn.connect();
			boolean isAuthenticated = conn.authenticateWithPassword(username,
					password);
			if (isAuthenticated == false)
				throw new RuntimeException("权限不够");
			Session sess = conn.openSession();
			sess.execCommand(cmd);
			InputStream stdout = new StreamGobbler(sess.getStdout());
			BufferedReader br = new BufferedReader(
					new InputStreamReader(stdout));
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				System.out.println(line);
				result.add(line);
			}
			br.close();
			sess.close();
			conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public boolean GetFileFromRemote(String host, String username,
			String password, String romoteFileName, String localDir) {
		try {
			Connection conn = new Connection(host);
			conn.connect();
			boolean isAuthenticated = conn.authenticateWithPassword(username,
					password);
			if (isAuthenticated == false) {
				throw new RuntimeException("权限不够!");
			}
			File inputFile = new File(localDir);
			if (!inputFile.exists()) // 如果文件夹不存在，则新建文件夹
			{
				inputFile.mkdirs();
			}
			SCPClient scpClient = conn.createSCPClient();
			scpClient.get(romoteFileName, localDir);
			conn.close();
		} catch (IOException e) {
			// return "出现了IO错误!";
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean PutFileToRemote(String host, String username,
			String password, String localFileName, String romoteDir) {
		try {
			Connection conn = new Connection(host);
			conn.connect();
			boolean isAuthenticated = conn.authenticateWithPassword(username,
					password);
			if (isAuthenticated == false) {
				throw new RuntimeException("权限不够!");
			}
			File inputFile = new File(localFileName);
			if (!inputFile.exists()) // 如果文件，则退出
			{
				return false;
			}
			SCPClient scpClient = conn.createSCPClient();
			scpClient.put(localFileName, romoteDir);
			conn.close();
		} catch (IOException e) {
			// return "出现了IO错误!";
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BaseControl test=new BaseControl();
		test.GetFileFromRemote("10.10.0.1", "root", "951753", "~/test.txt",
				"D:/android-x86");

	}

}
