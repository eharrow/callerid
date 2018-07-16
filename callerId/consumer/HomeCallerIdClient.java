package callerId.consumer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.log4j.Logger;




















public class HomeCallerIdClient
  extends CallerIdClient
{
  private static final Logger logger = Logger.getLogger(HomeCallerIdClient.class);
  private static final long ONCE_PER_DAY = 86400000L;
  static SqueezeboxCallerIdClient sqClient;
  
  public static void main(String[] args) {
    try {
      logger.info("Started");
      sqClient = new SqueezeboxCallerIdClient();
      sqClient.setSlimserverIpAddress("192.168.0.4");
      sqClient.setSlimserverIpPort(9090);
      try {
        tivoClient = new TivoCallerIdClient();
      } catch (Exception e) {
        logger.error("Error creating TivoCallerIdClient", e);
      }
      multicastClient = new MulticastBroadcaster();
      shellScriptClient = new ShellScriptClient();
      HomeCallerIdClient c = new HomeCallerIdClient();
      c.setAddress("127.0.0.1");
      c.setPort(514);
      c.listen();
    } catch (FileNotFoundException e) {
      logger.error(e);
    } catch (UnknownHostException e) {
      logger.error(e);
    } catch (IOException e) {
      logger.error(e);
    }
  }
  
  static CallerIdClient tivoClient;
  static MulticastBroadcaster multicastClient;
  static ShellScriptClient shellScriptClient;
  public HomeCallerIdClient() { Timer timer = new Timer("ClearCount");
    timer.scheduleAtFixedRate(clearCount(), getTomorrowMorning1am(), 86400000L);
  }
  
  private Date getTomorrowMorning1am() {
    Calendar tomorrow = new GregorianCalendar();
    tomorrow.add(5, 1);
    Calendar result = new GregorianCalendar(tomorrow.get(1), tomorrow.get(2), tomorrow.get(5), 1, 0);
    





    return result.getTime();
  }
  
  private TimerTask clearCount() {
    TimerTask timerTask = new TimerTask()
    {
      public void run()
      {
        CallerCounter instance = CallerCounter.getInstance();
        instance.clearCounter();
        HomeCallerIdClient.logger.info("Clearing callerId count");
      }
      
    };
    return timerTask;
  }
  
  protected void onRing(String callerId)
  {
    logger.debug("onRing callerId: " + callerId);
    String tmpMsg = null;
    String caller = lookup(callerId);
    
    if ((caller != null) && (!caller.equals(""))) {
      CallerCounter instance = CallerCounter.getInstance();
      int count = instance.update(caller);
      StringBuffer sb = new StringBuffer().append(caller);
      
      if (count > 1) {
        sb.append(" ").append(count).append(" times today");
      }
      
      tmpMsg = sb.toString();
    }
    
    final String msg = tmpMsg;
    
    Thread t1 = new Thread(new Runnable()
    {

      public void run() { HomeCallerIdClient.sqClient.onRing(msg); } }, "squeezebox");
    


    Thread t2 = new Thread(new Runnable()
    {
      public void run() {
        if (HomeCallerIdClient.tivoClient != null)
          HomeCallerIdClient.tivoClient.onRing(msg); } }, "tivo");
    



    Thread t3 = new Thread(new Runnable()
    {

      public void run() { HomeCallerIdClient.multicastClient.onRing(msg); } }, "multicast");
    


    Thread t4 = new Thread(new Runnable()
    {

      public void run() { HomeCallerIdClient.shellScriptClient.onRing(msg); } }, "shellScript");
    


    t1.start();
    t2.start();
    t3.start();
    t4.start();
  }
}
