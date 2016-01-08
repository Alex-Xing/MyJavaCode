package HttpClass;

import java.util.HashMap;
import java.util.Map;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HttpClass tmp = new HttpClass();
		Map<String, String> test = new HashMap<String, String>();
		test.put("typeId", "1");
		try {
			tmp.DoGet("http://autopatch.sdxlmobile.173.com//sdxlmobile/ios/appfeiliu/resource/.version.meta");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
