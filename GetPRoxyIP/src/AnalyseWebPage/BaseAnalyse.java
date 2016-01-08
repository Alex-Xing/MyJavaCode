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
	
	String SaveToFileOrDatabaseMark="DB";//"File";//������File��DB������ѡ��
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
		// ����HTML����
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

	// ҳ������������ÿ��ҵ�洦��������Լ��ع�
	public void LoadPage(DOMParser parser) {
		// ҳ������������ÿ��ҵ�洦��������Լ��ع�
		// ҳ������������ÿ��ҵ�洦��������Լ��ع�
		// ҳ������������ÿ��ҵ�洦��������Լ��ع�
	}

	// ��ȡָ��XPATH·���½ڵ�����
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

	// ��ȡָ��XPATH�ڵ��ֵ
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

	// ��ȡָ��DOM�ڵ��е�ȫ���ı�������DOMRootΪHTML��DOM���Ľڵ�������XPATH��·��
	public static String TextExtractor(Node DOMRoot) {
		// �����ı��ڵ�Ļ���ֱ�ӷ���
		if (DOMRoot.getNodeType() == Node.TEXT_NODE) {
			return DOMRoot.getNodeValue().trim();
		}
		if (DOMRoot.getNodeType() == Node.ELEMENT_NODE) {
			Element elmt = (Element) DOMRoot;
			// �����ű�
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
		// ���������͵Ľڵ㣬���ؿ�ֵ
		return "";
	}
	
	//����IP��ַ
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
	
	//����IP��ַ�����ݿ�
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
	
	//����IP��ַ���ı��ļ�
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
