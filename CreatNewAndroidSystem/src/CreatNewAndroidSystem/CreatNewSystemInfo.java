package CreatNewAndroidSystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import DataOperation.DataConnect;

import com.mysql.jdbc.Statement;

public class CreatNewSystemInfo {
	// private SystemParams MyParams = new SystemParams();
	private DataConnect conn = new DataConnect();

	public void SetConn(DataConnect dataconn) {
		conn = dataconn;
	}

	public void NewConn() {
		conn.GetConn(SystemParams.DatabaseHose, SystemParams.DatabasePort,
				SystemParams.DatabaseName, SystemParams.DatabaseUser,
				SystemParams.DatabasePassword);
	}

	public void CreatVMInfo() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String NowDate = df.format(new Date());
		// System.out.print(NowDate);
		int Count = 0;
		try {
			Statement statement = (Statement) conn.GetConn().createStatement();
			String sql = "SELECT count(*) as \"Count\" FROM `new_vm_system_info` where Creat_Time like \""
					+ NowDate.trim() + "%\" ";
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				Count = rs.getInt("Count");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (Count > 0) {
			int Total = Integer.parseInt(conn.LoadInitData("Today added"));
			if (Total == Count) {
				return;
			} else if (Total > Count) {
				CreatSystemInfo((Total - Count), new Date());
			} else if (Total < Count) {
				conn.UpdateOperationParams("Today added", String.valueOf(Count)
						.trim());
			}

		} else {
			CreatAllSystemInfo();
		}
	}

	// 建立当天全部VM的信息
	public void CreatAllSystemInfo() {
		int Upper_limit;
		int Lower_limit;
		int Total;
		// String WorkingDate;

		Upper_limit = Integer.parseInt(conn
				.LoadInitData("Every Day Creat Robot Upper limit"));
		Lower_limit = Integer.parseInt(conn
				.LoadInitData("Every Day Creat Robot Lower limit"));
		Random random = new Random();
		// 在上下限之间随机产生当天的新增VM总数
		Total = random.nextInt(Upper_limit) % (Upper_limit - Lower_limit + 1)
				+ Lower_limit;
		CreatSystemInfo(Total, new Date());
	}

	// 建立指定数量的VM信息
	public void CreatSystemInfo(int Total, Date CreatDate) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String WorkingDate = df.format(new Date());// new Date()为获取当前系统时间
		// System.out.println(Total + ";" + WorkingDate);

		// 更新运营参数表中的当天新增总数
		conn.UpdateOperationParams("Today added", String.valueOf(Total).trim());
		// 更新运营参数表中的最后新增操作时间
		conn.UpdateOperationParams("Last Creat Systm Date", WorkingDate.trim());

