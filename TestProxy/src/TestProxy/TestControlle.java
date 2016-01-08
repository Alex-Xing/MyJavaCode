package TestProxy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import DataOperation.DataConnect;

import com.mysql.jdbc.Statement;

public class TestControlle {
	private DataConnect MyConn = new DataConnect();
	// private SystemParams MyParams = new SystemParams();

	private String TargetURL = "http://www.ip.cn/";
	private ExecutorService fixedThreadPool = null;// Executors.newFixedThreadPool(10);

	public TestControlle() {
		MyConn.GetConn(SystemParams.DatabaseHose, SystemParams.DatabasePort,
				SystemParams.DatabaseName, SystemParams.DatabaseUser,
				SystemParams.DatabasePassword);
	}

	public void Run(int Tcount) {
		// System.out.println("P:"+Runtime.getRuntime().availableProcessors());
		// fixedThreadPool =
		// Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		fixedThreadPool = Executors.newFixedThreadPool(Tcount);
		try {
			Statement statement = (Statement) MyConn.GetConn()
					.createStatement();
			String sql = "SELECT IPAddress FROM `proxy_list`";
			ResultSet rs = statement.executeQuery(sql);
			String[] ProxyInfo = new String[Tcount];
			int I = 0;
			while (rs.next()) {
				ProxyInfo[I] = rs.getString("IPAddress");
				if (I >= Tcount - 1) {
					ProxyTest(ProxyInfo);
					System.out.println(rs.getRow());
					I = 0;
					ProxyInfo = new String[Tcount];
				} else {
					I++;
				}
			}
			ProxyTest(ProxyInfo);
			System.out.println(rs.getRow());
			rs.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fixedThreadPool.shutdown();
	}

	public void ProxyTest(String[] IPStr) {
		int I;
		int Total = 0;
		for (I = 0; I < IPStr.length; I++) {
			if (IPStr[I] == null || IPStr[I].indexOf(":") < 7) {
				continue;
			} else {
				Total++;
			}

		}
		if (Total < 1) {
			return;
		}

		String[] TmpIPStr = new String[Total];
		ProxyTestRobot[] Tester = new ProxyTestRobot[Total];
		Total = 0;
		for (I = 0; I < IPStr.length; I++) {
			if (IPStr[I] == null || IPStr[I].indexOf(":") < 7) {
				continue;
			} else {
				TmpIPStr[Total] = IPStr[I];
				Total++;
			}
		}

		for (I = 0; I < TmpIPStr.length; I++) {
			String[] IPInfo = new String[2];
			IPInfo = TmpIPStr[I].split(":");
			Tester[I] = new ProxyTestRobot();
			Tester[I].Init(TargetURL, IPInfo[0], Integer.parseInt(IPInfo[1]),
					10);
			fixedThreadPool.execute(Tester[I]);
		}

		boolean Flag = true;
		while (true) {
			Flag = false;
			for (I = 0; I < Tester.length; I++) {
				if (Tester[I].GetProcessStatus()) {
					Flag = true;
					break;
				}
			}
			if (Flag) {
				WaitTime(1000);
			} else {
				break;
			}
		}
		// Update Result an clear all
		for (I = 0; I < Tester.length; I++) {
			UpdateTestResult(Tester[I].GetTestResult());
			Tester[I] = null;
		}
	}

	//更新数据库中的测试结果
	public void UpdateTestResult(String[] Result) {
		String IPStr = Result[0] + ":" + Result[1];
		String Speed = Result[2];
		String Work = Result[3];
		String Type = Result[4];
		try {
			Statement statement = (Statement) MyConn.GetConn()
					.createStatement();
			String sql = "UPDATE `proxy_list` SET " + "`Speed` = '"
					+ Speed.trim() + "', "
					+
					// "`Location` = '"+Location.trim()+"', " +
					"`CheckTime` = now(), " + "`Work` = '" + Work.trim()
					+ "', ";
			if (Type == null || Type.equals("NULL")) {
				sql = sql + "`Type` = NULL ";
			} else {
				sql = sql + "`Type` = '" + Type.trim() + "' ";
			}

			sql = sql + "WHERE `IPAddress` = '" + IPStr.trim() + "'; ";
			//System.out.println(sql);
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
	}

	public void WaitTime(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestControlle test = new TestControlle();
		test.Run(5);

		// ScheduledExecutorService scheduledThreadPool =
		// Executors.newScheduledThreadPool(1);
		// scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
		//
		// @Override
		// public void run() {
		// TestControlle test = new TestControlle();
		// test.Run(10);
		// }
		// },0, 1, TimeUnit.DAYS);
	}
}
