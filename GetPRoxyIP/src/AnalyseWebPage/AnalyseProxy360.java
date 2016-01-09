package AnalyseWebPage;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;

public class AnalyseProxy360 extends BaseAnalyse {

	public void LoadPage(DOMParser parser) {
		Document doc = parser.getDocument();
		
		String xpath = "/html/body/form/div/table[3]/tbody/tr/td[1]/div/*";
		String IP = null;
		String Prot = null;
		int NodeCount = GetXpathNodeListCount(doc, xpath);
		
		for (int i = 0; i < (NodeCount + 1); i++) {
			xpath = "/html/body/form/div/table[3]/tbody/tr/td[1]/div/div["
					+ String.valueOf(i).trim() + "]/div[1]/span[1]";
			IP = queryByXpath(doc, xpath);
			if (IP == null) {
				continue;
			}
			xpath = "/html/body/form/div/table[3]/tbody/tr/td[1]/div/div["
					+ String.valueOf(i).trim() + "]/div[1]/span[2]";
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
		AnalyseProxy360 test = new AnalyseProxy360();
		test.Analyse("d:/test.html");
	}
}