		int i = 0;
		// Random rnd1 = new Random();
		String TmpStr[] = new String[Total];
		TmpStr = GetRandomDeviceId(Total);
		for (i = 0; i < Total; i++) {
			// CreatNewSystemInfoByDate(String.valueOf(i).trim() + "-" +
			// String.valueOf(rnd1.nextInt()).trim());
			CreatNewSystemInfoByDate(TmpStr[i]);
		}
	}

	// 建立单个VM信息
	public Boolean CreatNewSystemInfoByDate(String VMName) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String VM_Name = VMName;
		String Base_VM_Name = GetBase_VM_Name();
		String Creat_Time = df.format(new Date());// new Date()为获取当前系统时间
		;
		String App_List = "";
		String TR = GetRequest_App_List();
		String TM = GetM_App_List();
		String TO = GetOther_App_List();
		if (TR.length() > 0) {
			App_List = App_List + TR + ";";
		}
		if (TM.length() > 0) {
			App_List = App_List + TM + ";";
		}
		if (TO.length() > 0) {
			App_List = App_List + TO + ";";
		}
		if (App_List.substring(App_List.length() - 1).equals(";")) {
			App_List = App_List.substring(0, App_List.length() - 1);
		}

		try {

			Statement statement = (Statement) conn.GetConn().createStatement();
			String sql = "INSERT INTO `android`.`new_vm_system_info` ("
					+ "`VM_Name`," + " `Base_VM_Name`, " + "`App_List`, "
					+ "`Creat_Time`, " + "`Created_Flag`)" + " VALUES (" + "'"
					+ VM_Name.trim() + "', " + "'" + Base_VM_Name.trim()
					+ "', " + "'" + App_List.trim() + "', " + "'"
					+ Creat_Time.trim() + "', " + "'0');";
			// System.out.println(sql);
			int rs = statement.executeUpdate(sql);
			// System.out.println(rs);
			if (rs > 0) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	// 产生必需要安装的APP列表
	public String GetRequest_App_List() {

		try {
			Statement statement = (Statement) conn.GetConn().createStatement();
			String sql = "SELECT * FROM `app_info` where type=\""+SystemParams.Request_App+"\"";
			ResultSet rs = statement.executeQuery(sql);
			String List = "";
			while (rs.next()) {
				List = List + rs.getString("Signature") + ";";
			}
			if (List.length() > 1) {
				List = List.substring(0, List.length() - 1);
			}
			rs.close();
			return List;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 随机产生要安装的收钱APP列表
	public String GetM_App_List() {
		int M_APP_Upper_limit = Integer.parseInt(conn
				.LoadInitData("M_APP Upper limit"));
		int M_APP_Lower_limit = Integer.parseInt(conn
				.LoadInitData("M_APP Lower limi"));
		Random random = new Random();
		// 在上下限之间随机产生要安装的收钱APP总数
		int M_APP_Total = random.nextInt(M_APP_Upper_limit)
				% (M_APP_Upper_limit - M_APP_Lower_limit + 1)
				+ M_APP_Lower_limit;
		// System.out.println(M_APP_Total);
		String List = GetApp_List(SystemParams.M_App, M_APP_Total);
		// System.out.println(List);
		return List;

	}

	// 随机产生要安装的其它APP列表
	public String GetOther_App_List() {
		int Other_APP_Upper_limit = Integer.parseInt(conn
				.LoadInitData("other APP Upper limit"));
		int Other_APP_Lower_limit = Integer.parseInt(conn
				.LoadInitData("other APP Lower limit"));
		Random random = new Random();
		// 在上下限之间随机产生要安装的其它APP总数
		int Other_APP_Total = random.nextInt(Other_APP_Upper_limit)
				% (Other_APP_Upper_limit - Other_APP_Lower_limit + 1)
				+ Other_APP_Lower_limit;
		// System.out.println(Other_APP_Total);
		String List = GetApp_List(SystemParams.Other_App, Other_APP_Total);
		// System.out.println(List);
		return List;
	}

	// 按类型取得App的列表
	public String GetApp_List(String type, int Count) {
		try {
			Statement statement = (Statement) conn.GetConn().createStatement();
			String sql = "SELECT * FROM `app_info` where type=\"" + type.trim()
					+ "\" ORDER BY RAND() LIMIT "
					+ String.valueOf(Count).trim();
			ResultSet rs = statement.executeQuery(sql);
			String List = "";
			while (rs.next()) {
				List = List + rs.getString("Signature") + ";";
			}
			if (List.length() > 1) {
				List = List.substring(0, List.length() - 1);
			}
			rs.close();
			return List;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 根据Index取得Base_VM_Name
	public String GetBase_VM_Name() {
		try {
			Statement statement = (Statement) conn.GetConn().createStatement();
			String sql = "SELECT Signature FROM `system_prop` WHERE Enabled=true order by RAND() LIMIT 1";
			ResultSet rs = statement.executeQuery(sql);
			String Base_VM_Name = null;
			while (rs.next()) {
				Base_VM_Name = rs.getString("Signature");
			}
			rs.close();
			return Base_VM_Name;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	// 随机获取指定数量不重复的DeviceId
	public String[] GetRandomDeviceId(int Total) {
		String Tmp[] = new String[Total];

		int i, j;
		boolean falg;
		for (i = 0; i < Total; i++) {
			String RanStr = new String();
			falg = true;
			while (falg) {
				RanStr = GetRandomNumberStr(15);
				falg = false;
				for (j = 0; j < Total; j++) {
					if (Tmp[j] == null) {
						// continue;
						break;
					}
					if (Tmp[j].equals(RanStr)) {
						falg = true;
						break;
					}
				}
			}
			Tmp[i] = RanStr;
			System.out.println(Tmp[i]);
		}
		return Tmp;
	}

	// 随机获取一个指定长度的数字字符串
	public String GetRandomNumberStr(int Len) {
		Random random = new Random();
		int i, j;
		String Tmp = "";
		for (i = 0; i < Len; i++) {
			j = random.nextInt(10);
			Tmp = Tmp + String.valueOf(j).trim();
			// System.out.println(j);
		}
		return Tmp;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CreatNewSystemInfo test = new CreatNewSystemInfo();
		test.NewConn();
		// int i;
		// for(i=0;i<20;i++)
		// test.GetRandomDeviceId(15);
		test.CreatVMInfo();
	}

}
