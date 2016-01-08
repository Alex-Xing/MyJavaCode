package AnalyseWebPage;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import javax.xml.transform.TransformerException;

import org.apache.xpath.XPathAPI;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.mysql.jdbc.Statement;

import DataOperation.DataConnect;
import GetPRoxyIPRobot.SystemParams;

public class BaseAnalyse {
	
	String SaveToFileOrDatabaseMark="DB";//"File";//可以有File和DB两各种选择
	DataConnect MyConn = new DataConnect();
	//private SystemParams MyParams = new SystemParams();

	public BaseAnalyse() {
		switch(SaveToFileOrDatabaseMark)
		{
		case "File":
			;
			break;
		case "DB":
			MyConn.GetConn(SystemParams.DatabaseHose, SystemParams.DatabasePort,
					SystemParams.DatabaseName, SystemParams.DatabaseUser,
					SystemParams.DatabasePassword);
			break;
		default:
			;
		}
		
	}
	public void Analyse(String FilePath) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(
					FilePath), "UTF-8"));
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (in != null) {
			Analyse(in);
		}
	}

	public void AnalyseHTML(String HTML) {
		// 解析HTML内容
		DOMParser parser = new DOMParser();
		try {
			parser.parse(new InputSource(new ByteArrayInputStream(HTML
					.getBytes())));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LoadPage((DOMParser) parser);

	}

	public void Analyse(BufferedReader in) {
		DOMParser parser = new DOMParser();
		try {
			parser.parse(new InputSource(in));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LoadPage(parser);

	}

	// 页面数处理函数，每个业面处理对象都需自己重构
	public void LoadPage(DOMParser parser) {
		// 页面数处理函数，每个业面处理对象都需自己重构
		// 页面数处理函数，每个业面处理对象都需自己重构
		// 页面数处理函数，每个业面处理对象都需自己重构
	}

	// 获取指定XPATH路径下节点总数
	public int GetXpathNodeListCount(Document doc, String xpath) {
		NodeList products = null;
		try {
			products = XPathAPI.selectNodeList(doc, xpath.toUpperCase());
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return products.getLength();

	}

	// 获取指定XPATH节点的值
	public String queryByXpath(Document doc, String xpath) {
		String s = null;
		NodeList products;
		try {
			products = XPathAPI.selectNodeList(doc, xpath.toUpperCase());
			Node node = null;
			for (int i = 0; i < products.getLength(); i++) {
				node = products.item(i);
				s = node.getTextContent();
			}
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return s;
	}

	// 获取指点DOM节点中的全部文本，其中DOMRoot为HTML（DOM）的节点名，非XPATH的路径
	public static String TextExtractor(Node DOMRoot) {
		// 若是文本节点的话，直接返回
		if (DOMRoot.getNodeType() == Node.TEXT_NODE) {
			return DOMRoot.getNodeValue().trim();
		}
		if (DOMRoot.getNodeType() == Node.ELEMENT_NODE) {
			Element elmt = (Element) DOMRoot;
			// 抛弃脚本
			if (elmt.getTagName().equals("STYLE")
					|| elmt.getTagName().equals("SCRIPT"))
				return "";

			NodeList children = elmt.getChildNodes();
			StringBuilder text = new StringBuilder();
			for (int i = 0; i < children.getLength(); i++) {
				text.append(TextExtractor(children.item(i)));
			}
			return text.toString();
		}
		// 对其它类型的节点，返回空值
		return "";
	}
	
	//保存IP地址
	public void SaveIP(String IPStr) {
		switch(SaveToFileOrDatabaseMark)
		{
		case "File":
			SaveIPtoFile(IPStr);
			break;
		case "DB":
			SaveIPtoDB( IPStr);
			break;
		default:
			;
		}
	}
	
	//保存IP地址到数据库
	public void SaveIPtoDB(String IPStr) {
		try {
			Statement statement = (Statement) MyConn.GetConn()
					.createStatement();
			String sql = "INSERT INTO `proxy_list` (`";
			sql = sql
					+ "IPAddress`, `Speed`, `Location`, `CheckTime`, `Work`) "
					+ "VALUES ('" + IPStr.trim()
					+ "', NULL, NULL, CURRENT_TIMESTAMP, False) ";
			//System.out.println(sql);
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
	}
	
	//保存IP地址到文本文件
	public void SaveIPtoFile(String IPStr) {
		
		FileWriter fw;
		try {
			fw = new FileWriter("d:/IP_file.txt", true);
			//System.out.println(IPStr);
			fw.write(IPStr+"\r\n");
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
