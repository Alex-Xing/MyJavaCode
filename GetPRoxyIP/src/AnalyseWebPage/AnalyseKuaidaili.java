package AnalyseWebPage;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;

public class AnalyseKuaidaili extends BaseAnalyse {

	public void LoadPage(DOMParser parser) {
		Document doc = parser.getDocument();
		String xpath = "/html/body/div[3]/div[4]/table/thead/tr/th[1]";
		String TmpString = queryByXpath(doc, xpath);
		if (TmpString != null) {
			LoadFirstPage(doc);
		} else {
			xpath = "/html/body/div[2]/div/div[2]/table/thead/tr/th[1]";
			TmpString = queryByXpath(doc, xpath);
			if (TmpString != null) {
				LoadOtherPage(doc);
			}
		}

	}

	public void LoadOtherPage(Document doc) {
		String xpath = "/html/body/div[2]/div/div[2]/table/tbody/*";
		String IP = null;
		String Prot = null;
		int NodeCount = GetXpathNodeListCount(doc, xpath);
		// DataOperation DB = new DataOperation();
		for (int i = 0; i < (NodeCount + 1); i++) {
			xpath = "/html/body/div[2]/div/div[2]/table/tbody/tr[" + String.valueOf(i).trim() + "]/td[1]";
			IP = queryByXpath(doc, xpath);
			if (IP == null) {
				continue;
			}
			xpath = "/html/body/div[2]/div/div[2]/table/tbody/tr[" + String.valueOf(i).trim() + "]/td[2]";
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
		String xpath = "/html/body/div[3]/div[4]/table/tbody/*";
		String IP = null;
		String Prot = null;
		int NodeCount = GetXpathNodeListCount(doc, xpath);
		// DataOperation DB = new DataOperation();
		for (int i = 0; i < (NodeCount + 1); i++) {
			xpath = "/html/body/div[3]/div[4]/table/tbody/tr[" + String.valueOf(i).trim() + "]/td[1]";
			IP = queryByXpath(doc, xpath);
			if (IP == null) {
				continue;
			}
			xpath = "/html/body/div[3]/div[4]/table/tbody/tr[" + String.valueOf(i).trim() + "]/td[2]";
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
		AnalyseKuaidaili test = new AnalyseKuaidaili();
		test.Analyse("d:/test.html");
	}

}
