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

        // 记录debug级别的信息  
        logger.debug("This is debug message.");  
        // 记录info级别的信息  
        logger.info("This is info message.");  
        // 记录error级别的信息  
        logger.error("This is error message.");  
    }  
}
