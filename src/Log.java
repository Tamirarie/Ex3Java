import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
/*
 *Record all actions to a log document
 */

public class Log {
	Logger logger;
	public Log(String name) {                  ////builder
		logger = Logger.getLogger("MyLog");  
	    FileHandler fh;  
	    logger.setUseParentHandlers(false);
	    try {  

	        // This block configure the logger with handler and formatter  
	        fh = new FileHandler(name+".log");  
	        logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);  

	        // the following statement is used to log any messages  
	        logger.info("Created Log");  

	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  

	    
	}
	void addSentence(String s){
		logger.info(s);
	}
}
