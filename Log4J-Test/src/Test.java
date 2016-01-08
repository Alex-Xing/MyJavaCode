import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Test {
	
    /** 
     * @param args 
     */  
    public static void main(String[] args) {
    	PropertyConfigurator.configure("E:/Git/MyJavaCode/Log4J-Test/bin/log4j.properties");
    	//PropertyConfigurator.configure("E:/Git/MyJavaCode/Log4J-Test/bin/log4j.properties");
    	Logger logger = Logger.getLogger(Test.class);
    	//PropertyConfigurator.configure("classes/my.properties");
    	
        // System.out.println("This is println message.");  

        // ��¼debug�������Ϣ  
        logger.debug("This is debug message.");  
        // ��¼info�������Ϣ  
        logger.info("This is info message.");  
        // ��¼error�������Ϣ  
        logger.error("This is error message.");  
    }  
}
