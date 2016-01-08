package DataOperation;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class DataConnect {
	private String driver = "com.mysql.jdbc.Driver";
	private Connection conn;
	private String user = "";
	private String password = "";
	private String url = "jdbc:mysql://";

	
	public Boolean LoadDrive() {
		try {
			// 调用Class.forName()方法加载驱动程序
			Class.forName(driver);
			// System.out.println("成功加载MySQL驱动！");
			return true;
		} catch (ClassNotFoundException e1) {
			// System.out.println("找不到MySQL驱动!");
			e1.printStackTrace();
			return false;
		}
	}

	public Connection GetConn() {
		return conn;
	}

	public Connection GetConn(String Host, int Prot, String DataBase,
			String User, String Password) {
		url = url + Host.trim() + ":" + String.valueOf(Prot).trim() + "/"
				+ DataBase.trim();
		user = User;
		password = Password;
		if (!LoadDrive()) {
			conn = null;
			return null;
		}
		try {
			conn = (Connection) DriverManager
					.getConnection(url, user, password);
			if (conn.isClosed()) {
				conn = null;
				return null;
			}
			return conn;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public String LoadInitData(String Param_Name) {
		try {
			if (conn == null || conn.isClosed()) {
				return null;
			} else {
				Statement statement = (Statement) conn.createStatement();
				String sql = "select Value from operation_params where Param_Name=\""
						+ Param_Name.trim() + "\"";
				ResultSet rs = statement.executeQuery(sql);
				String value = "";
				while (rs.next()) {
					value = rs.getString("Value");
				}
				rs.close();
				return value;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public int UpdateOperationParams(String Param_Name, String Value) {
		try {

			Statement statement = (Statement) conn.createStatement();
			String sql = "replace into operation_params(Param_Name,Value) values(\""
					+ Param_Name.trim() + "\",\"" + Value.trim() + "\") ";
			//System.out.println(sql);
			int rs = statement.executeUpdate(sql);
			//System.out.println(rs);
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}

	}

	public void test() {
		GetConn("localhost", 3306, "Android", "android", "951753");
		try {
			if (conn == null || conn.isClosed()) {
				return;
			} else {
				Statement statement = (Statement) conn.createStatement();
				String sql = "select * from operation_params";
				ResultSet rs = statement.executeQuery(sql);
				String name = null;
				String value = null;
				String type = null;
				while (rs.next()) {
					name = rs.getString("Param_Name");
					value = rs.getString("Value");
					type = rs.getString("Value_Type");
					System.out.println(name + "\t" + value + "\t" + type);
				}
				rs.close();
				conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DataConnect test = new DataConnect();
		test.test();

	}

}
