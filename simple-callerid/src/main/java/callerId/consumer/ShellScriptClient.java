package callerId.consumer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.log4j.Logger;







public class ShellScriptClient
  extends CallerIdClient
{
  private static final Logger logger = Logger.getLogger(ShellScriptClient.class);
  String file;
  
  public ShellScriptClient()
  {
    file = System.getProperty("shellscript", "notify-phone.sh");
    
    logger.info("inited and will use " + file);
  }
  
  protected void onRing(String msg)
  {
    if (msg != null) {
      String[] p = { file, msg };
      try {
        Process process = Runtime.getRuntime().exec(p);
        InputStream stderr = process.getErrorStream();
        InputStream stdout = process.getInputStream();
        


        BufferedReader brCleanUp = new BufferedReader(new InputStreamReader(stdout));
        String line;
        while ((line = brCleanUp.readLine()) != null) {
          logger.info(line);
        }
        brCleanUp.close();
        

        brCleanUp = new BufferedReader(new InputStreamReader(stderr));
        while ((line = brCleanUp.readLine()) != null) {
          logger.error(line);
        }
        brCleanUp.close();
      } catch (IOException e) {
        logger.error("error executing shell script", e);
      }
    }
  }
  
  public static void main(String[] args) throws IOException {
    CallerIdClient client = new ShellScriptClient();
    client.onRing(args[0]);
  }
}
