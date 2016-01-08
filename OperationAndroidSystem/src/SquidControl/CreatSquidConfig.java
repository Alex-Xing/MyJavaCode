package SquidControl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import com.mysql.jdbc.Statement;

import DataOperation.DataConnect;
import OperationAndroidSystem.SystemParams;

public class CreatSquidConfig {
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

	public void NewConfigFile(String TmpPath){
		NewConn();
		CreatConfig(TmpPath);
	}
	public void CreatConfig(String TmpPath) {
		String TmpStr = null;
		try {
			Statement statement = (Statement) conn.GetConn().createStatement();
			String sql = "SELECT * FROM `proxy_list` where work =1 "
					+ "and type =\"HTTP\" order by rand() limit 1 ";
			// System.out.println(sql);
			ResultSet rs = statement.executeQuery(sql);
			// System.out.println(rs);

			while (rs.next()) {
				TmpStr = rs.getString("IPAddress");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(TmpStr);
		String[] IP = TmpStr.split(":");

		TmpStr = "cache_peer " + IP[0].trim() + " parent " + IP[1].trim()
				+ " 0 no-query default proxy-only\n"
				+ "http_port 3128 transparent\n" + "acl all src 0/0\n"
				+ "never_direct allow all\n" + "http_access allow all\n"
				+ "header_access X-Forwarded-For deny all\n"
				+ "header_access Via deny all\n";
		String CnfOutputFile = TmpPath;// GetRandomTempFilePath(SystemParams.TempPath);
		OutputConfigFile(CnfOutputFile, TmpStr);
	}

	public void OutputConfigFile(String Path, String Text) {
		File f = new File(Path);
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				System.out.println(e);
			}
		}

		byte[] bytes = null;
		try {
			bytes = Text.getBytes("UTF-8");// 一定由string得到bytes才能写入到文件里
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		OutputStream os;
		try {
			os = new FileOutputStream(Path);
			os.write(bytes);
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}// 将字节一个个写入文件
	}

	public void CreatPath(String Path) {
		File dir = new File(Path);
		if (dir.exists() && dir.isDirectory()) {
			// 目录存在
			return;
		} else {
			// 不存在
			dir.mkdir();
		}
	}

	// 获取随机临时文件路径
	public String GetRandomTempFilePath(String BasePath) {
		Random rnd1 = new Random();
		String Path = BasePath;
		while (true) {
			Path = Path + "/" + String.valueOf(rnd1.nextInt());
			File f = new File(Path);
			if (!f.exists()) {
				return Path;
			}
		}
	}

	// 删除临时文件
	public boolean DelTempFile(String BasePath) {
		boolean flag = false;
		File file = new File(BasePath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CreatSquidConfig test = new CreatSquidConfig();
		//test.NewConn();
		test.NewConfigFile(test.GetRandomTempFilePath(SystemParams.TempPath));
	}

}
