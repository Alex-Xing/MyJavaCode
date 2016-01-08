package AnalyseWebPage;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;

public class AnalyseXICI extends BaseAnalyse {
	/*
	private String SaveToFileOrDatabaseMark="File";//可以有File和DB两各种选择
	private DataConnect MyConn = new DataConnect();
	//private SystemParams MyParams = new SystemParams();

	public AnalyseXICI() {
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
	*/
	public void LoadPage(DOMParser parser) {
		Document doc = parser.getDocument();

		// Node body = doc.getElementsByTagName("BODY").item(0);
		// System.out.println(TextExtractor(body));

		String xpath = "/html/body/div/div[2]/div/div/table/tbody/tr[3]/td[2]";
		String TmpString = queryByXpath(doc, xpath);
		if (TmpString != null) {
			LoadFirstPage(doc);
		} else {
			xpath = "/html/body/div/div[2]/table/tbody/tr[2]/td[2]";
			TmpString = queryByXpath(doc, xpath);
			if (TmpString != null) {
				LoadOtherPage(doc);
			}
		}
		
	}

	public void LoadOtherPage(Document doc) {
		String xpath = "/html/body/div/div[2]/table/tbody/*";
		String IP = null;
		String Prot = null;
		int NodeCount = GetXpathNodeListCount(doc, xpath);
		// DataOperation DB = new DataOperation();
		for (int i = 0; i < (NodeCount + 1); i++) {
			xpath = "/html/body/div/div[2]/table/tbody/tr["
					+ String.valueOf(i).trim() + "]/td[2]";
			IP = queryByXpath(doc, xpath);
			if (IP == null) {
				continue;
			}
			xpath = "/html/body/div/div[2]/table/tbody/tr["
					+ String.valueOf(i).trim() + "]/td[3]";
			Prot = queryByXpath(doc, xpath);
			if (Prot == null) {
				continue;
			}
			IP = IP.trim();
			Prot = Prot.trim();

			if (!IP.equals("") && !Prot.equals("")) {
				// System.out.println(IP + ":" + Prot);
				SaveIP((IP + ":" + Prot));
			}

		}
	}

	public void LoadFirstPage(Document doc) {
		String xpath = "/html/body/div/div[2]/div/div/table/tbody/*";
		String IP = null;
		String Prot = null;
		int NodeCount = GetXpathNodeListCount(doc, xpath);
		// DataOperation DB = new DataOperation();
		for (int i = 0; i < (NodeCount + 1); i++) {
			xpath = "/html/body/div/div[2]/div/div/table/tbody/tr["
					+ String.valueOf(i).trim() + "]/td[2]";
			IP = queryByXpath(doc, xpath);
			if (IP == null) {
				continue;
			}
			xpath = "/html/body/div/div[2]/div/div/table/tbody/tr["
					+ String.valueOf(i).trim() + "]/td[3]";
			Prot = queryByXpath(doc, xpath);
			if (Prot == null) {
				continue;
			}
			IP = IP.trim();
			Prot = Prot.trim();
			if (!IP.equals("") && !Prot.equals("")) {
				// System.out.println(IP + ":" + Prot);
				SaveIP((IP + ":" + Prot));
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AnalyseXICI test = new AnalyseXICI();
		test.Analyse("d:/test.htm");
	}

}
